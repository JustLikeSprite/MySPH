package simulation.sensor.math;

public class VecTool
{
	public static double[] vecAdd(double[] vec1, double[] vec2)
	{
		double[] ans = new double[2];
		ans[0] = vec1[0] + vec2[0];
		ans[1] = vec1[1] + vec2[1];
		return ans;
	}
	
	public static double[] vecSub(double[] vec1, double[] vec2)
	{
		double[] ans = new double[2];
		ans[0] = vec1[0] - vec2[0];
		ans[1] = vec1[1] - vec2[1];
		return ans;
	}
	
	public static double[] vecMul(double[] vec1, double x)
	{
		double[] ans = new double[2];
		ans[0] = vec1[0] * x;
		ans[1] = vec1[1] * x;
		return ans;
	}
	
	public static double calcDist2(double[] vec)
	{
		double a = vec[0] * vec[0];
		double b = vec[1] * vec[1];
		return a + b;
	}
	
	public static double vecDot(double[] vec1, double[] vec2)
	{
		double a = vec1[0] * vec2[0];
		double b = vec1[1] * vec2[1];
		return a + b;
	}
	
	public static double calcDist(double[] vec)
	{
		double a = vec[0] * vec[0];
		double b = vec[1] * vec[1];
		return Math.sqrt(a + b);
	}
}