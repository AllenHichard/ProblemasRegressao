/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemaFuzzy;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.jFuzzyLogic.FIS;

/**
 *
 * @author allen
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.jFuzzyLogic.FIS;

public class Controlador {

    private FIS fis;
    private List atributos;
    private List classificacao;
    private String saida;
    private List linhaEntrada;
    private List colunaEntrada;
    private List TestEntrada; // pega os valores do arquivo para teste
    private List TestSimulado; // pega os valores fuzzy
    private String nomeArquivo;

    public Controlador(String nomeArquivo) throws IOException {
        classificacao = new LinkedList();
        atributos = new LinkedList();
        linhaEntrada = new LinkedList();
        TestEntrada = new LinkedList();
        TestSimulado = new LinkedList();
        this.nomeArquivo = nomeArquivo;
        iniciaInferencia();
        dadosEntrada();
        calculaInferencia();
        testSimulado();
    }

    private void iniciaInferencia() {
        try {
            BufferedReader ler = new BufferedReader(new FileReader("semente\\"+nomeArquivo+".FLC"));
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
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, "Erro ao abrir o arquivo", ex);
        }
    }

    public void classificacao(String linhatual){
        String tipoclasse = linhatual.substring(linhatual.indexOf("{") + 1, linhatual.lastIndexOf("}"));
        String aux[] = tipoclasse.split(",");
        for (int i = 0; i < aux.length; i++) {
            classificacao.add(aux[i]);
        }
    }
    
    public void dadosEntrada() throws FileNotFoundException, IOException {
        BufferedReader ler = new BufferedReader(new FileReader("entrada\\"+nomeArquivo+".DAT"));
        String linha = ler.readLine();
        while(!linha.contains("{")){
            linha = ler.readLine();
        }
        classificacao(linha);
        linha = ler.readLine(); // input
        String[] var = linha.split(" ");
        for (int i = 1; i < var.length; i++) {
            atributos.add(var[i].split(",")[0]);
        }
        linha = ler.readLine(); // output
        saida = linha.split(" ")[1];
        ler.readLine();
        linha = ler.readLine();
        while (linha != null) {
            String aux[];
            if(linha.contains(", ")){
                aux = linha.split(", ");
            } else {
                aux = linha.split(",");
            }
            colunaEntrada = new LinkedList();
            for (int i = 0; i < aux.length; i++) {
                
                /*if (aux[i].contains("-")) {
                    aux[i] = aux[i].split("-")[0] + aux[i].split("-")[1];
                }
                */
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
            for(int j = 0; j<atributos.size(); j++){
                 this.fis.setVariable(atributos.get(j).toString(), Double.parseDouble((String) coluna.get(j)));
            }
            double classificacaoSaida = Double.parseDouble(coluna.get(coluna.size()-1).toString());
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
    Esse metodo foi substituido pelo novo
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
    }
    */

    public int MaxRegras(){
        return TestEntrada.size();
    }
    
    private void testSimulado() {
        
        double et = 0;
        linhaEntrada.size();
        for (int i = 0; i < TestEntrada.size(); i++) {
            double entrada = (double) TestEntrada.get(i);
            double classif = (double) TestSimulado.get(i);
            //System.out.println(entrada + " " + classif);
            et += Math.pow(entrada - classif,2);
        }
        //System.out.println(TestEntrada.size());
        double EQM = et/TestEntrada.size();
        //System.out.println(TestEntrada.get(0).toString());
        //System.out.println(TestSimulado.get(0).toString());
        System.out.println("Nome do Arquivo: " + nomeArquivo);
        System.out.println("Total de dados entrada: " + TestEntrada.size());
        //System.out.println("Total de acertos do sistema: " + acertos);
        //System.out.println("Percentual de Acertos: " + ((double)acertos / TestEntrada.size()));
        System.out.println("error quadratico medio: " + EQM);
    }

    
}
