package simulator.factories;

import org.json.JSONObject;
import simulator.model.LightSwitchingStrategy;
import simulator.model.MostCrowdedStrategy;

public class MostCrowdedStrategyBuilder extends Builder<LightSwitchingStrategy> {

    public MostCrowdedStrategyBuilder() {
        super("most_crowded_lss", "Most crowded light switching strategy");
    }

    @Override
    protected LightSwitchingStrategy createInstance(JSONObject data) {
        int timeslot = 1;
        if (data.has("timeslot")) {
            timeslot = data.getInt("timeslot");
        }
        return new MostCrowdedStrategy(timeslot);
    }
}
