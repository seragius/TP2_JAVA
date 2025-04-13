package simulator.factories;

import org.json.JSONObject;
import simulator.model.DequeuingStrategy;
import simulator.model.MoveFirstStrategy;

public class MoveFirstStrategyBuilder extends Builder<DequeuingStrategy> {

    public MoveFirstStrategyBuilder() {
        super("move_first_dqs", "Move first dequeuing strategy");
    }

    @Override
    protected DequeuingStrategy create_instance(JSONObject data) {
        // La estructura JSON "data" se omite ya que no contiene información
        return new MoveFirstStrategy();
    }
}
