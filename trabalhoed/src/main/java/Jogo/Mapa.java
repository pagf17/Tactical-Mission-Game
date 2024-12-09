package Jogo;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import Estruturas.ArrayUnorderedList;
import Estruturas.NetworkExtension;
import Estruturas.LinkedList;

import java.util.Iterator;

public class Mapa {
    private NetworkExtension<Divisao> grafo;

    public Mapa() {
        this.grafo = new NetworkExtension<>();
    }

    public void adicionarDivisao(Divisao divisao) {
        if (!grafo.isConnected() || !grafo.iteratorBFS(divisao).hasNext()) {
            grafo.addVertex(divisao);
        }
    }

    public void conectarDivisoes(String nomeDivisao1, String nomeDivisao2) {
        Divisao divisao1 = obterDivisao(nomeDivisao1);
        Divisao divisao2 = obterDivisao(nomeDivisao2);

        if (divisao1 != null && divisao2 != null) {
            int peso = calcularPeso(divisao2);
            grafo.addEdge(divisao1, divisao2, peso);
            grafo.addEdge(divisao2, divisao1, peso);
        } else {
            System.out.println("Erro ao conectar divisões: " + nomeDivisao1 + ", " + nomeDivisao2);
        }
    }

    public Divisao obterDivisao(String nome) {
        for (int i = 0; i < grafo.size(); i++) {
            Divisao divisao = grafo.getVertex(i);
            if (divisao.getNome().equalsIgnoreCase(nome)) {
                return divisao;
            }
        }
        return null;
    }

    public void carregarMapa(String arquivoJson) throws IOException {
        try (Reader reader = new InputStreamReader(new FileInputStream(arquivoJson), "UTF-8")) {
            JsonObject jsonObject = (JsonObject) Jsoner.deserialize(reader);

            JsonArray divisaoJson = (JsonArray) jsonObject.get("edificio");
            if (divisaoJson != null) {
                for (Object obj : divisaoJson) {
                    String nomeDivisao = (String) obj;
                    Divisao divisao = new Divisao(nomeDivisao);
                    adicionarDivisao(divisao);
                }
            } else {
                System.out.println("Chave 'edificio' não encontrada no JSON.");
            }

            // Carregar ligações
            JsonArray ligacoesJson = (JsonArray) jsonObject.get("ligacoes");
            if (ligacoesJson != null) {
                for (Object obj : ligacoesJson) {
                    JsonArray ligacao = (JsonArray) obj;
                    if (ligacao.size() == 2) {
                        String nomeDivisao1 = (String) ligacao.get(0);
                        String nomeDivisao2 = (String) ligacao.get(1);

                        Divisao divisao1 = obterDivisao(nomeDivisao1);
                        Divisao divisao2 = obterDivisao(nomeDivisao2);

                        if (divisao1 != null && divisao2 != null) {
                            conectarDivisoes(nomeDivisao1, nomeDivisao2);
                        } else {
                            System.out.println("Erro ao conectar divisões: " + nomeDivisao1 + ", " + nomeDivisao2);
                        }
                    }
                }
            } else {
                System.out.println("Chave 'ligacoes' não encontrada no JSON.");
            }

            JsonArray itensJson = (JsonArray) jsonObject.get("itens");
            if (itensJson != null) {
                for (Object obj : itensJson) {
                    JsonObject itemJson = (JsonObject) obj;
                    String nomeDivisao = (String) itemJson.get("divisao");
                    Divisao divisao = obterDivisao(nomeDivisao);

                    if (divisao != null) {
                        String tipo = (String) itemJson.get("tipo");

                        if ("kit de vida".equalsIgnoreCase(tipo)) {
                            int pontosRecuperados = ((Number) itemJson.get("pontosRecuperados")).intValue();
                            Kit kit = new Kit(divisao, pontosRecuperados);
                            divisao.addItem(kit);
                        } else if ("colete".equalsIgnoreCase(tipo)) {
                            int pontosExtra = ((Number) itemJson.get("pontosExtra")).intValue();
                            Colete colete = new Colete(divisao, pontosExtra);
                            divisao.addItem(colete);
                        }
                    }
                }
            }

            ArrayUnorderedList<Inimigo> inimigos = Inimigo.carregarInimigos(jsonObject, this);
            Iterator<Inimigo> inimigosIterator = inimigos.iterator();
            while (inimigosIterator.hasNext()) {
                Inimigo inimigo = inimigosIterator.next();
                Divisao divisao = obterDivisao(inimigo.getDivisao().getNome());
                if (divisao != null) {
                    divisao.addInimigo(inimigo);
                } else {
                    System.out.println("Divisão não encontrada para o inimigo: " + inimigo.getNome());
                }
            }

            JsonArray entradasSaidasJson = (JsonArray) jsonObject.get("entradasSaidas");
            if (entradasSaidasJson != null) {
                for (Object obj : entradasSaidasJson) {
                    String nomeDivisao = (String) obj;
                    Divisao divisao = obterDivisao(nomeDivisao);
                    if (divisao != null) {
                        divisao.setEntradaSaida(true);
                    }
                }
            }

            Missao missao = Missao.carregarMissao(jsonObject);
            if (missao != null) {
                System.out.println(missao);
            }

        } catch (Exception e) {
            System.out.println("Erro ao carregar o mapa: " + e.getMessage());
            throw new IOException("Erro ao carregar o mapa", e);
        }
    }

    public Inimigo obterInimigo(Divisao divisaoAtual) {
        Divisao divisao = obterDivisao(divisaoAtual.getNome());
        if (divisao != null) {
            ArrayUnorderedList<Inimigo> inimigos = divisao.getInimigos();

            if (!inimigos.isEmpty()) {
                Iterator<Inimigo> iterator = inimigos.iterator();
                if (iterator.hasNext()) {
                    return iterator.next();
                }
            }
        }
        return null;
    }

    /**
     * Verifica se duas divisões estão conectadas.
     * 
     * @param divisao1
     * @param divisao2
     * @return
     */
    public boolean verificarLigacao(Divisao divisao1, Divisao divisao2) {
        if (divisao1 != null && divisao2 != null) {
            return grafo.verificarLigacao(divisao1, divisao2);
        }
        return false;
    }

    public void listarInimigos() {
        for (int i = 0; i < grafo.size(); i++) {
            Divisao divisao = grafo.getVertex(i);
            divisao.listarInimigos();
        }
    }

    public void listarItens() {
        for (int i = 0; i < grafo.size(); i++) {
            Divisao divisao = grafo.getVertex(i);
            divisao.listarItens();
        }
    }

    public void exibirCaminho(Divisao startDivisao, Divisao targetDivisao) {
        int startIndex = grafo.getIndex(startDivisao); 
        int targetIndex = grafo.getIndex(targetDivisao); 
    
        int[] predecessores = grafo.dijkstra(startDivisao, targetDivisao);
    
        // Reconstrução do caminho
        LinkedList<Divisao> caminho = new LinkedList<>();
        int atual = targetIndex;
    
        // Reconstrução do caminho da origem até o destino
        while (atual != startIndex) {
            caminho.add(grafo.getVertex(atual)); // Adiciona o vértice ao início da lista
            atual = predecessores[atual];  // Segue o predecessor
        }
    
        caminho.add(grafo.getVertex(startIndex)); // Adiciona o vértice inicial
    
        // Exibe o caminho
        System.out.print("Caminho até o objetivo: ");
        Iterator<Divisao> iterator = caminho.iterator();
        while (iterator.hasNext()) {
            Divisao divisao = iterator.next();
            System.out.print(divisao.getNome() + "<- ");
        }
        System.out.println();
    }
    

    private int calcularPeso(Divisao divisaoDestino) {
        int peso = 1;
        for (Inimigo inimigo : divisaoDestino.getInimigos()) {
            peso += inimigo.getDano();
        }
        return peso;
    }

    public void atualizarPesoLigacoes() {
        for (int i = 0; i < grafo.size(); i++) {
            Divisao divisaoOrigem = grafo.getVertex(i);
            for (int j = 0; j < grafo.size(); j++) {
                Divisao divisaoDestino = grafo.getVertex(j);
                if (grafo.verificarLigacao(divisaoOrigem, divisaoDestino)) {
                    int peso = calcularPeso(divisaoDestino);
                    grafo.atualizarPeso(divisaoOrigem, divisaoDestino, peso);
                }
            }
        }
    }

    public void verMapa() {
        System.out.println(grafo);
    }

    @Override
    public String toString() {
        return grafo.toString();
    }

    public NetworkExtension<Divisao> getNetwork() {
        return grafo;
    }
}
