//  OtimizacaoRegras.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.
package SistemaGenético;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.ArrayRealSolutionType;
import jmetal.encodings.solutionType.BinaryRealSolutionType;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.encodings.variable.ArrayReal;
import jmetal.util.JMException;
import jmetal.util.wrapper.XReal;

/**
 * Class representing problem OtimizacaoRegras
 */
public class OtimizacaoRegras extends Problem {

    public static boolean codSemente;
    public double maiorPercentual;
    public double interpretabilidade;
    public String melhorArquivo;
    int controleTest = 1;
    public Cromossomo cromossomo;
    public int[] vetorEntrada;
    public double[] vetorPontosMedios;
    public double[] vetorPontosIniciais;
    private int limiteInferiorSaida;
    private int limiteSuperiorSaida;
    public int TamanhoRegraTotal;
    public int TamanhoRegraEntrada;
    public ArquivoTestObjetivo gerenciamentoArquivo;
    public String nomeArquivo;
    public TestFuzzy testFuzzy;
    public String conteudoArquivoFis;
    public int MaxRegras;
    public int qtdSaida;
    public List melhorRegra;
    public static int controle;

    public double getMaiorPercentual() {
        return maiorPercentual;
    }

    public double getInterpretabilidade() {
        return interpretabilidade;
    }

    public String getMelhorArquivo() {
        return melhorArquivo;
    }

    /**
     * Constructor. Creates default instance of problem ZDT3 (30 decision
     * variables.
     *
     * @param solutionType The solution type must "Real", "BinaryReal, and
     * "ArrayReal".
     */
    // OtimizacaoRegras
    public void inicializarVariaveis(String nomeArq) throws Throwable {
        cromossomo = new ExecutavelCromossomo().getCromossomo(nomeArq);
        //maiorPercentual = 0;
        interpretabilidade = 0;
        maiorPercentual = Double.MAX_VALUE;
        codSemente = true;
        controle = 0;
        this.MaxRegras = cromossomo.getMaxRegras();
        this.vetorEntrada = cromossomo.getRegras();
        this.vetorPontosMedios = cromossomo.getPontosMedios();
        this.limiteInferiorSaida = cromossomo.getLimiteInferiorSaida();
        this.limiteSuperiorSaida = cromossomo.getLimiteSuperiorSaida();
        this.TamanhoRegraTotal = cromossomo.getTamanhoRegra();
        this.vetorPontosIniciais = cromossomo.getPontosIniciais();
        this.TamanhoRegraEntrada = this.TamanhoRegraTotal - 1;
        this.nomeArquivo = cromossomo.getNomeArquivo();
        this.qtdSaida = cromossomo.getSaida();
        this.gerenciamentoArquivo = new ArquivoTestObjetivo(nomeArquivo);
        System.out.println("Tamanho Máximo de regras - " + MaxRegras);

    }

    /**
     * Constructor. Creates a instance of ZDT3 problem.
     *
     * @param numberOfVariables Number of variables.
     * @param solutionType The solution type must "Real", "BinaryReal, and
     * "ArrayReal".
     */
    public OtimizacaoRegras(String nomeArq) throws Throwable {
        inicializarVariaveis(nomeArq);
        numberOfVariables_ = vetorEntrada.length + vetorPontosMedios.length;
        numberOfObjectives_ = 2;
        numberOfConstraints_ = 0;
        double[] semente = cromossomo.getSemente();
        problemName_ = "PontosMedios";
        upperLimit_ = new double[numberOfVariables_];
        lowerLimit_ = new double[numberOfVariables_];
        int entrada = 0;
        for (int var = 0; var < vetorEntrada.length; var++) {
            if (entrada < TamanhoRegraEntrada) {
                lowerLimit_[var] = new Integer(1);
                upperLimit_[var] = new Integer(3);
                entrada++;
            } else {
                lowerLimit_[var] = semente[var];
                upperLimit_[var] = semente[var];
                entrada = 0;
            }
        }
        entrada = 0;

        for (int var = vetorEntrada.length; var < numberOfVariables_; var++) {
            lowerLimit_[var] = calculoPontosMediosInf(vetorPontosMedios[entrada], vetorPontosIniciais[entrada]);
            upperLimit_[var] = calculoPontosMediosSup(vetorPontosMedios[entrada], vetorPontosIniciais[entrada]);
            entrada++;
        }

        solutionType_ = new ArrayRealSolutionType(this, vetorEntrada.length, semente);

    } //ZDT3

    public int[] converterDoubleInt(Solution solution) throws JMException {
        int[] doubleInt = new int[vetorEntrada.length];
        for (int i = 0; i < vetorEntrada.length; i++) {
            ArrayReal variavel = (ArrayReal) solution.getDecisionVariables()[0];
            doubleInt[i] = (int) variavel.getValue(i);
        }

        /*
         for(int i = 0; i < vetorEntrada.length; i++){
         System.out.print(doubleInt[i] + " ");
         }
         System.out.println("");
         */
        return doubleInt;
    }

    public double[] aproximacaoDouble(Solution solution) throws JMException {
        double[] real = new double[vetorPontosMedios.length];
        int posicao = 0;
        for (int i = vetorEntrada.length; i < numberOfVariables_; i++) {
            ArrayReal variavel = (ArrayReal) solution.getDecisionVariables()[0];
            double valor = variavel.getValue(i);
            BigDecimal valorExato = new BigDecimal(valor).setScale(5, RoundingMode.HALF_DOWN);
            //real[posicao] = valorExato.doubleValue(); precisão
            real[posicao] = valor;
            posicao++;
        }
        /*
         for(int i = 0; i < vetorPontosMedios.length ; i++){
         System.out.print(real[i] + " ");
         }
         System.out.println("");
         */
        return real;
    }

    public double calculoPontosMediosInf(double pontoMedio, double ponto) {
        double pontoDiferenca = (pontoMedio - ponto) / 2;
        double pontoM = pontoMedio - pontoDiferenca;
        BigDecimal valorExato = new BigDecimal(pontoM).setScale(5, RoundingMode.HALF_DOWN);
        System.out.println("Ponto Min " + valorExato);
        return pontoM;
    }

    public double calculoPontosMediosSup(double pontoMedio, double ponto) {
        double pontoDiferenca = (pontoMedio - ponto) / 2;
        double pontoM = pontoMedio + pontoDiferenca;
        BigDecimal valorExato = new BigDecimal(pontoM).setScale(5, RoundingMode.HALF_DOWN);
        System.out.println("Ponto MAX " + valorExato);
        return pontoM;
    }

    /**
     * Evaluates a solution
     *
     * @param solution The solution to evaluate
     * @throws JMException
     */
    public void evaluate(Solution solution) throws JMException {

        //if(controleTest < 1000){
        double valorP, tempInterpretabilidade;
        double[] pontosMedios = aproximacaoDouble(solution);
        int[] regrasInt = converterDoubleInt(solution);

        /*
         if (controleTest == 1) {
         for (int i = 0; i < regrasInt.length; i++) {
         System.out.print(regrasInt[i] + "-");
         }
         System.out.println("");
         }
         controleTest = 0;
         */
        Interpretabilidade inter = new Interpretabilidade(TamanhoRegraEntrada, regrasInt);
        regrasInt = inter.regrasSemelhantes();
        String conjuntoRegras = cromossomo.converterInverso(regrasInt, 1);
        gerenciamentoArquivo.alterarPontosMediosERegras(pontosMedios, conjuntoRegras);
        conteudoArquivoFis = gerenciamentoArquivo.getConteudoArquivoFis();
        testFuzzy = new TestFuzzy(conteudoArquivoFis, nomeArquivo);
        valorP = testFuzzy.testSimulado();
        tempInterpretabilidade = 1 - ((double) cromossomo.qtdRegrasAtual() / MaxRegras);
        //System.out.println(conteudoArquivoFis);
        if (valorP < maiorPercentual && ((double) cromossomo.qtdRegrasAtual()) >=3) {
            maiorPercentual = valorP;
            interpretabilidade = tempInterpretabilidade;
            ArrayReal variavel = (ArrayReal) solution.getDecisionVariables()[0];
            variavel.redefinindoIntervalo(regrasInt);
            melhorArquivo = conteudoArquivoFis;
            melhorRegra = cromossomo.getRegrasAGAtualizada();
            controle = 0;
            //System.out.println(conteudoArquivoFis);
            System.out.println("EQM  -  " + maiorPercentual);
            System.out.println("interpretabilidade - " +interpretabilidade);
        } else if(valorP == maiorPercentual && tempInterpretabilidade > interpretabilidade && ((double) cromossomo.qtdRegrasAtual()) >=3){
            maiorPercentual = valorP;
            interpretabilidade = tempInterpretabilidade;
            ArrayReal variavel = (ArrayReal) solution.getDecisionVariables()[0];
            variavel.redefinindoIntervalo(regrasInt);
            melhorArquivo = conteudoArquivoFis;
            melhorRegra = cromossomo.getRegrasAGAtualizada();
            //System.out.println(conteudoArquivoFis);
            System.out.println("EQM  -  " + maiorPercentual);
            System.out.println("interpretabilidade - " +interpretabilidade);
            controle = 0;
        }
        //controleTest++;
        solution.setObjective(0, valorP);
        solution.setObjective(1, -tempInterpretabilidade);
        //solution.setObjective(1, -tempInterpretabilidade);
        //solution.setObjective(1, -maiorPercentual);
        controle++;
        controleTest++;
       // }

    }
    
    public List getMelhorRegra(){
        return melhorRegra;
    }
    
} //evaluate



