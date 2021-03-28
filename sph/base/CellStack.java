package base;

import java.util.HashMap;

import simulation.Statics;
import base.Particle;
import base.Brick;

public class CellStack
{
	//public static double cellSize;
	
	public double lastUpdate;
	public Brick root;
	CellStackBrick cellStackBrick = new CellStackBrick();
	
	public CellStack()
	{}
	
	//refresh entire particle hash
	public void refreshAll()
	{
		for(int i = 0; i < Statics.particleArray.particleCount; i++)
		{
			Particle p = Statics.particleArray.particles[i];
			
			Brick brick = getBrick(p);
			brick.storeParticle(p);
		}
	}
	
	public Brick getBrick(Particle _p)
	{
		int x = (int)Math.floor(_p.pos[0] / Statics.cellSize / Brick.brickSize);
		int y = (int)Math.floor(_p.pos[1] / Statics.cellSize / Brick.brickSize);
		
		return getBrick(x, y);
	}
	
	public Brick getBrick(double _x, double _y)
	{
		int x = (int)Math.floor(_x / Statics.cellSize / Brick.brickSize);
		int y = (int)Math.floor(_y / Statics.cellSize / Brick.brickSize);
		
		return getBrick(x, y);
	}
	
	public Brick getBrick(int _x, int _y)
	{
		return CellStackBrick.getBrick(_x, _y);
	}
	
	//update from down to up
	public void update(Brick _brick)
	{
		if(lastUpdate != Statics.simulationTime)
		{
			lastUpdate = Statics.simulationTime;
			root = null;
		}
		_brick.next = root;
		root = _brick;
	}
}

class CellStackBrick
{
	private static HashMap<Integer, HashMap<Integer, Brick>> map = new HashMap<Integer, HashMap<Integer, Brick>>();
	
	public static Brick getBrick(int _x, int _y)
	{
		HashMap<Integer, Brick> submapX = map.get(_x);
		if(submapX == null)
		{
			submapX = new HashMap<Integer, Brick>();
			map.put(_x, submapX);
		}
		Brick brick = submapX.get(_y);
		if(brick == null)
		{
			brick = new Brick(_x, _y, Statics.cellStack);
			submapX.put(_y, brick);
		}
		return brick;
	}
}