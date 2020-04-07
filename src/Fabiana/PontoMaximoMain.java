package Fabiana;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.problems.singleObjective.OneMax;
import jmetal.util.JMException;

import java.util.HashMap;
import jmetal.metaheuristics.singleObjective.geneticAlgorithm.gGA;

/**
 * This class runs a single-objective genetic algorithm (GA). The GA can be 
 * a steady-state GA (class ssGA), a generational GA (class gGA), a synchronous
 * cGA (class scGA) or an asynchronous cGA (class acGA). The OneMax
 * problem is used to test the algorithms.
 */
public class PontoMaximoMain {

  public static void main(String [] args) throws JMException, ClassNotFoundException {
    Problem   problem   ;         // The problem to solve
    Algorithm algorithm ;         // The algorithm to use
    Operator  crossover ;         // Crossover operator
    Operator  mutation  ;         // Mutation operator
    Operator  selection ;         // Selection operator
            
    //int bits ; // Length of bit string in the OneMax problem
    HashMap  parameters ; // Operator parameters

    int bits = 22 ;
    problem = new PontoMaximoProblem("Binary", bits);
    algorithm = new gGA(problem) ; // Generational GA
    
    algorithm.setInputParameter("populationSize",100);
    algorithm.setInputParameter("maxEvaluations", 25000);
  
    parameters = new HashMap() ;
    parameters.put("probability", 0.9) ;
    crossover = CrossoverFactory.getCrossoverOperator("SinglePointCrossover", parameters);                   

    parameters = new HashMap() ;
    parameters.put("probability", 1.0/bits) ;
    mutation = MutationFactory.getMutationOperator("BitFlipMutation", parameters);                    
    
    /* Selection Operator */
    parameters = null ;
    selection = SelectionFactory.getSelectionOperator("BinaryTournament", parameters) ;                            
    
    /* Add the operators to the algorithm*/
    algorithm.addOperator("crossover",crossover);
    algorithm.addOperator("mutation",mutation);
    algorithm.addOperator("selection",selection);
 
    /* Execute the Algorithm */
    long initTime = System.currentTimeMillis();
    SolutionSet population = algorithm.execute();
    long estimatedTime = System.currentTimeMillis() - initTime;
    System.out.println("Total execution time: " + estimatedTime);

    /* Log messages */
    System.out.println("Objectives values have been writen to file FUN");
    population.printObjectivesToFile("FUN");
    System.out.println("Variables values have been writen to file VAR");
    population.printVariablesToFile("VAR");          
  } //main
} // GA_main
