package Jogo.Resultados;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.Jsoner;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;

import Estruturas.ArrayList;

public class ImportarResultados {
    private ArrayList<JsonObject> resultados;

    public ImportarResultados() {
        this.resultados = new ArrayList<>();
    }

    public void importarResultados(String resultadosFicheiro) {
        try (Reader reader = new InputStreamReader(new FileInputStream(resultadosFicheiro), "UTF-8")) {
            JsonArray jsonArray = (JsonArray) Jsoner.deserialize(reader);
            for (Object object : jsonArray) {
                if (object instanceof JsonObject) {
                    resultados.add((JsonObject) object);
                }
            }
        } catch (IOException | JsonException e) {
            System.out.println("Erro ao importar resultados: " + ((Exception) e).getMessage());

        }
    }

    public void mostrarResultados(String nomeMissao, int versao) {
        ArrayList<JsonObject> resultados = filtrarResultados(nomeMissao, versao);
        if (resultados.isEmpty()) {
            System.out.println("Nenhum resultado encontrado.");
            return;
        }
        System.out.println("Resultados da missão " + nomeMissao + " versão " + versao + ":");
        for (int i = 0; i < resultados.size(); i++) {
            JsonObject resultado = resultados.get(i);
            System.out.println("  HP Final: " + resultado.get("hpFinal"));
            System.out.println("  Extraído: " + resultado.get("extraido"));
            System.out.println("  Trajeto: " + resultado.get("trajeto"));
            System.out.println();
        }
    }

    public ArrayList<JsonObject> filtrarResultados(String missao, int versao) {
        ArrayList<JsonObject> resultadosFiltrados = new ArrayList<>();
        for (JsonObject resultado : resultados) {
            String codMissao = (String) resultado.get("codMissao");
            int versaoResultado = Integer.parseInt(resultado.get("versao").toString());
            if (codMissao.equals(missao) && versaoResultado == versao) {
                resultadosFiltrados.add(resultado);
            }
        }
        return resultadosFiltrados;
    }

    public ArrayList<JsonObject> getResultados() {
        return resultados;
    }
}
