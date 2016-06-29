package model;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Vector;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class PetrinetDeserializer implements JsonDeserializer<Petrinet> {

	@Override
	public Petrinet deserialize(JsonElement src, Type type, JsonDeserializationContext context)
			throws JsonParseException {
		JsonObject json = src.getAsJsonObject();

		JsonArray lugares = json.getAsJsonArray("lugares");
		JsonArray marcas = json.getAsJsonArray("marcas");
		Vector<Place> places = new Vector<>(lugares.size());

		int i = 0;
		for (JsonElement element : lugares) {
			JsonObject lugar = element.getAsJsonObject();
			Place place = new Place();

			place.setName(lugar.get("legenda").getAsString());
			place.setTokens(marcas.get(i).getAsInt());
			places.add(i, place);
			i++;
		}

		JsonArray transicoes = json.getAsJsonArray("transicoes");
		Vector<Transition> transitions = new Vector<>(transicoes.size());

		i = 0;
		for (JsonElement element : transicoes) {
			JsonObject transicao = element.getAsJsonObject();
			Transition transition = new Transition();

			transition.setName(transicao.get("legenda").getAsString());
			transitions.add(i, transition);
			i++;
		}

		JsonObject arcos = json.getAsJsonObject("arcos");
		JsonArray entrada = arcos.getAsJsonArray("entrada");
		JsonArray saida = arcos.getAsJsonArray("saida");
		JsonArray pesos = json.getAsJsonArray("pesos");
		Vector<Arc> incoming = new Vector<>(entrada.size());
		Vector<Arc> outgoing = new Vector<>(saida.size());

		i = 0;
		for (JsonElement element : entrada) {
			JsonObject arco = element.getAsJsonObject();
			int from = arco.get("lugar").getAsInt();
			int to = arco.get("transicao").getAsInt();
			Arc arc = new Arc("", places.get(from - 1), transitions.get(to - 1));
			arc.setWeight(pesos.get(i).getAsInt());
			incoming.add(i, arc);
			i++;
		}

		int j = 0;
		for (JsonElement element : saida) {
			JsonObject arco = element.getAsJsonObject();
			int from = arco.get("transicao").getAsInt();
			int to = arco.get("lugar").getAsInt();
			Arc arc = new Arc("", transitions.get(from - 1), places.get(to - 1));
			arc.setWeight(pesos.get(i).getAsInt());
			outgoing.add(j, arc);
			i++;
			j++;
		}

		ArrayList<PetrinetObject> objects = new ArrayList<>(
				places.size() + transitions.size() + incoming.size() + outgoing.size());
		objects.addAll(places);
		objects.addAll(transitions);
		objects.addAll(incoming);
		objects.addAll(outgoing);

		Petrinet petrinet = new Petrinet("Petrinet");
		for (PetrinetObject object : objects) {
			petrinet.add(object);
		}

		return petrinet;
	}

}
