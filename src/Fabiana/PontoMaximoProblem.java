
package Fabiana;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.encodings.solutionType.BinarySolutionType;
import jmetal.encodings.variable.Binary;


public class PontoMaximoProblem extends Problem {

  double MaxValueFunction = 0;
  double xMax;
  
  public PontoMaximoProblem(String solutionType, Integer numberOfBits) {
    numberOfVariables_  = 1;
    numberOfObjectives_ = 1;
    numberOfConstraints_= 0;
    problemName_        = "PontoMaximo";
             
    solutionType_ = new BinarySolutionType(this) ;  	    
    length_       = new int[numberOfVariables_];
    length_      [0] = numberOfBits ;
     
  } 
      
  @Override
  public void evaluate(Solution solution) {
       
    Binary variable ;
    int    counter, dec  ;
    variable = ((Binary)solution.getDecisionVariables()[0]) ;
    counter = 0 ;
    dec = 21;
    for (int i = 0; i < variable.getNumberOfBits() ; i++) {
      if (variable.bits_.get(i)){
        counter += Math.pow(2, dec)  ;
      }
      dec--;
    }
    
    double x = -1.0 + counter*(3/(Math.pow(2, 22)-1));  
    double fx = x*Math.sin(x*10*Math.PI) + 1;
    
    if(fx > MaxValueFunction){
        MaxValueFunction = fx;
        xMax = x;
        System.out.println("O Máximo Absoluto é em x: " + x + " y: " + MaxValueFunction);
        System.out.println("FX = " + fx);
        System.out.println("solucao = " + solution.getObjective(0));
        System.out.println("");
    }
      
    solution.setObjective(0, -fx);            
  } 
} 
