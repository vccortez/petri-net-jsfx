package model;

import java.lang.reflect.Type;

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

		for (Place place : src.getPlaces()) {
			JsonObject lugar = new JsonObject();
			lugar.addProperty("id", place.getId());
			lugar.addProperty("legenda", place.getLabel());
			marcas.add(place.getTokens());
			lugares.add(lugar);
		}

		JsonArray transicoes = new JsonArray();

		for (Transition transition : src.getTransitions()) {
			JsonObject transicao = new JsonObject();
			transicao.addProperty("id", transition.getId());
			transicao.addProperty("legenda", transition.getLabel());
			transicoes.add(transicao);
		}

		JsonArray entrada = new JsonArray();
		JsonArray saida = new JsonArray();
		JsonArray pesos = new JsonArray();

		for (FromTo arc : src.getArcin()) {
			JsonObject arco = new JsonObject();
			arco.addProperty("transicao", arc.getTo());
			arco.addProperty("lugar", arc.getFrom());
			pesos.add(arc.getWeight());
			entrada.add(arco);
		}

		for (FromTo arc : src.getArcout()) {
			JsonObject arco = new JsonObject();
			arco.addProperty("transicao", arc.getFrom());
			arco.addProperty("lugar", arc.getTo());
			pesos.add(arc.getWeight());
			saida.add(arco);
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
