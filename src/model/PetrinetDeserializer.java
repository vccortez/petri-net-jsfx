package model;

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class PetrinetDeserializer implements JsonDeserializer<PetriNet> {

	@Override
	public PetriNet deserialize(JsonElement src, Type type, JsonDeserializationContext context)
			throws JsonParseException {
		JsonObject json = src.getAsJsonObject();
		
		JsonArray lugares = json.getAsJsonArray("lugares");
		JsonArray marcas = json.getAsJsonArray("marcas");
		Place[] places = new Place[lugares.size()];
		
		int i = 0;
		for (JsonElement element : lugares) {
			JsonObject lugar = element.getAsJsonObject();
			Place place = new Place();
			place.setId(lugar.get("id").getAsInt());
			place.setLabel(lugar.get("legenda").getAsString());
			place.setTokens(marcas.get(i).getAsInt());
			places[i] = place;
			i++;
		}
		
		JsonArray transicoes = json.getAsJsonArray("transicoes");
		Transition[] transitions = new Transition[transicoes.size()];
		
		i = 0;
		for (JsonElement element : transicoes) {
			JsonObject transicao = element.getAsJsonObject();
			Transition transition = new Transition();
			transition.setId(transicao.get("id").getAsInt());
			transition.setLabel(transicao.get("legenda").getAsString());
			transitions[i] = transition;
			i++;
		}
		
		JsonObject arcos = json.getAsJsonObject("arcos");
		JsonArray entrada = arcos.getAsJsonArray("entrada");
		JsonArray saida = arcos.getAsJsonArray("saida");
		JsonArray pesos = json.getAsJsonArray("pesos");
		FromTo[] arcin = new FromTo[entrada.size()];
		FromTo[] arcout = new FromTo[saida.size()];
		
		i = 0;
		for (JsonElement element : entrada) {
			JsonObject arco = element.getAsJsonObject();
			FromTo arc = new FromTo();
			arc.setFrom(arco.get("lugar").getAsInt());
			arc.setTo(arco.get("transicao").getAsInt());
			arc.setWeight(pesos.get(i).getAsInt());
			arcin[i] = arc;
			i++;
		}
		
		int j = 0;
		for (JsonElement element : saida) {
			JsonObject arco = element.getAsJsonObject();
			FromTo arc = new FromTo();
			arc.setFrom(arco.get("transicao").getAsInt());
			arc.setTo(arco.get("lugar").getAsInt());
			arc.setWeight(pesos.get(i).getAsInt());
			arcout[j] = arc;
			i++;
			j++;
		}
		
		PetriNet petrinet = new PetriNet();
		petrinet.setPlaces(places);
		petrinet.setTransitions(transitions);
		petrinet.setArcin(arcin);
		petrinet.setArcout(arcout);
		
		return petrinet;
	}

}
