/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Genetico;

import java.util.HashMap;
import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.metaheuristics.singleObjective.geneticAlgorithm.gGA;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.util.JMException;


/**
 *
 * @author allen
 */


public class PontoMaximo {
 
    public static void main(String[] args) throws JMException, ClassNotFoundException {
        
        Problem   problem   ;         // The problem to solve
        Algorithm algorithm ;         // The algorithm to use
        Operator  crossover ;         // Crossover operator
        Operator  mutation  ;         // Mutation operator
        Operator  selection ;         // Selection operator
        HashMap  parameters ;         // Operator parameters
        int bits = 22;
        
        problem = new AGmaximo("Binary", bits);
        algorithm = new gGA(problem) ; // Generational GA
        algorithm.setInputParameter("populationSize",50);
        algorithm.setInputParameter("maxEvaluations", 150);
        
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
    }
    
    
    
    
    
}
