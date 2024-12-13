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

/**
 * Classe responsável por importar os resultados das missões a partir de um ficheiro JSON.
 */
public class ImportarResultados {
    private ArrayList<JsonObject> resultados;

    /**
     * Construtor da classe ImportarResultados.
     */
    public ImportarResultados() {
        this.resultados = new ArrayList<>();
    }

    /**
     * Método que importa os resultados das missões a partir de um ficheiro JSON.
     * Lê o ficheiro JSON e converte o seu conteudo para um JsonArray, depois adiciona
     * cada JsonObject do JsonArray à lista de resultados.
     * 
     * @param resultadosFicheiro o nome do ficheiro JSON com os resultados das missões.
     */
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

    /**
     * Método que mostra os resultados de uma missão específica.
     * 
     * Este método utiliza o método "filtrarResultados" especificando
     * o nome e a versao da missao, para obter os resultados desejados para cada missão.
     * 
     * 
     * @param nomeMissao o nome da missão.
     * @param versao a versão da missão.
     */
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

    /**
     * Método que filtra os resultados das missões.
     * 
     * Este método percorre a lista de resultados e verifica se o código da missão
     * e a versão do resultado correspondem aos parâmetros recebidos.
     * 
     * @param missao o nome da missão.
     * @param versao a versão da missão.
     * @return uma lista de JsonObjects com os resultados filtrados.
     */
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

    /**
     * Método que retorna a lista de resultados.
     * 
     * @return a lista de resultados.
     */
    public ArrayList<JsonObject> getResultados() {
        return resultados;
    }
}
