package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy {
    private int _timeSlot;

    public MostCrowdedStrategy(int timeslot) {
        _timeSlot = timeslot;
    }

    @Override
    public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime, int currTime) {
        if (roads.isEmpty())
            return -1;
        
        // Si todos están en rojo (currGreen = -1)
        if (currGreen == -1) {
            int maxSize = -1;
            int selected = -1;
            for (int i = 0; i < qs.size(); i++) {
                int size = qs.get(i).size();
                if (size > maxSize) {
                    maxSize = size;
                    selected = i;
                }
            }
            return selected;
        }
        
        if ((currTime - lastSwitchingTime) < _timeSlot)
            return currGreen;

        // Realiza búsqueda circular empezando en currGreen+1
        int n = roads.size();
        int selected = currGreen;
        int maxQueueSize = qs.get(currGreen).size();
        for (int i = 1; i < n; i++) {
            int index = (currGreen + i) % n;
            int size = qs.get(index).size();
            if (size > maxQueueSize) {
                maxQueueSize = size;
                selected = index;
            }
        }
        return selected;
    }
}
