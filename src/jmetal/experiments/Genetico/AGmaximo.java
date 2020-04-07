/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Genetico;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.BinarySolutionType;
import jmetal.encodings.variable.Binary;
import sun.security.util.Length;

/**
 * Class representing problem OneMax. The problem consist of maximizing the
 * number of '1's in a binary string.
 */
public class AGmaximo extends Problem {

  
  public AGmaximo(String solutionType, Integer numberOfBits) {
    
    int limiteInferior = -1;
    int limiteSuperior =  2;
    numberOfVariables_  = 50;
    numberOfObjectives_ = 1;
    numberOfConstraints_= 0;
    problemName_        = solutionType;
             
    solutionType_ = new BinarySolutionType(this) ;
    length_       = new int[numberOfVariables_];
    length_      [0] = numberOfBits ;
    if (solutionType.compareTo("Binary") == 0)
    	solutionType_ = new BinarySolutionType(this) ;
    else {
    	System.out.println("OneMax: solution type " + solutionType + " invalid") ;
    	System.exit(-1) ;
    }
    
  } // OneMax
 /** 
  * Evaluates a solution 
  * @param solution The solution to evaluate
  */      
  @Override
  public void evaluate(Solution solution) {
    Binary variable ;
    int    counter  ;
    variable = ((Binary)solution.getDecisionVariables()[0]) ;
    counter = 0;

    for (int i = 0; i < variable.getNumberOfBits() ; i++) 
      if (variable.bits_.get(i))
       
        counter ++ ;
    // OneMax is a maximization problem: multiply by -1 to minimize
    solution.setObjective(0, -1.0*counter);            
  } // evaluate      
  
  
} 

