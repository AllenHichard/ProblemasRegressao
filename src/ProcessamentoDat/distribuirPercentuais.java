/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProcessamentoDat;

import SistemaFuzzy.ConversaoValor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author allen
 */
public class distribuirPercentuais {

    String nomeArquivo;
    String cabecalho = "";
    //List saidas;
    //List entradasSeparadas;
    List entradas;
    double percFuzzy = 1;
    double percAG = 0;
    double percTest = 0;
    String fold;

    public distribuirPercentuais(String fold, String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
        this.fold = fold;
        this.entradas = new ArrayList();
        //saidas = new ArrayList();
        //entradasSeparadas = new ArrayList();

    }

    public void classificacao(String linhatual) {
        String tipoclasse = linhatual.substring(linhatual.indexOf("{") + 1, linhatual.lastIndexOf("}"));
        String aux[];
        if (tipoclasse.contains(", ")) {
            aux = tipoclasse.split(", ");
        } else {
            aux = tipoclasse.split(",");
        }

        /*
        for (int i = 0; i < aux.length; i++) {
            entradasSeparadas.add(new ArrayList());
            //System.out.println(aux[i]);
            if (aux[i].contains("-")) {
                aux[i] = aux[i].split("-")[0] + aux[i].split("-")[1];
            }
            saidas.add(aux[i]);
        }*/

    }

    public void distribuindo() throws FileNotFoundException, IOException {
        
        BufferedReader ler = new BufferedReader(new FileReader("arquivos\\" + fold + "\\" + nomeArquivo  + ".DAT"));
        String linha = ler.readLine();
        while (!linha.contains("{")) {
            cabecalho += linha + "\r\n";
            linha = ler.readLine();
        }
        //cabecalho += linha + "\r\n"; 
        classificacao(linha);
        while (!linha.contains("@data")) {
            cabecalho += linha + "\r\n";
            linha = ler.readLine();
        }
        cabecalho += linha + "\r\n";
        linha = ler.readLine();
        while (linha != null) {
            entradas.add(linha);
            //separarList(linha);
            linha = ler.readLine();
        }

        redistribuir();
    }

    public static void main(String[] args) throws IOException {
        //new distribuirPercentuais("iris").distribuindo();
    }

    private void separarList(String linha) {
        String aux[];
        String classe;
        if (linha.contains(", ")) {
            aux = linha.split(", ");
        } else {
            aux = linha.split(",");
        }
        int fim = aux.length - 1;
        if (aux[fim].contains("-")) {
            classe = aux[fim].split("-")[0] + aux[fim].split("-")[1];
        } else {
            classe = aux[fim];
        }
        //int index = saidas.indexOf(classe);
        //List lista = (List) entradasSeparadas.get(index);
        //lista.add(linha);
    }

    public void redistribuir() throws IOException {
        
        String fuzzy = cabecalho, ag = cabecalho, test = cabecalho;

       // for (int i = 0; i < entradasSeparadas.size(); i++) {
            List lista = entradas;
            int condicao1 = (int) (lista.size() * percFuzzy);
            for (int j = 0; j < condicao1; j++) {
                fuzzy += lista.get(j).toString() + "\r\n";
            }
            int condicao2 = (int) (lista.size() * (percFuzzy + percAG));
            for (int j = condicao1; j < condicao2; j++) {
                ag += lista.get(j).toString() + "\r\n";
            }
            int condicao3 = lista.size();
            for (int j = condicao2; j < condicao3; j++) {
                test += lista.get(j).toString() + "\r\n";
            }
        //}
        BufferedWriter gravar = new BufferedWriter(new FileWriter("entrada\\" + nomeArquivo + ".DAT"));
        gravar.write(fuzzy);
        gravar.close();
        gravar = new BufferedWriter(new FileWriter("entrada\\" + nomeArquivo + "AG" + ".DAT"));
        gravar.write(fuzzy);
        gravar.close();
        //gravar = new BufferedWriter(new FileWriter("entrada\\" + nomeArquivo + "TEST" + ".DAT"));
        //gravar.write(test);
        //gravar.close();
    }

}
