/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemaGenético;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author allen
 */
public class Interpretabilidade {

    private int tamVar;
    private int tamRegra;
    private int tamanhoConjuntoRegras;
    private List regraUnica;
    private List regrasSeparadas;
    private int[] regrasInt;
    private int[] novasRegrasInt;
    private int[] novasRegrasInt2;

    public Interpretabilidade(int tamVar, int[] regrasInt) {
        this.regrasSeparadas = new ArrayList(); 
        this.regrasInt = regrasInt;
        this.tamVar = tamVar;
        this.tamRegra = tamVar + 1;
        this.tamanhoConjuntoRegras = regrasInt.length;
        this.novasRegrasInt = new int[tamanhoConjuntoRegras];
        this.novasRegrasInt2 = new int[tamanhoConjuntoRegras];
        this.regraUnica = new ArrayList();
        regrasIguais();
        reconstracao();
        regrasSemelhantes();
        
        
    }

    private void regrasIguais() {
        for (int i = 0; i < tamanhoConjuntoRegras; i++) {
            regraUnica.add(regrasInt[i]);
            if ((i + 1) % tamRegra == 0) {
                if (equals(regraUnica)) {
                    //System.out.println("Não entrou");
                    regrasSeparadas.add(new ArrayList());
                } else {
                    //System.out.println("Entrou");
                    regrasSeparadas.add(regraUnica);
                }
                regraUnica = new ArrayList();
            } 
        }
        
    }
     public int[] reconstracao() {
        
        int index = 0;
        Iterator it = regrasSeparadas.iterator();
        while (it.hasNext()) {
            List regra = (List) it.next();
            if (regra.size() == 0) {  
                for (int i = 0; i < tamRegra; i++) {
                    novasRegrasInt[index] = 0;
                    index++;
                }
            } else {
                for (int i = 0; i < tamRegra; i++) {
                    novasRegrasInt[index] = (int) regra.get(i);
                    index++;
                    
                }
            }
        }
        // printVetor(novasRegrasInt);
        return novasRegrasInt;
    }
    public int[] regrasSemelhantes() {
        List ModelReListInter = new ArrayList();
        List excluir = new ArrayList();
        regraUnica = new ArrayList();
        for (int i = 0; i < tamanhoConjuntoRegras; i++) {
            regraUnica.add(novasRegrasInt[i]);
            novasRegrasInt2[i] = novasRegrasInt[i];
            if ((i + 1) % (tamRegra) == 0) {
                regraUnica.remove(regraUnica.size()-1);
                //System.out.println(regraUnica.toString());
                if(ModelReListInter.contains(regraUnica) == false){
                    ModelReListInter.add(regraUnica);
                    regraUnica = new ArrayList();
                } else {
                    if(!excluir.contains(regraUnica)){
                         excluir.add(regraUnica);
                    }
                    for(int j = (i+1)-tamRegra; j<=i; j++){
                        novasRegrasInt2[j] = 0;
                    }
                    regraUnica = new ArrayList();
                    //System.out.println("true");
                }
            } 
                
        }
        //printVetor(novasRegrasInt2);
        excluirRestoIgual(excluir);
        return novasRegrasInt2;
    }

    private void excluirRestoIgual(List excluir) {
        regraUnica = new ArrayList();
        for (int i = 0; i < tamanhoConjuntoRegras; i++) {
            regraUnica.add(novasRegrasInt2[i]);
            if ((i + 1) % (tamRegra) == 0) {
                regraUnica.remove(regraUnica.size()-1);
                if(excluir.contains(regraUnica)){
                    //System.out.println(regraUnica);
                    //System.out.println(excluir.toString());
                    for(int j = (i+1)-tamRegra; j<=i; j++){
                        novasRegrasInt2[j] = 0;
                    }
                }
                regraUnica = new ArrayList();
            }
        }
        //printVetor(novasRegrasInt2);
        
        
    }

    private boolean equals(List regraUnica) {
        Iterator it = regrasSeparadas.iterator();
        while (it.hasNext()) {
            List regra = (List) it.next();
            if (regra.equals(regraUnica)) {
                return true;
            }
        }
        return false;
    }

   
    
    public void printVetor(int [] vetor) {
        String novoVetor = "";
        for (int i = 0; i < tamanhoConjuntoRegras; i++) {
            novoVetor += vetor[i] + " ";
        }
        System.out.println(novoVetor);
        
    }

    public String toString() {
        String novoVetor = "";
        for (int i = 0; i < tamanhoConjuntoRegras; i++) {
            novoVetor += novasRegrasInt2[i] + " ";
        }
        return novoVetor;
    }

    public static void main(String[] args) {
        int[] regrasInt = {1, 2, 2, -3, 1, 2, 2, -3, 1, 2, 2, -1, 1, 2, 3, -2, 1, 2, 3, -2, 1, 2, 3, -3};
        Interpretabilidade inter = new Interpretabilidade(3, regrasInt);
        System.out.println(inter.toString());
    }

    /*
     public void Deffuzificacao() {
     Iterator i = linhaEntrada.iterator();
     Iterator j;
     int controle;
     //int c = 1;
     while (i.hasNext()) {
     //System.out.println(c++);
     List coluna = (List) i.next();
     j = atributos.iterator();
     controle = 0;
     while (j.hasNext()) {
     String var = (String) j.next();
     this.fis.setVariable(var, Double.parseDouble((String) coluna.get(controle)));
     controle++;
     }
     String classificacaoSaida = ConversaoValor.nomeValor(coluna.get(coluna.size() - 1).toString());
     TestEntrada.add(classificacaoSaida);
     DeffuzificacaoFuzzyAtivados();
     }

     }

     private void DeffuzificacaoFuzzyAtivados() {
     //int c = 1;
     double melhorTNorma = 0;
     double tnorma = 1; // calculo para encontrar a melhor regra    
     int controle = 0;
     String melhorClassificacao = "";
     for (int i = 0; i < regras.size(); i++) {
     // System.out.println(c++);
     if ((i + 1) % (varEntrada + 1) != 0) {
     //System.out.println(atributos.get(controle).toString() + " " + regras.get(i).toString());
     double grau = fis.getVariable(atributos.get(controle).toString()).getMembership(regras.get(i).toString());
     //System.out.println(grau);
     tnorma = tnorma*grau;
     } else {
     //System.out.println(tnorma);
     if(tnorma > melhorTNorma){
     melhorTNorma = tnorma;
     tnorma = 1;
     melhorClassificacao = regras.get(i).toString();
                     
     } 
     //System.out.println(" ");  System.out.println(regras.get(i)); System.out.println(" ");
     }
     if (controle < varEntrada) {
     controle++;
     } else {
     controle = 0;
     }
     }
     //System.out.println(melhorClassificacao);
     //System.out.println(melhorTNorma);
     TestSimulado.add(melhorClassificacao);
     }
    
     private void testSimulado() {
     int count = 0;
     linhaEntrada.size();
     for (int i = 0; i < TestEntrada.size(); i++) {
     if (TestEntrada.get(i).toString().equals(TestSimulado.get(i).toString())) {
     count++;
     }
     }
     //System.out.println(TestEntrada.get(0).toString());
     //System.out.println(TestSimulado.get(0).toString());
     System.out.println("Nome do Arquivo: " + nomeArquivo);
     System.out.println("Total de dados entrada: " + TestEntrada.size());
     System.out.println("Total de acertos do sistema: " + count);
     System.out.println("Percentual de Acertos: " + ((double) count / TestEntrada.size()));
     }
    
     */

    
}
    

