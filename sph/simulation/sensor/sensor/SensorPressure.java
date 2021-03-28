package simulation.sensor.sensor;

import simulation.Statics;
import base.Particle;
import base.CellStack;
import base.Brick;
import base.Cell;
import simulation.sensor.math.Kernel;
import simulation.sensor.math.VecTool;
import simulation.sensor.DataPack;

public class SensorPressure extends simulation.sensor.Sensor
{
	private double[] pos;
	private double curPressure;
	private double minPressure;
	private double maxPressure;
	
	public SensorPressure(String _id, double[] _pos)
	{
		super(_id);
		pos = _pos;
	}
	
	public void refresh()
	{
		Brick brick = Statics.cellStack.getBrick(pos[0], pos[1]);
		Cell cell = brick.getCell(pos[0], pos[1]);
		Cell[] neighbors = cell.neighbors;
		
		Particle[] particles = new Particle[200];
		int particleCount = 0;
		
		for(int i = 0; i < cell.neighborCount; i++)
		{
			for(Particle p = neighbors[i].root; p != null; p = p.next)
			{
				if(VecTool.calcDist(VecTool.vecSub(p.pos, pos)) < Statics.H)
				{
					particles[particleCount] = p;
					particleCount++;
				}
			}
		}
		
		double pressure = 0;
		double weight = 0;
		for(int i = 0; i < particleCount; i++)
		{
			double[] vecp = VecTool.vecSub(pos, particles[i].pos);
			double dist = VecTool.calcDist(vecp);
			double dw = Kernel.poly6(dist);
			pressure += dw * particles[i].pressure;
			weight += dw;
		}
		curPressure = pressure / weight;
		
		if(maxPressure < curPressure)
		{maxPressure = curPressure;}
		
		if(minPressure > curPressure)
		{minPressure = curPressure;}
	}
	
	public DataPack readSignal()
	{
		DataPack dp = new DataPack();
		dp.add("id", id);
		dp.add("type", "pressure sensor");
		
		dp.add("curPressure", curPressure);
		dp.add("maxPressure", maxPressure);
		dp.add("minPressure", minPressure);
		return dp;
	}
}