/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemaGenético;

import SistemaFuzzy.Atributo;
import SistemaFuzzy.ConversaoValor;
import SistemaFuzzy.RegrasFuzzy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author allen
 */
public class Cromossomo {

    private int limiteInferiorSaida;
    private int limiteSuperiorSaida;
    private int[] Regras;
    private double[] PontosMedios;
    private int tamanhoRegra;
    private int tamanhoRegraEntrada;
    private String[] saidas;
    private List ListRegras;
    private String[] RegrasNome;
    private int nRegras;
    private List entrada;
    private String nomeConjuntoSaida;
    private String nomeArquivo;
    private double[] pontosIniciais;
    private int MaxRegras;
    private int atualQtdRegraCromossomo;
    RegrasFuzzy Fuzzy;
    private List conjuntosDeRegrasAG;

    /*
     Sistema de Entrada que converte a entrada do sistema fuzzy para entrada
     no sistema genético, converte o nome de pertinência para um padrão de inteiros
     */
    public Cromossomo(RegrasFuzzy Fuzzy, String nomeArquivo, List ListRegras, double[] pontosIniciais, double[] pontosMedios, String[] saidas, List entrada, String nomeConjuntoSaida, int MaxRegras) {
        this.Fuzzy = Fuzzy;
        this.MaxRegras = MaxRegras;
        this.pontosIniciais = pontosIniciais;
        this.nomeArquivo = nomeArquivo;
        this.ListRegras = ListRegras;
        this.saidas = saidas;
        this.Regras = new int[ListRegras.size()];
        this.RegrasNome = new String[ListRegras.size()];
        this.PontosMedios = pontosMedios;
        this.limiteSuperiorSaida = -1;
        this.limiteInferiorSaida = saidas.length * (-1);
        this.entrada = entrada;
        this.nRegras = 1;
        this.nomeConjuntoSaida = nomeConjuntoSaida;
        gerarVetorEntrada();

    }

    public int getMaxRegras() {
        return MaxRegras;
    }

    public double[] getPontosIniciais() {
        return pontosIniciais;
    }

    public void setPontosIniciais(double[] pontosIniciais) {
        this.pontosIniciais = pontosIniciais;
    }

    public int getTamanhoRegraEntrada() {
        return tamanhoRegraEntrada;
    }

    public void setTamanhoRegraEntrada(int tamanhoRegraEntrada) {
        this.tamanhoRegraEntrada = tamanhoRegraEntrada;
    }

    public String[] getRegrasNome() {
        return RegrasNome;
    }

    public void setRegrasNome(String[] RegrasNome) {
        this.RegrasNome = RegrasNome;
    }

    public int getnRegras() {
        return nRegras;
    }

    public void setnRegras(int nRegras) {
        this.nRegras = nRegras;
    }

    public List getEntrada() {
        return entrada;
    }

    public void setEntrada(List entrada) {
        this.entrada = entrada;
    }

    public String getNomeConjuntoSaida() {
        return nomeConjuntoSaida;
    }

    public void setNomeConjuntoSaida(String nomeConjuntoSaida) {
        this.nomeConjuntoSaida = nomeConjuntoSaida;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public double[] getSemente() {
        double[] semente = new double[Regras.length + PontosMedios.length];
        for (int i = 0; i < Regras.length; i++) {
            semente[i] = Regras[i];
        }
        int j = 0;
        for (int i = Regras.length; i < semente.length; i++) {
            semente[i] = PontosMedios[j];
            j++;
        }
        return semente;
    }

    public void gerarVetorEntrada() {
        boolean tamanho = true;
        for (int i = 0; i < ListRegras.size(); i++) {
            if (ListRegras.get(i).equals("BAIXA")) {
                Regras[i] = 1;
            } else if (ListRegras.get(i).equals("MEDIA")) {
                Regras[i] = 2;
            } else if (ListRegras.get(i).equals("ALTA")) {
                Regras[i] = 3;
            } else {
                if (tamanho) {
                    this.tamanhoRegraEntrada = i;
                    this.tamanhoRegra = i + 1; // porque o i começa em zero
                    tamanho = false;
                }

                String saida = Fuzzy.conjuntosFuzzyAtivados2(ListRegras.get(i).toString());
                if (saida.equals("BAIXO")) {
                    Regras[i] = -1;
                } else if (saida.equals("MEDIO")) {
                    Regras[i] = -2;
                } else if (saida.equals("ALTO")) {
                    Regras[i] = -3;
                }

            }
        }
            /*
             for(int i = 0; i < Regras.length; i++){
             System.out.print(Regras[i] + "-");
             }
             System.out.println("");
             */

        //converterInverso(Regras);
            //stringRegraArquivo();
            // System.out.println(entrada.toString());
        /*System.out.println(tamanhoRegra);
             System.out.println(ListRegras.toString());
             for(int i = 0; i < ListRegras.size(); i++){
             System.out.print(Regras[i] + " ");
             }
             System.out.println("Conversão inversa \n");
        
             */
        }

    

    public String converterInverso(int vetor[], int inicioRegra) {
        nRegras = inicioRegra;
        boolean tamanho = true;
        this.conjuntosDeRegrasAG = new ArrayList();
        for (int i = 0; i < vetor.length; i++) {
            if (vetor[i] == 1) {
                RegrasNome[i] = "BAIXA";
            } else if (vetor[i] == 2) {
                RegrasNome[i] = "MEDIA";
            } else if (vetor[i] == 3) {
                RegrasNome[i] = "ALTA";
            } else if (vetor[i] == 0) {
                RegrasNome[i] = "NOT";
            } else {
                //System.out.print(vetor[i] + " ");
                int posicao = -1 * vetor[i] - 1; // tirei o -1
                RegrasNome[i] = saidas[posicao];
                //System.out.println(RegrasNome[i]);
            }
        }
        /*
         for(int i = 0; i < vetor.length; i++){
         System.out.print(RegrasNome[i] + " ");
         }*/
        return stringRegraArquivo();
    }

    public String stringRegraArquivo() {
        atualQtdRegraCromossomo = 0;
        String[] modelo = new String[tamanhoRegra];
        String regraGeral = "";
        int count = 0;
        for (int i = 0; i < RegrasNome.length; i++) {
            if (RegrasNome[i].equals("NOT")) {
                i += tamanhoRegraEntrada;
            } else {
                conjuntosDeRegrasAG.add(RegrasNome[i]);
                modelo[count] = RegrasNome[i];
                count++;
                if (count == tamanhoRegra) {
                    count = 0;
                    regraGeral += modelarStringRegraArquivo(modelo);
                    modelo = new String[tamanhoRegra];
                    atualQtdRegraCromossomo++;
                }
            }
        }
        //System.out.println("rest");
        //System.out.println(regraGeral);
        return regraGeral;
    }

    public int qtdRegrasAtual() {
        return atualQtdRegraCromossomo;
    }
    
    public List getRegrasAGAtualizada(){
       return conjuntosDeRegrasAG;     
    }

    public String modelarStringRegraArquivo(String[] regraModelo) {

        String regra = "\t\tRULE " + nRegras + " : IF ";
        for (int j = 0; j < tamanhoRegraEntrada - 1; j++) {
            //System.out.println(j);
            Atributo atributo = (Atributo) entrada.get(j);
            regra += atributo.getNome() + " IS " + regraModelo[j] + " AND ";
        }
        Atributo atributo = (Atributo) entrada.get(tamanhoRegraEntrada - 1);
        regra += atributo.getNome() + " IS " + regraModelo[tamanhoRegraEntrada - 1] + " THEN ";
        String saida = regraModelo[tamanhoRegraEntrada];
        regra += nomeConjuntoSaida + " IS " + saida + ";\r\n ";
        nRegras++;
        return regra;
    }
    
    public int getSaida() {
        return saidas.length;
    }

    public int getLimiteInferiorSaida() {
        return limiteInferiorSaida;
    }

    public void setLimiteInferiorSaida(int limiteInferiorSaida) {
        this.limiteInferiorSaida = limiteInferiorSaida;
    }

    public int getLimiteSuperiorSaida() {
        return limiteSuperiorSaida;
    }

    public void setLimiteSuperiorSaida(int limiteSuperiorSaida) {
        this.limiteSuperiorSaida = limiteSuperiorSaida;
    }

    public int[] getRegras() {
        return Regras;
    }

    public void setRegras(int[] Regras) {
        this.Regras = Regras;
    }

    public double[] getPontosMedios() {
        return PontosMedios;
    }

    public void setPontosMedios(double[] PontosMedios) {
        this.PontosMedios = PontosMedios;
    }

    public int getTamanhoRegra() {
        return tamanhoRegra;
    }

    public void setTamanhoRegra(int tamanhoRegra) {
        this.tamanhoRegra = tamanhoRegra;
    }

    public String[] getSaidas() {
        return saidas;
    }

    public void setSaidas(String[] saidas) {
        this.saidas = saidas;
    }

    public List getListRegras() {
        return ListRegras;
    }

    public void setListRegras(List ListRegras) {
        this.ListRegras = ListRegras;
    }

}
