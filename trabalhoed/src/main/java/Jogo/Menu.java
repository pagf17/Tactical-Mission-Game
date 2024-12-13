package Jogo;

import java.io.IOException;
import java.util.Scanner;

import com.github.cliftonlabs.json_simple.JsonObject;

import Estruturas.ArrayList;
import java.util.Iterator;

import Jogo.Resultados.ImportarResultados;
import Estruturas.ArrayUnorderedList;

public class Menu {
    private Scanner scanner;
    private Jogo jogo;

    public Menu() {
        this.scanner = new Scanner(System.in);
    }

    public void exibirMenu() {
        while (true) {
            System.out.println("\n|------/Menu Principal/------|");
            System.out.println("|    1 - Selecionar Missão   |");
            System.out.println("|    2 - Ver resultados      |");
            System.out.println("|    3 - Sair                |");
            System.out.println("|----------------------------|");
            System.out.print("Selecione uma opção: ");

            String comando = scanner.nextLine();

            switch (comando) {
                case "1":
                    selecionarMissao();
                    break;
                case "2":
                    mostrarResultados();
                    break;
                case "3":
                    System.out.println("Saindo do jogo...");
                    return;
                default:
                    System.out.println("Comando inválido!");
            }
        }
    }

    private void selecionarMissao() {
        try {
            System.out.println("\n|------/Escolha uma missão para jogar/------|");
            System.out.println("|   1. Missão: Pata de Coelho (Versão 1)    |");
            System.out.println("|   2. Missão: Pata de Coelho (Versão 2)    |");
            System.out.println("|   3. Voltar                               |");
            System.out.println("|-------------------------------------------|");
            System.out.print("Selecione uma opção: ");

            String comando = scanner.nextLine();

            switch (comando) {
                case "1":
                    selecionarModoDeJogo("missao.json");
                    break;
                case "2":
                    selecionarModoDeJogo("missao2.json");
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Comando inválido!");
            }

        } catch (IOException e) {
            System.out.println("Erro ao carregar a missão: " + e.getMessage());
        }
    }

    private void selecionarModoDeJogo(String arquivoJson) throws IOException {
        System.out.println("\n|-----/Selecione o Modo de Jogo/-----|");
        System.out.println("|    1. Modo Manual                 |");
        System.out.println("|    2. Modo Automático             |");
        System.out.println("|-----------------------------------|");
        System.out.print("Selecione uma opção: ");

        String comando = scanner.nextLine();

        switch (comando) {
            case "1":
                iniciarModoManual(arquivoJson);
                break;
            case "2":
                iniciarModoAutomatico(arquivoJson);
                break;
            default:
                System.out.println("Comando inválido!");
        }
    }

    private void iniciarModoAutomatico(String arquivoJson) throws IOException {
        jogo = new Jogo(arquivoJson, false);
        jogo.iniciarModoAutomatico();
    }

    private void iniciarModoManual(String arquivoJson) throws IOException {
        jogo = new Jogo(arquivoJson, true);
        escolherEntrada(scanner);
        jogo.iniciarJogo();

        while (jogo.isJogoAtivo()) {

            System.out.println("\n|--------/Menu de Jogo/--------|");
            System.out.println("|   1 - Mover Jogador          |");
            System.out.println("|   2 - Usar Kit               |");
            System.out.println("|   3 - Itens                  |");
            System.out.println("|   4 - Ver Status             |");
            System.out.println("|   5 - Atacar Inimigos        |");
            System.out.println("|   6 - Ver mapa               |");
            if (jogo.getMissao().getObjetivo().isConcluido()) {
                System.out.println("|   7 - Extrair                |");
            } else {
                System.out.println("|   7 - Interagir com Objetivo |");
            }
            System.out.println("|   8 - Finalizar Turno        |");
            System.out.println("|   9 - Desistir               |");
            System.out.println("|----------/Turno: " + jogo.getTurnoCount() + "/----------|");
            System.out.print("Selecione uma opção: ");

            String comando = scanner.nextLine();

            switch (comando) {
                case "1":
                    moverJogador();
                    break;
                case "2":
                    jogo.usarKit();
                    break;
                case "3":
                    resgatarItem();
                    break;
                case "4":
                    jogo.mostrarStatus();
                    break;
                case "5":
                    jogo.iniciarCombate();
                    break;
                case "6":
                    jogo.verMapa();
                    break;
                case "7":
                    jogo.verificarProgresso(jogo.getToCruz().getDivisaoAtual());
                    break;
                case "8":
                    jogo.finalizarTurnoToCruz();
                    System.out.println("Tó Cruz cedeu o turno.");
                    break;
                case "9":
                    System.out.println("To Cruz rendeu-se. Fim de jogo.");
                    jogo.finalizarJogo();
                    return;
                default:
                    System.out.println("Comando inválido!");
            }
        }
    }

    /**
     * Resgata um item da divisão atual.
     */
    public void resgatarItem() {
        ArrayUnorderedList<Item> itensDisponiveis = jogo.getMapa()
                .obterDivisao(jogo.getToCruz().getDivisaoAtual().getNome()).getItens();

        if (itensDisponiveis.size() > 0) {
            System.out.println("\nItens disponíveis para resgatar:");

            for (int i = 0; i < itensDisponiveis.size(); i++) {
                System.out.println((i + 1) + " - " + itensDisponiveis.get(i));
            }

            System.out.println("\n|---/Escolha uma opção/---|");
            System.out.println("|    1. Usar Item         |");
            System.out.println("|    2. Voltar            |");
            System.out.println("|-------------------------|");
            System.out.print("Selecione uma opção: ");

            String comando = scanner.nextLine();

            switch (comando) {
                case "1":
                    System.out.print("Escolha o item para usar (1-" + itensDisponiveis.size() + "): ");
                    String usarComando = scanner.nextLine();
                    try {
                        int opcao = Integer.parseInt(usarComando) - 1;
                        jogo.usarItemManual(itensDisponiveis, opcao);
                    } catch (NumberFormatException e) {
                        System.out.println("Opção inválida!");
                    }
                    break;
                case "2":
                    return;
                default:
                    System.out.println("Comando inválido!");
            }
        } else {
            System.out.println("Não há itens disponíveis nesta divisão.");
        }
    }

    /**
     * Método para jogador escolher a divisao para onde se quer mover.
     * Inicialmente é criado um iterador para obter as divisoes validas para se
     * mover.
     * Posteriormente essas divisoes são adicionadas a uma lista e depois
     * apresentadas ao jogador.
     */
    public void moverJogador() {
        System.out.println("\nDivisões disponiveis para se mover: ");

        Iterator<Divisao> divisoes = jogo.getMapa().obterDivisoesValidas(jogo.getToCruz().getDivisaoAtual());
        if (divisoes == null || !divisoes.hasNext()) {
            System.out.println("Não há divisões disponíveis para se mover.");
            return;
        }

        ArrayUnorderedList<Divisao> divisoesValidas = new ArrayUnorderedList<Divisao>();
        int opcao = 1;

        while (divisoes.hasNext()) {
            Divisao divisao = divisoes.next();
            System.out.println(opcao + " - " + divisao.getNome());
            divisoesValidas.addToRear(divisao);
            opcao++;
        }

        System.out.print("\nEscolha uma opção: ");
        String comando = scanner.nextLine();

        try {
            int selecao = Integer.parseInt(comando) - 1;
            if (selecao >= 0 && selecao < divisoesValidas.size()) {
                Divisao divisaoEscolhida = divisoesValidas.get(selecao);
                jogo.moverJogador(divisaoEscolhida);
            } else {
                System.out.println("Opção inválida!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Opção inválida!");
        }
    }

    /**
     * Permite ao jogador escolher uma divisão de entrada para iniciar o jogo.
     * 
     * @param scanner
     */
    public void escolherEntrada(Scanner scanner) {
        System.out.println("\nEscolha uma divisão de entrada:");

        Iterator<Divisao> entradas = jogo.getMapa().obterEntradas();
        int index = 1;
        while (entradas.hasNext()) {
            Divisao entrada = entradas.next();
            System.out.println("|" + index + " - " + entrada.getNome());
            index++;
        }

        while (true) {
            System.out.print("\nDigite o número da divisão escolhida: ");
            String opcao = scanner.nextLine();

            try {
                int escolha = Integer.parseInt(opcao) - 1;
                entradas = jogo.getMapa().obterEntradas();
                Divisao divisaoEscolhida = null;
                for (int i = 0; i <= escolha && entradas.hasNext(); i++) {
                    divisaoEscolhida = entradas.next();
                }

                if (divisaoEscolhida != null && divisaoEscolhida.isEntradaSaida()) {
                    jogo.definirEntrada(divisaoEscolhida);
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
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número.");
            }
        }
    }

    public void mostrarResultados() {
        Scanner scanner = new Scanner(System.in);
        ImportarResultados importarResultados = new ImportarResultados();
        importarResultados.importarResultados("resultados.json");

        ArrayList<JsonObject> resultados = importarResultados.getResultados();
        if (resultados.isEmpty()) {
            System.out.println("Nenhum resultado encontrado.");
            return;
        }

        ArrayList<String> missoes = new ArrayList<>();
        for (JsonObject resultado : resultados) {
            String codMissao = (String) resultado.get("codMissao");
            if (!missoes.contains(codMissao)) {
                missoes.add(codMissao);
            }
        }

        System.out.println("\n|--> Selecione uma missão para ver os resultados:");
        for (int i = 0; i < missoes.size(); i++) {
            System.out.println("|" + (i + 1) + " - " + missoes.get(i));
        }
        System.out.print("Selecione uma opção: ");
        int opcao = scanner.nextInt();

        if (opcao < 1 || opcao > missoes.size()) {
            System.out.println("Opção inválida.");
            return;
        }
        String missaoSelecionada = missoes.get(opcao - 1);

        ArrayList<Integer> versoes = new ArrayList<>();
        for (JsonObject resultado : resultados) {
            String nomeMissao = resultado.get("codMissao").toString();
            if (nomeMissao.equals(missaoSelecionada)) {
                int versao = Integer.parseInt(resultado.get("versao").toString());
                if (!versoes.contains(versao)) {
                    versoes.add(versao);
                }
            }
        }

        System.out.println("|--> Selecione uma versão para a missão '" + missaoSelecionada + "':");
        for (int i = 0; i < versoes.size(); i++) {
            System.out.println("|" + (i + 1) + ". Versão " + versoes.get(i));
        }
        System.out.print("Escolha (número): ");
        int escolhaVersao = scanner.nextInt();

        if (escolhaVersao < 1 || escolhaVersao > versoes.size()) {
            System.out.println("Opção inválida.");
            return;
        }
        int versaoSelecionada = versoes.get(escolhaVersao - 1);

        ArrayList<JsonObject> resultadosFiltrados = importarResultados.filtrarResultados(missaoSelecionada,
                versaoSelecionada);
        if (resultadosFiltrados.isEmpty()) {
            System.out.println("Nenhum resultado encontrado.");
            return;
        }

        System.out.println("Resultados da missão " + missaoSelecionada + " versão " + versaoSelecionada + ":");
        for (JsonObject resultado : resultadosFiltrados) {
            System.out.println("  HP Final: " + resultado.get("hpFinal"));
            System.out.println("  Extraído: " + resultado.get("extraido"));
            System.out.println("  Trajeto: " + resultado.get("trajeto"));
            System.out.println();
        }
    }
}
