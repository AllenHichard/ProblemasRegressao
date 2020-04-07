/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemaGenético;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author allen
 */
public class ArquivoTestObjetivo {

    public BufferedReader ler;
    public BufferedWriter gravar;
    public List<String> listaArquivo;
    public List<String> listaAlterada;
    public String pontoAalterar;

    public ArquivoTestObjetivo(String nomeArquivo){
        this.listaArquivo = new ArrayList<>();
        try {
            this.ler = new BufferedReader(new FileReader("semente\\" + nomeArquivo + ".FLC"));
            String linha = ler.readLine();
            while (linha != null) {
                listaArquivo.add(linha);
                linha = ler.readLine();
            }
            ler.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ArquivoTestObjetivo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ArquivoTestObjetivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        //gravar = new BufferedWriter(new FileWriter("saida\\"+nomeArquivo+"WM.FLC"));

    }

    public void alterarPontosMediosERegras(double[] pontos, String novaRegras) {
        this.listaAlterada = new ArrayList<>();
        String pontosMedios[] = vetorDoubleString(pontos);
        int conjunto = 0;
        String alterarLinha;
        String linhaAtual;
        Iterator it = listaArquivo.iterator();
        while (it.hasNext()) {
            linhaAtual = (String) it.next();
            
            if(linhaAtual.contains("DEFUZZIFY")){
                while(!linhaAtual.contains("END_DEFUZZIFY")){
                    listaAlterada.add(linhaAtual);
                    linhaAtual = (String) it.next();
                }
            }
            if (linhaAtual.contains("TERM BAIXA")) {
                alterarLinha = alterarBaixa(linhaAtual, pontosMedios[conjunto]);
                listaAlterada.add(alterarLinha);
                linhaAtual = (String) it.next();
                alterarLinha = alterarMedia(linhaAtual, pontosMedios[conjunto]);
                listaAlterada.add(alterarLinha);
                linhaAtual = (String) it.next();
                alterarLinha = alterarAlta(linhaAtual, pontosMedios[conjunto]);
                listaAlterada.add(alterarLinha);
                conjunto++;
            } else if (linhaAtual.contains("RULEBLOCK")) {
                listaAlterada.add(linhaAtual); // Ruleblock
                linhaAtual = (String) it.next();
                listaAlterada.add(linhaAtual); //ADD
                linhaAtual = (String) it.next();
                listaAlterada.add(linhaAtual); //ACT
                linhaAtual = (String) it.next();
                listaAlterada.add(linhaAtual); //ACCU
                linhaAtual = (String) it.next();
                listaAlterada.add(linhaAtual); //linha branco
                listaAlterada.add(novaRegras);
                listaAlterada.add("\r\n"); // linha Branco;
                while (!linhaAtual.contains("END_RULEBLOCK")) {
                    linhaAtual = (String) it.next();
                }
                listaAlterada.add(linhaAtual);
            } else {
                listaAlterada.add(linhaAtual);
            }
        }
        /*
         for(int i = 0; i < listaAlterada.size(); i++){
         System.out.println(listaAlterada.get(i));
         }
         */
    }

    public String alterarBaixa(String linha, String pontoMedio) {
        int posicao = 0; // achar o terceiro (
        char[] parenteses = linha.toCharArray();
        int i;
        for (i = 0; i < parenteses.length && posicao < 3; i++) {
            if (parenteses[i] == '(') {
                posicao++;
            }
        }
        int j;
        for (j = i; j < parenteses.length && parenteses[j] != ','; j++);
        this.pontoAalterar = linha.substring(i, j);
        linha = linha.replaceAll(pontoAalterar, pontoMedio);
        return linha;
    }

    public String alterarMedia(String linha, String pontoMedio) {
        return linha.replaceAll(pontoAalterar, pontoMedio);
    }

    public String alterarAlta(String linha, String pontoMedio) {
        return linha.replaceAll(pontoAalterar, pontoMedio);
    }

    private String[] vetorDoubleString(double[] pontosMedios) {
        String[] pontos = new String[pontosMedios.length];
        for (int i = 0; i < pontosMedios.length; i++) {
            pontos[i] = "" + pontosMedios[i];
            //System.out.println(pontos[i]);
        }
        return pontos;
    }

    public BufferedReader getLer() {
        return ler;
    }

    public void setLer(BufferedReader ler) {
        this.ler = ler;
    }

    public BufferedWriter getGravar() {
        return gravar;
    }

    public void setGravar(BufferedWriter gravar) {
        this.gravar = gravar;
    }

    public List<String> getListaArquivo() {
        return listaArquivo;
    }

    public void setListaArquivo(List<String> listaArquivo) {
        this.listaArquivo = listaArquivo;
    }

    public List<String> getListaAlterada() {
        return listaAlterada;
    }

    public void setListaAlterada(List<String> listaAlterada) {
        this.listaAlterada = listaAlterada;
    }

    public String getPontoAalterar() {
        return pontoAalterar;
    }

    public void setPontoAalterar(String pontoAalterar) {
        this.pontoAalterar = pontoAalterar;
    }

    public String getConteudoArquivoFis() {
        String conteudoFis = "";
        String atual;
        Iterator it = listaAlterada.iterator();
        while (it.hasNext()) {
            atual = (String) it.next();
            conteudoFis += atual + "\r\n";
        }
        return conteudoFis;
    }

    public String getConteudoOriginal() {
        String conteudoFis = "";
        String atual;
        Iterator it = listaArquivo.iterator();
        while (it.hasNext()) {
            atual = (String) it.next();
            conteudoFis += atual;
        }
        return conteudoFis;
    }

}
