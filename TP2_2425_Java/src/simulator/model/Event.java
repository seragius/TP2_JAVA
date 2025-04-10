package simulator.model;

public abstract class Event implements Comparable<Event> {

	protected int _time;

	public Event(int time) {
		if (time < 0)
			throw new IllegalArgumentException("Invalid time: " + time);
		this._time = time;
	}

	int getTime() {
		return _time;
	}

	@Override
	public int compareTo(Event o) {
		return Integer.compare(this._time, o._time);
	}

	abstract void execute(RoadMap map);
}
