package simulation.sensor;

import base.ParticleArray;
import base.Particle;
import base.CellStack;
import base.Brick;
import base.Cell;
import simulation.sensor.math.Kernel;
import simulation.sensor.math.VecTool;
import simulation.sensor.DataPack;

public class Sensor
{
	public String id = "";
	
	public Sensor(String _id)
	{
		id = _id;
	}
	
	public void refresh()
	{}
	
	public DataPack readSignal()
	{
		DataPack dp = new DataPack();
		dp.add("id", id);
		dp.add("type", "default sensor");
		return dp;
	}
}