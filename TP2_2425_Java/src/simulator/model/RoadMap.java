package simulator.model;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

class RoadMap {

	private List<Junction> junctions;
	private List<Road> roads;
	private List<Vehicle> vehicles;

	private Map<String, Junction> junctionMap;
	private Map<String, Road> roadMap;
	private Map<String, Vehicle> vehicleMap;

	RoadMap() {
		junctions = new ArrayList<>();
		roads = new ArrayList<>();
		vehicles = new ArrayList<>();

		junctionMap = new HashMap<>();
		roadMap = new HashMap<>();
		vehicleMap = new HashMap<>();
	}

	void addJunction(Junction j) {
		if (junctionMap.containsKey(j.getId()))
			throw new IllegalArgumentException("Duplicate junction id: " + j.getId());

		junctions.add(j);
		junctionMap.put(j.getId(), j);
	}

	void addRoad(Road r) {
		if (roadMap.containsKey(r.getId()))
			throw new IllegalArgumentException("Duplicate road id: " + r.getId());

		if (!junctionMap.containsValue(r.getSrc()) || !junctionMap.containsValue(r.getDest()))
			throw new IllegalArgumentException("Invalid junctions for road: " + r.getId());

		roads.add(r);
		roadMap.put(r.getId(), r);
	}

	void addVehicle(Vehicle v) {
		if (vehicleMap.containsKey(v.getId()))
			throw new IllegalArgumentException("Duplicate vehicle id: " + v.getId());

		// Validar que todos los tramos del itinerario existen
		List<Junction> it = v.getItinerary();
		for (int i = 0; i < it.size() - 1; i++) {
			Junction from = it.get(i);
			Junction to = it.get(i + 1);
			if (from.roadTo(to) == null)
				throw new IllegalArgumentException("No road from " + from.getId() + " to " + to.getId());
		}

		vehicles.add(v);
		vehicleMap.put(v.getId(), v);
	}

	public Junction getJunction(String id) {
		return junctionMap.get(id);
	}

	public Road getRoad(String id) {
		return roadMap.get(id);
	}

	public Vehicle getVehicle(String id) {
		return vehicleMap.get(id);
	}

	public List<Junction> getJunctions() {
		return Collections.unmodifiableList(junctions);
	}

	public List<Road> getRoads() {
		return Collections.unmodifiableList(roads);
	}

	public List<Vehicle> getVehicles() {
		return Collections.unmodifiableList(vehicles);
	}

	void reset() {
		junctions.clear();
		roads.clear();
		vehicles.clear();

		junctionMap.clear();
		roadMap.clear();
		vehicleMap.clear();
	}

	public JSONObject report() {
		JSONObject jo = new JSONObject();

		// Cruces
		jo.put("junctions", junctions.stream()
			.map(Junction::report)
			.collect(org.json.JSONArray::new, JSONArray::put, JSONArray::putAll));

		// Carreteras
		jo.put("roads", roads.stream()
			.map(Road::report)
			.collect(org.json.JSONArray::new, JSONArray::put, JSONArray::putAll));

		// Vehículos
		jo.put("vehicles", vehicles.stream()
			.map(Vehicle::report)
			.collect(org.json.JSONArray::new, JSONArray::put, JSONArray::putAll));

		return jo;
	}

}