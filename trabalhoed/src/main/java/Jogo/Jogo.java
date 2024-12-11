package Jogo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import java.util.Iterator;
import java.util.Scanner;

import Estruturas.ArrayUnorderedList;
import Estruturas.NetworkExtension;
import Estruturas.Exceptions.EmptyCollectionException;

public class Jogo {
    private Mapa mapa;
    private Missao missao;
    private ToCruz toCruz;
    private boolean jogoAtivo;
    private Scanner scanner;
    private int turnoCount = 1;

    public Jogo(String arquivoJson) throws IOException {
        this.mapa = new Mapa();
        this.mapa.carregarMapa(arquivoJson);
        this.toCruz = new ToCruz(50, null);
        this.jogoAtivo = true;
        this.scanner = new Scanner(System.in);
        mapa.atualizarPesoLigacoes();

        try {
            try (Reader reader = new InputStreamReader(new FileInputStream(arquivoJson), "UTF-8")) {
                JsonObject jsonObject = (JsonObject) Jsoner.deserialize(reader);
                this.missao = Missao.carregarMissao(jsonObject);
            } catch (IOException | JsonException e) {
                throw new IOException("Erro ao carregar o arquivo JSON da missão", e);
            }

        } catch (Exception e) {
            throw new IOException("Erro ao carregar a missão", e);
        }
    }

    public void iniciarTurnoToCruz() {
        mapa.atualizarPesoLigacoes();
        System.out.println("\n=== Turno de Tó Cruz ===");

        mapa.listarInimigos();
        System.out.println("\n");
        mapa.listarItens();
        System.out.println("\n");

        Divisao divisaoAlvo = mapa.obterDivisao(missao.getObjetivo().getDivisaoAlvo());
        if (!missao.getObjetivo().isConcluido()) {
            mapa.mostrarCaminhoParaObjetivo(toCruz.getDivisaoAtual(), divisaoAlvo);
        } else {
            mapa.mostrarCaminhoParaObjetivo(toCruz.getDivisaoAtual(), mapa.obterSaida());
        }
        mapa.caminhoParaKit(toCruz.getDivisaoAtual());

        Divisao divisaoAtual = mapa.obterDivisao(toCruz.getDivisaoAtual().getNome());
        System.out.println("Você está na divisão: " + divisaoAtual.getNome());

        if (!divisaoAtual.getItens().isEmpty()) {
            System.out.println("Itens disponíveis nesta sala:");
            for (Item item : divisaoAtual.getItens()) {
                System.out.println("- " + item);
            }
        }

        if (!divisaoAtual.getInimigos().isEmpty()) {
            System.out.println("Inimigos presentes nesta sala:");
            for (Inimigo inimigo : divisaoAtual.getInimigos()) {
                System.out.println("- " + inimigo.getNome() + " (HP: " + inimigo.getHp() + ")");
            }
        }

        if (getMissao().getObjetivo().getDivisaoAlvo().equalsIgnoreCase(divisaoAtual.getNome())) {
            System.out.println("Aviso: Você encontrou o objetivo!");
        }
    }

    public void finalizarTurnoToCruz() {
        System.out.println("Fim do turno de Tó Cruz...");
        iniciarTurnoInimigos();
    }

    public void iniciarTurnoInimigos() {
        System.out.println("\n=== Turno dos Inimigos ===");

        Divisao divisaoAtual = mapa.obterDivisao(toCruz.getDivisaoAtual().getNome());
        ArrayUnorderedList<Inimigo> inimigosNaSala = divisaoAtual.getInimigos();

        for (Inimigo inimigo : inimigosNaSala) {
            System.out.println(inimigo.getNome() + " atacou Tó Cruz!");
            toCruz.levarDano(inimigo.getDano());
            if (toCruz.morto()) {
                System.out.println("Tó Cruz foi derrotado. Fim do jogo.");
                finalizarJogo();
                return;
            }
        }
        System.out.println("\nInimigos em outras salas estão se movendo...");
        moverInimigos(divisaoAtual);

        finalizarTurnoInimigos();
    }

    public void finalizarTurnoInimigos() {
        System.out.println("Fim do turno dos inimigos.");
        turnoCount++;
        iniciarTurnoToCruz();
    }

    public void iniciarCombate() {
        Divisao divisaoAtual = mapa.obterDivisao(toCruz.getDivisaoAtual().getNome());
        ArrayUnorderedList<Inimigo> inimigosNaSala = divisaoAtual.getInimigos();

        if (inimigosNaSala.isEmpty()) {
            System.out.println("Não há inimigos para atacar nesta sala.");
            return;
        }

        System.out.println("Luta iniciada! Inimigos presentes:");
        for (Inimigo inimigo : inimigosNaSala) {
            System.out.println("- " + inimigo.getNome() + " (HP: " + inimigo.getHp() + ")");
        }

        System.out.println("\n--> To Cruz ataca os inimigos");
        ArrayUnorderedList<Inimigo> eliminados = new ArrayUnorderedList<>();
        for (Inimigo inimigo : inimigosNaSala) {
            toCruz.atacar(inimigo);
            System.out.println("Tó Cruz atacou " + inimigo.getNome());
            if (inimigo.morto()) {
                System.out.println(inimigo.getNome() + " foi eliminado!");
                eliminados.addToRear(inimigo);
            }
        }

        for (Inimigo inimigo : eliminados) {
            inimigosNaSala.remove(inimigo);
        }

        if (inimigosNaSala.isEmpty()) {
            System.out.println("Todos os inimigos na sala foram eliminados!");
        } else {
            System.out.println("Ainda restam inimigos na sala!");
        }
        finalizarTurnoToCruz();
    }

    public void resgatarItem() {
        ArrayUnorderedList<Item> itensDisponiveis = mapa.obterDivisao(toCruz.getDivisaoAtual().getNome()).getItens();

        if (itensDisponiveis.size() > 0) {
            System.out.println("\nItens disponíveis para resgatar:");

            for (int i = 0; i < itensDisponiveis.size(); i++) {
                System.out.println((i + 1) + " - " + itensDisponiveis.get(i));
            }

            System.out.println("\n|---/Escolha uma opção/---|");
            System.out.println("|    1. Usar Item         |");
            System.out.println("|    2. Descartar Item    |");
            System.out.println("|    3. Voltar            |");
            System.out.println("|-------------------------|");
            System.out.print("Selecione uma opção: ");

            String comando = scanner.nextLine();

            switch (comando) {
                case "1":
                    usarItem(itensDisponiveis);
                    break;
                case "2":
                    descartarItem(itensDisponiveis);
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Comando inválido!");
            }
        } else {
            System.out.println("Não há itens disponíveis nesta divisão.");
        }
    }

    private void usarItem(ArrayUnorderedList<Item> itensDisponiveis) {
        System.out.print("Escolha o item para usar (1-" + itensDisponiveis.size() + "): ");
        String comando = scanner.nextLine();
        try {
            int escolha = Integer.parseInt(comando) - 1;
            if (escolha >= 0 && escolha < itensDisponiveis.size()) {
                Item itemEscolhido = itensDisponiveis.get(escolha);

                if (itemEscolhido instanceof Kit) {
                    toCruz.addKitMochila((Kit) itemEscolhido);
                    System.out.println("Kit médico adicionado à mochila.");
                } else if (itemEscolhido instanceof Colete) {
                    String resultado = toCruz.usarColete((Colete) itemEscolhido);
                    System.out.println(resultado);
                }
                itensDisponiveis.remove(itemEscolhido);
            } else {
                System.out.println("Item inválido.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Opção inválida.");
        }
    }

    private void descartarItem(ArrayUnorderedList<Item> itensDisponiveis) {
        System.out.print("Escolha o item para descartar (1-" + itensDisponiveis.size() + "): ");
        String comando = scanner.nextLine();

        try {
            int escolha = Integer.parseInt(comando) - 1;
            if (escolha >= 0 && escolha < itensDisponiveis.size()) {
                Item itemEscolhido = itensDisponiveis.get(escolha);
                System.out.println("Item descartado: " + itemEscolhido);
                itensDisponiveis.remove(itemEscolhido);
            } else {
                System.out.println("Item inválido.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Opção inválida.");
        }
    }

    public void finalizarJogo() {
        jogoAtivo = false;
    }

    public void usarKit() {
        if (!toCruz.getMochila().isEmpty()) {
            String resultado = toCruz.usarKit();
            System.out.println(resultado);
            finalizarTurnoToCruz();
        } else {
            System.out.println("Não há kits disponíveis na mochila.");
        }
    }

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
                        System.out.println(inimigo.getNome() + " se moveu para a sala de Tó Cruz!");
                        toCruz.levarDano(inimigo.getDano());
                        if (toCruz.morto()) {
                            System.out.println("Tó Cruz foi derrotado. Fim do jogo.");
                            finalizarJogo();
                            return;
                        }
                        System.out.println("Tó Cruz sofreu dano de " + inimigo.getNome() + "!");
                    }
                }
            }
        }
        mapa.atualizarPesoLigacoes();
    }

    private void moverJogador(Divisao novaDivisao) {
        Divisao divisaoAtual = mapa.obterDivisao(toCruz.getDivisaoAtual().getNome());
        Divisao novaDivisaoObj = mapa.obterDivisao(novaDivisao.getNome());

        if (divisaoAtual == null || novaDivisaoObj == null) {
            System.out.println("Divisão inválida.");
            return;
        }

        if (!divisaoAtual.getInimigos().isEmpty()) {
            System.out.println("Tó Cruz não pode sair enquanto houver inimigos!");
            return;
        }

        if (!mapa.verificarLigacao(divisaoAtual, novaDivisaoObj)) {
            System.out.println("A divisão para qual se quer mover não está disponivel!");
            return;
        }

        toCruz.setDivisaoAtual(novaDivisao);
        System.out.println("Tó Cruz se moveu para: " + novaDivisao);

        if (!novaDivisaoObj.getItens().isEmpty()) {
            System.out.println("Aviso: Itens disponíveis na sala.");
        }

        if (missao.getObjetivo().getDivisaoAlvo().equals(novaDivisao.getNome())) {
            System.out.println("Aviso: O objetivo está nesta sala!");
        }

        if (!novaDivisaoObj.getInimigos().isEmpty()) {
            System.out.println("Aviso: Há inimigos nesta sala!");
        }

        finalizarTurnoToCruz();
    }

    public void moverToCruz() {
        System.out.println("\nDivisões disponíveis para se mover:");

        Divisao divisaoAtual = mapa.obterDivisao(toCruz.getDivisaoAtual().getNome());
        if (divisaoAtual == null) {
            System.out.println("Erro: Divisão atual inválida.");
            return;
        }

        ArrayUnorderedList<Divisao> divisoesValidas = new ArrayUnorderedList<>();
        for (int i = 0; i < mapa.getNetwork().size(); i++) {
            Divisao divisaoPotencial = mapa.getNetwork().getVertex(i);
            if (mapa.getNetwork().verificarLigacao(divisaoAtual, divisaoPotencial)) {
                divisoesValidas.addToRear(divisaoPotencial);
            }
        }

        if (divisoesValidas.isEmpty()) {
            System.out.println("Não há divisões disponíveis para se mover.");
            return;
        }

        for (int i = 0; i < divisoesValidas.size(); i++) {
            System.out.println("|   " + (i + 1) + " -> " + divisoesValidas.get(i).getNome());
        }

        System.out.print("\nSelecione uma opcao: ");
        String comando = scanner.nextLine();

        try {
            int escolha = Integer.parseInt(comando) - 1;
            if (escolha >= 0 && escolha < divisoesValidas.size()) {
                Divisao divisaoEscolhida = divisoesValidas.get(escolha);
                moverJogador(divisaoEscolhida);
            } else {
                System.out.println("Divisão inválida.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Opção inválida.");
        }
    }

    public void iniciarJogo() {
        System.out.println("A iniciar jogo...");
        escolherEntrada(scanner);
        exibirStatus();
    }

    /**
     * Permite ao jogador escolher uma divisão de entrada para iniciar o jogo.
     * 
     * @param scanner
     */
    public void escolherEntrada(Scanner scanner) {
        System.out.println("\nEscolha uma divisão de entrada:");

        NetworkExtension<Divisao> grafo = mapa.getNetwork();
        for (int i = 0; i < grafo.size(); i++) {
            Divisao divisao = grafo.getVertex(i);
            if (divisao.isEntradaSaida()) {
                System.out.println("|   " + (i + 1) + " -> " + divisao.getNome());
            }
        }

        while (true) {
            System.out.print("\nDigite o número da divisão escolhida: ");
            String comando = scanner.nextLine();

            try {
                int escolha = Integer.parseInt(comando) - 1;
                if (escolha >= 0 && escolha < grafo.size()) {
                    Divisao divisaoEscolhida = grafo.getVertex(escolha);
                    if (divisaoEscolhida.isEntradaSaida()) {
                        toCruz.setDivisaoAtual(divisaoEscolhida);
                        System.out.println("Tó Cruz começará na divisão: " + divisaoEscolhida.getNome());

                        if (!divisaoEscolhida.getInimigos().isEmpty()) {
                            System.out.println("Inimigos presentes nesta sala:");
                            for (Inimigo inimigo : divisaoEscolhida.getInimigos()) {
                                System.out.println("- " + inimigo.getNome() + " (HP: " + inimigo.getHp() + ")");
                            }
                        }
                        break;
                    } else {
                        System.out.println("Essa divisão não é válida como entrada/saída.");
                    }
                } else {
                    System.out.println("Escolha inválida. Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número.");
            }
        }
    }

    /**
     * Verifica se o jogador alcançou o objetivo da missão.
     * 
     * @param divisaoAtual
     */
    public void verificarProgresso(Divisao divisaoAtual) {
        Divisao divisaoAtual1 = mapa.obterDivisao(divisaoAtual.getNome());

        if (divisaoAtual1 == null) {
            System.out.println("Divisão atual inválida.");
            return;
        }

        if (!missao.getObjetivo().isConcluido()
                && divisaoAtual1.getNome().equalsIgnoreCase(missao.getObjetivo().getDivisaoAlvo())) {
            if (!divisaoAtual1.getInimigos().isEmpty()) {
                System.out.println("Há inimigos nesta sala! Elimine-os antes de interagir com o objetivo.");
            } else {
                System.out.println("Você alcançou a divisão-alvo: " + missao.getObjetivo().getDivisaoAlvo());
                missao.getObjetivo().concluir();
                missao.setObjetivoConcluido(true);
            }
        } else if (missao.getObjetivo().isConcluido() && divisaoAtual1.isEntradaSaida()) {
            System.out.println("Você extraiu o objetivo e saiu pela divisão: " + divisaoAtual1);
            missao.completar();
            finalizarJogo();
        } else if (missao.getObjetivo().isConcluido() && !divisaoAtual1.isEntradaSaida()) {
            System.out.println("Procure uma saída válida!");
        } else {
            System.out.println("Continue a explorar...");
        }
    }

    public void exibirStatus() {
        Divisao divisaoAlvo = mapa.obterDivisao(missao.getObjetivo().getDivisaoAlvo());
        mapa.mostrarCaminhoParaObjetivo(toCruz.getDivisaoAtual(), divisaoAlvo);
        mapa.caminhoParaKit(toCruz.getDivisaoAtual());
        System.out.println("\nStatus do Jogo:");
        mapa.listarInimigos();
        System.out.println("\n");
        mapa.listarItens();
        System.out.println("\n");
        System.out.println(missao);
        System.out.println(toCruz);
    }

    protected int getTurnoCount() {
        return turnoCount;
    }

    protected ToCruz getToCruz() {
        return toCruz;
    }

    protected Missao getMissao() {
        return missao;
    }

    /**
     * Retorna o mapa do jogo.
     * 
     * @return
     */
    protected Mapa getMapa() {
        return mapa;
    }

    /**
     * Verifica se o jogo está ativo.
     * 
     * @return
     */
    protected boolean isJogoAtivo() {
        return jogoAtivo;
    }

    // ************************************//
    // MÉTODOS MODO AUTOMÁTICO //
    // ************************************//

    public void iniciarModoAutomatico() {
        Divisao entrada = escolherEntrada();
        toCruz.setDivisaoAtual(entrada);
        System.out.println("Entrada escolhida: " + entrada.getNome());

        while (jogoAtivo) {
            turnoToCruzAutomatico();
            if (missao.getObjetivo().isConcluido() && toCruz.getDivisaoAtual().isEntradaSaida()) {
                finalizarJogo();
            } else {
                turnoInimigosAutomatico();
            }
        }
    }

    /**
     * Escolhe a melhor entrada para a missão
     * 
     * @return a melhor entrada
     */
    private Divisao escolherEntrada() {
        Divisao objetivoDivisao = mapa.obterDivisao(missao.getObjetivo().getDivisaoAlvo());
        Divisao melhorEntrada = mapa.escolherMelhorEntrada(objetivoDivisao);

        if (melhorEntrada == null) {
            System.out.println("Não foi possível encontrar uma entrada válida.");
        }

        return melhorEntrada;
    }

    public void turnoToCruzAutomatico() {
        mapa.atualizarPesoLigacoes();
        System.out.println("\n--> Truno de To Cruz");
        Divisao divisaoAlvo = mapa.obterDivisao(missao.getObjetivo().getDivisaoAlvo());
        if (!missao.getObjetivo().isConcluido()) {
            mapa.mostrarCaminhoParaObjetivo(toCruz.getDivisaoAtual(), divisaoAlvo);
        } else {
            mapa.mostrarCaminhoParaObjetivo(toCruz.getDivisaoAtual(), mapa.escolherMelhorSaida(toCruz.getDivisaoAtual()));
        }
        System.out.println("To Cruz está na divisão: " + toCruz.getDivisaoAtual().getNome());

        if (!toCruz.getDivisaoAtual().getInimigos().isEmpty()) {
            System.out.println("Inimigos presentes nesta divisão: ");
            for (Inimigo inimigo : toCruz.getDivisaoAtual().getInimigos()) {
                System.out.println("- " + inimigo.getNome() + " (HP: " + inimigo.getHp() + ")");
            }
            iniciarCombateAutomatico();
        }

        if (!toCruz.getDivisaoAtual().getItens().isEmpty()) {
            System.out.println("Itens disponíveis nesta divisão: ");
            ArrayUnorderedList<Item> itensDisponiveis = mapa.obterDivisao(toCruz.getDivisaoAtual().getNome()).getItens();
            for (Item item : itensDisponiveis) {
                System.out.println("- " + item);
                usarItem(itensDisponiveis);
            }

        }
        if (!missao.getObjetivo().isConcluido()) {
            moverParaObjetivoAutomatico();
        } else {
            moverParaSaidaAutomatico();
        }

    }

    public void finalizarTurnoToCruzAutomatico() {
        if (jogoAtivo = true) {
            System.out.println("Fim do turno de To Cruz");
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
        System.out.println("\n--> Turno dos Inimigos");

        moverInimigos(toCruz.getDivisaoAtual());

        if (toCruz.morto()) {
            System.out.println("To Cruz morreu! Fim de jogo.");
            finalizarJogo();
        } else {
            finalizarTurnoInimigosAutomatico();
        }
    }

    public void finalizarTurnoInimigosAutomatico() {
        System.out.println("Fim do turno dos inimigos.");
        turnoToCruzAutomatico();
    }

    public void moverParaObjetivoAutomatico() {
        Divisao objetivoDivisao = mapa.obterDivisao(missao.getObjetivo().getDivisaoAlvo());

        Iterator<Divisao> caminho = mapa.obterCaminhoModoAutomatico(toCruz.getDivisaoAtual(), objetivoDivisao);

        if (caminho.hasNext()) {
            caminho.next();
            if (caminho.hasNext()) {
                Divisao proximaDivisao = caminho.next();

                if (!toCruz.getDivisaoAtual().equals(proximaDivisao)) {
                    toCruz.setDivisaoAtual(proximaDivisao);
                    System.out.println("To Cruz moveu-se para " + proximaDivisao.getNome());
                }

                if (proximaDivisao.equals(objetivoDivisao)) {
                    System.out.println("To Cruz alcançou o objetivo!");
                    verificarProgresso(proximaDivisao);
                }
                finalizarTurnoToCruzAutomatico();
            }
        }
    }

    public void moverParaSaidaAutomatico() {
        Divisao saida = mapa.escolherMelhorSaida(toCruz.getDivisaoAtual());

        Iterator<Divisao> caminho = mapa.obterCaminhoModoAutomatico(toCruz.getDivisaoAtual(), saida);
        if (caminho.hasNext()) {
            caminho.next();
            if (caminho.hasNext()) {
                Divisao proximaDivisao = caminho.next();
                toCruz.setDivisaoAtual(proximaDivisao);
                System.out.println("To Cruz moveu-se para " + proximaDivisao.getNome());
                verificarProgresso(toCruz.getDivisaoAtual());
                if (!missao.isCompleta()) {
                    finalizarTurnoToCruzAutomatico();
                } else {
                    finalizarJogo();
                }
            }
        }
    }

    public void iniciarCombateAutomatico() {
        Divisao divisaoAtual = mapa.obterDivisao(toCruz.getDivisaoAtual().getNome());
        ArrayUnorderedList<Inimigo> inimigosNaSala = divisaoAtual.getInimigos();

        if (inimigosNaSala.isEmpty()) {
            throw new EmptyCollectionException("null");
        }

        System.out.println("Luta iniciada! Inimigos presentes:");
        for (Inimigo inimigo : inimigosNaSala) {
            System.out.println("- " + inimigo.getNome() + " (HP: " + inimigo.getHp() + ")");
        }

        System.out.println("\n--> To Cruz ataca os inimigos");
        ArrayUnorderedList<Inimigo> eliminados = new ArrayUnorderedList<>();
        for (Inimigo inimigo : inimigosNaSala) {
            toCruz.atacar(inimigo);
            System.out.println("Tó Cruz atacou " + inimigo.getNome());
            if (inimigo.morto()) {
                System.out.println(inimigo.getNome() + " foi eliminado!");
                eliminados.addToRear(inimigo);
            }
        }

        for (Inimigo inimigo : eliminados) {
            inimigosNaSala.remove(inimigo);
        }

        if (inimigosNaSala.isEmpty()) {
            System.out.println("Todos os inimigos na sala foram eliminados!");
        } else {
            System.out.println("Ainda restam inimigos na sala!");
        }
        finalizarTurnoToCruzAutomatico();
    }
}
