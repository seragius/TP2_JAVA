package simulator.control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.model.*;
import simulator.factories.*;

public class Controller {

    private TrafficSimulator simulator;
    private Factory<Event> eventsFactory;

    public Controller(TrafficSimulator sim, Factory<Event> eventsFactory) {
        if (sim == null || eventsFactory == null)
            throw new IllegalArgumentException("Simulador y Factory no pueden ser nulos.");
        this.simulator = sim;
        this.eventsFactory = eventsFactory;
    }

    // Cargar eventos desde un archivo JSON
    public void loadEvents(InputStream in) {
        JSONObject json = new JSONObject(new JSONTokener(in));
        JSONArray events = json.getJSONArray("events");

        for (int i = 0; i < events.length(); i++) {
            JSONObject eventData = events.getJSONObject(i);
            Event event = eventsFactory.create_instance(eventData);
            simulator.addEvent(event);
        }
    }

    // Ejecutar la simulación durante n ticks
    public void run(int n, OutputStream out) {
        for (int i = 0; i < n; i++) {
            simulator.advance();
            JSONObject state = simulator.report();
            try {
				out.write(state.toString(3).getBytes());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            try {
				out.write("\n".getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

    // Resetear el simulador
    public void reset() {
        simulator.reset();
    }
}
