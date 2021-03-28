package simulation.sensor.math;

public class Kernel
{
	//kernel radius
	public static double H;
	public static double EPSILON;
	//poly6 const
	//315/64PIh^3
	public static double const1;
	//spiky const
	//15/PIh^3
	public static double const2;
	
	static
	{
		setKernelH(2.0);
	}
	
	
	public static void setKernelH(double _H)
	{
		H = _H;
		EPSILON = 1e-10 * H;
		const1 = 4 / Math.PI / Math.pow(H, 2);
		const2 = 10d / Math.PI / Math.pow(H, 2);
	}
	
	/*functions for poly6*/
	
	//315/64PIh^9*(h^2-r^2)^3
	//const1*(1-a^2)^3
	public static double poly6(double _dist)
	{
		if(_dist > H)
		{return 0;}
		double a = _dist / H;
		double b = 1 - a * a;
		return const1 * b * b * b;
	}
	
	//D(315/64PIh^9*(h^2-r^2)^3)
	//const1*-6a(1-a^2)^2
	public static double poly6D(double _dist)
	{
		if(_dist > H)
		{return 0;}
		double a = _dist / H;
		double b = 1 - a * a;
		return const1 * -6 * a * b * b / H;
	}
	
	//gradient of poly6
	public static double[] poly6G(double[] _vecp)
	{
		double ra = _vecp[0] * _vecp[0];
		double rb = _vecp[1] * _vecp[1];
		double _dist = Math.sqrt(ra + rb);
		
		if(_dist > H || _dist < EPSILON)
		{return new double[2];}
		
		double a = _dist / H;
		double b = 1 - a * a;
		double d = const1 * -6 * a * b * b / H;
		
		double[] ans = new double[2];
		ans[0] = d * _vecp[0] / _dist;
		ans[1] = d * _vecp[1] / _dist;
		
		return ans;
	}
	
	//gradient of poly6
	public static double[] polykyG(double[] _vecp)
	{
		double ra = _vecp[0] * _vecp[0];
		double rb = _vecp[1] * _vecp[1];
		double _dist = Math.sqrt(ra + rb);
		
		if(_dist > H || _dist < EPSILON)
		{return new double[2];}
		
		double a = _dist / H;
		double b = 1 - a * a;
		double d = const1 * -6 * a * b * b / H;
		
		double[] ans = new double[2];
		ans[0] = d * _vecp[0] / _dist;
		ans[1] = d * _vecp[1] / _dist;
		
		return ans;
	}
	
	/*functions for spiky*/
	
	//15/PIh^6*(h-r)^3
	//const2*(1-a)^3
	public static double spiky(double _dist)
	{
		if(_dist > H)
		{return 0;}
		double a = _dist / H;
		double b = 1 - a;
		return const2 * b * b * b;
	}
	
	//15/PIh^6*-3(h-r)^2
	//const2*-3(1-a)^2
	public static double spikyD(double _dist)
	{
		if(_dist > H)
		{return 0;}
		double a = _dist / H;
		double b = 1 - a;
		return const2 * -3 * b * b / H;
	}
	
	//gradient of spiky6
	public static double[] spikyG(double[] _vecp)
	{
		double ra = _vecp[0] * _vecp[0];
		double rb = _vecp[1] * _vecp[1];
		double _dist = Math.sqrt(ra + rb);
		
		if(_dist > H || _dist < EPSILON)
		{return new double[2];}
		
		double a = _dist / H;
		double b = 1 - a;
		double d = const2 * -3 * b * b / H;
		
		double[] ans = new double[2];
		ans[0] = d * _vecp[0] / _dist;
		ans[1] = d * _vecp[1] / _dist;
		
		return ans;
	}
}