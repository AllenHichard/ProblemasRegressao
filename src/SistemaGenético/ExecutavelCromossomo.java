/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemaGenético;

import SistemaFuzzy.SistemaArquivo;
import SistemaFuzzy.Controlador;
import SistemaFuzzy.RegrasFuzzy;
import java.io.IOException;

/**
 *
 * @author allen
 */
public class ExecutavelCromossomo {
     
    public Cromossomo getCromossomo(String nome) throws Throwable{
            SistemaArquivo arquivosFuzzy = new SistemaArquivo();
            arquivosFuzzy.gerarFLC(nome);
            arquivosFuzzy.gerarRegras(nome);
            Controlador controlador = new Controlador(nome);
            RegrasFuzzy Fuzzy = arquivosFuzzy.getFuzzy();
            Cromossomo EntradaGenetica = new Cromossomo(Fuzzy, nome, arquivosFuzzy.getRegras(), 
                    arquivosFuzzy.getPontosIniciais(), arquivosFuzzy.getPontosMedios(), arquivosFuzzy.getClasse(), 
                    arquivosFuzzy.getAtributos(), arquivosFuzzy.getNomeClasseSaida(), controlador.MaxRegras());
            return EntradaGenetica;
    }
    
    
    public static void main(String[] args)  throws IOException, Throwable {
        String nomeArq[] = {"abalone","iris","vehicle","pima","haberman","ecoli", "balance","cleveland", "glass", "contraceptive","bupa" , "wisconsin" , "wine", "tae" , "hepatitis", "newthyroid", "heart"};     
        //String nomeArq[] = {"hepatitis"};
        System.out.println("Quantidade total de arquivos testes "+ nomeArq.length+ "\n");
        for(int i = 0; i < 1; i++){
            SistemaArquivo arquivosFuzzy = new SistemaArquivo();
            arquivosFuzzy.gerarFLC(nomeArq[i]);
            arquivosFuzzy.gerarRegras(nomeArq[i]);
            Controlador controlador = new Controlador(nomeArq[i]);
            //Cromossomo EntradaGenetica = new Cromossomo(nome, arquivosFuzzy.getRegras();
            //EntradaGenetica.gerarVetorEntrada();
            System.out.println("");
        }

    }
    
}
