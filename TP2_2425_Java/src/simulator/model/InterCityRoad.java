package simulator.model;

public class InterCityRoad extends Road {

	public InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	public void reduceTotalContamination() {
		int x;
		switch (_weather) {
			case SUNNY: x = 2; break;
			case CLOUDY: x = 3; break;
			case RAINY: x = 10; break;
			case WINDY: x = 15; break;
			case STORM: x = 20; break;
			default: x = 0;
		}
		_totalCO2 = ((100 - x) * _totalCO2) / 100;
	}

	@Override
	public void updateSpeedLimit() {
		if (_totalCO2 > _contLimit) {
			_speedLimit = _maxSpeed / 2;
		} else {
			_speedLimit = _maxSpeed;
		}
	}

	@Override
	public int calculateVehicleSpeed(Vehicle v) {
		int speed = _speedLimit;
		if (_weather == Weather.STORM) {
			speed = (speed * 8) / 10;
		}
		return speed;
	}
}
