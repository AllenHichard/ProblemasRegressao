/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemaFuzzy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.jFuzzyLogic.FIS;
import org.antlr.runtime.RecognitionException;

/**
 *
 * @author allen
 */
public class RegrasFuzzy {

    private FIS fis;
    private List atributos;
    List valoresTnorma;
    double tnorma;
    String nomeSaida;

    
    public RegrasFuzzy(String nomeArq, String nomeSaida) {
        this.nomeSaida = nomeSaida;
        iniciaInferencia(nomeArq);
    }
    

    private void iniciaInferencia(String nomeArq) {
        try {
            
            BufferedReader ler = new BufferedReader(new FileReader("semente\\"+nomeArq+"WM.FLC"));
            String linha = ler.readLine();
            String conteudoArquivoFis = "";
            while(linha !=null ){
                conteudoArquivoFis += linha + "\n";
                linha = ler.readLine();
            }
            ler.close();
            new File("semente\\"+nomeArq+"WM.FLC").deleteOnExit();
            
            fis = FIS.createFromString(conteudoArquivoFis, true);
            //System.out.println("Instancia de inferencias carregada com sucesso");
        } catch (Exception ex) {
                Logger.getLogger(RegrasFuzzy.class.getName()).log(Level.SEVERE, "Erro ao abrir o arquivo", ex);
        }
    }
    
    public GrauPertinencia calculaInferencia(List valores, List atributos) {
        
        this.atributos = atributos;
        for(int i=0; i<atributos.size(); i++){
            Atributo a = (Atributo)atributos.get(i);
            this.fis.setVariable(a.getNome(), Double.parseDouble((String) valores.get(i)));
            this.fis.evaluate();
        }  
        List temp = conjuntosFuzzyAtivados();
        tnorma = 1;
        for(int i =0; i < valoresTnorma.size(); i++){
                tnorma *= Double.parseDouble(valoresTnorma.get(i).toString());
        }
        GrauPertinencia grau = new GrauPertinencia(temp, tnorma);
        return grau;
    }
    
    
    

    public List calculaInferencia(double x1, double x2, double x3, double x4) {
        this.fis.setVariable("SepalLength", x1);
        this.fis.setVariable("SepalWidth", x2);
        this.fis.setVariable("PetalLength", x3);
        this.fis.setVariable("PetalWidth", x4);
        this.fis.evaluate();
        return conjuntosFuzzyAtivados();
    }

    private List conjuntosFuzzyAtivados() {
        double valor;
        double temp;
       
        
        String clas;
        String varEnt[] = new String[atributos.size()];
        for(int i = 0; i < atributos.size(); i++){
            Atributo a = (Atributo)atributos.get(i);
            varEnt[i] = a.getNome();
        }
        String variacao[] = {"BAIXA", "MEDIA", "ALTA"};
        List classes = new ArrayList();
        valoresTnorma = new LinkedList();
            for (int j = 0; j < varEnt.length; j++) {
            valor = 0;
            temp = 0;
            clas = null;
            for (int i = 0; i < variacao.length; i++) {
                temp = this.fis.getVariable(varEnt[j]).getMembership(variacao[i]);
                if (temp >= valor) {
                    valor = temp;
                    clas = variacao[i];
                }
                
                //tnorma *= temp;
            }
            valoresTnorma.add(valor);
            classes.add(clas);
        }
        //System.out.println(this.fis.getVariable("SepalLength"));
        //System.out.println(this.fis.getVariable("SepalWidth"));
        //System.out.println(this.fis.getVariable("PetalLength"));
        //System.out.println(this.fis.getVariable("PetalWidth"));
        return classes;

    }
    
    public  String conjuntosFuzzyAtivados2(String valor) {
        double value = Double.parseDouble(valor);
        //System.out.println(value);
        this.fis.setVariable(nomeSaida, value);
        double valorM = 0;
        String nome = "";
        List classificacao = new  ArrayList();
        classificacao.add("BAIXO");
        classificacao.add("MEDIO");
        classificacao.add("ALTO");
        for (int i = 0; i < classificacao.size(); i++) {
            double atual = fis.getVariable(nomeSaida).getMembership(classificacao.get(i).toString());
            if (atual > valorM) {
                valorM = atual;
                nome = classificacao.get(i).toString();
            }
        }
        //System.out.println(nome);
        //System.out.println(this.fis.getVariable(nomeSaida));
        return nome;
    }
}
