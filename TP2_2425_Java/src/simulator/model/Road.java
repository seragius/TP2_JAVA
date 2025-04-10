package simulator.model;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Road extends SimulatedObject {

	protected Junction srcJunction;
	protected Junction destJunction;
	protected int length;
	protected int maxSpeed;
	protected int speedLimit;
	protected int contLimit;
	protected Weather weather;
	protected int totalCO2;

	protected List<Vehicle> vehicles;

	Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id);

		if (srcJunc == null || destJunc == null)
			throw new IllegalArgumentException("Cruces no pueden ser null");
		if (maxSpeed <= 0 || contLimit < 0 || length <= 0 || weather == null)
			throw new IllegalArgumentException("Parámetros inválidos en carretera");

		this.srcJunction = srcJunc;
		this.destJunction = destJunc;
		this.maxSpeed = maxSpeed;
		this.speedLimit = maxSpeed;
		this.contLimit = contLimit;
		this.length = length;
		this.weather = weather;
		this.totalCO2 = 0;

		this.vehicles = new ArrayList<>();

		srcJunc.addOutGoingRoad(this);
		destJunction.addIncommingRoad(this);
	}

	void enter(Vehicle v) {
		if (v.getLocation() != 0 || v.getSpeed() != 0)
			throw new IllegalArgumentException("Vehículo no válido para entrar");

		vehicles.add(v);
	}

	void exit(Vehicle v) {
		vehicles.remove(v);
	}

	void setWeather(Weather w) {
		if (w == null)
			throw new IllegalArgumentException("Weather no puede ser null");

		this.weather = w;
	}

	void addContamination(int c) {
		if (c < 0)
			throw new IllegalArgumentException("La contaminación no puede ser negativa");

		this.totalCO2 += c;
	}

	@Override
	void advance(int currTime) {
		reduceTotalContamination();
		updateSpeedLimit();

		for (Vehicle v : vehicles) {
			int newSpeed = calculateVehicleSpeed(v);
			v.setSpeed(newSpeed);
			v.advance(currTime);
		}

		vehicles.sort((v1, v2) -> Integer.compare(v2.getLocation(), v1.getLocation()));
	}

	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();

		jo.put("id", _id);
		jo.put("speedlimit", speedLimit);
		jo.put("weather", weather.toString());
		jo.put("co2", totalCO2);

		JSONArray ja = new JSONArray();
		for (Vehicle v : vehicles) {
			ja.put(v.getId());
		}
		jo.put("vehicles", ja);

		return jo;
	}

	// Métodos abstractos que implementarán CityRoad e InterCityRoad
	abstract void reduceTotalContamination();

	abstract void updateSpeedLimit();

	abstract int calculateVehicleSpeed(Vehicle v);

	// Getters públicos
	public int getLength() {
		return length;
	}

	public Junction getDest() {
		return destJunction;
	}

	public Junction getSrc() {
		return srcJunction;
	}

	public Weather getWeather() {
		return weather;
	}

	public int getContLimit() {
		return contLimit;
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}

	public int getTotalCO2() {
		return totalCO2;
	}

	public int getSpeedLimit() {
		return speedLimit;
	}

	public List<Vehicle> getVehicles() {
		return Collections.unmodifiableList(vehicles);
	}
}
