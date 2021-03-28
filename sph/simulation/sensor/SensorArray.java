package simulation.sensor;

import simulation.sensor.Sensor;
import simulation.sensor.DataPack;

public class SensorArray
{
	public Sensor[] sensors = new Sensor[10000];
	public int sensorCount = 0;
	
	public void addSensor(Sensor _sensor)
	{
		sensors[sensorCount] = _sensor;
		sensorCount++;
	}
	
	public void refreshAllSensors()
	{
		for(int i = 0; i < sensorCount; i++)
		{
			sensors[i].refresh();
		}
	}
	
	public DataPack readSignal(String _id)
	{
		for(int i = 0; i < sensorCount; i++)
		{
			if(sensors[i].id.equals(_id) == true)
			{
				return sensors[i].readSignal();
			}
		}
		DataPack dp = new DataPack();
		dp.add("state", "error");
		return dp;
	}
}