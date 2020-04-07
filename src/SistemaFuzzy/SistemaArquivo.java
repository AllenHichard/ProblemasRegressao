package SistemaFuzzy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class SistemaArquivo {

    boolean PM = false;
    String[] saidas;
    List regrasCromossomo;
    List<Double> pontosMedios;
    List<Double> pontosIniciais;
    RegrasFuzzy Fuzzy;
    BufferedReader ler;
    BufferedWriter gravar;
    List atributos;
    List classes; // primeiro da lista é o nome, os demais são as classificações;

    public void gerarFLC(String nome) throws IOException {
        atributos = new ArrayList();
        classes = new ArrayList();
        criandoArquivoFLC(nome);
    }

    public void gerarRegras(String nome) throws IOException {
        atributos = new ArrayList();
        classes = new ArrayList();
        criandoArquivoFLC2(nome);
    }

    public SistemaArquivo() throws IOException, Throwable {
        // Aqui gera um arquivo sem regras;
        pontosMedios = new ArrayList<>();
        pontosIniciais = new ArrayList<>();

        // utiliza o arquivo gerado anteriormente para um sistema fuzzy gerar as regras
    }

    public void criandoArquivoFLC(String nome) throws FileNotFoundException, IOException {
        ler = new BufferedReader(new FileReader("entrada\\" + nome + ".DAT"));
        gravar = new BufferedWriter(new FileWriter("semente\\" + nome + "WM.FLC"));
        String linha = ler.readLine();
        gravar.write("FUNCTION_BLOCK " + linha.split(" ")[1] + "\r\n\r\n");
        String linhatual = carregandoAtributos();
        carregandoClasses(linhatual);
        varInput();
        varOutput();
        fuzzify();
        defuzzify();
        ruleblock();
        gravar.write("END_FUNCTION_BLOCK ");
        gravar.flush();
        gravar.close();
        ler.close();

    }

    public void criandoArquivoFLC2(String nome) throws FileNotFoundException, IOException {
        ler = new BufferedReader(new FileReader("entrada\\" + nome + ".DAT"));
        gravar = new BufferedWriter(new FileWriter("semente\\" + nome + ".FLC"));
        String linha = ler.readLine();
        gravar.write("FUNCTION_BLOCK " + linha.split(" ")[1] + "\r\n\r\n");
        String linhatual = carregandoAtributos();
        carregandoClasses(linhatual);
        varInput();
        varOutput();
        fuzzify();
        defuzzify();
        ruleblock2(nome);
        gravar.write("END_FUNCTION_BLOCK ");
        gravar.close();
        ler.close();
    }

    private String carregandoAtributos() throws IOException {
        String linha = ler.readLine();
        while (!linha.contains("{")) {
            String aux = linha.split(" ")[3];
            String aux2 = linha.split(" ")[4];
            atributos.add(new Atributo(linha.split(" ")[1], linha.split(" ")[2],
                    aux.substring(aux.indexOf("[") + 1, aux.lastIndexOf(",")),
                    aux2.substring(0, aux2.lastIndexOf("]"))));
            linha = ler.readLine();
        }
        return linha;
    }

    private void carregandoClasses(String linhatual) throws IOException {
        classes.add(linhatual.substring(linhatual.indexOf(" ") + 1, linhatual.indexOf(" {")));
        String tipoclasse = linhatual.substring(linhatual.indexOf("{") + 1, linhatual.lastIndexOf("}"));
        classes.add(tipoclasse);

    }

    private void varInput() throws IOException {
        ler.readLine();
        gravar.write("\tVAR_INPUT\r\n");
        Iterator it = atributos.iterator();
        while (it.hasNext()) {
            Atributo input = (Atributo) it.next();
            if (input.getTipo().equals("integer")) {
                gravar.write("\t\t");
                gravar.write(input.getNome() + ": " + "real" + ";");
                gravar.write("\r\n");
            } else {
                gravar.write("\t\t");
                gravar.write(input.getNome() + ": " + input.getTipo() + ";");
                gravar.write("\r\n");
            }
        }
        gravar.write("\tEND_VAR\r\n");
        gravar.write("\r\n");
    }

    private void varOutput() throws IOException {
        ler.readLine();
        gravar.write("\tVAR_OUTPUT\r\n");
        gravar.write("\t\t");
        gravar.write(classes.get(0).toString() + ": real;");
        gravar.write("\r\n");
        gravar.write("\tEND_VAR\r\n");
        gravar.write("\r\n");
    }

    private void fuzzify() throws IOException {
        double mediana;
        Iterator it = atributos.iterator();
        while (it.hasNext()) {
            Atributo input = (Atributo) it.next();
            gravar.write("\t");
            gravar.write("FUZZIFY " + input.getNome());
            gravar.write("\r\n");
            mediana = Double.parseDouble(input.limInferior) / 2 + Double.parseDouble(input.limSuperior) / 2;
            if (PM) {
                pontosMedios.add(mediana);
                pontosIniciais.add(Double.parseDouble(input.limInferior));
            }
            definirIntervalos(mediana, input);
            gravar.write("\tEND_FUZZIFY\r\n");
            gravar.write("\r\n");
        }
        PM = true;
    }

    private void definirIntervalos(double mediana, Atributo input) throws IOException {
        String triangulo = null;
        triangulo = "(" + input.limInferior + ", 1) (" + input.limInferior + ", 1) (" + mediana + ", 0)";
        gravar.write("\t\t");
        gravar.write("TERM BAIXA := " + triangulo + " ;");
        gravar.write("\r\n");
        triangulo = "(" + input.limInferior + ", 0) (" + mediana + ", 1) (" + input.limSuperior + ", 0)";
        gravar.write("\t\t");
        gravar.write("TERM MEDIA := " + triangulo + " ;");
        gravar.write("\r\n");
        triangulo = "(" + mediana + ", 0) (" + input.limSuperior + ", 1) (" + input.limSuperior + ", 1)";
        gravar.write("\t\t");
        gravar.write("TERM ALTA  := " + triangulo + " ;");
        gravar.write("\r\n");

    }

    private String compactar(String x[]) {
        String juntar = "";
        for (int i = 0; i < x.length; i++) {
            juntar += x[i];
        }
        return juntar;
    }

    private void defuzzify() throws IOException {
        //alterando aqui
        /*
        
         */

        gravar.write("\t");
        gravar.write("DEFUZZIFY " + classes.get(0).toString());
        //System.out.println(classes.get(0).toString());
        gravar.write("\r\n");
        String x[];
        if (classes.get(1).toString().contains(", ")) {
            x = classes.get(1).toString().split(", ");
        } else {
            x = classes.get(1).toString().split(",");
        }
        String aux[] = new String[x.length];

        saidas = x;
        deffuzNumero(x);
        /*
         if(!Pattern.matches("[-]?\\d*[.]?\\d+", x[0])){            
         for(int i = 0; i < x.length; i++){
         aux[i] = "" + (+i+1);
         }
         deffuzNome(x, aux);
         }else{
        
            
         }
         */
    }

    /*
    private void deffuzNome(String x[], String aux[]) throws IOException {
        String triangulo;
        triangulo = "(" + aux[0] + ", 1) (" + aux[0] + ", 1) (" + aux[1] + ", 0)";
        gravar.write("\t\t");
        gravar.write("TERM " + ConversaoValor.nomeValor(x[0]) + " := " + triangulo + " ;");
        gravar.write("\r\n");

        for (int i = 1; i < x.length - 1; i++) {
            triangulo = "(" + aux[i - 1] + ", 0) (" + aux[i] + ", 1) (" + aux[i + 1] + ", 0)";
            gravar.write("\t\t");
            gravar.write("TERM " + ConversaoValor.nomeValor(x[i]) + " := " + triangulo + " ;");
            gravar.write("\r\n");
        }
        triangulo = "(" + aux[aux.length - 2] + ", 0) (" + aux[aux.length - 1] + ", 1) (" + aux[aux.length - 1] + ", 1)";
        gravar.write("\t\t");
        gravar.write("TERM " + ConversaoValor.nomeValor(x[x.length - 1]) + " := " + triangulo + " ;");
        gravar.write("\r\n");

        //gravar.write("TERM "+ juntar+ " := " + triangulo + " ;");
        gravar.write("\r\n");
        gravar.write("\t\t");
        gravar.write("METHOD : COG;\r\n");
        gravar.write("\t\t");
        gravar.write("DEFAULT := 0;\r\n");
        gravar.write("\tEND_DEFUZZIFY\r\n");
        gravar.write("\r\n");

    }
    */

    private void deffuzNumero(String x[]) throws IOException {
        String triangulo;
        
        double media = Double.parseDouble(x[0]) / 2 + Double.parseDouble(x[1]) / 2;

        triangulo = "(" + x[0]+ ", 1) (" + x[0] + ", 1) (" + media + ", 0)";
        gravar.write("\t\t");
        gravar.write("TERM BAIXO := " + triangulo + " ;");
        gravar.write("\r\n");
        triangulo = "(" + x[0]+ ", 0) (" + media + ", 1) (" + x[1] + ", 0)";
        gravar.write("\t\t");
        gravar.write("TERM MEDIO := " + triangulo + " ;");
        gravar.write("\r\n");
        triangulo = "(" + media + ", 0) (" + x[1] + ", 1) (" + x[1]+ ", 1)";
        gravar.write("\t\t");
        gravar.write("TERM ALTO  := " + triangulo + " ;");
        gravar.write("\r\n");

        //gravar.write("TERM "+ juntar+ " := " + triangulo + " ;");
        gravar.write("\r\n");
        gravar.write("\t\t");
        gravar.write("METHOD : COG;\r\n");
        gravar.write("\t\t");
        gravar.write("DEFAULT := 0;\r\n");
        gravar.write("\tEND_DEFUZZIFY\r\n");
        gravar.write("\r\n");

    }

    private void ruleblock() throws IOException {
        gravar.write("\t");
        gravar.write("RULEBLOCK No1 ");
        gravar.write("\r\n");
        gravar.write("\t\t");
        gravar.write("AND : MIN; \r\n");
        gravar.write("\t\t");
        gravar.write("ACT : MIN; \r\n");
        gravar.write("\t\t");
        gravar.write("ACCU : MAX; \r\n\r\n");
        gravar.write("\t\r\n");
        gravar.write("\tEND_RULEBLOCK \r\n\r\n");
    }

    private void ruleblock2(String nome) throws IOException {
        gravar.write("\t");
        gravar.write("RULEBLOCK No1 ");
        gravar.write("\r\n");
        gravar.write("\t\t");
        gravar.write("AND : MIN; \r\n");
        gravar.write("\t\t");
        gravar.write("ACT : MIN; \r\n");
        gravar.write("\t\t");
        gravar.write("ACCU : MAX; \r\n\r\n");
        metodoWM(nome);
        gravar.write("\t\r\n");
        gravar.write("\tEND_RULEBLOCK \r\n\r\n");
    }

    private void metodoWM(String nome) throws IOException {
        ler.readLine(); //@data
        Regras regras = new Regras(ler, gravar, (String) classes.get(0), atributos, nome);
        regrasCromossomo = regras.getRegraCromossomo();
        Fuzzy = regras.getFuzzy();
    }

    
    public RegrasFuzzy getFuzzy(){
        return Fuzzy;
    }
    
    public String[] getClasse() {
        saidas = new String[3];
        saidas[0] = "BAIXO";
        saidas[1] = "MEDIO";
        saidas[2] = "ALTO";
        return saidas;
    }
    
    /*
    public String[] getClasse() {
        return saidas;
    }
    */

    public List getRegras() {
        return regrasCromossomo;
    }

    public double[] getPontosMedios() {
        double[] pontoMedio = new double[pontosMedios.size()];
        for (int i = 0; i < pontosMedios.size(); i++) {
            pontoMedio[i] = pontosMedios.get(i);
        }
        return pontoMedio;
    }

    public double[] getPontosIniciais() {
        double[] pontoIniciais = new double[pontosIniciais.size()];
        for (int i = 0; i < pontosIniciais.size(); i++) {
            pontoIniciais[i] = pontosIniciais.get(i);
        }
        return pontoIniciais;
    }

    public List getAtributos() {
        return atributos;
    }

    public String getNomeClasseSaida() {
        return classes.get(0).toString();
    }
}
