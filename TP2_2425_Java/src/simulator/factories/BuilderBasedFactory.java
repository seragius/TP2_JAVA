package simulator.factories;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import org.json.JSONObject;

import simulator.model.Event;

public class BuilderBasedFactory<T> implements Factory<T> {

    private Map<String, Builder<T>> builders; // Mapa que relaciona el tipo de evento con su builder
    private List<JSONObject> buildersInfo; // Información sobre los builders

    public BuilderBasedFactory(List<Builder<T>> buildersList) {
        builders = new HashMap<>();
        buildersInfo = new ArrayList<>();

        // Agregar todos los builders al mapa y la info de los builders a la lista
        for (Builder<T> builder : buildersList) {
            builders.put(builder.get_type_tag(), builder);
            buildersInfo.add(builder.get_info());
        }
    }

    @Override
    public T create_instance(JSONObject data) {
        if (data == null)
            throw new IllegalArgumentException("Data cannot be null");

        String type = data.getString("type"); // Obtener el tipo de evento
        Builder<T> builder = builders.get(type); // Buscar el builder correspondiente

        if (builder == null)
            throw new IllegalArgumentException("Unknown type: " + type);

        return builder.create_instance(data); // Crear y devolver el evento
    }

    @Override
    public List<JSONObject> get_info() {
        return buildersInfo; // Devuelve la información de todos los builders
    }
}
