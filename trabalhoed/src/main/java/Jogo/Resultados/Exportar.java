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

/**
 * Classe responsável por exportar os resultados das missões para um ficheiro JSON.
 */
public class Exportar {
    private ArrayList<JsonObject> missoes;

    /**
     * Construtor da classe Exportar.
     */
    public Exportar() {
        this.missoes = new ArrayList<>();
    }

    /**
     * Método que inicia uma nova missão para posteriormente exportar.
     * 
     * @param codMissao Código da missão.
     * @param versao Versão da missão.
     */
    public void iniciarNovaMissao(String codMissao, int versao) {
        JsonObject missao = new JsonObject();
        missao.put("codMissao", codMissao);
        missao.put("versao", versao);
        missao.put("trajeto", new JsonArray());
        missoes.add(missao);
    }

    /**
     * Método que regista o caminho efetuado pelo jogador.
     * 
     * @param divisaoOrigem Divisão de origem.
     * @param divisaoDestino Divisão de destino.
     */
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

    /**
     * Método que finaliza uma missão e regista o HP final e se a missão foi concluida.
     * 
     * @param hpFinal HP final do jogador.
     * @param extraido Indica se a missão foi concluida.
     */
    public void finalizarMissao(int hpFinal, boolean extraido) {
        if (missoes.isEmpty()) {
            return;
        }
        JsonObject missao = missoes.get(missoes.size() - 1);
        missao.put("hpFinal", hpFinal);
        missao.put("extraido", extraido);
    }

    /**
     * Método que exporta o resultado das missões para um ficheiro JSON.
     * O método lê o ficheiro JSON, adiciona as missões e ordena-as e escreve novamente no ficheiro.
     * 
     * @param nomeFicheiro Nome do ficheiro JSON para onde exportar, caso não exista é criado.
     */
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