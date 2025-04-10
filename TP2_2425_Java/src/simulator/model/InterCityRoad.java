package simulator.model;

public class InterCityRoad extends Road {

	InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {
		int x = switch (weather) {
			case SUNNY -> 2;
			case CLOUDY -> 3;
			case RAINY -> 10;
			case WINDY -> 15;
			case STORM -> 20;
		};

		totalCO2 = ((100 - x) * totalCO2) / 100;
	}

	@Override
	void updateSpeedLimit() {
		if (totalCO2 > contLimit) {
			speedLimit = maxSpeed / 2;
		} else {
			speedLimit = maxSpeed;
		}
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		int speed = speedLimit;

		if (weather == Weather.STORM) {
			speed = (speed * 8) / 10;
		}

		return speed;
	}
}
