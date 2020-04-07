/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemaFuzzy;

/**
 *
 * @author allen
 */
public class Atributo {

    String nome;
    String tipo;
    String limInferior;
    String limSuperior;

    public Atributo(String nome, String tipo, String limInferior, String limSuperior) {
        this.nome = nome;
        this.tipo = tipo;
        this.limInferior = limInferior;
        this.limSuperior = limSuperior;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getLimInferior() {
        return limInferior;
    }

    public void setLimInferior(String limInferior) {
        this.limInferior = limInferior;
    }

    public String getLimSuperior() {
        return limSuperior;
    }

    public void setLimSuperior(String limSuperior) {
        this.limSuperior = limSuperior;
    }
    
    public String toString(){
        return nome;
    }

}
