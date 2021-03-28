package physical.equation.material;
import java.io.*;


public class Water extends base.Particle
{
	private static String TYPE = "water";
	private static double MASS = 250;
	private static double[] GRAVITY = new double[] {0, -10};
	
	public Water()
	{
		type = TYPE;
		mass = MASS;
		gravity = GRAVITY;
		energy = 0;
	}
	
	public void refreshProperties()
	{
		temperature = getTemperature(density, energy);
		pressure = getPressure(density, energy);
		soundSpeed = getSoundSpeed(density, energy);
		kappa = 0;
		mu = 0;
		heatKappa = 0;
	}
	
	private static float[][][] dataChunk;
	private static float[][] pChunk;
	private static float[][] tChunk;
	private static float[][] cChunk;
	private static double dMin = 0;
	private static double dMax = 2000;
	private static double dInc = 2;
	private static double eMin = 0;
	private static double eMax = 6000e3;
	private static double eInc = 6e3;
	
	static
	{
		//load data
		// 0:temperature    1:pressure    2:mu    3:heatkappa    4:sound speed    5:phase
		try
		{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("physical/equation/data/data_Chunk_Water1"));
			dataChunk = (float[][][])ois.readObject();
			ois.close();
		}
		catch(Exception e)
		{System.out.println("load water data failed!");}
		
		tChunk = dataChunk[0];
		pChunk = dataChunk[1];
		cChunk = dataChunk[4];
	}
	
	private static int[] transPos(double _x1, double _x2)
	{
		int d = (int)((_x1 - dMin) / dInc);
		int e = (int)((_x2 - eMin) / eInc);
		if(d < 0) {d = 0;}
		if(d > 999) {d = 999;}
		if(e < 0) {e = 0;}
		if(e > 999) {e = 999;}
		return new int[] {d, e};
	}
	private static double getTemperature(double _density, double _energy)
	{
		int[] pos = transPos(_density, _energy);
		return tChunk[pos[0]][pos[1]];
	}
	private static double getPressure(double _density, double _energy)
	{
		int[] pos = transPos(_density, _energy);
		return pChunk[pos[0]][pos[1]];
	}
	private static double getSoundSpeed(double _density, double _energy)
	{
		int[] pos = transPos(_density, _energy);
		return cChunk[pos[0]][pos[1]];
	}
}