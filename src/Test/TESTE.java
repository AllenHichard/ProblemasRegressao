/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import SistemaFuzzy.ConversaoValor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;

/**
 *
 * @author allen
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.jFuzzyLogic.FIS;

public class TESTE {

    private FIS fis;
    private List atributos;
    private List classificacao;
    private String saida;
    private List linhaEntrada;
    private List colunaEntrada;
    private List TestEntrada; // pega os valores do arquivo para teste
    private List TestSimulado; // pega os valores fuzzy
    private String nomeArquivo;
    private String nomeArquivoTest;
    private List regras;
    private int varEntrada; // testar esse var - caso der erro será aqui

    public TESTE(String fold, int T, String nomeArquivo, String test, double[] Acuracia, double[] Interpretabilidade, int i) throws IOException {
        classificacao = new LinkedList();
        atributos = new LinkedList();
        linhaEntrada = new LinkedList();
        TestEntrada = new LinkedList();
        TestSimulado = new LinkedList();
        this.nomeArquivo = nomeArquivo;
        this.nomeArquivoTest = test;
        Carregarregras(fold, T);
        iniciaInferencia(fold, T);
        dadosEntrada();
        calculaInferencia();
        testSimulado(Acuracia, Interpretabilidade, i);
    }

    private void Carregarregras(String fold, int T) throws FileNotFoundException, IOException {
        regras = new ArrayList();
        BufferedReader ler = new BufferedReader(new FileReader("saida\\" + fold + "\\" + "TREINAMENTO" + T + "\\" + nomeArquivo + " - Regras.txt"));
        String regra = ler.readLine();
        regra = regra.substring(1, regra.length() - 1);
        String[] r = regra.split(", ");
        for (int i = 0; i < r.length; i++) {
            regras.add(r[i]);
        }
        ler.close();
    }

    private void iniciaInferencia(String fold, int T) {
        try {
            BufferedReader ler = new BufferedReader(new FileReader("saida\\" + fold + "\\" + "TREINAMENTO" + T + "\\" + nomeArquivo + ".FLC"));
            String linha = ler.readLine();
            String conteudoArquivoFis = "";
            while (linha != null) {
                conteudoArquivoFis += linha + "\n";
                linha = ler.readLine();
            }
            ler.close();
            fis = FIS.createFromString(conteudoArquivoFis, true);
            //System.out.println("Instancia de inferencias carregada com sucesso");
        } catch (Exception ex) {
            Logger.getLogger(TESTE.class.getName()).log(Level.SEVERE, "Erro ao abrir o arquivo", ex);
        }
    }

    public void classificacao(String linhatual) {

        String tipoclasse = linhatual.substring(linhatual.indexOf("{") + 1, linhatual.lastIndexOf("}"));
        String aux[] = tipoclasse.split(",");
        for (int i = 0; i < aux.length; i++) {
            classificacao.add(aux[i]); // foi tirado o conversor
        }
    }

    public void dadosEntrada() throws FileNotFoundException, IOException {
        BufferedReader ler = new BufferedReader(new FileReader("teste\\" + nomeArquivoTest + ".DAT"));
        String linha = ler.readLine();
        while (!linha.contains("{")) {
            linha = ler.readLine();
        }
        classificacao(linha);
        linha = ler.readLine(); // input
        String[] var = linha.split(" ");
        for (int i = 1; i < var.length; i++) {
            atributos.add(var[i].split(",")[0]);
        }
        this.varEntrada = atributos.size();
        linha = ler.readLine(); // output
        saida = linha.split(" ")[1];
        ler.readLine();
        linha = ler.readLine();
        while (linha != null) {
            String aux[];
            if (linha.contains(", ")) {
                aux = linha.split(", ");
            } else {
                aux = linha.split(",");
            }
            colunaEntrada = new LinkedList();
            for (int i = 0; i < aux.length; i++) {
                //if (aux[i].contains("-")) {
                //aux[i] = aux[i].split("-")[0] + aux[i].split("-")[1];
                // }
                colunaEntrada.add(aux[i]);
            }
            linhaEntrada.add(colunaEntrada);
            linha = ler.readLine();
        }

    }

    public void calculaInferencia() {
        //linhaEntrada.size()
        for (int i = 0; i < linhaEntrada.size(); i++) {
            List coluna = (List) linhaEntrada.get(i);
            for (int j = 0; j < atributos.size(); j++) {
                this.fis.setVariable(atributos.get(j).toString(), Double.parseDouble((String) coluna.get(j)));
            }
            String classificacaoSaida = coluna.get(coluna.size() - 1).toString(); // foi tirado o conversor
            TestEntrada.add(classificacaoSaida);
            //System.out.println(coluna.get(coluna.size()-1));
            this.fis.evaluate();
            double classe = this.fis.getVariable(saida).getLatestDefuzzifiedValue();
            //System.out.println(classe);
            conjuntosFuzzyAtivados();
        }
    }

    //public static void main(String[] args) throws IOException {
    //Controlador c = new Controlador();
    //}
    private void conjuntosFuzzyAtivados() {
        TestSimulado.add(this.fis.getVariable(saida).getLatestDefuzzifiedValue());
    }

    /*
    private void conjuntosFuzzyAtivados() {
        double valorM = 0;
        String nome = "";
        for (int i = 0; i < classificacao.size(); i++) {
            double atual = fis.getVariable(saida).getMembership(classificacao.get(i).toString());
            if (atual > valorM) {
                valorM = atual;
                nome = classificacao.get(i).toString();
            }
        }
        TestSimulado.add(nome);
    }*/
    private void testSimulado(double[] Acuracia, double[] Interpretabilidade, int posicao) {
        double et = 0;
        linhaEntrada.size();
        for (int i = 0; i < TestEntrada.size(); i++) {
            //System.out.println(TestEntrada.get(i) + " - " + TestSimulado.get(i));
            double entrada = Double.parseDouble((String) TestEntrada.get(i));
            double classif = (double) TestSimulado.get(i);
            //System.out.println(entrada + " " + classif);
            et += Math.pow(entrada - classif, 2);
        }
        //System.out.println(TestEntrada.size());
        double EQM = et / TestEntrada.size();
        Acuracia[posicao] = (double) EQM;
        Interpretabilidade[posicao] = 1 - ((double) (regras.size() / (varEntrada + 1)) / TestEntrada.size());
    }

    public static void main(String[] args) throws IOException {
        int tam = 5;
        double[] Interpretabilidade = new double[tam];
        double[] Acuracia = new double[tam];
        String nomeArq[] = new String[tam];
        String nomeArqTest[] = new String[tam];
         String folds[] = {"diabetes", "machineCPU", "autoMPG6", "autoMPG8", "dee", "plastic", "laser", "quake"};

        //"ANACALT", "concrete", "delta_ail", "friedman", "wankara" apenas 1
        // "stock" 3
        // "forestFires" 4
        //String folds[] = {"forestFires"};
        for (int j = 0; j < folds.length; j++) {

            for (int i = 1; i <= tam; i++) {
                nomeArq[i - 1] = folds[j] + "-5-" + i + "tra";
                nomeArqTest[i - 1] = folds[j] + "-5-" + i + "tst";
            }

            String nomeArquivo;
            for (int T = 1; T <= 1; T++) {
                for (int i = 0; i < nomeArq.length; i++) {
                    new TESTE(folds[j], T, nomeArq[i], nomeArqTest[i], Acuracia, Interpretabilidade, i);
                }

                desvioPadrão(folds[j], T, Acuracia, Interpretabilidade, folds[j]);
            }
        }
    }

    public static void desvioPadrão(String fold, int T, double[] acuracias, double[] interpretabilidades, String nome) throws IOException {
        double somaAcuracia = 0, mediaAcuracia = 0, somaInterpretabilidade = 0, mediaInterpretabilidade = 0;
        double somatorioDPA = 0;
        double somatorioDPI = 0;
        for (int i = 0; i < acuracias.length; i++) {
            somaAcuracia += acuracias[i];
            somaInterpretabilidade += interpretabilidades[i];
        }
        mediaAcuracia = somaAcuracia / acuracias.length;
        mediaInterpretabilidade = somaInterpretabilidade / acuracias.length;
        for (int i = 0; i < acuracias.length; i++) {
            somatorioDPA += Math.pow(acuracias[i] - mediaAcuracia, 2);
            somatorioDPI += Math.pow(interpretabilidades[i] - mediaInterpretabilidade, 2);
        }
        double dpA = Math.sqrt(somatorioDPA / acuracias.length);
        double dpI = Math.sqrt(somatorioDPI / acuracias.length);
        File diretorio = new File("saida\\" + fold + "\\" + "TESTE" + T);
        diretorio.mkdir();
        double total = 0;
        BufferedWriter gravar = new BufferedWriter(new FileWriter("saida\\" + fold + "\\" + "TESTE" + T + "\\Resultados.txt"));
        for (int i = 0; i < acuracias.length; i++) {
            //gravar.write("Arquivo " + i + " - Acurácia = " + acuracias[i] + " +/- " + dpA + " - Interpretabilidade = " + interpretabilidades[i] + " +/- " + dpI + "\r\n");
            gravar.write("Arquivo " + i + " - Acurácia = " + acuracias[i] + " +/- " + dpA + "\r\n");
            total += acuracias[i];

        }
        gravar.write("total  " + total / 5 + "\r\n");

        gravar.close();
    }

}

    // 
