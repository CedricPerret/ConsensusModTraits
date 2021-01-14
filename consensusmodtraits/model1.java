package consensusmodtraits;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.math3.analysis.function.Sigmoid;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
/**
 *
 * @author Cedric Perret
 */
public class model1 extends Thread {
    private int nIndIni;
    private int Nsimul; 
    private int limListener; 
    private double fThr; 
    private int nLead;
    private double leadBeta; 
    private double follBeta; 
    private double leadGamma; 
    private double follGamma; 
    private double leadTheta; 
    private double follTheta; 
    private double epsilon;
    private PrintWriter pw;
    private int detail;

    
    public model1(int nIndIni,
     int Nsimul, 
     int limListener, 
     double fThr, 
     int nLead,
     double leadBeta, 
     double follBeta, 
     double leadGamma, 
     double follGamma,
     double leadTheta, 
     double follTheta,
     double epsilon,
     PrintWriter pw,
     int detail){
        this.nIndIni = nIndIni;
        this.Nsimul = Nsimul;
        this.limListener = limListener;
        this.fThr = fThr;
        this.nLead = nLead;
        this.leadBeta = leadBeta;
        this.follBeta = follBeta;
        this.leadGamma = leadGamma;
        this.follGamma = follGamma;
        this.leadTheta = leadTheta;
        this.follTheta = follTheta;
        this.epsilon = epsilon;
        this.pw = pw;
        this.detail = detail;
    }
    
    
    public void run(){
        
        double fVar; double fVarLead; int nEvent; int speaker; int limListener2 = limListener; String resTemp ="";
        double fMax = 1;
        double alphaSum; 
        double diffAlpha; double newFSpeaker;                                                       //CHANGE
        int[] listenerList;
        UpdateFunction updateFunction = new UpdateFunction();
        if(nIndIni <= limListener){limListener2 = nIndIni-1;}                   //If there are less people than the limit of listener, speaker talks to all
        //else{limListener2 = Math.round((limListener*nIndIni)/100);}           //If limListener is a percentage
        

            resTemp = "";
            for(int iSimul = 0; iSimul < Nsimul; iSimul++){
                //Initialisation
                List<Individual> popNow = new ArrayList<>();
                
                
                for(int i = 0; i < nLead; i++){                                 //Number of leaders

                	//With random opinion
                	popNow.add(new Individual(leadBeta, leadGamma, leadTheta, Math.random()*fMax));

                	//For fixed leader opinion (the most spread out possible)
                	//double x_lead_init = ((double)i +1)/((double)nLead+1);
                	//popNow.add(new Individual(leadBeta, leadGamma, leadTheta, x_lead_init, Math.random()));
                }
                fVarLead = Utility.variancePref(popNow);
                for(int i = nLead; i < nIndIni; i++){                           //Number of follower
                	popNow.add(new Individual(follBeta, follGamma, follTheta, Math.random()*fMax));
                }
                

                fVar = Utility.variancePref(popNow);                                    //Initial variance of f
                nEvent = -1;
                
                alphaSum = 0.0d;                                                 // Sum of the alpha to calculate the probability of being chosen as a speaker
                double[] probSpeaker = new double[nIndIni];                     //Array of the probability of each individual
                for(int i=0; i<nIndIni; i++){                       
                    alphaSum += Math.pow(popNow.get(i).getTheta(),4);
                }
                double probTemp = 0;                
                for(int i=0; i<nIndIni; i++){                                   //We calculate the weigthed probabilities
                    probSpeaker[i]= probTemp + (Math.pow(popNow.get(i).getTheta(),4)/alphaSum);
                    probTemp = probSpeaker[i];
                }                                                   
                //Simulation of consensus decision making
                
                while(fVar > fThr){
                    if(nEvent != -1){
                        speaker = Utility.probSample(probSpeaker, Math.random());         //Sample a speaker with Theta
                        if(nIndIni <= limListener2){limListener2 = nIndIni-1;}  
                        listenerList = Utility.randomSampleOtherList(nIndIni, limListener2,speaker);              //Create the list of listener (random individuals except speaker)
                        for(int i=0; i<limListener2; i++){
                            //Choose Update function
                        	//Linear and bounded
                        	//diffAlpha = updateFunction.diffLinearBounded(popNow.get(speaker).getBeta(), popNow.get(listenerList[i]).getGamma(),0.01);
                            //Linear
                        	//diffAlpha = updateFunction.diffLinear(popNow.get(speaker).getBeta(), popNow.get(listenerList[i]).getGamma());
                        	//Sigmoid
                        	//diffAlpha = updateFunction.diffSigmoid(popNow.get(speaker).getBeta(), popNow.get(listenerList[i]).getGamma());
                        	//Ratio (be careful, ratio needs to be lower than 1/baseUpdate)
                        	diffAlpha = updateFunction.ratio(popNow.get(speaker).getBeta(), popNow.get(listenerList[i]).getGamma(), 0.01);
                            
                            popNow.get(listenerList[i]).setF(popNow.get(listenerList[i]).getF() + diffAlpha * (popNow.get(speaker).getF()-popNow.get(listenerList[i]).getF()));  
                        }
                        fVar = Utility.variancePref(popNow);
                    }
                    //Counter of time of consensus
                    nEvent++;
                    if(detail == 2){
                        for(int k=0; k<nIndIni;k++){
                        resTemp += nEvent
                        		+","
                                + nIndIni
                                +","
                                + nLead
                                +","
                                + popNow.get(k).getF()
                                +","
                                + popNow.get(k).getBeta()
                                +","
                                + popNow.get(k).getGamma()
                                +","
                                + popNow.get(k).getTheta()
                            	+","
                            	+ k
                            	+","
                            	+ fVar        
                            	+","
                            	+ iSimul  
                            	+ "\r\n";
                    pw.write(resTemp); resTemp = "";
                        }
                        pw.flush();
                    }
                }
                //Writing
                if(detail == 0){
                    resTemp += nEvent
                            +","
                            + nIndIni
                            +","
                            + nLead
                            +","
                            + limListener
                            +","        
                            + fThr
                            +","        
                            + leadBeta
                            +"," 
                            + leadGamma
                            +","
                            + leadTheta
                            +","
                        	+ follBeta
                            +"," 
                            + follGamma
                            +","
                            + follTheta
                            +","
                            + iSimul  
                            + "\r\n";
                    pw.write(resTemp); resTemp = "";
                }
                if(detail == 1) {
                    for(int i = 0; i<popNow.size(); i++) {
                	resTemp += nEvent
                            +","
                            + (1 - Math.abs(popNow.get(i).getXInit() - Utility.meanPref(popNow)))
                            +","
                            + popNow.get(i).getBeta()
                            +","
                            + popNow.get(i).getGamma()
                            +","
                            + popNow.get(i).getTheta()
                            +","
                            + i
                            +","
                            + nIndIni
                            +","
                            + nLead
                            +","
                            + limListener
                            +","        
                            + fThr
                            +","      
                            + leadBeta
                            +"," 
                            + leadGamma
                            +","
                            + leadTheta
                            +","
                        	+ follBeta
                            +"," 
                            + follGamma
                            +","
                            + follTheta
                            +","
                            + iSimul 
                            + "\r\n";
                    }
                    pw.write(resTemp); resTemp = "";
                }
                pw.flush();
            }
            
            //if(nLead % 100 == 0){ System.out.println("nLead = " + nLead);}
        
        //System.out.println("Thread remaining : " +  Thread.activeCount());
        
    } 
        
            
}
