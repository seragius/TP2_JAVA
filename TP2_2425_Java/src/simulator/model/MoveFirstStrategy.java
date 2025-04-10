package simulator.model;

import java.util.*;

public class MoveFirstStrategy implements DequeuingStrategy {

	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {
		if (q.isEmpty()) return Collections.emptyList();
		return Collections.singletonList(q.get(0));
	}
}
