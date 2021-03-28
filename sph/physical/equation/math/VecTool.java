package physical.equation.math;

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

	public static double[][] deviatoric(double[][] matrix)
	{
		matrix[0][0]=0.5*(matrix[0][0]-matrix[1][1]);
		matrix[1][1]=0.5*(matrix[1][1]-matrix[0][0]);
		return matrix;
	}

	public static double[] scalarMul(double k,double[] vec)
	{
		double[] ans=new double[2];
		ans[0]=k*vec[0];
		ans[1]=k*vec[1];
		return ans;
	}

	public static double[][] transpose(double[][] matrix)
	{
		double[][] ans=new double[2][2];
		ans[1][0]=matrix[0][1];
		ans[0][1]=matrix[1][0];
		ans[1][1]=matrix[1][1];
		ans[0][0]=matrix[0][0];

		return ans;
	}

    public static double[][] matrixproduct(double[][] a, double[][] b)
	{
        int y = a.length;
        int x = b[0].length;
        double c[][] = new double[y][x];
        for (int i = 0; i < y; i++)
		{
            for (int j = 0; j < x; j++)
			{
                for (int k = 0; k < b.length; k++)
				{
                    c[i][j] += a[i][k] * b[k][j];
				}
			}
		}
        return c;
    }
}