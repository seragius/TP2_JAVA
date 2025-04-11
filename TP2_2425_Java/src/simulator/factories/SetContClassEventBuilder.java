package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetContClassEvent;

public class SetContClassEventBuilder extends Builder<Event> {

    public SetContClassEventBuilder() {
        super("set_cont_class", "Set contamination class event");
    }

    @Override
    public Event create_instance(JSONObject data) {
        if (data == null)
            throw new IllegalArgumentException("Data for SetContClassEvent is null.");

        int time = data.getInt("time");

        JSONArray info = data.getJSONArray("info");
        List<Pair<String, Integer>> cs = new ArrayList<>();

        for (int i = 0; i < info.length(); i++) {
            JSONObject o = info.getJSONObject(i);
            String vehicleId = o.getString("vehicle");
            int classValue = o.getInt("class");
            cs.add(new Pair<>(vehicleId, classValue));
        }

        return new SetContClassEvent(time, cs);
    }
}
