package physical.equation.material;
import java.io.*;


public class Nitrogen extends base.Particle
{
	private static String TYPE = "Nitrogen";
	private static double MASS = 0.308;
	private static double[] GRAVITY = new double[] {0, -10};
	
	public Nitrogen()
	{
		type = TYPE;
		mass = MASS;
		gravity = GRAVITY;
		energy = 200e3;
	}
	
	public void refreshProperties()
	{
		temperature = getTemperature(density, energy);
		pressure = getPressure(density, energy);
		soundSpeed = getSoundSpeed(density, energy);
		kappa = 0;
		mu = 0;
		heatKappa = 100;
	}
	
	private static double getTemperature(double _density, double _energy)
	{
		double t = _energy * (2.0 * 0.028 / (5.0 * 8.314));
		return t;
	}
	private static double getPressure(double _density, double _energy)
	{
		double p = (2.0 * _density * _energy) / 5.0;
		return p;
	}
	private static double getSoundSpeed(double _density, double _energy)
	{
		double c = Math.sqrt((14.0 * _energy) / 25.0);
		return c;
	}
}