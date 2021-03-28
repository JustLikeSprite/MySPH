package simulation.sensor;

public class DataPack
{
	public String rawData = "";
	
	public DataPack()
	{}
	
	public void add(String _attribute, Object obj)
	{
		rawData += "\n" + _attribute + "\t" + obj.toString();
	}
	
	public String get(String _attribute)
	{
		String[] lst = rawData.split("\n");
		for(int i = 0; i < lst.length; i++)
		{
			String[] _lst = lst[i].split("\t");
			if(_lst[0].equals(_attribute))
			{
				return _lst[1];
			}
		}
		return "no attribute found";
	}
}