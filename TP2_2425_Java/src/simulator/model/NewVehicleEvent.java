package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class NewVehicleEvent extends Event {

    private String id;
    private int maxSpeed;
    private int contClass;
    private List<String> itinerary;

    public NewVehicleEvent(int time, String id, int maxSpeed, int contClass, List<String> itinerary) {
        super(time);
        if (id == null || maxSpeed <= 0 || contClass < 0 || contClass > 10 || itinerary == null || itinerary.size() < 2)
            throw new IllegalArgumentException("Invalid parameters for NewVehicleEvent");

        this.id = id;
        this.maxSpeed = maxSpeed;
        this.contClass = contClass;
        this.itinerary = new ArrayList<>(itinerary);
    }

    @Override
    void execute(RoadMap map) {
        List<Junction> junctions = new ArrayList<>();

        for (String jId : itinerary) {
            Junction j = map.getJunction(jId);
            if (j == null)
                throw new IllegalArgumentException("Junction " + jId + " not found in map");
            junctions.add(j);
        }

        Vehicle v = new Vehicle(id, maxSpeed, contClass, junctions);
        map.addVehicle(v);
        v.moveToNextRoad();
    }
}
