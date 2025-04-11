package simulator.factories;

import org.json.JSONObject;
import org.json.JSONArray;
import simulator.model.*;
import simulator.factories.*;

public class NewJunctionEventBuilder extends Builder<Event> {

    private Factory<LightSwitchingStrategy> lssFactory;
    private Factory<DequeuingStrategy> dqsFactory;

    public NewJunctionEventBuilder(Factory<LightSwitchingStrategy> lssFactory,
                                    Factory<DequeuingStrategy> dqsFactory) {
        super("new_junction", "New junction event");
        this.lssFactory = lssFactory;
        this.dqsFactory = dqsFactory;
    }

    @Override
    protected Event create_instance(JSONObject data) {
        String id = data.getString("id");
        int time = data.getInt("time");
        JSONArray coor = data.getJSONArray("coor");

        int x = coor.getInt(0);
        int y = coor.getInt(1);

        JSONObject lssData = data.getJSONObject("ls_strategy");
        JSONObject dqsData = data.getJSONObject("dq_strategy");

        LightSwitchingStrategy lss = lssFactory.create_instance(lssData);
        DequeuingStrategy dqs = dqsFactory.create_instance(dqsData);

        return new NewJunctionEvent(time, id, lss, dqs, x, y);
    }
}
