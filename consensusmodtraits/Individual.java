/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package consensusmodtraits;
import java.util.Comparator;
/**
 *
 * @author 40011091
 */
public class Individual  {
    // Persuasivness
    private double beta;
    //Stubborness
    private double gamma;
    //Talkativeness
    private double theta;
    //Opinion
    private double f;
    //Initial opinion
    private double x_init;
    
    
    public static int nbreIndividual = 0;
    public static int getNbreIndividual(){
        return nbreIndividual;
    }
    
    public Individual(){
        System.out.println("Creation of a default individual");
        beta = 0;
        gamma = 0;
        theta = 0;
        f = 0;
        x_init = 0;
        nbreIndividual++;
    }
    
    public Individual(double pBeta, double pGamma, double pTheta, double pF){
        beta= pBeta;
        gamma = pGamma;
        theta = pTheta;
        f = pF; 
        x_init = pF;
        nbreIndividual++;
    }
    
    public Individual(Individual pIndividual){
        beta = pIndividual.beta;
        gamma = pIndividual.gamma;
        theta = pIndividual.theta;
        f = pIndividual.f;
        x_init = 0;
        nbreIndividual++;
    }
    
    public void setAlpha(double pBeta){
        beta = pBeta;
    }
    public void setGamma(double pGamma){
        gamma = pGamma;
    }
    public void setTheta(double pTheta){
        theta = pTheta;
    }
    public void setF(double pF){
        f = pF;
    }
    public void setXInit(double pXInit) {
    	x_init = pXInit;
    }
    
    public double getBeta(){
        return beta;
    }
    public double getGamma(){
        return gamma;
    }
    public double getTheta(){
        return theta;
    }
    public double getF(){
        return f;
    }
    public double getXInit() {
    	return x_init;
    }

    

    
   
}


