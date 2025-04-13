package simulator.model;

import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class Junction extends SimulatedObject {

    private List<Road> _inRoads;
    private Map<Junction, Road> _outRoads;
    private List<List<Vehicle>> _queues;
    private Map<Road, List<Vehicle>> _roadQueueMap;

    private int _greenLightIndex;
    private int _lastSwitchingTime;

    private LightSwitchingStrategy _lsStrategy;
    private DequeuingStrategy _dqStrategy;

    private int _x, _y;

    Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int x, int y) {
        super(id);

        if (lsStrategy == null || dqStrategy == null)
            throw new IllegalArgumentException("Strategies cannot be null.");

        if (x < 0 || y < 0)
            throw new IllegalArgumentException("Coordinates must be non-negative.");

        _lsStrategy = lsStrategy;
        _dqStrategy = dqStrategy;

        _x = x;
        _y = y;

        _inRoads = new ArrayList<>();
        _outRoads = new HashMap<>();
        _queues = new ArrayList<>();
        _roadQueueMap = new HashMap<>();

        _greenLightIndex = -1;
        _lastSwitchingTime = 0;
    }

    void addIncommingRoad(Road r) {
        if (r.getDest() != this) {
            throw new IllegalArgumentException("Destination must be this junction.");
        }

        _inRoads.add(r);
        List<Vehicle> queue = new ArrayList<>();
        _queues.add(queue);
        _roadQueueMap.put(r, queue);
    }

    void addOutGoingRoad(Road r) {
        Junction dest = r.getDest();

        if (_outRoads.containsKey(dest)) {
            throw new IllegalArgumentException("There is already an outgoing road to junction " + dest.getId());
        }

        if (r.getSrc() != this) {
            throw new IllegalArgumentException("This road does not start from this junction.");
        }

        _outRoads.put(dest, r);
    }

    void enter(Vehicle v) {
        Road r = v.getRoad();
        List<Vehicle> queue = _roadQueueMap.get(r);

        if (queue == null) {
            throw new IllegalArgumentException("Vehicle entered through an unknown road.");
        }

        queue.add(v);
    }

    Road roadTo(Junction j) {
        return _outRoads.get(j);
    }

    @Override
    void advance(int currTime) {
        if (_greenLightIndex != -1 && !_queues.isEmpty()) {
            List<Vehicle> greenQueue = _queues.get(_greenLightIndex);
            List<Vehicle> toMove = _dqStrategy.dequeue(greenQueue);

            for (Vehicle v : toMove) {
                greenQueue.remove(v);
                v.moveToNextRoad();
            }
        }

        int newGreenIndex = _lsStrategy.chooseNextGreen(_inRoads, _queues, _greenLightIndex, _lastSwitchingTime, currTime);

        if (newGreenIndex != _greenLightIndex) {
            _greenLightIndex = newGreenIndex;
            _lastSwitchingTime = currTime;
        }
    }

    @Override
    public JSONObject report() {
        JSONObject jo = new JSONObject();
        jo.put("id", _id);

        if (_greenLightIndex != -1) {
            jo.put("green", _inRoads.get(_greenLightIndex).getId());
        } else {
            jo.put("green", "none");
        }

        JSONArray queuesArray = new JSONArray();

        for (int i = 0; i < _inRoads.size(); i++) {
            JSONObject qObj = new JSONObject();
            qObj.put("road", _inRoads.get(i).getId());

            JSONArray vArray = new JSONArray();
            for (Vehicle v : _queues.get(i)) {
                vArray.put(v.getId());
            }

            qObj.put("vehicles", vArray);
            queuesArray.put(qObj);
        }

        jo.put("queues", queuesArray);
        return jo;
    }
}
