package model;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class PetrinetSerializer implements JsonSerializer<Petrinet> {

	@Override
	public JsonElement serialize(Petrinet src, Type type, JsonSerializationContext context) {

		JsonArray lugares = new JsonArray();
		JsonArray marcas = new JsonArray();

		List<Place> places = src.getPlaces();

		for (Place place : places) {
			JsonObject lugar = new JsonObject();
			lugar.addProperty("id", places.indexOf(place));
			lugar.addProperty("legenda", place.getName());
			marcas.add(place.getTokens());
			lugares.add(lugar);
		}

		JsonArray transicoes = new JsonArray();

		List<Transition> transitions = src.getTransitions();

		for (Transition transition : transitions) {
			JsonObject transicao = new JsonObject();
			transicao.addProperty("id", transitions.indexOf(transition));
			transicao.addProperty("legenda", transition.getName());
			transicoes.add(transicao);
		}

		JsonArray entrada = new JsonArray();
		JsonArray saida = new JsonArray();
		JsonArray pesos = new JsonArray();

		List<Arc> arcs = src.getArcs();

		for (Arc arc : arcs) {
			JsonObject arco = new JsonObject();
			if (arc.getDirection() == Direction.PLACE_TO_TRANSITION) {
				arco.addProperty("transicao", transitions.indexOf(arc.getTransition()));
				arco.addProperty("lugar", places.indexOf(arc.getPlace()));
				pesos.add(arc.getWeight());
				entrada.add(arco);
			} else if (arc.getDirection() == Direction.TRANSITION_TO_PLACE) {
				arco.addProperty("transicao", transitions.indexOf(arc.getTransition()));
				arco.addProperty("lugar", places.indexOf(arc.getPlace()));
				pesos.add(arc.getWeight());
				saida.add(arco);
			}
		}

		JsonObject arcos = new JsonObject();
		arcos.add("entrada", entrada);
		arcos.add("saida", saida);

		JsonObject petrinetJson = new JsonObject();
		petrinetJson.add("lugares", lugares);
		petrinetJson.add("marcas", marcas);
		petrinetJson.add("transicoes", transicoes);
		petrinetJson.add("arcos", arcos);
		petrinetJson.add("pesos", pesos);

		return petrinetJson;
	}

}
