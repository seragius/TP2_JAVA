package simulator.factories;

import java.util.List;
import org.json.JSONObject;

public interface Factory<T> {
    T create_instance(JSONObject info);
    List<JSONObject> get_info();
}
