
package SistemaGenético;

import SistemaFuzzy.Controlador;
import SistemaFuzzy.ConversaoValor;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
public class TestFuzzy {

    private FIS fis;
    private List atributos;
    private List classificacao;
    private String saida;
    private List linhaEntrada;
    private List colunaEntrada;
    private List TestEntrada; // pega os valores do arquivo para teste
    private List TestSimulado; // pega os valores fuzzy
    private String nomeArquivo;

    

    public TestFuzzy(String conteudoArquivoFis, String nomeArquivo) {
        try {
            classificacao = new LinkedList();
            atributos = new LinkedList();
            linhaEntrada = new LinkedList();
            TestEntrada = new LinkedList();
            TestSimulado = new LinkedList();
            this.nomeArquivo = nomeArquivo;
            //System.out.println(conteudoArquivoFis);
            fis = FIS.createFromString(conteudoArquivoFis, true);
            dadosEntrada();
            calculaInferencia();
            //testSimulado();
            
        } catch (RecognitionException ex) {
            Logger.getLogger(TestFuzzy.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestFuzzy.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    public void classificacao(String linhatual) {

        String tipoclasse = linhatual.substring(linhatual.indexOf("{") + 1, linhatual.lastIndexOf("}"));
        String aux[] = tipoclasse.split(",");
        for (int i = 0; i < aux.length; i++) {
            classificacao.add(aux[i]);
        }
    }

    public void dadosEntrada() throws FileNotFoundException, IOException {
        BufferedReader ler = new BufferedReader(new FileReader("entrada\\" + nomeArquivo + "AG.DAT"));
        String linha = ler.readLine();
        while (!linha.contains("{")) {
            linha = ler.readLine();
        }
        classificacao(linha);
        linha = ler.readLine(); // input
        String[] var = linha.split(" ");
        for (int i = 1; i < var.length; i++) {
            atributos.add(var[i].split(",")[0]);
        }
        linha = ler.readLine(); // output
        saida = linha.split(" ")[1];
        ler.readLine();
        linha = ler.readLine();
        while (linha != null) {
            String aux[];
            if (linha.contains(", ")) {
                aux = linha.split(", ");
            } else {
                aux = linha.split(",");
            }
            colunaEntrada = new LinkedList();
            for (int i = 0; i < aux.length; i++) {
                /*if (aux[i].contains("-")) {
                    aux[i] = aux[i].split("-")[0] + aux[i].split("-")[1];
                }
                */
                colunaEntrada.add(aux[i]);
            }
            linhaEntrada.add(colunaEntrada);
            linha = ler.readLine();
        }

    }

    public void calculaInferencia() {
        //linhaEntrada.size()
        for (int i = 0; i < linhaEntrada.size(); i++) {
            List coluna = (List) linhaEntrada.get(i);
            
            for (int j = 0; j < atributos.size(); j++) {
                this.fis.setVariable(atributos.get(j).toString(), Double.parseDouble((String) coluna.get(j)));
            }
            double classificacaoSaida = Double.parseDouble(coluna.get(coluna.size()-1).toString());
            TestEntrada.add(classificacaoSaida);
            //System.out.println(coluna.get(coluna.size()-1));
            this.fis.evaluate();
            double classe = this.fis.getVariable(saida).getLatestDefuzzifiedValue();
            //System.out.println(classe);
            conjuntosFuzzyAtivados();
        }
    }

    //public static void main(String[] args) throws IOException {
    //Controlador c = new Controlador();
    //}
    
    private void conjuntosFuzzyAtivados() {
        TestSimulado.add(this.fis.getVariable(saida).getLatestDefuzzifiedValue());
    }
    
    
    /*
    private void conjuntosFuzzyAtivados() {
        double valorM = 0;
        String nome = "";
        for (int i = 0; i < classificacao.size(); i++) {
            double atual = fis.getVariable(saida).getMembership(classificacao.get(i).toString());
            if (atual > valorM) {
                valorM = atual;
                nome = classificacao.get(i).toString();
            }
        }
        TestSimulado.add(nome);
    }
    */
    public double testSimulado() {
        double et = 0;
        linhaEntrada.size();
        
        for (int i = 0; i < TestEntrada.size(); i++) {
           
            double entrada = (double) TestEntrada.get(i);
            double classif = (double) TestSimulado.get(i);
             //if(classif == 0.0){
                 //System.out.println(classif);
             //}
             //System.out.println(entrada + " " + classif);
            et += Math.pow(entrada - classif,2);
        }
        //System.out.println(TestEntrada.size());
        double EQM = et/TestEntrada.size();
        //System.out.println(TestEntrada.get(0).toString());
        //System.out.println(TestSimulado.get(0).toString());
        //System.out.println("Nome do Arquivo: " + nomeArquivo);
        //System.out.println("Total de dados entrada: " + TestEntrada.size());
        //System.out.println("Contagem: " + count);
        //System.out.println("Percentual de Acertos: " + ((double) count / TestEntrada.size()));
       //System.out.println("error quadratico medio: " + EQM);
        return EQM;
    
    }

    

}
