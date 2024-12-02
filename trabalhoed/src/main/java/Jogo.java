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

    public Jogo(String arquivoJson) throws IOException {
        this.mapa = new Mapa();
        this.mapa.carregarMapa(arquivoJson);
        this.toCruz = new ToCruz(20, null);

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

        this.jogoAtivo = true;
        this.scanner = new Scanner(System.in); 
        this.jogoAtivo = true;
    }

    public void iniciar() {
        if (missao != null) {
            missao.iniciar();
        } else {
            System.out.println("Erro: Nenhuma missão carregada.");
        }
    }


    //Podre
    public void combaterInimigos() {
        Divisao divisaoAtual = mapa.obterDivisao(toCruz.getDivisaoAtual());

        if (divisaoAtual == null) {
            System.out.println("Divisão atual inválida.");
            return;
        }

        ArrayUnorderedList<Inimigo> inimigos = divisaoAtual.getInimigos();
        if (inimigos.isEmpty()) {
            System.out.println("Nenhum inimigo na divisão atual.");
            return;
        }

        System.out.println("Inimigos encontrados na divisão " + divisaoAtual.getNome() + ":");
        for (Inimigo inimigo : inimigos) {
            System.out.println(" - " + inimigo);
        }

        while (!inimigos.isEmpty() && !toCruz.estaMorto()) {
            Inimigo inimigo = inimigos.first(); 
            System.out.println("Tó Cruz ataca " + inimigo.getNome());
            toCruz.atacar(inimigo);

            if (inimigo.estaMorto()) {
                System.out.println(inimigo.getNome() + " foi derrotado!");
                inimigos.removeFirst();
            } else {
                System.out.println(inimigo.getNome() + " contra ataca!");
                inimigo.atacar(toCruz);
            }

            if (toCruz.estaMorto()) {
                System.out.println("Tó Cruz foi derrotado! Fim de jogo.");
                finalizarJogo();
                return;
            }
        }

        if (inimigos.isEmpty()) {
            System.out.println("Todos os inimigos foram derrotados na divisão " + divisaoAtual.getNome() + ".");
        }
    }

    public void moverJogador(String novaDivisao) {
        Divisao divisaoAtual = mapa.obterDivisao(toCruz.getDivisaoAtual());
        Divisao novaDivisaoObj = mapa.obterDivisao(novaDivisao);
        if (!mapa.verificarLigacao(divisaoAtual, novaDivisaoObj)) {
            return;
        }

        toCruz.setDivisaoAtual(novaDivisao);
        System.out.println("Tó Cruz se moveu para: " + novaDivisao);

        Divisao novaDivisaoAtual = mapa.obterDivisao(novaDivisao);
        if (!novaDivisaoAtual.getItens().isEmpty()) {
            System.out.println("Aviso: Há itens disponíveis nesta sala!");
        }
    
        if (missao.getObjetivo().getDivisaoAlvo().equalsIgnoreCase(novaDivisao)) {
            System.out.println("Aviso: O objetivo está nesta sala!");
        }
    }

    public void proximaIteracao() {
        System.out.println("\nNova iteração:");
        mapa.moverInimigos();
    }

    public void verificarEstadoJogo() {
        if (missao.isCompleta()) {
            System.out.println("Missão completada com sucesso!");
            finalizarJogo();
        } else {
            System.out.println("Missão ainda em andamento...");
        }
    }

    public void verificarProgresso() {
        missao.verificarProgresso(toCruz.getDivisaoAtual());
    }

    public void finalizarJogo() {
        jogoAtivo = false;
        System.out.println("Fim do jogo.");
    }

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
    
    public void exibirStatus() {
        System.out.println("\nStatus do Jogo:");
        System.out.println(missao);
        System.out.println(toCruz);
    }

    public ToCruz getToCruz() {
        return toCruz;
    }

    public Missao getMissao() {
        return missao;
    }

    public Mapa getMapa() {
        return mapa;
    }

    public boolean isJogoAtivo() {
        return jogoAtivo;
    }
}
