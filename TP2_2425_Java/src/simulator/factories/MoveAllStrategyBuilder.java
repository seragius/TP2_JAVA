package simulator.factories;

import org.json.JSONObject;
import simulator.model.DequeuingStrategy;
import simulator.model.MoveAllStrategy;

public class MoveAllStrategyBuilder extends Builder<DequeuingStrategy> {

    public MoveAllStrategyBuilder() {
        super("move_all_dqs", "Move all dequeuing strategy");
    }

    @Override
    protected DequeuingStrategy create_instance(JSONObject data) {
        // La estructura JSON "data" se omite ya que no contiene información
        return new MoveAllStrategy();
    }
}
