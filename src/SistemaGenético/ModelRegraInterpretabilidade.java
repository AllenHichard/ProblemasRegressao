/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemaGenético;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author allen
 */
public class ModelRegraInterpretabilidade {

    List entrada;
    boolean existe;
    
    public ModelRegraInterpretabilidade(){
        existe = false;
        entrada = new ArrayList();
    }
    
    public void setEntrada(List ent){
        entrada = ent;
    }
    
    public boolean equals(Object o){
        List lista = (List) o;
        existe =  entrada.equals(lista);
        System.out.println(existe);
        return existe;
    }
}
