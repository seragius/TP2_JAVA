package simulator.model;

import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

public class Vehicle extends SimulatedObject {

	enum VehicleStatus {
		PENDING, TRAVELING, WAITING, ARRIVED
	}

	private List<Junction> itinerary;
	private int maxSpeed;
	private int speed;
	private VehicleStatus status;
	private Road road;
	private int location;
	private int contClass;
	private int totalCO2;
	private int totalDistance;
	private int itineraryIndex;
	private int currJunctionIndex;


	Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) {
		super(id);

		if (maxSpeed <= 0)
			throw new IllegalArgumentException("maxSpeed debe ser > 0");
		if (contClass < 0 || contClass > 10)
			throw new IllegalArgumentException("contClass debe estar entre 0 y 10");
		if (itinerary == null || itinerary.size() < 2)
			throw new IllegalArgumentException("El itinerario debe tener al menos 2 cruces");

		this.maxSpeed = maxSpeed;
		this.contClass = contClass;
		this.itinerary = Collections.unmodifiableList(itinerary);
		this.status = VehicleStatus.PENDING;
		this.speed = 0;
		this.location = 0;
		this.totalCO2 = 0;
		this.totalDistance = 0;
		this.road = null;
		this.itineraryIndex = 0;
		this.currJunctionIndex = 0;

	}

	// Métodos: setSpeed, setContaminationClass, advance, moveToNextRoad, report
	// y getters públicos que definiremos después
	
	void setSpeed(int s) {
		if (s < 0)
			throw new IllegalArgumentException("La velocidad no puede ser negativa");

		if (status != VehicleStatus.TRAVELING) {
			this.speed = 0;
		} else {
			this.speed = Math.min(s, maxSpeed);
		}
	}

	void setContaminationClass(int c) {
		if (c < 0 || c > 10)
			throw new IllegalArgumentException("El grado de contaminación debe estar entre 0 y 10");

		this.contClass = c;
	}

	@Override
	void advance(int currTime) {
		if (status != VehicleStatus.TRAVELING) return;

		int oldLocation = location;
		location = Math.min(location + speed, road.getLength());

		int distanceTravelled = location - oldLocation;
		int emittedCO2 = distanceTravelled * contClass;

		totalCO2 += emittedCO2;
		totalDistance += distanceTravelled;
		road.addContamination(emittedCO2);

		if (location >= road.getLength()) {
			speed = 0;
			status = VehicleStatus.WAITING;
			road.getDest().enter(this);
		}
	}
	
	void moveToNextRoad() {
		if (status != VehicleStatus.PENDING && status != VehicleStatus.WAITING)
			throw new IllegalStateException("El vehículo debe estar en estado PENDING o WAITING");

		if (status == VehicleStatus.WAITING)
			road.exit(this); // salimos de la carretera actual

		if (currJunctionIndex >= itinerary.size() - 1) {
			status = VehicleStatus.ARRIVED;
			road = null;
		} else {
			Junction from = itinerary.get(currJunctionIndex);
			Junction to = itinerary.get(currJunctionIndex + 1);
			currJunctionIndex++;

			road = from.roadTo(to);
			location = 0;
			speed = 0;
			road.enter(this);
			status = VehicleStatus.TRAVELING;
		}
	}
	
	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();

		jo.put("id", _id);
		jo.put("speed", speed);
		jo.put("distance", totalDistance);
		jo.put("co2", totalCO2);
		jo.put("class", contClass);
		jo.put("status", status.toString());

		if (status == VehicleStatus.TRAVELING || status == VehicleStatus.WAITING) {
			jo.put("road", road.getId());
			jo.put("location", location);
		}

		return jo;
	}
	
	public int getLocation() {
		return location;
	}

	public int getSpeed() {
		return speed;
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}

	public int getContClass() {
		return contClass;
	}

	public VehicleStatus getStatus() {
		return status;
	}

	public int getTotalCO2() {
		return totalCO2;
	}

	public int getTotalDistance() {
		return totalDistance;
	}

	public List<Junction> getItinerary() {
		return itinerary;
	}

	public Road getRoad() {
		return road;
	}




}
