package physical.equation.material;
import java.io.*;


public class Rubber extends base.Particle
{
	private static String TYPE = "Rubber";
	private static double MASS = 250;
	private static double SHEERMODULUS = 1e6;
	private static double STRESSMU= 1e7;
	private static double[] GRAVITY = new double[] {0, -10};
	
	public Rubber()
	{
		type = TYPE;
		mass = MASS;
		sheermodulus = SHEERMODULUS;
		gravity = GRAVITY;
		energy = 0;
	}
	
	public void refreshProperties()
	{
		temperature = getTemperature(density, energy);
		pressure = getPressure(density, energy);
		soundSpeed = getSoundSpeed(density, energy);
		kappa = 0;
		mu = 10;
		heatKappa = 0;
	}
	
	private static double getTemperature(double _density, double _energy)
	{
		return 0;
	}
	private static double getPressure(double _density, double _energy)
	{
		double p = STRESSMU * (_density / 1000 - 1);
		return Math.max(0, p);
	}
	private static double getSoundSpeed(double _density, double _energy)
	{
		return 10;
	}
}