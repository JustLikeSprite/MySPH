package simulation;

import base.ParticleArray;
import base.CellStack;
import simulation.sensor.SensorArray;

public class Statics
{
	public static double H;
	public static double cellSize;
	
	public static ParticleArray particleArray = new ParticleArray();
	
	public static CellStack cellStack = new CellStack();
	
	public static SensorArray sensorArray = new SensorArray();
	
	public static double simulationTime;
	public static double DT;
	
	public static boolean isSimulating = true;
	
}