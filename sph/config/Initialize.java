package config;

import simulation.Statics;
import base.Particle;
import physical.equation.math.Kernel;
import physical.equation.material.*;
import simulation.sensor.sensor.*;

public class Initialize
{
	public static void init()
	{
		setConditions();
		addParticles();
		addSensors();
	}
	
	private static void setConditions()
	{
		Statics.H = 1.0;
		Statics.cellSize = 1.0;
		Statics.DT = 0.0001;
		Statics.simulationTime = 0;
		
		Kernel.setKernelH(Statics.H);
	}
	
	private static void addSensors()
	{
		{
			//SensorPressure sp1 = new SensorPressure("sp1", new double[] {0, 0});
			//Statics.sensorArray.addSensor(sp1);
		}
	}
	
	private static void addParticles()
	{
		for(int i = -10; i <= 10; i++)
		{
			for(int j = -10; j <= 10; j++)
			{
				Particle p = new Rubber();
				//p.energy = 200e3;
				p.pos = new double[] {0.5 * i, 0.5 * j};
				p.spd = new double[] {0, 0};
				p.gravity = new double[] {0, -10};
				Statics.particleArray.addParticle(p);
			}
		}
		/*
		*/
		
		
		/*
		for(int i = -20; i <= 20; i++)
		{
			for(int j = -80; j <= 40; j++)
			{
				Particle p = new Rubber();
				p.energy = 0;
				p.pos = new double[] {0.5 * i, 0.5 * j};
				p.spd = new double[] {0, 0};
				//p.gravity = new double[] {0, -10};
				Statics.particleArray.addParticle(p);
			}
		}
		for(int i = -10; i <= 10; i++)
		{
			for(int j = -10; j < 0; j++)
			{
				Particle p = new Rubber();
				p.energy = 0;
				p.pos = new double[] {0.5 * i, 0.5 * j};
				p.spd = new double[] {-100, 0};
				p.gravity = new double[] {0, 0};
				Statics.particleArray.addParticle(p);
			}
		}
		*/
		
		/*
		for(int i = -50; i <= 50; i++)
		{
			for(int j = -50; j <= 50; j++)
			{
				if(i >= -5 && i <= 5 && j >= -5 && j <= 5)
				{
					continue;
				}
				Particle p = new Water();
				p.energy = 0;
				p.pos = new double[] {0.5 * i, 0.5 * j};
				p.spd = new double[] {0, 0};
				Statics.particleArray.addParticle(p);
			}
		}
		
		for(int i = -5; i <= 5; i++)
		{
			for(int j = -5; j <= 5 ; j++)
			{
				Particle p = new Water();
				p.energy = 2e7;
				p.pos = new double[] {0.5 * i, 0.5 * j};
				p.spd = new double[] {0, 0};
				Statics.particleArray.addParticle(p);
			}
		}
		*/
	}
}