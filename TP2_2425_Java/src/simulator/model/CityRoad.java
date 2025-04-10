package simulator.model;

public class CityRoad extends Road {

	CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {
		int reduction = (weather == Weather.WINDY || weather == Weather.STORM) ? 10 : 2;
		totalCO2 = Math.max(0, totalCO2 - reduction);
	}

	@Override
	void updateSpeedLimit() {
		speedLimit = maxSpeed;
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		return ((11 - v.getContClass()) * speedLimit) / 11;
	}
}
