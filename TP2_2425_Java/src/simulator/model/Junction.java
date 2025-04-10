package simulator.model;

import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class Junction extends SimulatedObject {

	private List<Road> inRoads;
	private Map<Junction, Road> outRoads;
	private Map<Road, List<Vehicle>> roadQueues;
	private List<List<Vehicle>> queueList;

	private int greenLightIndex;
	private int lastSwitchingTime;

	private LightSwitchingStrategy lsStrategy;
	private DequeuingStrategy dqStrategy;

	private int xCoor, yCoor;

	Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
		super(id);

		if (lsStrategy == null || dqStrategy == null)
			throw new IllegalArgumentException("Estrategias no pueden ser null");

		if (xCoor < 0 || yCoor < 0)
			throw new IllegalArgumentException("Coordenadas no pueden ser negativas");

		this.lsStrategy = lsStrategy;
		this.dqStrategy = dqStrategy;
		this.xCoor = xCoor;
		this.yCoor = yCoor;

		this.inRoads = new ArrayList<>();
		this.outRoads = new HashMap<>();
		this.roadQueues = new HashMap<>();
		this.queueList = new ArrayList<>();

		this.greenLightIndex = -1;
		this.lastSwitchingTime = 0;
	}
	
	void addIncommingRoad(Road r) {
		if (r.getDest() != this)
			throw new IllegalArgumentException("La carretera no termina en este cruce");

		inRoads.add(r);

		List<Vehicle> q = new LinkedList<>();
		roadQueues.put(r, q);
		queueList.add(q);
	}

	void addOutGoingRoad(Road r) {
		Junction dest = r.getDest();

		if (outRoads.containsKey(dest))
			throw new IllegalArgumentException("Ya hay una carretera hacia ese cruce");

		if (r.getSrc() != this)
			throw new IllegalArgumentException("La carretera no sale de este cruce");

		outRoads.put(dest, r);
	}

	void enter(Vehicle v) {
		roadQueues.get(v.getRoad()).add(v);
	}

	Road roadTo(Junction j) {
		return outRoads.get(j);
	}

	@Override
	void advance(int currTime) {
		if (greenLightIndex != -1) {
			List<Vehicle> colaActual = queueList.get(greenLightIndex);
			List<Vehicle> toMove = dqStrategy.dequeue(colaActual);

			for (Vehicle v : toMove) {
				colaActual.remove(v);
				v.moveToNextRoad();
			}
		}

		int nextGreen = lsStrategy.chooseNextGreen(
			inRoads,
			queueList,
			greenLightIndex,
			lastSwitchingTime,
			currTime
		);

		if (nextGreen != greenLightIndex) {
			greenLightIndex = nextGreen;
			lastSwitchingTime = currTime;
		}
	}

	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();

		jo.put("id", _id);
		jo.put("green", greenLightIndex == -1 ? "none" : inRoads.get(greenLightIndex).getId());

		JSONArray ja = new JSONArray();
		for (int i = 0; i < inRoads.size(); i++) {
			JSONObject qReport = new JSONObject();
			qReport.put("road", inRoads.get(i).getId());

			JSONArray vArray = new JSONArray();
			for (Vehicle v : queueList.get(i)) {
				vArray.put(v.getId());
			}
			qReport.put("vehicles", vArray);

			ja.put(qReport);
		}

		jo.put("queues", ja);
		return jo;
	}

	public int getX() {
		return xCoor;
	}

	public int getY() {
		return yCoor;
	}

	public List<Road> getInRoads() {
		return Collections.unmodifiableList(inRoads);
	}

	public Map<Junction, Road> getOutRoads() {
		return Collections.unmodifiableMap(outRoads);
	}

	public int getGreenLightIndex() {
		return greenLightIndex;
	}

	public Road getInRoad(int index) {
		return inRoads.get(index);
	}

	public List<Vehicle> getQueueFor(Road r) {
		return Collections.unmodifiableList(roadQueues.get(r));
	}

	
}
