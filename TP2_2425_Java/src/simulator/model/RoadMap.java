package simulator.model;

import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class RoadMap {

	private List<Junction> _junctions;
	private List<Road> _roads;
	private List<Vehicle> _vehicles;

	private Map<String, Junction> _junctionsMap;
	private Map<String, Road> _roadsMap;
	private Map<String, Vehicle> _vehiclesMap;

	RoadMap() {
		_junctions = new ArrayList<>();
		_roads = new ArrayList<>();
		_vehicles = new ArrayList<>();

		_junctionsMap = new HashMap<>();
		_roadsMap = new HashMap<>();
		_vehiclesMap = new HashMap<>();
	}

	void addJunction(Junction j) {
		if (_junctionsMap.containsKey(j.getId())) {
			throw new IllegalArgumentException("Duplicate junction id: " + j.getId());
		}
		_junctions.add(j);
		_junctionsMap.put(j.getId(), j);
	}

	void addRoad(Road r) {
		if (_roadsMap.containsKey(r.getId())) {
			throw new IllegalArgumentException("Duplicate road id: " + r.getId());
		}
		if (!_junctionsMap.containsValue(r.getSrc()) || !_junctionsMap.containsValue(r.getDest())) {
			throw new IllegalArgumentException("Road connects to unknown junctions.");
		}
		_roads.add(r);
		_roadsMap.put(r.getId(), r);
	}

	void addVehicle(Vehicle v) {
		if (_vehiclesMap.containsKey(v.getId())) {
			throw new IllegalArgumentException("Duplicate vehicle id: " + v.getId());
		}
		List<Junction> itinerary = v.getItinerary();
		for (int i = 0; i < itinerary.size() - 1; i++) {
			if (itinerary.get(i).roadTo(itinerary.get(i + 1)) == null) {
				throw new IllegalArgumentException("Invalid itinerary: no road from " +
					itinerary.get(i).getId() + " to " + itinerary.get(i + 1).getId());
			}
		}
		_vehicles.add(v);
		_vehiclesMap.put(v.getId(), v);
	}

	public Junction getJunction(String id) {
		return _junctionsMap.get(id);
	}

	public Road getRoad(String id) {
		return _roadsMap.get(id);
	}

	public Vehicle getVehicle(String id) {
		return _vehiclesMap.get(id);
	}

	public List<Junction> getJunctions() {
		return Collections.unmodifiableList(_junctions);
	}

	public List<Road> getRoads() {
		return Collections.unmodifiableList(_roads);
	}

	public List<Vehicle> getVehicles() {
		return Collections.unmodifiableList(_vehicles);
	}

	void reset() {
		_junctions.clear();
		_roads.clear();
		_vehicles.clear();

		_junctionsMap.clear();
		_roadsMap.clear();
		_vehiclesMap.clear();
	}

	public JSONObject report() {
		JSONObject jo = new JSONObject();
		JSONArray jaJunctions = new JSONArray();
		JSONArray jaRoads = new JSONArray();
		JSONArray jaVehicles = new JSONArray();

		for (Junction j : _junctions) {
			jaJunctions.put(j.report());
		}
		for (Road r : _roads) {
			jaRoads.put(r.report());
		}
		for (Vehicle v : _vehicles) {
			jaVehicles.put(v.report());
		}

		jo.put("junctions", jaJunctions);
		jo.put("roads", jaRoads);
		jo.put("vehicles", jaVehicles);
		return jo;
	}
}
