package dissimlab.monitors;

import java.math.BigDecimal;

/**
 * Description...
 * 
 * @author Dariusz Pierzchala
 *
 */
public class MonitoredVar {

	private String name;
	private Histogram histogram;
	private ChangesList changes;
	private double actualVal;
	private double givenVariance;

	public MonitoredVar(String name, double givenVariance) {
		this.changes = new ChangesList();
		this.name = name;
		this.givenVariance = givenVariance;
	}

	public MonitoredVar(double value, double givenVariance) {
		this.actualVal = value;
		this.changes = new ChangesList();
		this.changes.add(new Change(value));
		this.givenVariance = givenVariance;
	}

	public void setValue(double newValue) {
		this.changes.add(new Change(newValue));
		this.actualVal = newValue;

		if(checkVariance())
		{
			resetVariable();
		}
	}

	public void setValue(double newValue, double simtime) {
		this.changes.add(new Change(newValue, simtime));
		this.actualVal = newValue;

		if(checkVariance())
		{
			resetVariable();
		}
	}

	private boolean checkVariance()
	{
		double variance = getVariance();
		boolean con = numberOfSamples() > 1 ? (variance < givenVariance) : false;

		if(con)
		{
			System.out.println("MonitoredVar - "+name+" - RESET.");
		}
		System.out.println("MonitoredVar - "+name+" - Variance: "+variance);
		System.out.println("MonitoredVar - "+name+" - givenVariance: "+ givenVariance);

		return con;
	}

	public double getValue() {
		return actualVal;
	}

	public Histogram getHistogram() {
		if (histogram == null)
			histogram = new Histogram(this);
		return histogram;
	}

	public double getVariance()
	{
		double sum = 0.0;
		double avg = calculateArithmeticAverage();

		for(int i = 0; i < numberOfSamples(); i++)
		{
			Change ch = changes.get(i);
			sum += Math.pow(ch.getValue()-avg, 2.0);
		}
		double var = sum/numberOfSamples();
		return new BigDecimal(var).setScale(2,
				BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	private double calculateArithmeticAverage()
	{
		double sum = 0.0;

		for(int i = 0; i < numberOfSamples(); i++)
		{
			Change ch = changes.get(i);
			sum += ch.getValue();
		}

		return new BigDecimal(sum/numberOfSamples()).setScale(2,
				BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	private void resetVariable()
	{
		this.changes = new ChangesList();
		this.actualVal = 0.0;
	}

	public String getName() {
		return name;
	}

	public ChangesList getChanges() {
		return changes;
	}

	public int numberOfSamples() {
		return changes.size();
	}

}