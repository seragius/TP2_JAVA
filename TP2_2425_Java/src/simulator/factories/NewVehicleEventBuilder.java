package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewVehicleEvent;

public class NewVehicleEventBuilder extends Builder<Event> {

    public NewVehicleEventBuilder() {
        super("new_vehicle", "New vehicle event");
    }

    @Override
    public Event createInstance(JSONObject data) {
        int time = data.getInt("time");
        String id = data.getString("id");
        int maxspeed = data.getInt("maxspeed");
        int contclass = data.getInt("class");

        // Parseamos el itinerario
        JSONArray ja = data.getJSONArray("itinerary");
        List<String> itinerary = new ArrayList<>();
        for (int i = 0; i < ja.length(); i++) {
            itinerary.add(ja.getString(i));
        }

        return new NewVehicleEvent(time, id, maxspeed, contclass, itinerary);
    }
}
