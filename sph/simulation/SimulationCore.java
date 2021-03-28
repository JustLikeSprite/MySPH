package simulation;

import simulation.Statics;
import base.Cell;
import base.Brick;
import base.Particle;
import physical.Physical;

public class SimulationCore
{
	private SimulationCoreThread[] threads = new SimulationCoreThread[100];
	public int threadCount = 8;
	
	public SimulationCore()
	{}
	
	public void step()
	{
		if(Statics.isSimulating == false)
		{return;}
		
		Physical.doRefresh();
		
		//update physical time
		Statics.simulationTime += Statics.DT;
		//update particle hash
		Statics.cellStack.refreshAll();
		
		try
		{
			//parallel doPhysical1
			SimulationCoreThread.publicBrick = Statics.cellStack.root;
			for(int i = 0; i < threadCount; i++)
			{threads[i] = new SimulationCoreThread("doPhysical1"); threads[i].start();}
			for(int i = 0; i < threadCount; i++)
			{threads[i].join();}
			
			//parallel doPhysical2
			SimulationCoreThread.publicBrick = Statics.cellStack.root;
			for(int i = 0; i < threadCount; i++)
			{threads[i] = new SimulationCoreThread("doPhysical2"); threads[i].start();}
			for(int i = 0; i < threadCount; i++)
			{threads[i].join();}
			
			//parallel doPhysical3
			SimulationCoreThread.publicBrick = Statics.cellStack.root;
			for(int i = 0; i < threadCount; i++)
			{threads[i] = new SimulationCoreThread("doPhysical3"); threads[i].start();}
			for(int i = 0; i < threadCount; i++)
			{threads[i].join();}
			
			//parallel doPhysical4
			SimulationCoreThread.publicBrick = Statics.cellStack.root;
			for(int i = 0; i < threadCount; i++)
			{threads[i] = new SimulationCoreThread("doPhysical4"); threads[i].start();}
			for(int i = 0; i < threadCount; i++)
			{threads[i].join();}
		}
		catch(InterruptedException ie)
		{
			System.out.println("stepping failed!");
		}
		
		Statics.sensorArray.refreshAllSensors();
	}
}

class SimulationCoreThread extends Thread
{
	public String task;
	
	public static Brick publicBrick;
	
	public static synchronized Brick nextBrick()
	{
		if(publicBrick == null)
		{
			return null;
		}
		else
		{
			Brick _brick = publicBrick;
			publicBrick = publicBrick.next;
			return _brick;
		}
	}
	
	public SimulationCoreThread(String _task)
	{
		task = _task;
	}
	
	public void run()
	{
		for(Brick brick = nextBrick(); brick != null; brick = nextBrick())
		{
			if(task == "doPhysical1")
			{
				for(Cell c = brick.root; c != null; c = c.next)
				{
					for(Particle p = c.root; p != null; p = p.next)
					{
						Physical.doPhysical1(p);
					}
				}
			}
			else if(task == "doPhysical2")
			{
				for(Cell c = brick.root; c != null; c = c.next)
				{
					for(Particle p = c.root; p != null; p = p.next)
					{
						Physical.doPhysical2(p);
					}
				}
			}
			else if(task == "doPhysical3")
			{
				for(Cell c = brick.root; c != null; c = c.next)
				{
					for(Particle p = c.root; p != null; p = p.next)
					{
						Physical.doPhysical3(p);
					}
				}
			}
			else if(task == "doPhysical4")
			{
				for(Cell c = brick.root; c != null; c = c.next)
				{
					for(Particle p = c.root; p != null; p = p.next)
					{
						Physical.doPhysical4(p);
					}
				}
			}
			else if(task == "doPhysical5")
			{
				for(Cell c = brick.root; c != null; c = c.next)
				{
					for(Particle p = c.root; p != null; p = p.next)
					{
						Physical.doPhysical5(p);
					}
				}
			}
			else if(task == "doPhysical6")
			{
				for(Cell c = brick.root; c != null; c = c.next)
				{
					for(Particle p = c.root; p != null; p = p.next)
					{
						Physical.doPhysical6(p);
					}
				}
			}
			else if(task == "doPhysical7")
			{
				for(Cell c = brick.root; c != null; c = c.next)
				{
					for(Particle p = c.root; p != null; p = p.next)
					{
						Physical.doPhysical7(p);
					}
				}
			}
		}
	}
}