import java.io.IOException;
import java.util.Scanner;

import Estruturas.ArrayUnorderedList;

public class Menu {
    private Scanner scanner;
    private Jogo jogo;

    public Menu() {
        this.scanner = new Scanner(System.in);
    }

    // Exibe o menu principal
    public void exibirMenu() {
        while (true) {
            System.out.println("=== Menu Principal ===");
            System.out.println("1 - Selecionar Missão");
            System.out.println("2 - Sair");

            String comando = scanner.nextLine();

            switch (comando) {
                case "1":
                    selecionarMissao(); // Carregar a missão selecionada
                    break;
                case "2":
                    System.out.println("Saindo do jogo...");
                    return; // Sai do jogo
                default:
                    System.out.println("Comando inválido!");
            }
        }
    }

    private void selecionarMissao() {
        try {
            // Exibe as missões disponíveis
            System.out.println("Escolha uma missão para jogar:");
            System.out.println("1 - Missão: Pata de Coelho (Versão 1)");
            System.out.println("2 - Missão: Pata de Coelho (Versão 2)");
            System.out.println("3 - Voltar");

            String comando = scanner.nextLine();

            switch (comando) {
                case "1":
                    iniciarJogo("missao.json");
                    break;
                case "2":
                    iniciarJogo("missao2.json");
                    break;
                case "3":
                    return; // Voltar ao menu principal
                default:
                    System.out.println("Comando inválido!");
            }

        } catch (IOException e) {
            System.out.println("Erro ao carregar a missão: " + e.getMessage());
        }
    }

    private void iniciarJogo(String arquivoJson) throws IOException {
        jogo = new Jogo(arquivoJson); // Inicia o jogo com o arquivo JSON e versão da missão
        jogo.iniciar();

        // Loop principal do jogo
        exibirMenuJogo();
    }

    private void exibirMenuJogo() {
        while (jogo.isJogoAtivo()) {
            System.out.println("\n=== Menu de Jogo ===");
            System.out.println("1 - Mover Jogador");
            System.out.println("2 - Usar Kit");
            System.out.println("3 - Itens");
            System.out.println("4 - Ver Status");
            System.out.println("5 - Desistir");

            String comando = scanner.nextLine();

            switch (comando) {
                case "1":
                    moverJogador();
                    break;
                case "2":
                    usarKit();
                    break;
                case "3":
                    resgatarItem();
                    break;
                case "4":
                    verStatus();
                    break;
                case "5":
                    desistir();
                    return;
                default:
                    System.out.println("Comando inválido!");
            }
        }
    }

    private void moverJogador() {
        System.out.println("\nDivisões disponíveis para mover-se:");

        ArrayUnorderedList<Divisao> divisoesValidas = jogo.getMapa()
                .obterDivisoesValidas(jogo.getToCruz().getDivisaoAtual());

        for (int i = 0; i < divisoesValidas.size(); i++) {
            System.out.println((i + 1) + " - " + divisoesValidas.get(i).getNome());
        }

        System.out.print("\nEscolha a divisão para onde mover: ");
        String comando = scanner.nextLine();

        try {
            int escolha = Integer.parseInt(comando) - 1;
            if (escolha >= 0 && escolha < divisoesValidas.size()) {
                Divisao divisaoEscolhida = divisoesValidas.get(escolha);
                jogo.moverJogador(divisaoEscolhida.getNome());
            } else {
                System.out.println("Divisão inválida.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Opção inválida.");
        }
    }

    private void usarKit() {
        if (!jogo.getToCruz().getMochila().isEmpty()) {  
            String resultado = jogo.getToCruz().usarKit(); 
            System.out.println(resultado);  
        } else {
            System.out.println("Não há kits disponíveis na mochila.");
        }
    }

    private void atacarInimigo() {
        System.out.println("Digite 'inimigo' para atacar um inimigo.");
        String comando = scanner.nextLine();
        if (comando.equalsIgnoreCase("inimigo")) {
            jogo.interagir("inimigo");
        } else {
            System.out.println("Comando inválido!");
        }
    }

    private void resgatarItem() {
        ArrayUnorderedList<Item> itensDisponiveis = jogo.getMapa()
                .obterDivisao(jogo.getToCruz().getDivisaoAtual()).getItens();
    
        if (itensDisponiveis.size() > 0) {
            System.out.println("\nItens disponíveis para resgatar:");
    
            for (int i = 0; i < itensDisponiveis.size(); i++) {
                System.out.println((i + 1) + " - " + itensDisponiveis.get(i));
            }
    
            // Apresenta opções de ação
            System.out.println("\nEscolha uma opção:");
            System.out.println("1 - Usar Item");
            System.out.println("2 - Descartar Item");
            System.out.println("3 - Voltar");
    
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
                    jogo.getToCruz().addKitMochila(itemEscolhido);  
                    System.out.println("Kit médico adicionado à mochila.");
                } else if (itemEscolhido.getTipo().equalsIgnoreCase("colete")) {
                    jogo.getToCruz().usarColete(); 
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
            int escolha = Integer.parseInt(comando) - 1;  // Ajusta a escolha para o índice
            if (escolha >= 0 && escolha < itensDisponiveis.size()) {
                Item itemEscolhido = itensDisponiveis.get(escolha);
                System.out.println("Item descartado: " + itemEscolhido);
                itensDisponiveis.remove(itemEscolhido);  // Remover item da divisão
            } else {
                System.out.println("Item inválido.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Opção inválida.");
        }
    }

    private void verStatus() {
        jogo.exibirStatus();
    }

    private void desistir() {
        System.out.println("Você desistiu da missão.");
        jogo.finalizarJogo();
    }
}
