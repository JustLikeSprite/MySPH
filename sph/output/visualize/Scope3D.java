package output.visualize;
//3D scope

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;

import simulation.Statics;
import base.Cell;
import base.Particle;

public class Scope3D extends JFrame
{
	private Scope3DCanvas canvas = new Scope3DCanvas(this);
	
	private int fps = 0;
	private long lastPaint = 0;
	private long lastUpdate = 0;
	
	public Scope3D()
	{
		super("Scope3D");
		
		setLayout(null);
		
		add(canvas);
		canvas.setBounds(18, 6, 800, 600);
		setSize(850, 650);
		//pack();
		
		setVisible(true);
	}
	
	public void refresh()
	{
		fps += 1;
		if(System.currentTimeMillis() - lastUpdate > 500)
		{
			setTitle("Scope3D fps: " + String.format("%.4f", 1000f / (System.currentTimeMillis() - lastUpdate) * fps) + String.format("\ttime: %.4fs", Statics.simulationTime));
			lastUpdate = System.currentTimeMillis();
			fps = 0;
		}
		
		if(System.currentTimeMillis() - lastPaint > 100)
		{
			lastPaint = System.currentTimeMillis();
			canvas.myPaint();
		}
	}
}

class Scope3DCanvas extends JPanel
{
	public Scope3D father;
	public int[] size = new int[] {800, 600};
	public int dist = 400;
	public double radius = 30;
	public double theta = 0.1;
	public double phi = -0.05;
	public long lastPaint = 0;
	
	BufferedImage image = new BufferedImage(size[0], size[1], BufferedImage.TYPE_INT_RGB);
	Graphics graphics = image.getGraphics();
	
	
	public Scope3DCanvas(Scope3D _father)
	{
		father = _father;
		
		MouseHandler mh = new MouseHandler();
		addMouseListener(mh);
		addMouseMotionListener(mh);
		addMouseWheelListener(mh);
	}
	
	public synchronized void myPaint()
	{
		if(System.currentTimeMillis() - lastPaint < 10)
		{return;}
		
		graphics.setColor(new Color(255, 255, 255));
		graphics.fillRect(0, 0, size[0], size[1]);
		
		for(int i = 0; i < Statics.particleArray.particleCount; i++)
		{
			Particle particle = Statics.particleArray.particles[i];
			double[] newPos = calcPos(particle.pos);
			if(newPos[0] < radius)
			{
				double x = dist * newPos[1] / (radius - newPos[0]);
				double y = dist * newPos[2] / (radius - newPos[0]);
				
				graphics.setColor(Scope3DAid.getColor(particle));
				graphics.fillRect((int)(x + size[0] / 2 - 1), (int)(-y + size[1] / 2 - 1), 2, 2);
				
				//500+50+5=555
				/*if(i == 555)
				{
					System.out.println(Statics.particleArray.particles[i].pressure);
					System.out.println(Statics.particleArray.particles[i].density);
					System.out.println("");
					graphics.setColor(new Color(150, 150, 255));
					graphics.fillRect((int)(x + size[0] / 2 - 1), (int)(-y + size[1] / 2 - 1), 4, 4);
				}*/
			}
		}
		
		double[] posX = calcPos(new double[] {0.6 * radius, 0, 0});
		double[] posY = calcPos(new double[] {0, 0.6 * radius, 0});
		double[] posZ = calcPos(new double[] {0, 0, 0.6 * radius});
		
		graphics.setColor(new Color(255, 0, 0));
		double x = dist * posX[1] / (radius - posX[0]);
		double y = dist * posX[2] / (radius - posX[0]);
		graphics.drawLine((int)(x + size[0] / 2 - 1), (int)(-y + size[1] / 2 - 1), (int)(size[0] / 2 - 1), (int)(size[1] / 2 - 1));
		
		graphics.setColor(new Color(0, 255, 0));
		x = dist * posY[1] / (radius - posY[0]);
		y = dist * posY[2] / (radius - posY[0]);
		graphics.drawLine((int)(x + size[0] / 2 - 1), (int)(-y + size[1] / 2 - 1), (int)(size[0] / 2 - 1), (int)(size[1] / 2 - 1));
		
		graphics.setColor(new Color(0, 0, 255));
		x = dist * posZ[1] / (radius - posZ[0]);
		y = dist * posZ[2] / (radius - posZ[0]);
		graphics.drawLine((int)(x + size[0] / 2 - 1), (int)(-y + size[1] / 2 - 1), (int)(size[0] / 2 - 1), (int)(size[1] / 2 - 1));
		
		graphics.setColor(new Color(0, 0, 0));
		graphics.drawRect(0, 0, size[0] - 1, size[1] - 1);
		this.repaint();
		
		lastPaint = System.currentTimeMillis();
	}
	
	public void paint(Graphics g)
	{
		g.drawImage(image, 0, 0, null);
	}
	
	private double[] calcPos(double[] _pos)
	{
		double[] newPos = new double[3];
		newPos[0] = _pos[0] * Math.cos(theta) * Math.cos(phi) - _pos[1] * Math.cos(theta) * Math.sin(phi) + _pos[2] * Math.sin(theta);
		newPos[1] = _pos[0] * Math.sin(phi) + _pos[1] * Math.cos(phi);
		newPos[2] = _pos[2] * Math.cos(theta) - _pos[0] * Math.cos(phi) * Math.sin(theta) + _pos[1] * Math.sin(theta) * Math.sin(phi);
		return newPos;
	}
	
	private class MouseHandler extends MouseAdapter implements MouseWheelListener
	{
		private double oldTheta;
		private double oldPhi;
		Point start = new Point(0, 0);
		Point current = new Point(0, 0);
		@Override
		public void mousePressed(MouseEvent me)
		{
			oldTheta = theta;
			oldPhi = phi;
			start = me.getPoint();
		}
		@Override
		public void mouseDragged(MouseEvent me)
		{
			int dx = me.getX() - start.x;
			int dy = me.getY() - start.y;
			phi = oldPhi + 0.008 * dx;
			theta = oldTheta + 0.008 * dy;
			if(theta > 0.5 * Math.PI)
			{theta = 0.5 * Math.PI;}
			if(theta < -0.5 * Math.PI)
			{theta = -0.5 * Math.PI;}
			myPaint();
		}
		@Override
		public void mouseWheelMoved(MouseWheelEvent e)
		{
			if(e.getWheelRotation() == 1)
			{
				radius /= 1.1;
			}
			if(e.getWheelRotation() == -1)
			{
				radius *= 1.1;
			}
			myPaint();
		}
	}
}

class Scope3DAid
{
	public static double[] vecAdd(double[] vec1, double[] vec2)
	{
		double[] ans = new double[3];
		ans[0] = vec1[0] + vec2[0];
		ans[1] = vec1[1] + vec2[1];
		ans[2] = vec1[2] + vec2[2];
		return ans;
	}
	
	public static double[] vecMul(double[] vec1, double x)
	{
		double[] ans = new double[3];
		ans[0] = vec1[0] * x;
		ans[1] = vec1[1] * x;
		ans[2] = vec1[2] * x;
		return ans;
	}
	
	public static Color getColor(Particle _particle)
	{
		if(_particle.type == "water")
		{return new Color(150, 150, 255);}
		else if(_particle.type == "honey")
		{return new Color(255, 150, 150);}
		else
		{return new Color(150, 150, 150);}
	}
}