package simulator.factories;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import org.json.JSONObject;

public class BuilderBasedFactory<T> implements Factory<T> {

    private Map<String, Builder<T>> _builders;
    private List<JSONObject> _builders_info;

    public BuilderBasedFactory(List<Builder<T>> builders) {
        _builders = new HashMap<>();
        _builders_info = new ArrayList<>();

        for (Builder<T> b : builders) {
            _builders.put(b.get_type_tag(), b);
            _builders_info.add(b.get_info());
        }
    }

    @Override
    public T create_instance(JSONObject info) {
        if (info == null) {
            throw new IllegalArgumentException("'info' cannot be null");
        }

        String type = info.getString("type");
        Builder<T> builder = _builders.get(type);
        if (builder != null) {
            JSONObject data = info.has("data") ? info.getJSONObject("data") : new JSONObject();
            T instance = builder.createInstance(data);
            if (instance != null) {
                return instance;
            }
        }

        throw new IllegalArgumentException("Unrecognized 'info': " + info.toString());
    }

    @Override
    public List<JSONObject> get_info() {
        return _builders_info;
    }
}
