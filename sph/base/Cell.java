package base;

import simulation.Statics;
import base.Particle;
import base.Brick;

public class Cell
{
	public int neighborCount = 0;
	public Cell[] neighbors = new Cell[30];
	
	public Brick brick;
	public double lastUpdate = 0;
	public Particle root;
	public Cell next;
	
	public Cell(Brick _brick)
	{
		brick = _brick;
	}
	
	public void addNeighbor(Cell _neighbor)
	{
		for(int i = 0; i < neighborCount + 1; i++)
		{
			if(neighbors[i] == _neighbor)
			{
				return;
			}
			else if(neighbors[i] == null)
			{
				neighbors[i] = _neighbor;
				_neighbor.addNeighbor(this);
				neighborCount++;
				return;
			}
		}
	}
	
	//update from down to up
	public void update(Particle _p)
	{
		if(lastUpdate != Statics.simulationTime)
		{
			lastUpdate = Statics.simulationTime;
			root = null;
			brick.update(this);
		}
		_p.next = root;
		root = _p;
		//force clear outdated data
		if(_p.cell != null)
		{
			_p.cell.refresh();
		}
		_p.cell = this;
	}
	
	public void refresh()
	{
		if(lastUpdate != Statics.simulationTime)
		{
			root = null;
		}
	}
}