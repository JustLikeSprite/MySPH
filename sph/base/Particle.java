package base;

import base.Cell;

public class Particle
{
	public String type;
	public Cell cell;
	public Particle next;
	
	public double[] pos = new double[2];
	public double[] acl = new double[2];
	public double[] spd = new double[2];
	
	public double mass;
	
	public double density;
	public double energy;
	
	public double temperature;
	public double pressure;
	public double soundSpeed;
	public double heatKappa;
	public double mu;
	public double kappa;
	public double[] gravity;
	public double work;
	
	public double sheermodulus;
	public double[][] stressTensor = new double[2][2];
	public double[][] stressTensorD = new double[2][2];
	
	
	public int neighborCount;
	public Particle[] neighbors = new Particle[200];
	public double[] poly6W = new double[200];
	public double[] spikyW = new double[200];
	public double[][] poly6GW = new double[200][2];
	public double[][] spikyGW = new double[200][2];
	
	public void init(double[] _pos, double[] _spd)
	{
		pos = _pos;
		spd = _spd;
	}
	
	public Particle(double[] _pos, double[] _spd)
	{
		init(_pos, _spd);
	}
	public Particle(double[] _pos)
	{
		init(_pos, new double[2]);
	}
	public Particle()
	{
		init(new double[2], new double[2]);
	}
	
	public void refreshProperties()
	{}
}