/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemaFuzzy;

import java.util.List;

/**
 *
 * @author allen
 */
public class GrauPertinencia {
    
    private List classificacao; // alto. baixo. medio
    private double GrauPertinencia;

    public GrauPertinencia(List atributos, double GrauPertinencia) {
        this.classificacao = atributos;
        this.GrauPertinencia = GrauPertinencia;
    }
    
    

    public List getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(List classificacao) {
        this.classificacao = classificacao;
    }

    public double getGrauPertinencia() {
        return GrauPertinencia;
    }

    public void setGrauPertinencia(double GrauPertinencia) {
        this.GrauPertinencia = GrauPertinencia;
    }
    
    public boolean contains(Object o){
         List c = (List) o;   
         return this.classificacao.equals(c);
    }
    
}
