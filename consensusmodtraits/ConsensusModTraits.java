package consensusmodtraits;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
/**
 *
 * @author Cedric Perret
 */
public class ConsensusModTraits {

    public static void main(String[] args) {

    	int nEvent;double fVar;int speaker;int listener;

    	//Parameters
    	// Number of individuals
    	int nIndIni;
    	//Number of simulations
    	int Nsimul;
    	//Number of listeners
    	int limListener;
    	//Consensus threshold
    	double fThr;
    	int nLead;

    	//Leader profile
    	double leadBeta;
    	double leadGamma;
    	double leadTheta;
    	//Follower profile
    	double follBeta;
    	double follGamma;
    	double follTheta;
    	
    	//Base rate
    	double epsilon;

    	String wd = "C:/Users/cedri/";
    	String nameFile = "";
    	PrintWriter pw = null;
    	String resTemp = "";
    	int detail;
    	System.out.println(wd);
    	long toComplete;
    	//Profile


    	ExecutorService pool = Executors.newFixedThreadPool(5);

    	//Ratio traits
    	
    	nameFile = wd + "ConsensusData-ratio-" + 
    			"de_0-L_1-beta-gamma-theta-N_100-epsilon_0.1-fThr_0.05-nL_1-S500"+
    			".txt";
    	
    	try {pw = new PrintWriter(new FileWriter(nameFile));} 
    	catch (IOException ex) {System.out.println(ex);}
    	resTemp = "";
    	
    	nIndIni = 100;
    	//int[] nIndIniRange = new int[nIndIni+1];
    	//for(int i = 0; i <= nIndIniRange.length;i++) {nIndIniRange[i]=i;}
    	
    	int[] nIndIniRange = {0,1};
    	
    	double[] betaRange = {1};
    	double[] gammaRange = {1};
    	double[] thetaRange = {10};

    	for(int i = 0; i< betaRange.length ; i += 1) {
    		for(int j = 0; j < gammaRange.length ; j += 1) {
    			for(int k = 0; k < thetaRange.length ; k += 1) {
    				//for(int l = 0; l <= nIndIni; l++) {
    					for(int l = 0; l < nIndIniRange.length; l++) {
    				pool.execute(new Thread(new model1(nIndIni = nIndIni, Nsimul = 500, limListener = 1, fThr = 0.05, nLead = nIndIniRange[l],
    						//leadBeta = 0.5 + betaRange[i]/2, follBeta = 0.5 - betaRange[i]/2, 
    						//leadGamma = 0.5 + gammaRange[j]/2, follGamma = 0.5 - gammaRange[j]/2,
    						//leadTheta = 0.5 + thetaRange[k]/2, follTheta = 0.5 -  thetaRange[k]/2,
    						leadBeta = betaRange[i], follBeta = 1, 
    						leadGamma = gammaRange[j], follGamma = 1,
    						leadTheta = thetaRange[k], follTheta = 1,
    						epsilon = 0.1,
    						pw = pw, detail = 0)));
    				}
    			}
    		}
    	}

    	//Ratio alpha
//    	nameFile = wd + "ConsensusData-ratio-base0.1" + 
//    			"de_1-L-alpha-N_100-fThr_0.05-nL_1-S500"+
//    			".txt";
//    	try {pw = new PrintWriter(new FileWriter(nameFile));} 
//    	catch (IOException ex) {System.out.println(ex);}
//    	resTemp = "";
//
//    	double[] alphaRange = {1.5,2,3,5,10};
//    	nIndIni = 100;
//    	for(int i = 0; i< alphaRange.length ; i += 1) {
//    		for(int j = 0; j <= nIndIni; j++) {
//    			pool.execute(new Thread(new model1(nIndIni = 100, Nsimul = 500, limListener = 1, fThr = 0.05, nLead = j,
//    					leadBeta = alphaRange[i], follBeta = 1, 
//    					leadGamma = alphaRange[i], follGamma = 1,
//    					leadTheta = alphaRange[i], follTheta = 1,
//    					pw = pw, detail = 1)));
//    		}
//    	}
//    	
    	
    	
    	

    	toComplete=((ThreadPoolExecutor) pool).getTaskCount()-((ThreadPoolExecutor) pool).getCompletedTaskCount();
    	while(toComplete>0) {
    		toComplete = ((ThreadPoolExecutor) pool).getTaskCount()-((ThreadPoolExecutor) pool).getCompletedTaskCount();
    		System.out.println("Pool size is now " + toComplete);
    		try{Thread.sleep(10000);}catch(InterruptedException ex){}
    	}

    	pool.shutdown();
    	try {
    		pool.awaitTermination(600, TimeUnit.SECONDS);
    	} catch (InterruptedException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	
    	
    	
    	
    	
    	
    	
    	
    }

}
