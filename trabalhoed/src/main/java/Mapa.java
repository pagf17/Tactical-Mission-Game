import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import Estruturas.Graph;
import java.util.Iterator;

public class Mapa {
    private Graph<Divisao> grafo; 

    public Mapa() {
        this.grafo = new Graph<>();
    }

    // Método para adicionar uma nova Divisao
    public void adicionarDivisao(Divisao divisao) {
        if (!grafo.isConnected() || !grafo.iteratorBFS(divisao).hasNext()) { // Evita duplicatas
            grafo.addVertex(divisao); 
        }
    }

    // Método para conectar duas divisões
    public void conectarDivisoes(String nomeDivisao1, String nomeDivisao2) {
        Divisao divisao1 = obterDivisao(nomeDivisao1);
        Divisao divisao2 = obterDivisao(nomeDivisao2);

        if (divisao1 != null && divisao2 != null) {
            grafo.addEdge(divisao1, divisao2); 
        } else {
            System.out.println("Uma ou ambas as divisões não existem.");
        }
    }

    // Método para buscar uma Divisao pelo nome (baseado no grafo)
    public Divisao obterDivisao(String nome) {
        // Iterar sobre todos os vértices do grafo (os índices dos vértices)
        for (int i = 0; i < grafo.size(); i++) {
            Divisao divisao = grafo.getVertex(i);  // Obtemos o vértice no índice i
            if (divisao.getNome().equalsIgnoreCase(nome)) {
                return divisao; // Retorna a divisão correspondente
            }
        }
        return null; // Retorna null se não encontrar a divisão
    }

    // Método para encontrar um caminho entre duas divisões
    public void encontrarCaminho(String nomeDivisao1, String nomeDivisao2) {
        // Buscar as divisões pelo nome
        Divisao divisao1 = obterDivisao(nomeDivisao1);
        Divisao divisao2 = obterDivisao(nomeDivisao2);
    
        // Verificar se as divisões existem no grafo
        if (divisao1 != null && divisao2 != null) {
            System.out.println("Caminho entre " + nomeDivisao1 + " e " + nomeDivisao2 + ":");
    
            // Usar o método do grafo para encontrar o caminho mais curto
            Iterator<Divisao> caminho = grafo.iteratorShortestPath(divisao1, divisao2);
    
            // Exibir o caminho, se existir
            if (caminho.hasNext()) {
                while (caminho.hasNext()) {
                    System.out.println(caminho.next().getNome());
                }
            } else {
                System.out.println("Não existe caminho entre as divisões.");
            }
        } else {
            System.out.println("Uma ou ambas as divisões não existem.");
        }
    }
    

    public void carregarMapa(String arquivoJson) throws IOException {
        try (Reader reader = new InputStreamReader(new FileInputStream(arquivoJson), "UTF-8")) {
            JsonObject jsonObject = (JsonObject) Jsoner.deserialize(reader);
    
            // Carregar divisões
            JsonArray divisaoJson = (JsonArray) jsonObject.get("edificio");
            if (divisaoJson != null) {
                for (Object obj : divisaoJson) {
                    String nomeDivisao = (String) obj;
                    Divisao divisao = new Divisao(nomeDivisao);
                    adicionarDivisao(divisao); // Adiciona a divisão ao grafo
                }
            } else {
                System.out.println("Chave 'edificio' não encontrada no JSON.");
            }
    
            // Carregar conexões
            JsonArray ligacoesJson = (JsonArray) jsonObject.get("ligacoes");
            if (ligacoesJson != null) {
                for (Object obj : ligacoesJson) {
                    JsonArray ligacao = (JsonArray) obj;
                    if (ligacao.size() == 2) {
                        String divisao1 = (String) ligacao.get(0);
                        String divisao2 = (String) ligacao.get(1);
                        conectarDivisoes(divisao1, divisao2); // Conecta as divisões no grafo
                    }
                }
            } else {
                System.out.println("Chave 'ligacoes' não encontrada no JSON.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar o mapa: " + e.getMessage());
            throw new IOException("Erro ao carregar o mapa", e);
        }
    }
    
    

    // Método para exibir o mapa
    @Override
    public String toString() {
        return grafo.toString(); 
    }

}
