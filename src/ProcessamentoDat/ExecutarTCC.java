/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProcessamentoDat;

import SistemaGenético.NSGAII_main;
import java.io.IOException;
import jmetal.metaheuristics.nsgaII.NSGAII;

/**
 *
 * @author allen
 */
public class ExecutarTCC {
    
    // "diabetes","machineCPU","autoMPG6","autoMPG8","ele-1","dee","plastic","","laser","forestFires","quake","ele-2","stock","wankara","wizmir","friedman","mortgage","treasury","concrete","ANACALT","abalone","delta_ail","delta_elv","california","compactiv","elevators","mv","house"
    // 
    public static void main(String[] args) throws IOException, SecurityException, ClassNotFoundException, Throwable {
        String nomeArq[] = {"forestFires"};
        
        String nome;
        for(int i = 0; i < 1; i++){
            nome = nomeArq[i];
            new NSGAII_main(nome);
        }
    }
}
