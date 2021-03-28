10-7 cell-based sph framework schedule

root
{
	base
	{
		//store all bulk data, cell structure, neighbor particle
		Particle.class//define particle properties?
		ParticleArray.class
		Cell.class//mini cell
		Brick.class//computational unit
		CellStack.class
		/*
		CellStack: contain almost all methods required.
		
		refreshAll(): rebuild the entire particle hash
		
		root: store the first link of a brick linked list,
		which contains all bricks that have particles.
		a brick is the min computational unit for a computing thread.
		*/
	}
	physical
	{
		equation
		{
			//define behavior of particles
		}
	}
	config
	{
		Initiate.class
		
		MainSimulation.class
	}
	simulation
	{
		Statics.class//store all data
		
		SimulationCore.class//define how to simulate
	}
	output
	{
		visualize
		{
			Scope2D.class
		}
		save
		{
			
		}
	}
}