package simulator.model;

import java.util.ArrayList;
import java.util.List;

import simulator.misc.Pair;

public class SetContClassEvent extends Event {

    private List<Pair<String, Integer>> cs;

    public SetContClassEvent(int time, List<Pair<String, Integer>> cs) {
        super(time);
        if (cs == null)
            throw new IllegalArgumentException("'cs' cannot be null");
        this.cs = new ArrayList<>(cs);
    }

    @Override
    void execute(RoadMap map) {
        for (Pair<String, Integer> p : cs) {
            Vehicle v = map.getVehicle(p.getFirst());
            if (v == null)
                throw new IllegalArgumentException("Vehicle with id '" + p.getFirst() + "' not found");
            v.setContaminationClass(p.getSecond());
        }
    }
}
