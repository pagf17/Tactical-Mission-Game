package Jogo.Resultados;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import com.github.cliftonlabs.json_simple.JsonException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import Estruturas.ArrayList;
import Estruturas.SortSearch.SortAndSearch;

public class Exportar {
    private ArrayList<JsonObject> missoes;

    public Exportar() {
        this.missoes = new ArrayList<>();
    }

    public void iniciarNovaMissao(String codMissao, int versao) {
        JsonObject missao = new JsonObject();
        missao.put("codMissao", codMissao);
        missao.put("versao", versao);
        missao.put("trajeto", new JsonArray());
        missoes.add(missao);
    }

    public void registarMovimento(String divisaoOrigem, String divisaoDestino) {
        if (missoes.isEmpty()) {
            return;
        }
        JsonObject missao = missoes.get(missoes.size() - 1);
        JsonArray trajeto = (JsonArray) missao.get("trajeto");
        if (trajeto.isEmpty()) {
            trajeto.add(divisaoOrigem);
        }
        trajeto.add(divisaoDestino);
    }

    public void finalizarMissao(int hpFinal, boolean extraido) {
        if (missoes.isEmpty()) {
            return;
        }
        JsonObject missao = missoes.get(missoes.size() - 1);
        missao.put("hpFinal", hpFinal);
        missao.put("extraido", extraido);
    }

    public void exportar(String nomeFicheiro) {
        JsonArray missoesJson = new JsonArray();
    
        try (FileReader reader = new FileReader(nomeFicheiro)) {
            missoesJson = (JsonArray) Jsoner.deserialize(reader);
        } catch (IOException | JsonException e) {
        }
    
        for (int i = 0; i < missoes.size(); i++) {
            missoesJson.add(missoes.get(i));
        }
    
        OrdenarResultado[] missoesArray = new OrdenarResultado[missoesJson.size()];
        for (int i = 0; i < missoesJson.size(); i++) {
            missoesArray[i] = new OrdenarResultado((JsonObject) missoesJson.get(i));
        }
    
        SortAndSearch.mergeSort(missoesArray, 0, missoesArray.length - 1);
    
        JsonArray missoesOrdenadasArray = new JsonArray();
        for (OrdenarResultado aux : missoesArray) {
            missoesOrdenadasArray.add(aux.missao);
        }
    
        try (FileWriter writer = new FileWriter(nomeFicheiro)) {
            writer.write(Jsoner.prettyPrint(missoesOrdenadasArray.toJson()));
        } catch (IOException e) {
        }
    }
    
}