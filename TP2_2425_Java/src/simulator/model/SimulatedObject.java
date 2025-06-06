package simulator.model;

import org.json.JSONObject;

public abstract class SimulatedObject {

    protected String _id;

    SimulatedObject(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("the 'id' must be a nonempty string.");
        }
        _id = id;
    }

    public String getId() {
        return _id;
    }

    @Override
    public String toString() {
        return _id;
    }

    abstract void advance(int currTime);
    abstract public JSONObject report();
}
