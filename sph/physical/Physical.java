package physical;

import simulation.Statics;
import base.Particle;
import base.Cell;
import physical.equation.SolidSPH;

public class Physical
{
	public static void doRefresh()
	{
		SolidSPH.doRefresh();
	}
	public static void doPhysical1(Particle _particle)
	{
		SolidSPH.calcNeighborGWDensityRefresh(_particle);
	}
	
	public static void doPhysical2(Particle _particle)
	{
		SolidSPH.calcAccelerationWork(_particle);
	}
	
	public static void doPhysical3(Particle _particle)
	{
		SolidSPH.calcStress(_particle);
	}
	
	public static void doPhysical4(Particle _particle)
	{
		SolidSPH.calcIterate(_particle);
	}
	
	public static void doPhysical5(Particle _particle)
	{
	
	}
	
	public static void doPhysical6(Particle _particle)
	{
	
	}
	
	public static void doPhysical7(Particle _particle)
	{
	
	}
}