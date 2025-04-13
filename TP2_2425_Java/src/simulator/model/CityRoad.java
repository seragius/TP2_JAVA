package simulator.model;

public class CityRoad extends Road {

	public CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	public void reduceTotalContamination() {
		int reduction = (_weather == Weather.WINDY || _weather == Weather.STORM) ? 10 : 2;
		_totalCO2 = Math.max(0, _totalCO2 - reduction);
	}

	@Override
	public void updateSpeedLimit() {
		_speedLimit = _maxSpeed;
	}

	@Override
	public int calculateVehicleSpeed(Vehicle v) {
		return ((_speedLimit * (11 - v.getContClass())) / 11);
	}
}
