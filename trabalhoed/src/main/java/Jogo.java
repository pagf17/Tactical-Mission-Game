import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import java.util.Scanner;

import Estruturas.ArrayUnorderedList;

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
        System.out.println("\n=== Turno de Tó Cruz ===");

        Divisao divisaoAtual = mapa.obterDivisao(toCruz.getDivisaoAtual());
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

        Divisao divisaoAtual = mapa.obterDivisao(toCruz.getDivisaoAtual());
        ArrayUnorderedList<Inimigo> inimigosNaSala = divisaoAtual.getInimigos();

        for (Inimigo inimigo : inimigosNaSala) {
            System.out.println(inimigo.getNome() + " atacou Tó Cruz!");
            toCruz.levarDano(inimigo.getDano());
            if (toCruz.estaMorto()) {
                System.out.println("Tó Cruz foi derrotado. Fim do jogo.");
                finalizarJogo();
                return;
            }
        }
        System.out.println("\nInimigos em outras salas estão se movendo...");
        mapa.moverInimigos(toCruz.getDivisaoAtual());

        finalizarTurnoInimigos();
    }

    public void finalizarTurnoInimigos() {
        System.out.println("Fim do turno dos inimigos.");
        turnoCount++;
        iniciarTurnoToCruz();
    }

    public void iniciarCombate() {
        Divisao divisaoAtual = mapa.obterDivisao(toCruz.getDivisaoAtual());
        ArrayUnorderedList<Inimigo> inimigosNaSala = divisaoAtual.getInimigos();

        if (inimigosNaSala.isEmpty()) {
            System.out.println("Não há inimigos para atacar nesta sala.");
            return;
        }

        System.out.println("Confronto iniciado! Inimigos presentes:");
        for (Inimigo inimigo : inimigosNaSala) {
            System.out.println("- " + inimigo.getNome() + " (HP: " + inimigo.getHp() + ")");
        }

        System.out.println("\n=== Fase do Jogador ===");
        ArrayUnorderedList<Inimigo> eliminados = new ArrayUnorderedList<>();
        for (Inimigo inimigo : inimigosNaSala) {
            toCruz.atacar(inimigo);
            System.out.println("Tó Cruz atacou " + inimigo.getNome());
            if (inimigo.estaMorto()) {
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
        ArrayUnorderedList<Item> itensDisponiveis = mapa.obterDivisao(toCruz.getDivisaoAtual()).getItens();

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

                if (itemEscolhido.getTipo().equalsIgnoreCase("kit de vida")) {
                    toCruz.addKitMochila(itemEscolhido);
                    System.out.println("Kit médico adicionado à mochila.");
                } else if (itemEscolhido.getTipo().equalsIgnoreCase("colete")) {
                    toCruz.usarColete();
                    System.out.println("Colete usado com sucesso.");
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

    // OK?
    public void moverJogador(String novaDivisao) {
        Divisao divisaoAtual = mapa.obterDivisao(toCruz.getDivisaoAtual());
        Divisao novaDivisaoObj = mapa.obterDivisao(novaDivisao);

        if (divisaoAtual == null || novaDivisaoObj == null) {
            System.out.println("Divisão inválida.");
            return;
        }

        if (!divisaoAtual.getInimigos().isEmpty()) {
            System.out.println("Você não pode sair enquanto houver inimigos!");
            return;
        }

        if (!mapa.verificarLigacao(divisaoAtual, novaDivisaoObj)) {
            System.out.println("Movimento inválido. Divisão não conectada.");
            return;
        }

        toCruz.setDivisaoAtual(novaDivisao);
        System.out.println("Tó Cruz se moveu para: " + novaDivisao);

        if (!novaDivisaoObj.getItens().isEmpty()) {
            System.out.println("Aviso: Itens disponíveis na sala.");
        }

        if (missao.getObjetivo().getDivisaoAlvo().equalsIgnoreCase(novaDivisao)) {
            System.out.println("Aviso: O objetivo está nesta sala!");
        }

        if (!novaDivisaoObj.getInimigos().isEmpty()) {
            System.out.println("Aviso: Há inimigos nesta sala!");
        }

        finalizarTurnoToCruz();
    }

    public void iniciarJogo() {
        System.out.println("Iniciando jogo...");
        escolherEntrada(scanner);
        exibirStatus();
    }

    // OK
    public void escolherEntrada(Scanner scanner) {
        ArrayUnorderedList<String> entradasSaidas = missao.getEntradasSaidas();

        System.out.println("\nEscolha a divisão de entrada para começar:");
        for (int i = 0; i < entradasSaidas.size(); i++) {
            System.out.println("|   " + (i + 1) + " -> " + entradasSaidas.get(i));
        }

        while (true) {
            System.out.print("\nDigite o número da divisão escolhida: ");
            String comando = scanner.nextLine();

            try {
                int escolha = Integer.parseInt(comando) - 1;
                if (escolha >= 0 && escolha < entradasSaidas.size()) {
                    String divisaoEscolhida = entradasSaidas.get(escolha);
                    toCruz.setDivisaoAtual(divisaoEscolhida);
                    System.out.println("Tó Cruz começará na divisão: " + divisaoEscolhida);
                    break;
                } else {
                    System.out.println("Escolha inválida. Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número.");
            }
        }
    }

    // OK
    public void verificarProgresso(String divisaoAtual) {
        Divisao divisaoAtualObj = mapa.obterDivisao(divisaoAtual);

        if (!missao.getObjetivo().isConcluido()
                && divisaoAtual.equalsIgnoreCase(missao.getObjetivo().getDivisaoAlvo())) {
            if (!divisaoAtualObj.getInimigos().isEmpty()) {
                System.out.println("Há inimigos nesta sala! Elimine-os antes de interagir com o objetivo.");
            } else {
                System.out.println("Você alcançou a divisão-alvo: " + missao.getObjetivo().getDivisaoAlvo());
                missao.getObjetivo().concluir();
                missao.setObjetivoConcluido(true);
            }
        } else if (missao.getObjetivo().isConcluido() && missao.isSaidaValida(divisaoAtual)) {
            System.out.println("Você extraiu o objetivo e saiu pela divisão: " + divisaoAtual);
            missao.completar();
            finalizarJogo();
        } else if (missao.getObjetivo().isConcluido() && !missao.isSaidaValida(divisaoAtual)) {
            System.out.println("Procure uma saída válida!");
        } else {
            System.out.println("Continue a explorar...");
        }
    }

    // OK
    public void exibirStatus() {
        System.out.println("\nStatus do Jogo:");
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

    protected Mapa getMapa() {
        return mapa;
    }

    protected boolean isJogoAtivo() {
        return jogoAtivo;
    }
}
