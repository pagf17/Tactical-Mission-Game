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
import Interfaces.MapaStatus;
import Jogo.Printer.MapaPrinter;
import Estruturas.LinkedList;

import java.util.Iterator;

public class Mapa {
    private NetworkExtension<Divisao> grafo;

    /**
     * Construtor da classe Mapa.
     */
    public Mapa() {
        this.grafo = new NetworkExtension<>();
    }

    /**
     * Carrega o mapa a partir de um arquivo JSON especificado.
     *
     * @param arquivoJson o caminho do arquivo JSON a ser carregado
     * @throws IOException se ocorrer um erro ao ler o arquivo JSON
     *
     * - Lê o arquivo JSON e desserializa seu conteúdo.
     * - Carrega as divisões do edifício a partir da chave "edificio".
     * - Conecta as divisões com base nas ligações especificadas na chave "ligacoes".
     * - Adiciona itens às divisões com base nas informações da chave "itens".
     * - Carrega os inimigos e os adiciona às divisões correspondentes.
     * - Marca as divisões como entradas/saídas com base na chave "entradasSaidas".
     * - Carrega a missão a partir do JSON e a exibe.
     */
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
                MapaPrinter.printMapa(MapaStatus.CHAVE_EDIFICIO_NAO_ENCONTRADA, null);
            }

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
                            MapaPrinter.erroConectarDivisao(nomeDivisao1, nomeDivisao2);
                        }
                    }
                }
            } else {
                MapaPrinter.printMapa(MapaStatus.CHAVE_LIGACAO_NAO_ENCONTRADA, null);
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
                    MapaPrinter.printMapa(MapaStatus.DIVISAO_NAO_ENCONTRADA_INIMIGO, inimigo.getDivisao().getNome());
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
                MapaPrinter.printMapa(MapaStatus.MOSTRAR_MISSAO, missao.toString());
            }

        } catch (Exception e) {
            throw new IOException("Erro ao carregar o mapa", e);
        }
    }

    public void adicionarDivisao(Divisao divisao) {
        if (!grafo.isConnected() || !grafo.iteratorBFS(divisao).hasNext()) {
            grafo.addVertex(divisao);
        }
    }

    /**
     * Conecta duas divisões no mapa
     *
     * @param nomeDivisao1 o nome da primeira divisão 
     * @param nomeDivisao2 o nome da segunda divisão a ser conectada
     */
    public void conectarDivisoes(String nomeDivisao1, String nomeDivisao2) {
        Divisao divisao1 = obterDivisao(nomeDivisao1);
        Divisao divisao2 = obterDivisao(nomeDivisao2);

        if (divisao1 != null && divisao2 != null) {
            int peso = calcularPesoDivisao(divisao2);
            grafo.addEdge(divisao1, divisao2, peso);
            grafo.addEdge(divisao2, divisao1, peso);
        } else {
            MapaPrinter.erroConectarDivisao(nomeDivisao1, nomeDivisao2);
        }
    }

    /**
     * Obtem uma divisão a partir de um nome.
     *
     * @param nome é o nome da divisão a ser obtida
     * @return a divisão correspondente ao nome, ou null.
     */
    public Divisao obterDivisao(String nome) {
        for (int i = 0; i < grafo.size(); i++) {
            Divisao divisao = grafo.getVertex(i);
            if (divisao.getNome().equalsIgnoreCase(nome)) {
                return divisao;
            }
        }
        return null;
    }

    /**Não faz nada
     * Obter o inimigo presente na divisão atual.
     * 
     * @param divisaoAtual é a divisão atual do Tó Cruz
     * @return o inimigo presente na divisão atual
     */
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

    /**
     * Lista os inimigos presentes em cada divisão do mapa
     * Itera sobre todas as divisões do grafo e chama o método
     * "listarInimigos", da classe Divisão, em cada uma delas.
     */
    public void listarInimigos() {
        for (int i = 0; i < grafo.size(); i++) {
            Divisao divisao = grafo.getVertex(i);
            divisao.listarInimigos();
        }
    }

    /**
     * Lista os itens presentes em cada divisão do mapa
     * Itera sobre todas as divisões do grafo e chama o método
     * "listarItens", da classe Divisão, em cada uma delas.
     */
    public void listarItens() {
        for (int i = 0; i < grafo.size(); i++) {
            Divisao divisao = grafo.getVertex(i);
            divisao.listarItens();
        }
    }

    /**
     * Obtém um iterador das divisões válidas a partir da divisão atual.
     *
     * @param divisaoAtual a divisão a partir da qual as divisões válidas serão obtidas.
     * @return um iterador das divisões válidas, ou null.
     */
    public Iterator<Divisao> obterDivisoesValidas(Divisao divisaoAtual) {
        if (divisaoAtual == null) {
            return null;
        }

        LinkedList<Divisao> divisoesValidas = new LinkedList<>();
        for (int i = 0; i < grafo.size(); i++) {
            Divisao divisao = grafo.getVertex(i);
            if (verificarLigacao(divisaoAtual, divisao)) {
                divisoesValidas.add(divisao);
            }
        }

        return divisoesValidas.iterator();
    }
    
    /**
     * Mostra o caminho até à divisão onde se encontra o objetivo
     * 
     * @param origem divisão atual do Tó Cruz
     * @param destino divisão onde se encontra o objetivo
     */
    public void mostrarCaminhoParaObjetivo(Divisao origem, Divisao destino) {
        Iterator<Divisao> caminho = grafo.iteratorShortestPath(origem, destino);

        if (!caminho.hasNext()) {
            MapaPrinter.caminhoInexistente(origem.getNome(), destino.getNome());
            return;
        }

        String caminhoString = "";
        while (caminho.hasNext()) {
            Divisao divisao = caminho.next();
            caminhoString += divisao.getNome();
            if (caminho.hasNext()) {
                caminhoString += " -> ";
            }
        }
        MapaPrinter.printMapa(MapaStatus.CAMINHO_PARA_OBJETIVO, caminhoString);
    }

    /**
     * Verifica qual é o kit de vida mais próximo do Tó Cruz.
     * 
     * @param startDivisao é a divisão atual do Tó Cruz
     * @return a divisão onde está o kit mais próximo
     */
    public Divisao encontrarKitMaisProximo(Divisao startDivisao) {
        int startIndex = grafo.getIndex(startDivisao);
        double minDistancia = Double.MAX_VALUE;
        Divisao kitDivisao = null;

        for (int i = 0; i < grafo.size(); i++) {
            Divisao divisao = grafo.getVertex(i);

            boolean temKit = false;
            for (Item item : divisao.getItens()) {
                if (item instanceof Kit) {
                    temKit = true;
                    break;
                }
            }
            if (temKit) {
                Iterator<Divisao> caminhoIterator = grafo.iteratorShortestPath(startIndex, i);

                if (caminhoIterator.hasNext()) {
                    double distancia = 0;
                    Divisao anterior = startDivisao;
                    while (caminhoIterator.hasNext()) {
                        Divisao divisaoNoCaminho = caminhoIterator.next();
                        distancia += 1;
                        anterior = divisaoNoCaminho;
                    }
                    if (distancia < minDistancia) {
                        minDistancia = distancia;
                        kitDivisao = divisao;
                    }
                }
            }
        }
        return kitDivisao;
    }

    /**
     * Mostra o caminho até ao kit de vida mais próximo 
     * @param startDivisao é a divisão atual do Tó Cruz
     */
    public void caminhoParaKit(Divisao startDivisao) {
        Divisao kitDivisao = encontrarKitMaisProximo(startDivisao);

        if (kitDivisao == null) {
            return;
        }

        int startIndex = grafo.getIndex(startDivisao);
        int targetIndex = grafo.getIndex(kitDivisao);
        Iterator<Divisao> caminho = grafo.iteratorShortestPath(startIndex, targetIndex);

        if (!caminho.hasNext()) {
            MapaPrinter.caminhoInexistente(startDivisao.getNome(), kitDivisao.getNome());
            return;
        }
        String caminhoString = "";
        while (caminho.hasNext()) {
            Divisao divisao = caminho.next();
            caminhoString += divisao.getNome();
            if (caminho.hasNext()) {
                caminhoString += " -> ";
            }
        }
        MapaPrinter.printMapa(MapaStatus.CAMINHO_PARA_KIT, caminhoString);
    }

    /**
     * Calcula o peso de um caminho.
     * 
     * @param caminho
     * @return o peso do caminho
     */
    private double calcularPesoCaminho(Iterable<Divisao> caminho) {
        double peso = 0;

        Iterator<Divisao> iterator = caminho.iterator();
        if (!iterator.hasNext()) {
            return peso;
        }

        Divisao anterior = iterator.next();
        while (iterator.hasNext()) {
            Divisao atual = iterator.next();
            Divisao divisaoAnterior = obterDivisao(anterior.getNome());
            Divisao divisaoAtual = obterDivisao(atual.getNome());
            peso += grafo.shortestPathWeight(divisaoAnterior, divisaoAtual);
            anterior = atual;
        }
        return peso;
    }

    /**
     * Obtém o melhor caminho entre duas divisões.
     * 
     * @param divisaoOrigem 
     * @param divisaoDestino
     * @return um iterador do caminho
     */
    public Iterator<Divisao> obterCaminhoModoAutomatico(Divisao divisaoOrigem, Divisao divisaoDestino) {
        return grafo.iteratorShortestPath(divisaoOrigem, divisaoDestino);
    }

    /**
     * Calcula o peso de uma divisão baseado no dano dos inimigos presentes nela.
     * 
     * @param divisaoDestino
     * @return o peso, ou seja, o valor da soma do dano dos inimigos
     */
    private int calcularPesoDivisao(Divisao divisaoDestino) {
        int peso = 1;
        for (Inimigo inimigo : divisaoDestino.getInimigos()) {
            peso += inimigo.getDano();
        }
        return peso;
    }

    /**
     *  Atualiza o peso das arestas do grafo com base no dano dos inimigos 
     *  calculado através do método "calcularPesoDivisao".
     */
    public void atualizarPesoLigacoes() {
        for (int i = 0; i < grafo.size(); i++) {
            Divisao divisaoOrigem = grafo.getVertex(i);
            for (int j = 0; j < grafo.size(); j++) {
                Divisao divisaoDestino = grafo.getVertex(j);
                if (verificarLigacao(divisaoOrigem, divisaoDestino)) {
                    int peso = calcularPesoDivisao(divisaoDestino);
                    grafo.atualizarPeso(divisaoOrigem, divisaoDestino, peso);
                }
            }
        }
    }

    public Iterator<Divisao> obterEntradas() {
        LinkedList<Divisao> entradas = new LinkedList<>();
        for (int i = 0; i < grafo.size(); i++) {
            Divisao divisao = grafo.getVertex(i);
            if (divisao.isEntradaSaida()) {
                entradas.add(divisao);
            }
        }
        return entradas.iterator();
    }

    /**
     * Escolhe a melhor entrada para o objetivo.
     * 
     * @param divisaoObjetivo
     * @return a melhor entrada
     */
    public Divisao escolherMelhorEntrada(Divisao divisaoObjetivo) {
        LinkedList<Divisao> entradas = new LinkedList<>();
        Divisao melhorEntrada = null;
        double menorPeso = Double.POSITIVE_INFINITY;

        for (int i = 0; i < grafo.size(); i++) {
            Divisao entradaSaida = grafo.getVertex(i);
            if (entradaSaida.isEntradaSaida()) {
                entradas.add(entradaSaida);
            }
        }

        if (entradas.isEmpty()) {
            return null;
        }

        Iterator<Divisao> iterator = entradas.iterator();
        while (iterator.hasNext()) {
            Divisao entrada = iterator.next();
            Iterator<Divisao> caminho = grafo.iteratorShortestPath(entrada, divisaoObjetivo);
            double peso = calcularPesoCaminho(() -> caminho);

            if (peso < menorPeso) {
                menorPeso = peso;
                melhorEntrada = entrada;
            }
        }
        return melhorEntrada;
    }

    protected Divisao escolherMelhorSaida(Divisao divisaoAtual) {
        LinkedList<Divisao> saidas = new LinkedList<>();
        Divisao melhorSaida = null;
        double menorPeso = Double.POSITIVE_INFINITY;

        for (int i = 0; i < grafo.size(); i++) {
            Divisao entradaSaida = grafo.getVertex(i);
            if (entradaSaida.isEntradaSaida()) {
                saidas.add(entradaSaida);
            }
        }

        if (saidas.isEmpty()) {
            return null;
        }

        Iterator<Divisao> iterator = saidas.iterator();
        while (iterator.hasNext()) {
            Divisao saida = iterator.next();
            Iterator<Divisao> caminho = grafo.iteratorShortestPath(divisaoAtual, saida);
            double peso = calcularPesoCaminho(() -> caminho);

            if (peso < menorPeso) {
                menorPeso = peso;
                melhorSaida = saida;
            }
        }
        return melhorSaida;
    }

    protected void verMapa() {
        Iterator<Divisao> divisoes = grafo.iteratorBFS(0); 
        while (divisoes.hasNext()) {
            Divisao divisao = divisoes.next();
            String ligacoes = "";
            boolean first = true;
            for (int j = 0; j < grafo.size(); j++) {
                Divisao outraDivisao = grafo.getVertex(j);
                if (!outraDivisao.equals(divisao) && verificarLigacao(divisao, outraDivisao)) {
                    if (!first) {
                        ligacoes += ", ";
                    }
                    ligacoes += outraDivisao.getNome();
                    first = false;
                }
            }
            
            String itens = "";
            if (!divisao.getItens().isEmpty()) {
                for (Item item : divisao.getItens()) {
                    itens += "|" + item + "\n";
                }
            }
    
            String inimigos = "";
            if (!divisao.getInimigos().isEmpty()) {
                for (Inimigo inimigo : divisao.getInimigos()) {
                    inimigos += "|" + inimigo + "\n";
                }
            }
    
            MapaPrinter.verMapa(divisao.getNome(), ligacoes, itens, inimigos);
        }
    }

    @Override
    public String toString() {
        return grafo.toString();
    }

    public NetworkExtension<Divisao> getNetwork() {
        return grafo;
    }
}
