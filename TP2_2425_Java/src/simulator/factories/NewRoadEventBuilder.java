package simulator.factories;

import org.json.JSONObject;
import simulator.model.*;
import simulator.factories.Builder;

abstract class NewRoadEventBuilder extends Builder<Event> {

    public NewRoadEventBuilder(String type, String desc) {
        super(type, desc);
    }

    @Override
    protected Event create_instance(JSONObject data) {
        String id = data.getString("id");
        String src = data.getString("src");
        String dest = data.getString("dest");
        int time = data.getInt("time");
        int length = data.getInt("length");
        int co2limit = data.getInt("co2limit");
        int maxspeed = data.getInt("maxspeed");
        Weather weather = Weather.valueOf(data.getString("weather").toUpperCase());

        return createRoadEvent(time, id, src, dest, length, co2limit, maxspeed, weather);
    }

    protected abstract Event createRoadEvent(int time, String id, String src, String dest,
                                             int length, int co2limit, int maxspeed, Weather weather);
}
