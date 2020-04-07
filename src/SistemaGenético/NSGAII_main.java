//  NSGAII_main.java
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

import ProcessamentoDat.distribuirPercentuais;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import jmetal.metaheuristics.nsgaII.*;
import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.problems.ProblemFactory;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Configuration;
import jmetal.util.JMException;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Class to configure and execute the NSGA-II algorithm.
 *
 * Besides the classic NSGA-II, a steady-state version (ssNSGAII) is also
 * included (See: J.J. Durillo, A.J. Nebro, F. Luna and E. Alba "On the Effect
 * of the Steady-State Selection Scheme in Multi-Objective Genetic Algorithms"
 * 5th International Conference, EMO 2009, pp: 183-197. April 2009)
 */
public class NSGAII_main {

    //public static Logger logger_;      // Logger object
    //public static FileHandler fileHandler_; // FileHandler object

    /**
     * @param args Command line arguments.
     * @throws JMException
     * @throws IOException
     * @throws SecurityException Usage: three options -
     * jmetal.metaheuristics.nsgaII.NSGAII_main -
     * jmetal.metaheuristics.nsgaII.NSGAII_main problemName -
     * jmetal.metaheuristics.nsgaII.NSGAII_main problemName paretoFrontFile
     */
    public NSGAII_main(String fold) throws JMException, SecurityException, IOException, ClassNotFoundException, Throwable {
        int datasets = 5;
        String nomeArq[] = new String[datasets];
        double acuracias[] = new double[datasets];
        double interpretabilidades[] = new double[datasets];
        double geracao[] = new double[datasets];
        File diretorio = new File("saida\\" + fold);
        diretorio.mkdir();
        for (int t = 1; t <= 1; t++) {
            diretorio = new File("saida\\" + fold + "\\" + "TREINAMENTO" + t);
            diretorio.mkdir();
            
            for (int i = 1; i <= datasets; i++) {
                nomeArq[i - 1] = fold + "-5-" + i + "tra";
                //nomeArq[i-1] = fold;
            }
            String nomeArquivo = null;
            for (int i = 1; i < datasets; i++) {
                nomeArquivo = nomeArq[i];
                new distribuirPercentuais(fold, nomeArquivo).distribuindo();

                Problem problem; // The problem to solve
                Algorithm algorithm; // The algorithm to use
                Operator crossover; // Crossover operator
                Operator mutation; // Mutation operator
                Operator selection; // Selection operator

                HashMap parameters; // Operator parameters

                QualityIndicator indicators; // Object to get quality indicators

                // Logger object and file to store log messages
                //logger_ = Configuration.logger_;
                //fileHandler_ = new FileHandler("NSGAII_main.log");
                //logger_.addHandler(fileHandler_);

                indicators = null;
                
                 problem = new OtimizacaoRegras(nomeArquivo);
      

                algorithm = new NSGAII(problem);
    //algorithm = new ssNSGAII(problem);

                // Algorithm parameters
                algorithm.setInputParameter("populationSize", 100);
                algorithm.setInputParameter("maxEvaluations", Integer.MAX_VALUE);

                // Mutation and Crossover for Real codification 
                parameters = new HashMap();
                parameters.put("probability", 0.9);
                parameters.put("distributionIndex", 20.0);
                crossover = CrossoverFactory.getCrossoverOperator("SBXCrossover", parameters);

                parameters = new HashMap();
                parameters.put("probability", 0.05);
                parameters.put("distributionIndex", 20.0);
                mutation = MutationFactory.getMutationOperator("PolynomialMutation", parameters);

                // Selection Operator 
                parameters = null;
                selection = SelectionFactory.getSelectionOperator("BinaryTournament2", parameters);

                // Add the operators to the algorithm
                algorithm.addOperator("crossover", crossover);
                algorithm.addOperator("mutation", mutation);
                algorithm.addOperator("selection", selection);

                // Add the indicator object to the algorithm
                algorithm.setInputParameter("indicators", indicators);

                // Execute the Algorithm
                long initTime = System.currentTimeMillis();
                SolutionSet population = algorithm.execute();
                long estimatedTime = System.currentTimeMillis() - initTime;

                /*
                // Result messages 
                logger_.info("Total execution time: " + estimatedTime + "ms");
                logger_.info("Variables values have been writen to file VAR");
                population.printVariablesToFile("VAR");
                logger_.info("Objectives values have been writen to file FUN");
                population.printObjectivesToFile("FUN");

                
                if (indicators != null) {
                    logger_.info("Quality indicators");
                    logger_.info("Hypervolume: " + indicators.getHypervolume(population));
                    logger_.info("GD         : " + indicators.getGD(population));
                    logger_.info("IGD        : " + indicators.getIGD(population));
                    logger_.info("Spread     : " + indicators.getSpread(population));
                    logger_.info("Epsilon    : " + indicators.getEpsilon(population));

                    int evaluations = ((Integer) algorithm.getOutputParameter("evaluations")).intValue();
                    logger_.info("Speed      : " + evaluations + " evaluations");
                } // if
                        */
                String nome = nomeArquivo;
                BufferedWriter gravar = new BufferedWriter(new FileWriter("saida\\" + fold + "\\" + "TREINAMENTO" + t +"\\"+ nome + ".FLC"));
                OtimizacaoRegras o = (OtimizacaoRegras) problem;
                gravar.write(o.getMelhorArquivo());
                gravar.close();
            //gravar = new BufferedWriter(new FileWriter("saida\\" + nome + " - Desenpenho.txt"));
                //gravar.write("Acurácia - " + o.getMaiorPercentual() + "\r\n");
                //gravar.write("Interpretabilidade - " + o.getInterpretabilidade());
                //gravar.close();
                gravar = new BufferedWriter(new FileWriter("saida\\" + fold + "\\" + "TREINAMENTO" + t + "\\" + nome + " - Regras.txt"));
                gravar.write(o.getMelhorRegra().toString());
                gravar.close();
                
                gravar = new BufferedWriter(new FileWriter("saida\\" + fold + "\\" + "TREINAMENTO" + t + "\\" + nome + " - Geracao.txt"));
                gravar.write(o.getMaiorPercentual()+"\r\n");
                gravar.write(o.getInterpretabilidade()+"\r\n");
                gravar.write(NSGAII.evolucao+"");
                gravar.close();
                
                
                acuracias[i] = o.getMaiorPercentual();
                interpretabilidades[i] = o.getInterpretabilidade();
                geracao[i] = NSGAII.evolucao;
            //System.out.println(acuracias[i]);
                //System.out.println("ok");
            }
            desvioPadrão(acuracias, interpretabilidades, fold, t, geracao);
            }
        } //main
    
    

    public static void desvioPadrão(double[] acuracias, double[] interpretabilidades, String nome, int t, double[] geracao) throws IOException {
        double somaAcuracia = 0, mediaAcuracia = 0, somaInterpretabilidade = 0, mediaInterpretabilidade = 0;
        double somatorioDPA = 0;
        double somatorioDPI = 0;
        for (int i = 0; i < acuracias.length; i++) {
            somaAcuracia += acuracias[i];
            somaInterpretabilidade += interpretabilidades[i];
        }
        mediaAcuracia = somaAcuracia / acuracias.length;
        mediaInterpretabilidade = somaInterpretabilidade / acuracias.length;
        for (int i = 0; i < acuracias.length; i++) {
            somatorioDPA += Math.pow(acuracias[i] - mediaAcuracia, 2);
            somatorioDPI += Math.pow(interpretabilidades[i] - mediaInterpretabilidade, 2);
        }
        double dpA = Math.sqrt(somatorioDPA / acuracias.length);
        double dpI = Math.sqrt(somatorioDPI / acuracias.length);
        BufferedWriter gravar = new BufferedWriter(new FileWriter("saida\\" + nome + "\\" + "TREINAMENTO" + t + "\\" + nome + " - Resultados.txt"));
        
        double total1 = 0;
        double total2 = 0;
        double total3 = 0;
        for (int i = 0; i < acuracias.length; i++) {
            total1+=acuracias[i];
            total2+=interpretabilidades[i];
            gravar.write("Arquivo " + (i + 1) + " - Acurácia = " + acuracias[i] + " +/- " + dpA + " - Interpretabilidade = " + interpretabilidades[i] + " +/- " + dpI + "\r\n");
        }
        for (int i = 0; i < acuracias.length; i++) {
            total3 += geracao[i];
            gravar.write("Arquivo " + (i + 1) + " - Geração = " + geracao[i] + "\r\n");
        }
         gravar.write("Média EQM " + total1/acuracias.length+ "\r\n");
         gravar.write("Média Interpretabilidade " +total2/acuracias.length+ "\r\n");
         gravar.write("Média Gerações " + total3/acuracias.length + "\r\n");
        
        gravar.close();
    }
} // NSGAII_main

