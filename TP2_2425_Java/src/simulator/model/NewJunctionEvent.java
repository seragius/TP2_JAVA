package simulator.model;

public class NewJunctionEvent extends Event {

    private String id;
    private LightSwitchingStrategy lsStrategy;
    private DequeuingStrategy dqStrategy;
    private int x, y;

    public NewJunctionEvent(int time, String id, LightSwitchingStrategy lsStrategy,
                            DequeuingStrategy dqStrategy, int x, int y) {
        super(time);
        if (id == null || lsStrategy == null || dqStrategy == null || x < 0 || y < 0)
            throw new IllegalArgumentException("Invalid parameters for NewJunctionEvent");

        this.id = id;
        this.lsStrategy = lsStrategy;
        this.dqStrategy = dqStrategy;
        this.x = x;
        this.y = y;
    }

    @Override
    void execute(RoadMap map) {
        map.addJunction(new Junction(id, lsStrategy, dqStrategy, x, y));
    }
}
