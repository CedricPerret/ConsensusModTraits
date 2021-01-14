package consensusmodtraits;

import org.apache.commons.math3.analysis.function.Sigmoid;

public class UpdateFunction {
	public UpdateFunction() {};
	
	
	public double diffLinear (double betaSpeaker, double gammaListener) {
		double update = 0.5 + 0.5*(betaSpeaker - gammaListener);
		return(update);
	}
	
	public double diffSigmoid (double betaSpeaker, double gammaListener) {
		Sigmoid sig = new Sigmoid();
		double update =  sig.value((betaSpeaker - gammaListener));
		return(update);
	}
	
	public double diffLinearBounded (double betaSpeaker, double gammaListener, double minUpdate) {
		double update = betaSpeaker - gammaListener;
		if (update <= minUpdate) {update = minUpdate;}
		return(update);
	}
	
	public double ratio (double betaSpeaker, double gammaListener, double baseUpdate) {
		double update = (betaSpeaker/gammaListener) * baseUpdate;
		if(update > 1) {System.out.println("error update superior to 1");}
		return(update);
	}
	
}
