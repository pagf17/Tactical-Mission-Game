package Jogo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import java.util.Iterator;

import Interfaces.JogoStatus;
import Jogo.Printer.JogoPrinter;
import Jogo.Resultados.Exportar;
import Estruturas.ArrayUnorderedList;
import Estruturas.Exceptions.EmptyCollectionException;

public class Jogo {
    private Mapa mapa;
    private Missao missao;
    private ToCruz toCruz;
    private Exportar exportar;
    private boolean jogoAtivo;
    private int turnoCount = 1;
    private boolean modoManual;

    /**
     * Construtor da classe Jogo, inicia o mapa, a missão, o jogador e o scanner.
     * 
     * @param arquivoJson recebe como parametro o arquivo JSON com as informações do
     *                    jogo
     * @throws IOException
     */
    public Jogo(String arquivoJson, boolean modoManual) throws IOException {
        this.mapa = new Mapa();
        this.mapa.carregarMapa(arquivoJson);
        this.toCruz = new ToCruz(50, null);
        this.exportar = new Exportar();
        this.jogoAtivo = true;
        this.modoManual = modoManual;
        mapa.atualizarPesoLigacoes();

        try {
            try (Reader reader = new InputStreamReader(new FileInputStream(arquivoJson), "UTF-8")) {
                JsonObject jsonObject = (JsonObject) Jsoner.deserialize(reader);
                this.missao = Missao.carregarMissao(jsonObject);
                exportar.iniciarNovaMissao(missao.getCodMissao(), missao.getVersao());
            } catch (IOException | JsonException e) {
                throw new IOException("Erro ao carregar o arquivo JSON da missão", e);
            }

        } catch (Exception e) {
            throw new IOException("Erro ao carregar a missão", e);
        }
    }

    /**
     * Inicia o turno do personagem Tó Cruz no jogo.
     * 
     * 1. Atualiza o peso das ligações no mapa.
     * 2. Exibe uma mensagem indicando o início do turno de Tó Cruz.
     * 3. Lista os inimigos e itens presentes no mapa.
     * 4. Mostra o caminho para o objetivo da missão ou para a saída, caso o
     * objetivo já tenha sido concluído.
     * 5. Mostra o caminho para o kit de primeiros socorros.
     * 6. Exibe a divisão atual em que Tó Cruz se encontra.
     * 7. Lista os itens disponíveis na divisão atual, se houver.
     * 8. Lista os inimigos presentes na divisão atual, se houver.
     * 9. Exibe uma mensagem de aviso se Tó Cruz encontrar o objetivo da missão na
     * divisão atual.
     */
    public void iniciarTurnoToCruz() {
        mapa.atualizarPesoLigacoes();
        JogoPrinter.printJogo(JogoStatus.INICIO_TURNO_JOGADOR, null);
        mapa.listarInimigos();
        mapa.listarItens(); 

        Divisao divisaoAlvo = mapa.obterDivisao(missao.getObjetivo().getDivisaoAlvo());
        if (!missao.getObjetivo().isConcluido()) {
            mapa.mostrarCaminhoParaObjetivo(toCruz.getDivisaoAtual(), divisaoAlvo);
        } else {
            mapa.mostrarCaminhoParaObjetivo(toCruz.getDivisaoAtual(), mapa.escolherMelhorSaida(toCruz.getDivisaoAtual()));
        }
        mapa.caminhoParaKit(toCruz.getDivisaoAtual());

        Divisao divisaoAtual = mapa.obterDivisao(toCruz.getDivisaoAtual().getNome());
        JogoPrinter.printJogo(JogoStatus.DIVISAO_ATUAL, divisaoAtual.getNome());

        if (!divisaoAtual.getItens().isEmpty()) {
            JogoPrinter.printJogo(JogoStatus.ITENS_PRESENTES, divisaoAtual.getItens().toString());
        }

        if (!divisaoAtual.getInimigos().isEmpty()) {
            JogoPrinter.printJogo(JogoStatus.INIMIGOS_PRESENTES, divisaoAtual.getInimigos().toString());
        }

        if (getMissao().getObjetivo().getDivisaoAlvo().equalsIgnoreCase(divisaoAtual.getNome())) {
            JogoPrinter.printJogo(JogoStatus.OBJETIVO_ENCONTRADO, null);
        }
    }

    /**
     * Finaliza o turno de Tó Cruz e inicia o turno dos inimigos.
     */
    public void finalizarTurnoToCruz() {
        JogoPrinter.printJogo(JogoStatus.FIM_TURNO_JOGADOR, null);
        iniciarTurnoInimigos();
    }

    /**
     * Inicia o turno dos inimigos.
     */
    private void iniciarTurnoInimigos() {
        JogoPrinter.printJogo(JogoStatus.INICIO_TURNO_INIMIGO, null);

        Divisao divisaoAtual = mapa.obterDivisao(toCruz.getDivisaoAtual().getNome());
        ArrayUnorderedList<Inimigo> inimigosNaSala = divisaoAtual.getInimigos();

        for (Inimigo inimigo : inimigosNaSala) {
            JogoPrinter.printJogo(JogoStatus.ATAQUE_INIMIGO, inimigo.getNome());
            toCruz.levarDano(inimigo.getDano());
            if (toCruz.morto()) {
                JogoPrinter.printJogo(JogoStatus.JOGADOR_DERROTADO, null);
                finalizarJogo();
                return;
            }
        }
        JogoPrinter.printJogo(JogoStatus.MOVER_INIMIGOS, null);
        moverInimigos(divisaoAtual);

        finalizarTurnoInimigos();
        }

        /**
         * Finaliza o turno dos inimigos e inicia o turno de Tó Cruz.
         */
        public void finalizarTurnoInimigos() {
        JogoPrinter.printJogo(JogoStatus.FIM_TURNO_INIMIGO, null);
        turnoCount++;
        iniciarTurnoToCruz();
        }

        /**
         * Inicia o combate entre Tó Cruz e os inimigos presentes na divisão atual.
         */
        public void iniciarCombate() {
        Divisao divisaoAtual = mapa.obterDivisao(toCruz.getDivisaoAtual().getNome());
        ArrayUnorderedList<Inimigo> inimigosNaSala = divisaoAtual.getInimigos();

        if (inimigosNaSala.isEmpty()) {
            JogoPrinter.printJogo(JogoStatus.SEM_INIMIGOS, null);
            return;
        }

        JogoPrinter.printJogo(JogoStatus.LUTA_INICIADA, null);
        for (Inimigo inimigo : inimigosNaSala) {
            JogoPrinter.printJogo(JogoStatus.INIMIGOS_PRESENTES, inimigo.getNome());
        }

        ArrayUnorderedList<Inimigo> eliminados = new ArrayUnorderedList<>();
        for (Inimigo inimigo : inimigosNaSala) {
            toCruz.atacar(inimigo);
            JogoPrinter.printJogo(JogoStatus.ATAQUE_JOGADOR, inimigo.getNome());
            if (inimigo.morto()) {
                JogoPrinter.printJogo(JogoStatus.INIMIGO_ELIMINADO, inimigo.getNome());
                eliminados.addToRear(inimigo);
            }
        }

        for (Inimigo inimigo : eliminados) {
            inimigosNaSala.remove(inimigo);
        }

        if (inimigosNaSala.isEmpty()) {
            JogoPrinter.printJogo(JogoStatus.TODOS_INIMIGOS_ELIMINADOS, null);
        } else {
            JogoPrinter.printJogo(JogoStatus.ALERTA_INIMIGOS, null);
        }
        finalizarTurnoToCruz();
    }

    public void verMapa() {
        mapa.verMapa();
    }

    protected void usarItemManual(ArrayUnorderedList<Item> itensDisponiveis, int escolha) {
        if (escolha >= 0 && escolha < itensDisponiveis.size()) {
            Item itemEscolhido = itensDisponiveis.get(escolha);

            if (itemEscolhido instanceof Kit) {
                toCruz.addKitMochila((Kit) itemEscolhido);
                JogoPrinter.printJogo(JogoStatus.KIT_ADD_MOCHILA, null);
                itensDisponiveis.remove(itemEscolhido);
            } else if (itemEscolhido instanceof Colete) {
                toCruz.usarColete((Colete) itemEscolhido);
                itensDisponiveis.remove(itemEscolhido);
            }
        } else {
            JogoPrinter.printJogo(JogoStatus.ITEM_INVALIDO, null);
        }
    }


    public void finalizarJogo() {
        jogoAtivo = false;
        if (modoManual) {
            exportar.finalizarMissao(toCruz.getHp(), missao.isCompleta());
            exportar.exportar("resultados.json");
        }
    }

    public void usarKit() {
        if (!toCruz.getMochila().isEmpty()) {
            toCruz.usarKit();
            finalizarTurnoToCruz();
        } else {
            JogoPrinter.printJogo(JogoStatus.MOCHILA_VAZIA, null);
        }
    }

    /**
     * Move os inimigos da divisão atual do Tó Cruz para outras divisões.
     * Se um inimigo se mover para a divisão de Tó Cruz ataca-o.
     * Se com esse ataque o Tó Cruz morrer, o jogo acaba.
     *
     * @param divisaoAtualToCruz é a divisão atual do Tó Cruz
     */
    public void moverInimigos(Divisao divisaoAtualToCruz) {
        for (int i = 0; i < mapa.getNetwork().size(); i++) {
            Divisao divisaoAtual = mapa.getNetwork().getVertex(i);
            ArrayUnorderedList<Inimigo> inimigos = divisaoAtual.getInimigos();
    
            for (Inimigo inimigo : inimigos) {
                if (divisaoAtual == divisaoAtualToCruz) {
                    continue;
                }
    
                ArrayUnorderedList<Divisao> divisoesConectadas = new ArrayUnorderedList<>();
                for (int j = 0; j < mapa.getNetwork().size(); j++) {
                    Divisao divisaoPotencial = mapa.getNetwork().getVertex(j);
                    if (mapa.verificarLigacao(divisaoAtual, divisaoPotencial)) {
                        divisoesConectadas.addToRear(divisaoPotencial);
                    }
                }
                if (!divisoesConectadas.isEmpty()) {
                    int randomIndex = (int) (Math.random() * divisoesConectadas.size());
                    Divisao novaDivisao = divisoesConectadas.get(randomIndex);
                    inimigo.mover(novaDivisao);
                    if (toCruz.getDivisaoAtual() == novaDivisao) {
                        JogoPrinter.printJogo(JogoStatus.INIMIGO_MOVEU_PARA_JOGADOR, inimigo.getNome());
                        toCruz.levarDano(inimigo.getDano());
                        if (toCruz.morto()) {
                            JogoPrinter.printJogo(JogoStatus.JOGADOR_DERROTADO, null);
                            finalizarJogo();
                            return;
                        }
                        JogoPrinter.printJogo(JogoStatus.JOGADOR_ATACADO, inimigo.getNome());
                    }
                }
            }
        }
        mapa.atualizarPesoLigacoes();
    }
    
    public void moverJogador(Divisao novaDivisao) {
        Divisao divisaoAtual = mapa.obterDivisao(toCruz.getDivisaoAtual().getNome());
        Divisao novaDivisaoObj = mapa.obterDivisao(novaDivisao.getNome());

        if (divisaoAtual == null || novaDivisaoObj == null) {
            JogoPrinter.printJogo(JogoStatus.DIVISAO_INVALIDA, null);
            return;
        }

        if (!divisaoAtual.getInimigos().isEmpty()) {
            JogoPrinter.printJogo(JogoStatus.MOVIMENTO_BLOQUEADO_POR_INIMIGO, null);
            return;
        }

        if (!mapa.verificarLigacao(divisaoAtual, novaDivisaoObj)) {
            JogoPrinter.printJogo(JogoStatus.DIVISAO_INVALIDA, null);
            return;
        }

        toCruz.setDivisaoAtual(novaDivisao);
        exportar.registarMovimento(divisaoAtual.getNome(), novaDivisao.getNome());
        JogoPrinter.printJogo(JogoStatus.JOGADOR_MOVEU, novaDivisao.getNome());

        if (!novaDivisaoObj.getItens().isEmpty()) {
            JogoPrinter.printJogo(JogoStatus.ITENS_DISPONIVEIS, null);
        }

        if (missao.getObjetivo().getDivisaoAlvo().equals(novaDivisao.getNome())) {
            JogoPrinter.printJogo(JogoStatus.OBJETIVO_ENCONTRADO, null);
        }

        if (!novaDivisaoObj.getInimigos().isEmpty()) {
            JogoPrinter.printJogo(JogoStatus.ALERTA_INIMIGOS, null);
        }

        finalizarTurnoToCruz();
    }

    /**
     * Método para iniciar o jogo
     */
    public void iniciarJogo() {
        mostrarStatus();
    }

    /**
     * Método para definir a entrada 
     * 
     * @param entrada a divisao de entrada
     */
    public void definirEntrada(Divisao entrada) {
        if ( entrada != null && entrada.isEntradaSaida()) {
            toCruz.setDivisaoAtual(entrada);
        } 
    }

    /**
     * Verifica se o jogador alcançou o objetivo da missão.
     * 
     * @param divisaoAtual
     */
    public JogoStatus verificarProgresso(Divisao divisaoAtual) {
        Divisao divisaoAtual1 = mapa.obterDivisao(divisaoAtual.getNome());

        if (divisaoAtual1 == null) {
            return JogoStatus.DIVISAO_INVALIDA;
        }

        if (!missao.getObjetivo().isConcluido() && divisaoAtual1.getNome().equalsIgnoreCase(missao.getObjetivo().getDivisaoAlvo())) {
            if (!divisaoAtual1.getInimigos().isEmpty()) {
                return JogoStatus.INIMIGOS_PRESENTES_OBJETIVO;
            } else {
                missao.getObjetivo().concluir();
                missao.setObjetivoConcluido(true);
                return JogoStatus.OBJETIVO_ALCANCADO;
            }
        } else if (missao.getObjetivo().isConcluido() && divisaoAtual1.isEntradaSaida()) {
            missao.completar();
            finalizarJogo();
            return JogoStatus.OBJETIVO_EXTRAIDO;
        } else if (missao.getObjetivo().isConcluido() && !divisaoAtual1.isEntradaSaida()) {
            return JogoStatus.PROCURE_SAIDA_VALIDA;
        } else {
            return JogoStatus.CONTINUAR_EXPLORACAO;
        }
    }

    public void mostrarStatus() {
        JogoPrinter.printStatus(missao.toString(), toCruz.toString());
        mapa.listarInimigos();
        mapa.listarItens();
    }

    protected int getTurnoCount() {
        return turnoCount;
    }

    protected ToCruz getToCruz() {
        return toCruz;
    }

    /**
     * @return a missão 
     */
    protected Missao getMissao() {
        return missao;
    }

    /**
     * @return o mapa
     */
    protected Mapa getMapa() {
        return mapa;
    }

    /**
     * @return se o jogo se encontra ativo ou não
     */
    protected boolean isJogoAtivo() {
        return jogoAtivo;
    }

    // ************************************//
    // MÉTODOS MODO AUTOMÁTICO             //
    // ************************************//

    /**
     * Inicia o modo automático
     */
    public void iniciarModoAutomatico() {
        Divisao entrada = escolherEntrada();
        toCruz.setDivisaoAtual(entrada);
        JogoPrinter.printJogo(JogoStatus.ENTRADA_ESCOLHIDA, entrada.getNome());

        while (jogoAtivo) {
            if (toCruz.morto() == true) {
                JogoPrinter.printJogo(JogoStatus.JOGADOR_DERROTADO, null);
                finalizarJogo();
                break;
            }
            turnoJogadorAutomatico();
            if (!jogoAtivo) {
                break;
            }
            if (missao.getObjetivo().isConcluido() && toCruz.getDivisaoAtual().isEntradaSaida()) {
                finalizarJogo();
                break;
            } else {
                turnoInimigosAutomatico();
            }
            if (!jogoAtivo) {
                break;
            }

        }
    }

    /**
     * Escolhe a melhor entrada para a missão, recorrendo a métodos auxiliares presentes na classe Mapa.
     * 
     * @return a melhor entrada
     */
    private Divisao escolherEntrada() {
        Divisao objetivoDivisao = mapa.obterDivisao(missao.getObjetivo().getDivisaoAlvo());
        Divisao melhorEntrada = mapa.escolherMelhorEntrada(objetivoDivisao);

        if (melhorEntrada == null) {
            JogoPrinter.printJogo(JogoStatus.ENTRADA_INVALIDA, null);
        }

        return melhorEntrada;
    }

    /**
     * Método para mover o jogador automaticamente para o objetivo da missão
     */
    public void turnoJogadorAutomatico() {
        mapa.atualizarPesoLigacoes();
        JogoPrinter.printJogo(JogoStatus.INICIO_TURNO_JOGADOR, null);
        mostrarStatus();

        if (toCruz.morto()) {
            finalizarJogo();
            return;
        }

        if (toCruz.getHp() < 50 && !toCruz.getMochila().isEmpty()) {
            usarKit();

            finalizarTurnoJogadorAutomatico();
            return;
        }

        if (!toCruz.getDivisaoAtual().getInimigos().isEmpty()) {
            JogoPrinter.printJogo(JogoStatus.INIMIGOS_PRESENTES, toCruz.getDivisaoAtual().getInimigos().toString());
            iniciarCombateAutomatico();
            if (toCruz.morto()) {
                finalizarJogo();
                return;
            }
        }

        if (!toCruz.getDivisaoAtual().getItens().isEmpty()) {
            JogoPrinter.printJogo(JogoStatus.ITENS_PRESENTES, toCruz.getDivisaoAtual().getItens().toString());
            ArrayUnorderedList<Item> itensDisponiveis = mapa.obterDivisao(toCruz.getDivisaoAtual().getNome()).getItens();
            for (Item item : itensDisponiveis) {
                usarItemAutomatico(itensDisponiveis);
            }
        }

        if (!missao.getObjetivo().isConcluido()) {
            moverParaObjetivoAutomatico();
        } else {
            moverParaSaidaAutomatico();
        }
    }

    /**
     * Método para finalizar o turno do jogador automático
     */
    public void finalizarTurnoJogadorAutomatico() {
        if (jogoAtivo) {
            JogoPrinter.printJogo(JogoStatus.FIM_TURNO_JOGADOR, null);
            turnoInimigosAutomatico();
        }
    }

    /**
     * Método para iniciar o turno dos inimigos no modo automático
     * Move todos os inimigos que não estão presentes na sala do ToCruz com recurso
     * ao método "moverInimigos"
     * Caso o ToCruz morra, é chamado o método "finalizarJogo" que termina o jogo,
     * caso contrário o turno dos inimigos é finalizado com recurso ao método
     * "finalizarTurnoInimigosAutomatico"
     */
    public void turnoInimigosAutomatico() {
        JogoPrinter.printJogo(JogoStatus.INICIO_TURNO_INIMIGO, null);

        Divisao divisaoAtual = mapa.obterDivisao(toCruz.getDivisaoAtual().getNome());
        ArrayUnorderedList<Inimigo> inimigosNaSala = divisaoAtual.getInimigos();

        for (Inimigo inimigo : inimigosNaSala) {
            JogoPrinter.printJogo(JogoStatus.ATAQUE_INIMIGO, inimigo.getNome());
            toCruz.levarDano(inimigo.getDano());
            if (toCruz.morto()) {
                JogoPrinter.printJogo(JogoStatus.JOGADOR_DERROTADO, null);
                finalizarJogo();
                return;
            }
        }
        moverInimigos(toCruz.getDivisaoAtual());
        finalizarTurnoInimigosAutomatico();
    }

    /**
     * Método para finalizar o turno dos inimigos.
     */
    public void finalizarTurnoInimigosAutomatico() {
        if (jogoAtivo) {
            JogoPrinter.printJogo(JogoStatus.FIM_TURNO_INIMIGO, null);
            turnoJogadorAutomatico();
        }
    }

    /**
     * Método para mover o jogador automaticamente para o objetivo da missão
     */
    public void moverParaObjetivoAutomatico() {
        Divisao objetivoDivisao = mapa.obterDivisao(missao.getObjetivo().getDivisaoAlvo());

        Iterator<Divisao> caminho = mapa.obterCaminhoModoAutomatico(toCruz.getDivisaoAtual(), objetivoDivisao);

        if (caminho.hasNext()) {
            caminho.next();
            if (caminho.hasNext()) {
                Divisao proximaDivisao = caminho.next();

                if (!toCruz.getDivisaoAtual().equals(proximaDivisao)) {
                    toCruz.setDivisaoAtual(proximaDivisao);
                    JogoPrinter.printJogo(JogoStatus.JOGADOR_MOVEU, proximaDivisao.getNome());
                }

                if (proximaDivisao.equals(objetivoDivisao)) {
                    JogoPrinter.printJogo(JogoStatus.OBJETIVO_ENCONTRADO, null);
                    verificarProgresso(proximaDivisao);
                }
                finalizarTurnoJogadorAutomatico();
            }
        }
    }

    /**
     * Método para mover o jogador automaticamente para a saída, funciona da mesma maneira do método de mover 
     * o jogador para o objetivo.
     */
    public void moverParaSaidaAutomatico() {
        Divisao saida = mapa.escolherMelhorSaida(toCruz.getDivisaoAtual());

        Iterator<Divisao> caminho = mapa.obterCaminhoModoAutomatico(toCruz.getDivisaoAtual(), saida);
        if (caminho.hasNext()) {
            caminho.next();
            if (caminho.hasNext()) {
                Divisao proximaDivisao = caminho.next();
                toCruz.setDivisaoAtual(proximaDivisao);
                JogoPrinter.printJogo(JogoStatus.JOGADOR_MOVEU, proximaDivisao.getNome());
                verificarProgresso(toCruz.getDivisaoAtual());
                if (!missao.isCompleta()) {
                    finalizarTurnoJogadorAutomatico();
                } else {
                    finalizarJogo();
                    return;
                }
            }
        }
    }

    /**
     * Percorre a lista de itensDisponiveis para posteriormente
     * os adicionar à mochila, no caso de ser um kit, ou utilizá-los
     * caso sejam coletes.
     * 
     * @param itensDisponiveis uma lista com os itens disponiveis na divisão
     *atual de Tó Cruz.
     */
    private void usarItemAutomatico(ArrayUnorderedList<Item> itensDisponiveis) {
        for (int i = 0; i < itensDisponiveis.size(); i++) {
            Item itemEscolhido = itensDisponiveis.get(i);

            if (itemEscolhido instanceof Kit) {
                toCruz.addKitMochila((Kit) itemEscolhido);
                JogoPrinter.printJogo(JogoStatus.KIT_ADD_MOCHILA, null);
                itensDisponiveis.remove(itemEscolhido);
                break;
            } else if (itemEscolhido instanceof Colete) {
                toCruz.usarColete((Colete) itemEscolhido);
                itensDisponiveis.remove(itemEscolhido);
                break;
            }
        }
    }

    public void iniciarCombateAutomatico() {
        Divisao divisaoAtual = mapa.obterDivisao(toCruz.getDivisaoAtual().getNome());
        ArrayUnorderedList<Inimigo> inimigosNaSala = divisaoAtual.getInimigos();

        if (inimigosNaSala.isEmpty()) {
            throw new EmptyCollectionException("null");
        }

        JogoPrinter.printJogo(JogoStatus.LUTA_INICIADA, null);
        JogoPrinter.printJogo(JogoStatus.INIMIGOS_PRESENTES, inimigosNaSala.toString());

        ArrayUnorderedList<Inimigo> eliminados = new ArrayUnorderedList<>();
        for (Inimigo inimigo : inimigosNaSala) {
            toCruz.atacar(inimigo);
            JogoPrinter.printJogo(JogoStatus.ATAQUE_JOGADOR, inimigo.getNome());
            if (inimigo.morto()) {
                JogoPrinter.printJogo(JogoStatus.INIMIGO_ELIMINADO, inimigo.getNome());
                eliminados.addToRear(inimigo);
            }
        }

        for (Inimigo inimigo : eliminados) {
            inimigosNaSala.remove(inimigo);
        }

        if (inimigosNaSala.isEmpty()) {
            JogoPrinter.printJogo(JogoStatus.TODOS_INIMIGOS_ELIMINADOS, null);
        } else {
            JogoPrinter.printJogo(JogoStatus.ALERTA_INIMIGOS, null);
        }
        finalizarTurnoJogadorAutomatico();
    }
}
