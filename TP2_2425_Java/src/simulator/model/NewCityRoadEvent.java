package simulator.model;

public class NewCityRoadEvent extends Event {

    private String id, src, dest;
    private int length, co2Limit, maxSpeed;
    private Weather weather;

    public NewCityRoadEvent(int time, String id, String src, String dest,
                            int length, int co2Limit, int maxSpeed, Weather weather) {
        super(time);
        if (id == null || src == null || dest == null || length <= 0 || co2Limit < 0 || maxSpeed <= 0 || weather == null)
            throw new IllegalArgumentException("Invalid parameters for NewCityRoadEvent");

        this.id = id;
        this.src = src;
        this.dest = dest;
        this.length = length;
        this.co2Limit = co2Limit;
        this.maxSpeed = maxSpeed;
        this.weather = weather;
    }

    @Override
    void execute(RoadMap map) {
        Junction j1 = map.getJunction(src);
        Junction j2 = map.getJunction(dest);
        Road r = new CityRoad(id, j1, j2, maxSpeed, co2Limit, length, weather);
        map.addRoad(r);
    }
}

