package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event> {

    public SetWeatherEventBuilder() {
        super("set_weather", "Set weather event");
    }

    @Override
    public Event create_instance(JSONObject data) {
        if (data == null)
            throw new IllegalArgumentException("Data for SetWeatherEvent is null.");

        int time = data.getInt("time");

        JSONArray info = data.getJSONArray("info");
        List<Pair<String, Weather>> ws = new ArrayList<>();

        for (int i = 0; i < info.length(); i++) {
            JSONObject o = info.getJSONObject(i);
            String roadId = o.getString("road");
            Weather w = Weather.valueOf(o.getString("weather").toUpperCase());
            ws.add(new Pair<>(roadId, w));
        }

        return new SetWeatherEvent(time, ws);
    }
}
