package Jogo;
import java.io.IOException;
import java.util.Scanner;

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
            System.out.println("|    2 - Sair                |");
            System.out.println("|----------------------------|");
            System.out.print("Selecione uma opção: ");

            String comando = scanner.nextLine();

            switch (comando) {
                case "1":
                    selecionarMissao();
                    break;
                case "2":
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
                // Chama o modo automático
                break;
            default:
                System.out.println("Comando inválido!");
        }
    }

    private void iniciarModoManual(String arquivoJson) throws IOException {
        jogo = new Jogo(arquivoJson);
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
                    jogo.moverToCruz();
                    break;
                case "2":
                    jogo.usarKit();
                    break;
                case "3":
                    jogo.resgatarItem();
                    break;
                case "4":
                    jogo.exibirStatus();
                    break;
                case "5":
                    jogo.iniciarCombate();
                    break;
                case "6":
                    jogo.getMapa().verMapa();
                    break;
                case "7":
                    jogo.verificarProgresso(jogo.getToCruz().getDivisaoAtual());
                    break;
                case "8":
                    jogo.finalizarTurnoToCruz();
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
    
}
