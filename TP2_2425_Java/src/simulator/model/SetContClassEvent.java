package simulator.model;

import java.util.List;
import simulator.misc.Pair;

public class SetContClassEvent extends Event {

    private List<Pair<String, Integer>> vehiclesContClass;

    public SetContClassEvent(int time, List<Pair<String, Integer>> vehiclesContClass) {
        super(time);
        if (vehiclesContClass == null) {
            throw new IllegalArgumentException("Contamination class information cannot be null");
        }
        this.vehiclesContClass = vehiclesContClass;
    }

    @Override
    public void execute(RoadMap map) {
        for (Pair<String, Integer> pair : vehiclesContClass) {
            Vehicle vehicle = map.getVehicle(pair.getFirst());
            if (vehicle != null) {
                vehicle.setContaminationClass(pair.getSecond());
            } else {
                throw new IllegalArgumentException("Vehicle with id " + pair.getFirst() + " does not exist.");
            }
        }
    }
}
