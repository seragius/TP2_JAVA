package simulator.model;

import java.util.*;

public class MoveAllStrategy implements DequeuingStrategy {

	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {
		return new ArrayList<>(q);
	}
}
