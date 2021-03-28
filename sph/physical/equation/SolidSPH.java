package physical.equation;

import simulation.Statics;
import base.Cell;
import base.Particle;
import physical.equation.math.Kernel;
import physical.equation.math.VecTool;
import physical.equation.material.*;

public class SolidSPH {
	private static double[] rangeX = new double[] { -50, +50 };
	private static double[] rangeY = new double[] { -15, +50 };
	private static double strength = 1e6;
	private static double lastAdd = 0;

	public static void doRefresh() {
		// if(true || rangeX[0] < -40)
		// {rangeX[0] += 2000 * Statics.DT;}
	}

	public static void calcNeighborGWDensityRefresh(Particle _particle) {
		// find neighbors of the particle
		int particleNeighborCount = 0;
		int cellNeighborCount = _particle.cell.neighborCount;
		Cell[] neighbors = _particle.cell.neighbors;
		double[] pos = _particle.pos;
		for (int i = 0; i < cellNeighborCount; i++) {
			for (Particle p = neighbors[i].root; p != null; p = p.next) {
				if (VecTool.calcDist(VecTool.vecSub(p.pos, pos)) < Statics.H) {
					_particle.neighbors[particleNeighborCount] = p;
					particleNeighborCount++;
				}
			}
		}
		// for(int i = particleNeighborCount; i < _particle.neighborCount; i++)
		// {
		// _particle.neighbors[i] = null;
		// }
		_particle.neighborCount = particleNeighborCount;

		// store value and gradients of kernel
		for (int i = 0; i < _particle.neighborCount; i++) {
			double[] vecp = VecTool.vecSub(_particle.neighbors[i].pos, _particle.pos);
			double[] vecv = VecTool.vecSub(_particle.neighbors[i].spd, _particle.spd);
			double dist = VecTool.calcDist(vecp);

			_particle.poly6W[i] = Kernel.poly6(dist);
			_particle.spikyW[i] = Kernel.spiky(dist);

			double[] vec1 = Kernel.poly6G(vecp);
			double[] vec2 = Kernel.spikyG(vecp);
			_particle.poly6GW[i][0] = vec1[0];
			_particle.poly6GW[i][1] = vec1[1];
			_particle.spikyGW[i][0] = vec2[0];
			_particle.spikyGW[i][1] = vec2[1];
		}

		// calc density
		double density = 0;
		for (int i = 0; i < _particle.neighborCount; i++) {
			density += _particle.neighbors[i].mass * _particle.poly6W[i];
		}
		_particle.density = density;

		// calc other properties
		_particle.refreshProperties();
	}

	public static void calcAccelerationWork(Particle _particle)
	{
		double[] aclPressure = new double[2];
		double workPressure = 0;

		// calc acceleration by pressure
		for(int i = 0; i < _particle.neighborCount; i++)
		{
			double[] vecp = VecTool.vecSub(_particle.neighbors[i].pos, _particle.pos);
			double[] vecv = VecTool.vecSub(_particle.neighbors[i].spd, _particle.spd);
			double a1 = _particle.neighbors[i].pressure / (_particle.neighbors[i].density * _particle.neighbors[i].density);
			double a2 = _particle.pressure / (_particle.density * _particle.density);
			double mul1 = _particle.neighbors[i].mass * (a1 + a2);
			
			double acl0 = mul1 * _particle.spikyGW[i][0];
			double acl1 = mul1 * _particle.spikyGW[i][1];
			aclPressure[0] += acl0;
			aclPressure[1] += acl1;
			
			workPressure += 0.5 * (acl0 * vecv[0] + acl1 * vecv[1]);
		}
		
		// calc aclViscosity
		double[] aclViscosity = new double[2];
		double workViscosity = 0;
		for(int i = 0; i < _particle.neighborCount; i++)
		{
			if(_particle.neighbors[i] != _particle)
			{
				double[] vecp = VecTool.vecSub(_particle.neighbors[i].pos, _particle.pos);
				double[] vecv = VecTool.vecSub(_particle.neighbors[i].spd, _particle.spd);
				double dist2 = VecTool.calcDist2(vecp);
				double mul1 = (-2 * _particle.mu * _particle.neighbors[i].mass) * VecTool.vecDot(vecp, _particle.poly6GW[i]) / (_particle.neighbors[i].density * dist2);
				
				double acl0 = mul1 * vecv[0];
				double acl1 = mul1 * vecv[1];
				aclViscosity[0] += acl0;
				aclViscosity[1] += acl1;
				
				workViscosity += 0.5 * (acl0 * vecv[0] + acl1 * vecv[1]);
			}
		}
		
		//calc aclArtViscosity
		double[] aclArtViscosity = new double[2];
		double workArtViscosity = 0;
		for(int i = 0; i < _particle.neighborCount; i++)
		{
			double[] vecp = VecTool.vecSub(_particle.neighbors[i].pos, _particle.pos);
			double[] vecv = VecTool.vecSub(_particle.neighbors[i].spd, _particle.spd);
			double dot1 = VecTool.vecDot(vecp, vecv);
			if(dot1 < 0)
			{
				double dist = VecTool.calcDist(vecp);
				
				double alpha = 0.1;
				double beta = 2;
				
				double chi = Statics.H * dot1 / (dist * dist + 0.01 * Statics.H * Statics.H);
				double pi = (-0.5 * alpha * (_particle.soundSpeed + _particle.neighbors[i].soundSpeed) * chi + beta * chi * chi) / (0.5 * (_particle.density + _particle.neighbors[i].density));
				
				double mul1 = _particle.neighbors[i].mass * pi;
				
				double acl0 = mul1 * _particle.spikyGW[i][0];
				double acl1 = mul1 * _particle.spikyGW[i][1];
				aclArtViscosity[0] += acl0;
				aclArtViscosity[1] += acl1;
				
				workArtViscosity += 0.5 * (acl0 * vecv[0] + acl1 * vecv[1]);
			}
		}
		

		//calc acl of deviatoric stress
		double[] aclStress=new double[2];
		for(int i = 0; i < _particle.neighborCount; i++)
		{
			double a1=VecTool.vecDot(_particle.stressTensor[0], _particle.poly6GW[i])/(_particle.density*_particle.density);
			double a2=VecTool.vecDot(_particle.neighbors[i].stressTensor[0], _particle.poly6GW[i])/(_particle.neighbors[i].density*_particle.neighbors[i].density);

			aclStress[0]+=a1+a2;

			double b1=VecTool.vecDot(_particle.stressTensor[1], _particle.poly6GW[i])/(_particle.density*_particle.density);
			double b2=VecTool.vecDot(_particle.neighbors[i].stressTensor[1], _particle.poly6GW[i])/(_particle.neighbors[i].density*_particle.neighbors[i].density);

			aclStress[1]+=b1+b2;
		}

		double[] gravity = _particle.gravity;
		
		
		//heat transfer
		double workHeat = 0;
		for(int i = 0; i < _particle.neighborCount; i++)
		{
			if(_particle.neighbors[i] != _particle)
			{
				double[] vecp = VecTool.vecSub(_particle.neighbors[i].pos, _particle.pos);
				double dt = _particle.neighbors[i].temperature - _particle.temperature;
				double dist = VecTool.calcDist(vecp);
				
				double mul1 = -dt * 2 * _particle.heatKappa * _particle.neighbors[i].mass / (_particle.neighbors[i].density * _particle.density * (dist * dist));
				workHeat += mul1 * VecTool.vecDot(_particle.poly6GW[i], vecp);
			}
		}
		
		//acceleration
		double[] acl = new double[2];
		acl[0] = 1.0 * aclPressure[0] + 1.0 * aclViscosity[0] + 1.0 * aclArtViscosity[0] + 1.0*aclStress[0]  + 1.0 * gravity[0];
		acl[1] = 1.0 * aclPressure[1] + 1.0 * aclViscosity[1] + 1.0 * aclArtViscosity[1] + 1.0*aclStress[1]  + 1.0 * gravity[1];
		_particle.acl[0] = acl[0];
		_particle.acl[1] = acl[1];
		
		//work
		double work = 1.0 * workPressure + 1.0 * workViscosity + 1.0 * workArtViscosity;
		_particle.work = work;
	}

	public static void calcStress(Particle _particle)
	{
		// calc deviatoric tensor rate
		// 1.calc velocity gradient
		double[][] velocityGT=new double[2][2];
		for(int i = 0; i < _particle.neighborCount; i++)
		{
			//for x direction
			double mul1x=_particle.density*_particle.neighbors[i].mass;
			double mul2x=(_particle.spd[0]/(_particle.density*_particle.density)+_particle.neighbors[i].spd[0]/(_particle.neighbors[i].density*_particle.neighbors[i].density));

			//for y direcion
			double mul1y=_particle.density*_particle.neighbors[i].mass;
			double mul2y=(_particle.spd[1]/(_particle.density*_particle.density)+_particle.neighbors[i].spd[1]/(_particle.neighbors[i].density*_particle.neighbors[i].density));

			velocityGT[0]=VecTool.vecAdd(velocityGT[0],VecTool.scalarMul(mul1x*mul2x, _particle.poly6GW[i]));
			velocityGT[1]=VecTool.vecAdd(velocityGT[1],VecTool.scalarMul(mul1y*mul2y, _particle.poly6GW[i]));
		}

		double[][] velocityG=VecTool.transpose(velocityGT);

		//2. calc rotationrate and strainrate
		double[][] rotationRate=new double[2][2];
		double[][] strainRate=new double[2][2];

		{
			rotationRate[0][0]=0/2;
			rotationRate[1][1]=0/2;
			rotationRate[1][0]=velocityG[1][0]/2-velocityGT[1][0]/2;
			rotationRate[0][1]=velocityG[0][1]/2-velocityGT[0][1]/2;

			strainRate[0][0]=(velocityG[0][0]+velocityGT[0][0])/2;
			strainRate[1][0]=(velocityG[1][0]+velocityGT[1][0])/2;
			strainRate[0][1]=(velocityG[0][1]+velocityGT[0][1])/2;
			strainRate[1][1]=(velocityG[1][1]+velocityGT[1][1])/2;
		}

		//3. calc deviatoric stressTensorRate
		double[][] deviaStrainRate=VecTool.deviatoric(strainRate);
		double[][] stressTensorD1=VecTool.matrixproduct(rotationRate, _particle.stressTensor);
		double[][] stressTensorD2=VecTool.matrixproduct( _particle.stressTensor,rotationRate);
		{
			_particle.stressTensorD[0][0]=stressTensorD1[0][0]-stressTensorD2[0][0]+2*_particle.sheermodulus*deviaStrainRate[0][0];
			_particle.stressTensorD[1][1]=stressTensorD1[1][1]-stressTensorD2[1][1]+2*_particle.sheermodulus*deviaStrainRate[1][1];
			_particle.stressTensorD[1][0]=stressTensorD1[1][0]-stressTensorD2[1][0]+2*_particle.sheermodulus*deviaStrainRate[1][0];
			_particle.stressTensorD[0][1]=stressTensorD1[0][1]-stressTensorD2[0][1]+2*_particle.sheermodulus*deviaStrainRate[0][1];
		}
	}

	public static void calcIterate(Particle _particle) {
		double[] force = new double[2];
		force[0] += (_particle.pos[0] < rangeX[0]) ? (rangeX[0] - _particle.pos[0]) : 0;
		force[0] += (_particle.pos[0] > rangeX[1]) ? (rangeX[1] - _particle.pos[0]) : 0;
		force[1] += (_particle.pos[1] < rangeY[0]) ? (rangeY[0] - _particle.pos[1]) : 0;
		force[1] += (_particle.pos[1] > rangeY[1]) ? (rangeY[1] - _particle.pos[1]) : 0;

		_particle.acl[0] += strength * force[0];
		_particle.acl[1] += strength * force[1];

		_particle.spd[0] += _particle.acl[0] * Statics.DT;
		_particle.spd[1] += _particle.acl[1] * Statics.DT;

		_particle.pos[0] += _particle.spd[0] * Statics.DT;
		_particle.pos[1] += _particle.spd[1] * Statics.DT;

		_particle.energy += _particle.work * Statics.DT;

		_particle.stressTensor[0][0]+=_particle.stressTensorD[0][0]*Statics.DT;
		_particle.stressTensor[0][1]+=_particle.stressTensorD[0][1]*Statics.DT;
		_particle.stressTensor[1][0]+=_particle.stressTensorD[1][0]*Statics.DT;
		_particle.stressTensor[1][1]+=_particle.stressTensorD[1][1]*Statics.DT;
	}
}