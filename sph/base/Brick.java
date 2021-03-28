package base;

import simulation.Statics;
import base.Particle;
import base.Cell;
import base.CellStack;

public class Brick
{
	public static final int brickSize = 5;
	
	public int[] grid = new int[2];
	public Cell[][] cells = new Cell[brickSize][brickSize];
	
	public CellStack cellStack;
	public double lastUpdate = 0;
	public Cell root;
	public Brick next;
	
	public Brick(int _x, int _y, CellStack _cellStack)
	{
		grid[0] = _x;
		grid[1] = _y;
		cellStack = _cellStack;
	}
	
	public void storeParticle(Particle _p)
	{
		int x = (int)Math.floor(_p.pos[0] / Statics.cellSize) - brickSize * grid[0];
		int y = (int)Math.floor(_p.pos[1] / Statics.cellSize) - brickSize * grid[1];
		
		int bMin = 0;
		int bMax = brickSize - 1;
		
		if(x < bMin)
		{x = bMin;}
		else if(x > bMax)
		{x = bMax;}
		if(y < bMin)
		{y = bMin;}
		else if(y > bMax)
		{y = bMax;}
		
		if(cells[x][y] == null)
		{
			cells[x][y] = new Cell(this);
			for(int i = x - 1; i < x + 2; i++)
			{
				for(int j = y - 1; j < y + 2; j++)
				{
					linkCell(cells[x][y], i, j);
				}
			}
		}
		cells[x][y].update(_p);
	}
	
	public Cell getCell(double _x, double _y)
	{
		int x = (int)Math.floor(_x / Statics.cellSize) - brickSize * grid[0];
		int y = (int)Math.floor(_y / Statics.cellSize) - brickSize * grid[1];
		
		int bMin = 0;
		int bMax = brickSize - 1;
		
		if(x < bMin)
		{x = bMin;}
		else if(x > bMax)
		{x = bMax;}
		if(y < bMin)
		{y = bMin;}
		else if(y > bMax)
		{y = bMax;}
		
		if(cells[x][y] == null)
		{
			cells[x][y] = new Cell(this);
			for(int i = x - 1; i < x + 2; i++)
			{
				for(int j = y - 1; j < y + 2; j++)
				{
					linkCell(cells[x][y], i, j);
				}
			}
		}
		cells[x][y].refresh();
		return cells[x][y];
	}
	
	public void linkCell(Cell _cell, int _i, int _j)
	{
		int x = grid[0];
		int y = grid[1];
		if(_i < 0)
		{_i += brickSize; x -= 1;}
		if(_i > brickSize - 1)
		{_i -= brickSize; x += 1;}
		if(_j < 0)
		{_j += brickSize; y -= 1;}
		if(_j > brickSize - 1)
		{_j -= brickSize; y += 1;}
		Cell neighbor = cellStack.getBrick(x, y).cells[_i][_j];
		if(neighbor != null)
		{
			_cell.addNeighbor(neighbor);
		}
	}
	
	//update from down to up
	public void update(Cell _cell)
	{
		if(lastUpdate != Statics.simulationTime)
		{
			lastUpdate = Statics.simulationTime;
			root = null;
			cellStack.update(this);
		}
		_cell.next = root;
		root = _cell;
	}
}