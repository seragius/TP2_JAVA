package simulator.model;

import java.util.*;
import org.json.JSONObject;
import simulator.model.Event;


public class TrafficSimulator {

	private RoadMap map;
	private PriorityQueue<Event> eventQueue;
	private int currentTime;

	public TrafficSimulator() {
		this.map = new RoadMap();
		this.eventQueue = new PriorityQueue<>();
		this.currentTime = 0;
	}
	
	public void addEvent(Event e) {
		if (e == null)
			throw new IllegalArgumentException("Evento no puede ser null");
		eventQueue.add(e);
	}

	public void advance() {
		currentTime++;

		// Ejecutar eventos del tick actual
		while (!eventQueue.isEmpty() && eventQueue.peek().getTime() == currentTime) {
			eventQueue.poll().execute(map);
		}

		// Avanzar cruces
		for (Junction j : map.getJunctions()) {
			j.advance(currentTime);
		}

		// Avanzar carreteras
		for (Road r : map.getRoads()) {
			r.advance(currentTime);
		}
	}

	public void reset() {
		map.reset();
		eventQueue.clear();
		currentTime = 0;
	}

	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("time", currentTime);
		jo.put("state", map.report());
		return jo;
	}

	public int getCurrentTime() {
		return currentTime;
	}

	public RoadMap getRoadMap() {
		return map;
	}
	/*
	public void advance() {
		currentTime++;

		// 1. Ejecutar eventos con tiempo actual
		while (!eventQueue.isEmpty() && eventQueue.peek().getTime() == currentTime) {
			eventQueue.poll().execute(map);
		}

		// 2. Avanzar cruces
		for (Junction j : map.getJunctions()) {
			j.advance(currentTime);
		}

		// 3. Avanzar carreteras
		for (Road r : map.getRoads()) {
			r.advance(currentTime);
		}
	}

	public void reset() {
		this.map.reset(); // limpia todas las listas del mapa
		this.eventQueue.clear(); // limpia la cola de eventos
		this.currentTime = 0;
	}

	public JSONObject report() {
		JSONObject jo = new JSONObject();

		jo.put("time", this.currentTime);
		jo.put("state", map.report());

		return jo;
	}
	*/


	
}
