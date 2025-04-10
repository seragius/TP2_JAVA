package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy {

	private int timeSlot;

	public MostCrowdedStrategy(int timeSlot) {
		this.timeSlot = timeSlot;
	}

	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime, int currTime) {
		if (roads.isEmpty()) return -1;

		if (currGreen == -1) {
			int max = -1, maxSize = -1;
			for (int i = 0; i < qs.size(); i++) {
				if (qs.get(i).size() > maxSize) {
					maxSize = qs.get(i).size();
					max = i;
				}
			}
			return max;
		}

		if (currTime - lastSwitchingTime < timeSlot)
			return currGreen;

		int n = roads.size();
		int max = currGreen;
		int maxSize = -1;

		for (int i = 1; i <= n; i++) {
			int idx = (currGreen + i) % n;
			if (qs.get(idx).size() > maxSize) {
				maxSize = qs.get(idx).size();
				max = idx;
			}
		}

		return max;
	}
}
