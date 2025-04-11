package simulator.factories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class BuilderBasedFactory<T> implements Factory<T> {

    private Map<String, Builder<T>> _builders;
    private List<JSONObject> _buildersInfo;

    public BuilderBasedFactory(List<Builder<T>> builders) {
        _builders = new HashMap<>();
        _buildersInfo = new ArrayList<>();

        for (Builder<T> b : builders) {
            _builders.put(b.get_type_tag(), b);
            _buildersInfo.add(b.get_info());
        }
    }

    @Override
    public T create_instance(JSONObject info) {
        if (info == null)
            throw new IllegalArgumentException("'info' cannot be null");

        String type = info.getString("type");
        JSONObject data = info.has("data") ? info.getJSONObject("data") : new JSONObject();

        Builder<T> b = _builders.get(type);
        if (b == null)
            throw new IllegalArgumentException("Unknown type: " + type);

        return b.create_instance(data);
    }

    @Override
    public List<JSONObject> get_info() {
        return Collections.unmodifiableList(_buildersInfo);
    }


}
