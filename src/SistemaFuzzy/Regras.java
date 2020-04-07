/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemaFuzzy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author allen
 */
public class Regras {
    
    private List regrasCromossomo;
    private BufferedReader ler;
    private List linhaEntrada;
    private List colunaEntrada;
    private int porc; // porcentagem reservada para teste
    private double porcentagemBaseRegra = 1;
    private String nameOutput;
    BufferedWriter gravar;
    List atributos;
    String nomeArquiAtual;
    RegrasFuzzy fuzzy;
    
    public Regras(){}

    
    public Regras(BufferedReader ler, BufferedWriter gravar, String name, List atributos, String nomeArq) throws IOException {
        this.regrasCromossomo = new ArrayList();
        this.ler = ler;
        this.nomeArquiAtual = nomeArq;
        this.gravar = gravar;
        linhaEntrada = new LinkedList();
        nameOutput = name;
        this.atributos = atributos;
        carregar();
        regras();
    }
    

    private void carregar() throws IOException {
        String line = ler.readLine();
        while (line != null) {
            String aux[];
            if (line.contains(", ")) {
                aux = line.split(", ");
            } else {
                aux = line.split(",");
            }
            colunaEntrada = new LinkedList();
            for (int i = 0; i < aux.length; i++) {    
                /*if (aux[i].contains("-")) {
                    aux[i] = aux[i].split("-")[0] + aux[i].split("-")[1];
                }*/
                colunaEntrada.add(aux[i]);
            }
            linhaEntrada.add(colunaEntrada);
            line = ler.readLine();
        }
        porc = (int) (linhaEntrada.size() * porcentagemBaseRegra);
    }

    private void regras() throws IOException {
        int nRegras = 1;
        RegrasFuzzy c = new RegrasFuzzy(nomeArquiAtual, nameOutput);
        fuzzy = c;
        List linhaEstradaTemp = new LinkedList();
        List<GrauPertinencia> Listagrau = new ArrayList<>();
        for (int i = 0; i < porc; i++) {
            List a = (List) linhaEntrada.get(i);
            gravar.flush();
            boolean existeregra = false;
            int indiceRegraCompare = 0;
            GrauPertinencia grau = c.calculaInferencia(a, atributos);
            List aux = grau.getClassificacao();
            for (int k = 0; k < Listagrau.size(); k++) {
                GrauPertinencia g = (GrauPertinencia) Listagrau.get(k);
                if (g.getClassificacao().equals(aux)) {
                    existeregra = true;
                    indiceRegraCompare = k;
                    break;
                }
            }
            
            
            
            if (!existeregra) {
                Listagrau.add(grau);
                linhaEstradaTemp.add(a);
            
                //nRegras = salvar(nRegras, aux, a);

            } else {
                GrauPertinencia g = Listagrau.get(indiceRegraCompare);
                if (g.getGrauPertinencia() < grau.getGrauPertinencia()) {
                    Listagrau.remove(indiceRegraCompare);
                    Listagrau.add(indiceRegraCompare, grau);
                    linhaEstradaTemp.remove(indiceRegraCompare);
                    linhaEstradaTemp.add(indiceRegraCompare, a);
                }
            }
            
        }

        nRegras = 1;
        for (int i = 0; i < Listagrau.size(); i++) {
            List aux = (List) Listagrau.get(i).getClassificacao();
            List a = (List) linhaEstradaTemp.get(i);
            regrasCromossomo.addAll(aux);
            regrasCromossomo.add(a.get(a.size() - 1).toString());
            salvar(nRegras, aux, a, c);
            nRegras++;
        }

    }

    public int salvar(int nRegras, List aux, List a, RegrasFuzzy c) throws IOException {
        String regra = "\t\tRULE " + nRegras + " : IF ";
        for (int j = 0; j < atributos.size() - 1; j++) {
            Atributo atributo = (Atributo) atributos.get(j);
            regra += atributo.getNome() + " IS " + aux.get(j) + " AND ";
        }
        Atributo atributo = (Atributo) atributos.get(atributos.size() - 1);
        regra += atributo.getNome() + " IS " + aux.get(atributos.size() - 1) + " THEN ";
        //String saida = ConversaoValor.nomeValor(a.get(a.size() - 1).toString());
        String saida = c.conjuntosFuzzyAtivados2(a.get(a.size() - 1).toString());
        regra += nameOutput + " IS " + saida + ";\r\n ";
        //regra += nameOutput + " IS " + "um" + ";\r\n ";
        gravar.write(regra);
        nRegras++;
        return nRegras;

    }
    
    public List getRegraCromossomo(){
        return this.regrasCromossomo;
    }
    
    public RegrasFuzzy getFuzzy(){
        return fuzzy;
    }
        //for (int i = 0; i < classes.size(); i++) {
    // System.out.println(classes.get(i).toString());
    // }
}

/*
                
 */
