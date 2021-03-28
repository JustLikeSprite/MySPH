package base;

import base.Particle;

public class ParticleArray
{
	public Particle[] particles = new Particle[10000000];
	public int particleCount = 0;
	
	public void addParticle(Particle _particle)
	{
		particles[particleCount] = _particle;
		particleCount++;
	}
	
	public void removeParticle(Particle _particle)
	{
		for(int i = 0; i < particleCount; i++)
		{
			if(_particle == particles[i])
			{
				particleCount--;
				particles[i] = particles[particleCount];
				particles[particleCount] = null;
				
				//clear links
				_particle.next = null;
				_particle.neighbors = null;
				return;
			}
		}
	}
}