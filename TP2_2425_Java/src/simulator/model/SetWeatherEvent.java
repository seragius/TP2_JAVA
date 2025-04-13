package simulator.model;

import java.util.List;
import simulator.misc.Pair;

public class SetWeatherEvent extends Event {

    private List<Pair<String, Weather>> roadsWeather;

    public SetWeatherEvent(int time, List<Pair<String, Weather>> roadsWeather) {
        super(time);
        if (roadsWeather == null) {
            throw new IllegalArgumentException("Weather information cannot be null");
        }
        this.roadsWeather = roadsWeather;
    }

    @Override
    public void execute(RoadMap map) {
        for (Pair<String, Weather> pair : roadsWeather) {
            Road road = map.getRoad(pair.getFirst());
            if (road != null) {
                road.setWeather(pair.getSecond());
            } else {
                throw new IllegalArgumentException("Road with id " + pair.getFirst() + " does not exist.");
            }
        }
    }
}
