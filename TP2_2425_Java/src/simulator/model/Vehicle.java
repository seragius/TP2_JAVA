package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

public class Vehicle extends SimulatedObject {

    private int _maxSpeed;
    private int _speed;
    private int _contClass;
    private List<Junction> _itinerary;
    private VehicleStatus _status;
    private Road _road;
    private int _location;
    private int _totalCO2;
    private int _totalDistance;
    private int _currItineraryIndex;

    Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) {
        super(id);

        if (maxSpeed <= 0)
            throw new IllegalArgumentException("Max speed must be positive");
        if (contClass < 0 || contClass > 10)
            throw new IllegalArgumentException("Contamination class must be between 0 and 10");
        if (itinerary == null || itinerary.size() < 2)
            throw new IllegalArgumentException("Itinerary must have at least two junctions");

        _maxSpeed = maxSpeed;
        _contClass = contClass;
        _itinerary = Collections.unmodifiableList(new ArrayList<>(itinerary));
        _status = VehicleStatus.PENDING;
        _speed = 0;
        _location = 0;
        _totalCO2 = 0;
        _totalDistance = 0;
        _currItineraryIndex = 0;
        _road = null;
    }

    void setSpeed(int s) {
        if (s < 0) throw new IllegalArgumentException("Speed cannot be negative");
        _speed = Math.min(s, _maxSpeed);
    }

    void setContaminationClass(int c) {
        if (c < 0 || c > 10) throw new IllegalArgumentException("Contamination class must be between 0 and 10");
        _contClass = c;
    }

    void moveToNextRoad() {
        if (_status == VehicleStatus.ARRIVED)
            throw new IllegalStateException("Vehicle has already arrived");
        if (_status != VehicleStatus.PENDING && _status != VehicleStatus.WAITING)
            throw new IllegalStateException("Vehicle not in a valid state to move");

        if (_status == VehicleStatus.WAITING)
            _road.exit(this);

        if (_currItineraryIndex == _itinerary.size() - 1) {
            _road = null;
            _status = VehicleStatus.ARRIVED;
        } else {
            Junction src = _itinerary.get(_currItineraryIndex);
            Junction dest = _itinerary.get(_currItineraryIndex + 1);
            _road = src.roadTo(dest);
            _location = 0;
            _speed = 0;
            _road.enter(this);
            _status = VehicleStatus.TRAVELING;
            _currItineraryIndex++;
        }
    }

    @Override
    void advance(int currTime) {
        if (_status != VehicleStatus.TRAVELING) return;

        int prevLocation = _location;
        _location = Math.min(_location + _speed, _road.getLength());
        int delta = _location - prevLocation;
        int c = delta * _contClass;

        _totalCO2 += c;
        _road.addContamination(c);
        _totalDistance += delta;

        if (_location >= _road.getLength()) {
            _status = VehicleStatus.WAITING;
            _speed = 0;
            _road.getDest().enter(this);
        }
    }

    @Override
    public JSONObject report() {
        JSONObject jo = new JSONObject();
        jo.put("id", _id);
        jo.put("speed", _speed);
        jo.put("distance", _totalDistance);
        jo.put("co2", _totalCO2);
        jo.put("class", _contClass);
        jo.put("status", _status);

        if (_status == VehicleStatus.TRAVELING || _status == VehicleStatus.WAITING) {
            jo.put("road", _road.getId());
            jo.put("location", _location);
        }

        return jo;
    }

    // Public getters
    public int getLocation() { return _location; }
    public int getSpeed() { return _speed; }
    public int getMaxSpeed() { return _maxSpeed; }
    public int getContClass() { return _contClass; }
    public VehicleStatus getStatus() { return _status; }
    public int getTotalCO2() { return _totalCO2; }
    public List<Junction> getItinerary() { return _itinerary; }
    public Road getRoad() { return _road; }
}
