package output.visualize;

public class ScopeTerminal
{
	private long lastUpdate = 0;
	private int fps=0;
	
	public void refresh()
	{
		fps += 1;
		if (System.currentTimeMillis() - lastUpdate > 500)
		{
			System.out.println("ScopeTerminal fps: " + 1000f / (System.currentTimeMillis() - lastUpdate) * fps);
			lastUpdate = System.currentTimeMillis();
			fps = 0;
		}
	}
}