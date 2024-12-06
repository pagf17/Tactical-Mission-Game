import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import Estruturas.ArrayUnorderedList;
import Estruturas.GraphExtension;
import Estruturas.LinkedQueue;

import java.util.Iterator;

public class Mapa {
    private GraphExtension<Divisao> grafo;

    public Mapa() {
        this.grafo = new GraphExtension<>();
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
            grafo.addEdge(divisao1, divisao2);
        } else {
            System.out.println("Uma ou ambas as divisões não existem.");
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

            JsonArray ligacoesJson = (JsonArray) jsonObject.get("ligacoes");
            if (ligacoesJson != null) {
                for (Object obj : ligacoesJson) {
                    JsonArray ligacao = (JsonArray) obj;
                    if (ligacao.size() == 2) {
                        String divisao1 = (String) ligacao.get(0);
                        String divisao2 = (String) ligacao.get(1);
                        conectarDivisoes(divisao1, divisao2);
                    }
                }
            } else {
                System.out.println("Chave 'ligacoes' não encontrada no JSON.");
            }

            ArrayUnorderedList<Item> itens = Item.carregarItens(jsonObject);
            for (Item item : itens) {
                Divisao divisao = obterDivisao(item.getlocalizacao());
                if (divisao != null) {
                    divisao.addItem(item);
                } else {
                    System.out.println("Divisão não encontrada para o item: " + item.getTipo());
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

            Missao missao = Missao.carregarMissao(jsonObject);
            if (missao != null) {
                System.out.println(missao);
            }

        } catch (Exception e) {
            System.out.println("Erro ao carregar o mapa: " + e.getMessage());
            throw new IOException("Erro ao carregar o mapa", e);
        }
    }

    public Inimigo obterInimigo(String divisaoAtual) {
        Divisao divisao = obterDivisao(divisaoAtual);
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

    public boolean verificarLigacao(Divisao divisao1, Divisao divisao2) {
        if (divisao1 != null && divisao2 != null) {
            return grafo.verificarLigacao(divisao1, divisao2);
        }
        return false;
    }

    public ArrayUnorderedList<Divisao> obterDivisoesValidas(String divisaoAtual) {
        ArrayUnorderedList<Divisao> divisoesValidas = new ArrayUnorderedList<>();
        Divisao divisao = obterDivisao(divisaoAtual);
    
        for (int i = 0; i < grafo.size(); i++) {
            Divisao divisaoPotencial = grafo.getVertex(i);
    
            if (verificarLigacao(divisao, divisaoPotencial)) {
                divisoesValidas.addToRear(divisaoPotencial);
            }
        }
    
        return divisoesValidas;
    }

    public void moverInimigos(String divisaoAtualToCruz) {
        LinkedQueue<MovimentoInimigo> movimentosPendentes = new LinkedQueue<>();
        for (int i = 0; i < grafo.size(); i++) {
            Divisao divisaoAtual = grafo.getVertex(i);
            ArrayUnorderedList<Inimigo> inimigos = divisaoAtual.getInimigos();
    
            for (Inimigo inimigo : inimigos) {
                if (divisaoAtual.getNome().equalsIgnoreCase(divisaoAtualToCruz)) {
                    continue;
                }
                ArrayUnorderedList<Divisao> divisoesConectadas = new ArrayUnorderedList<>();
                for (int j = 0; j < grafo.size(); j++) {
                    Divisao divisaoPotencial = grafo.getVertex(j);
                    if (verificarLigacao(divisaoAtual, divisaoPotencial)) {
                        divisoesConectadas.addToRear(divisaoPotencial);
                    }
                }
                if (!divisoesConectadas.isEmpty()) {
                    int randomIndex = (int) (Math.random() * divisoesConectadas.size());
                    Divisao novaDivisao = divisoesConectadas.get(randomIndex);
                    movimentosPendentes.enqueue(new MovimentoInimigo(inimigo, divisaoAtual, novaDivisao));
                }
            }
        }
        while (!movimentosPendentes.isEmpty()) {
            MovimentoInimigo movimento = movimentosPendentes.dequeue(); 
            movimento.getDivisaoOrigem().getInimigos().remove(movimento.getInimigo()); 
            movimento.getDivisaoDestino().addInimigo(movimento.getInimigo()); 
            System.out.println("Inimigo " + movimento.getInimigo().getNome() + " moveu-se para a divisão " + movimento.getDivisaoDestino().getNome());
        }
    }
    
    public void verMapa() {
        System.out.println(grafo);
    }

    @Override
    public String toString() {
        return grafo.toString();
    }
}
