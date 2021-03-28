package config;

import simulation.Statics;
import base.Particle;
import config.Initialize;
import simulation.SimulationCore;
import output.visualize.Scope2D;
//import output.store.ScopeStoreData;
import output.visualize.ScopeTerminal;

public class MainSimulation extends Thread
{
	public SimulationCore core = new SimulationCore();
	//max iterations
	public long FRAME = 100000000;
	//max simulation time
	public double maxTime = 100.0;
	//output source
	public Scope2D scope = new Scope2D();
	//public ScopeStoreData store = new ScopeStoreData();
	//public ScopeTerminal scope = new ScopeTerminal();
	//minimum real time delta t
	public long tMin = 2;
	//last frame system time
	public long lastFrame = 0;
	
	public MainSimulation()
	{
		Initialize.init();
	}
	
	//main program
	public void run()
	{
		lastFrame = System.currentTimeMillis();
		
		for(long i = 0; i < FRAME; i++)
		{
			mainLoop();
			if(Statics.simulationTime > maxTime)
			{break;}
		}
		
		System.out.println("program finished!");
	}
	
	public void mainLoop()
	{
		//minimum delay
		while(System.currentTimeMillis() < lastFrame + tMin)
		{
			try
			{Thread.sleep(1);}
			catch(InterruptedException e)
			{e.printStackTrace();}
		}
		if(System.currentTimeMillis() > lastFrame + 10 * tMin)
		{lastFrame = System.currentTimeMillis() - 9 * tMin;}
		lastFrame += tMin;
		
		
		//do all physical stuff
		core.step();
		
		scope.refresh();
		//store.refresh();
	}
}