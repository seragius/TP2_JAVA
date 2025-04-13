package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

public abstract class Road extends SimulatedObject {

	protected Junction _srcJunc;
	protected Junction _destJunc;
	protected int _length;
	protected int _maxSpeed;
	protected int _speedLimit;
	protected int _contLimit;
	protected Weather _weather;
	protected int _totalCO2;
	protected List<Vehicle> _vehicles;

	Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id);

		if (maxSpeed <= 0)
			throw new IllegalArgumentException("Max speed must be positive");
		if (contLimit < 0)
			throw new IllegalArgumentException("Contamination limit cannot be negative");
		if (length <= 0)
			throw new IllegalArgumentException("Length must be positive");
		if (srcJunc == null || destJunc == null)
			throw new IllegalArgumentException("Junctions cannot be null");
		if (weather == null)
			throw new IllegalArgumentException("Weather cannot be null");

		_srcJunc = srcJunc;
		_destJunc = destJunc;
		_length = length;
		_maxSpeed = maxSpeed;
		_speedLimit = maxSpeed;
		_contLimit = contLimit;
		_weather = weather;
		_totalCO2 = 0;
		_vehicles = new ArrayList<>();

		_srcJunc.addOutGoingRoad(this);
		_destJunc.addIncommingRoad(this);
	}

	void enter(Vehicle v) {
		if (v.getLocation() != 0 || v.getSpeed() != 0)
			throw new IllegalArgumentException("Vehicle must enter at location 0 with speed 0");
		_vehicles.add(v);
	}

	void exit(Vehicle v) {
		_vehicles.remove(v);
	}

	void setWeather(Weather w) {
		if (w == null)
			throw new IllegalArgumentException("Weather cannot be null");
		_weather = w;
	}

	void addContamination(int c) {
		if (c < 0)
			throw new IllegalArgumentException("Contamination cannot be negative");
		_totalCO2 += c;
	}

	public abstract void reduceTotalContamination();

	public abstract void updateSpeedLimit();

	public abstract int calculateVehicleSpeed(Vehicle v);

	@Override
	void advance(int currTime) {
		reduceTotalContamination();
		updateSpeedLimit();

		for (Vehicle v : _vehicles) {
			v.setSpeed(calculateVehicleSpeed(v));
			v.advance(currTime);
		}

		_vehicles.sort((v1, v2) -> v2.getLocation() - v1.getLocation());
	}

	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();

		jo.put("id", _id);
		jo.put("speedlimit", _speedLimit);
		jo.put("weather", _weather.toString());
		jo.put("co2", _totalCO2);

		List<String> vs = new ArrayList<>();
		for (Vehicle v : _vehicles)
			vs.add(v.getId());

		jo.put("vehicles", vs);

		return jo;
	}

	// Public getters
	public int getLength() {
		return _length;
	}

	public Junction getDest() {
		return _destJunc;
	}

	public Junction getSrc() {
		return _srcJunc;
	}

	public Weather getWeather() {
		return _weather;
	}

	public int getContLimit() {
		return _contLimit;
	}

	public int getMaxSpeed() {
		return _maxSpeed;
	}

	public int getTotalCO2() {
		return _totalCO2;
	}

	public int getSpeedLimit() {
		return _speedLimit;
	}

	public List<Vehicle> getVehicles() {
		return Collections.unmodifiableList(_vehicles);
	}
}
