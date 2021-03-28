package output.visualize;
//3D scope

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;

import simulation.Statics;
import base.Cell;
import base.Particle;

public class Scope2D extends JFrame
{
	private Scope2DCanvas canvas = new Scope2DCanvas(this);
	
	private int fpsCount = 0;
	private double fps = 0;
	private long lastPaint = 0;
	private long lastUpdate = 0;
	private String title;
	
	public Scope2D()
	{
		super("Scope2D");
		
		//setLayout(null);
		
		KeyHandler kh = new KeyHandler();
		addKeyListener(kh);
		
		Box box = new Box(BoxLayout.Y_AXIS);
		box.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		box.add(Box.createVerticalGlue());
		box.add(canvas);
		box.add(Box.createVerticalGlue());
		add(box);
		pack();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMaximumSize(getMinimumSize());
		setMinimumSize(getMinimumSize());
		setPreferredSize(getPreferredSize());
		setLocation(100, 100);
		
		setVisible(true);
	}
	
	public void refresh()
	{
		fpsCount += 1;
		if(System.currentTimeMillis() - lastUpdate > 500)
		{
			fps = fpsCount * 1000f / (System.currentTimeMillis() - lastUpdate);
			lastUpdate = System.currentTimeMillis();
			fpsCount = 0;
		}
		title = "Scope2D fps: " + String.format("%.4f", fps) + String.format("    time: %.5fs", Statics.simulationTime);
		if(title.equals(getTitle()) == false)
		{
			setTitle(title);
		}
		
		if(System.currentTimeMillis() - lastPaint > 20)
		{
			lastPaint = System.currentTimeMillis();
			canvas.myPaint();
		}
	}
	
	private class KeyHandler extends KeyAdapter
	{
		@Override
		public void keyPressed(KeyEvent ke)
		{
			//System.out.println("->" + ke.getKeyChar());
			if(ke.getKeyChar() == ' ')
			{
				Statics.isSimulating = !Statics.isSimulating;
				if(Statics.isSimulating == true)
				{
					System.out.println("simulation started!");
				}
				else
				{
					System.out.println("simulation stopped!");
				}
			}
			if(ke.getKeyChar() == 'p')
			{
				System.out.println(Statics.sensorArray.readSignal("sp1").get("maxPressure"));
			}
			if(ke.getKeyChar() == 'q')
			{
				System.out.println(Statics.sensorArray.readSignal("sp1").rawData);
			}
		}
	}
}

class Scope2DCanvas extends JPanel
{
	public Scope2D father;
	public int[] size = new int[] {800, 600};
	public double zoom = 20;
	public double centerX = 0;
	public double centerY = 0;
	
	public double lastPaint = 0;
	
	public BufferedImage image = new BufferedImage(size[0], size[1], BufferedImage.TYPE_INT_RGB);
	public BufferedImage outImage = new BufferedImage(size[0], size[1], BufferedImage.TYPE_INT_RGB);
	public Graphics2D graphics = (Graphics2D)image.getGraphics();
	
	private int focusNum = -1;
	
	public Scope2DCanvas(Scope2D _father)
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
			double[] relPos = calcPos(particle.pos);
			
			graphics.setColor(Scope2DAid.getColor(particle));
			graphics.fillRect((int)(relPos[0] + size[0] / 2 - 1), (int)(-relPos[1] + size[1] / 2 - 1), 2, 2);
			
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
		
		if(focusNum >= 0)
		{
			//draw grid
			graphics.setColor(new Color(200, 200, 200));
			
			double deltaLine = 10;
			double xMin = (-20 * deltaLine - centerX) * zoom;
			double xMax = (+20 * deltaLine - centerX) * zoom;
			double yMin = (-20 * deltaLine - centerY) * zoom;
			double yMax = (+20 * deltaLine - centerY) * zoom;
			for(int i = -20; i <= 20; i++)
			{
				double x = (i * deltaLine - centerX) * zoom;
				double y = (i * deltaLine - centerY) * zoom;
				graphics.drawLine((int)(x + size[0] / 2 - 1), (int)(yMin + size[1] / 2 - 1), (int)(x + size[0] / 2 - 1), (int)(yMax + size[1] / 2 - 1));
				graphics.drawLine((int)(xMin + size[0] / 2 - 1), (int)(-y + size[1] / 2 - 1), (int)(xMax + size[0] / 2 - 1), (int)(-y + size[1] / 2 - 1));
			}
		}
		
		double[] posX = calcPos(new double[] {200d / zoom, 0});
		double[] posY = calcPos(new double[] {0, 200d / zoom});
		double[] pos0 = calcPos(new double[] {0, 0});
		
		graphics.setColor(new Color(255, 0, 0));
		graphics.drawLine((int)(posX[0] + size[0] / 2 - 1), (int)(-posX[1] + size[1] / 2 - 1), (int)(pos0[0] + size[0] / 2 - 1), (int)(-pos0[1] + size[1] / 2 - 1));
		
		graphics.setColor(new Color(0, 255, 0));
		graphics.drawLine((int)(posY[0] + size[0] / 2 - 1), (int)(-posY[1] + size[1] / 2 - 1), (int)(pos0[0] + size[0] / 2 - 1), (int)(-pos0[1] + size[1] / 2 - 1));
		
		graphics.setColor(new Color(0, 0, 0));
		graphics.drawRect(0, 0, size[0] - 1, size[1] - 1);
		
		if(focusNum >= 0)
		{
			//show particle info
			Particle particle = Statics.particleArray.particles[focusNum];
			double[] relPos = calcPos(particle.pos);
			graphics.setColor(new Color(50, 50, 255));
			graphics.fillRect((int)(relPos[0] + size[0] / 2 - 2), (int)(-relPos[1] + size[1] / 2 - 2), 4, 4);
			
			graphics.setColor(new Color(50, 50, 255));
			Font stringFont = new Font("Arial", Font.PLAIN, 24);
			graphics.setFont(stringFont);
			graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
			
			String data = "";
			data += "id = " + String.format("%d\n", focusNum);
			data += "density = " + String.format("%.4f\n", particle.density);
			data += "energy = " + String.format("%.4f\n", particle.energy);
			data += "pressure = " + String.format("%.4f\n", particle.pressure);
			data += "temperature = " + String.format("%.4f\n", particle.temperature);
			data += "vx = " + String.format("%.4f\n", particle.spd[0]);
			drawString(graphics, data, 10, 0);
			
			double h = Statics.H * zoom;
			
			graphics.setColor(new Color(50, 50, 255));
			graphics.drawLine((int)(size[0] - 20 - 0 * h), 30, (int)(size[0] - 20 - 0 * h), 20);
			graphics.drawLine((int)(size[0] - 20 - 0 * h), 30, (int)(size[0] - 20 - 1 * h), 30);
			graphics.drawLine((int)(size[0] - 20 - 1 * h), 30, (int)(size[0] - 20 - 1 * h), 20);
			graphics.drawLine((int)(size[0] - 20 - 1 * h), 30, (int)(size[0] - 20 - 2 * h), 30);
			graphics.drawLine((int)(size[0] - 20 - 2 * h), 30, (int)(size[0] - 20 - 2 * h), 20);
		}
		
		outImage.getGraphics().drawImage(image, 0, 0, null);
		this.repaint();
		
		lastPaint = System.currentTimeMillis();
	}
	private void drawString(Graphics2D g, String text, int x, int y)
	{
		for(String line : text.split("\n"))
		{ g.drawString(line, x, y += g.getFontMetrics().getHeight()); }
	}
	
	private void markParticle(double[] pos)
	{
		double dist = 1000;
		int _focusNum = -1;
		for(int i = 0; i < Statics.particleArray.particleCount; i++)
		{
			Particle particle = Statics.particleArray.particles[i];
			double[] pos1 = particle.pos;
			double _dist = Math.sqrt((pos1[0] - pos[0]) * (pos1[0] - pos[0]) + (pos1[1] - pos[1]) * (pos1[1] - pos[1]));
			if(_dist < 50 / zoom)
			{
				if(_dist < dist)
				{
					dist = _dist;
					_focusNum = i;
				}
			}
		}
		focusNum = _focusNum;
	}
	
	public void paint(Graphics g)
	{
		g.drawImage(image, 5, 5, null);
	}
	
	private double[] calcPos(double[] _pos)
	{
		double[] ans = new double[2];
		ans[0] = (_pos[0] - centerX) * zoom;
		ans[1] = (_pos[1] - centerY) * zoom;
		return ans;
	}
	
	private double[] antiCalcPos(double[] _pos)
	{
		double[] ans = new double[2];
		ans[0] = _pos[0] / zoom + centerX;
		ans[1] = _pos[1] / zoom + centerY;
		return ans;
	}
	
	private class MouseHandler extends MouseAdapter implements MouseWheelListener
	{
		private double oldX;
		private double oldY;
		private boolean leftDown = false;
		private boolean rightDown = false;
		Point start = new Point(0, 0);
		@Override
		public void mousePressed(MouseEvent me)
		{
			if(me.getButton() == MouseEvent.BUTTON1)
			{
				leftDown = true;
				
				oldX = centerX;
				oldY = centerY;
				start = me.getPoint();
			}
			else if(me.getButton() == MouseEvent.BUTTON3)
			{
				Point _point = me.getPoint();
				double[] pos = new double[2];
				pos[0] = _point.getX() - 5 - size[0] / 2;
				pos[1] = -_point.getY() + 5 + size[1] / 2;
				
				double[] relPos = antiCalcPos(pos);
				markParticle(relPos);
			}
		}
		@Override
		public void mouseDragged(MouseEvent me)
		{
			if(leftDown == true)
			{
				int dx = me.getX() - start.x;
				int dy = me.getY() - start.y;
				centerX = oldX - dx / zoom;
				centerY = oldY + dy / zoom;
				
				myPaint();
			}
		}
		@Override
		public void mouseReleased(MouseEvent me)
		{
			if(me.getButton() == MouseEvent.BUTTON1)
			{
				leftDown = false;
			}
		}
		@Override
		public void mouseWheelMoved(MouseWheelEvent e)
		{
			if(e.getWheelRotation() == 1)
			{
				zoom *= 1.1;
			}
			if(e.getWheelRotation() == -1)
			{
				zoom /= 1.1;
			}
			myPaint();
		}
	}
	
	@Override
	public Dimension getMinimumSize()
	{return new Dimension(size[0] + 10, size[1] + 10);}
	@Override
	public Dimension getMaximumSize()
	{return new Dimension(size[0] + 10, size[1] + 10);}
	@Override
	public Dimension getPreferredSize()
	{return new Dimension(size[0] + 10, size[1] + 10);}
}

class Scope2DAid
{
	public static double[] vecAdd(double[] vec1, double[] vec2)
	{
		double[] ans = new double[2];
		ans[0] = vec1[0] + vec2[0];
		ans[1] = vec1[1] + vec2[1];
		return ans;
	}
	
	public static double[] vecMul(double[] vec1, double x)
	{
		double[] ans = new double[2];
		ans[0] = vec1[0] * x;
		ans[1] = vec1[1] * x;
		return ans;
	}
	
	public static Color getColor(Particle _particle)
	{
		return getColorByPressure(_particle);
	}
	
	public static Color getColorByType(Particle _particle)
	{
		if(_particle.type == "water")
		{return new Color(150, 150, 255);}
		else if(_particle.type == "honey")
		{return new Color(255, 150, 150);}
		else
		{return new Color(150, 150, 150);}
	}
	
	public static Color getColorByTemperature(Particle _particle)
	{
		double tMin = 200;
		double tMax = 400;
		
		double mag = (_particle.temperature - tMin) / (tMax - tMin);
		//mag = Math.sqrt(10 * mag);
		mag = mag > 0.9999 ? 0.9999 : mag;
		mag = mag < 0.0001 ? 0.0001 : mag;
		
		double r = 127d * (1 - Math.cos(mag * 3.1415));
		double g = 255d * Math.sin(mag * 3.1415);
		double b = 127d * (1 + Math.cos(mag * 3.1415));
		
		return new Color((int)r, (int)g, (int)b);
	}
	
	public static Color getColorByPressure(Particle _particle)
	{
		double tMin = 1e5;
		double tMax = 5e5;
		
		double mag = (_particle.pressure - tMin) / (tMax - tMin);
		//mag = Math.sqrt(10 * mag);
		mag = mag > 0.9999 ? 0.9999 : mag;
		mag = mag < 0.0001 ? 0.0001 : mag;
		
		double r = 127d * (1 - Math.cos(mag * 3.1415));
		double g = 255d * Math.sin(mag * 3.1415);
		double b = 127d * (1 + Math.cos(mag * 3.1415));
		
		return new Color((int)r, (int)g, (int)b);
	}
}