import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import Estruturas.ArrayUnorderedList;

public class Jogo {
    private Mapa mapa; // O mapa do jogo
    private Missao missao; // A missão atual
    private ToCruz toCruz; // O jogador (ToCruz)
    private boolean jogoAtivo; // Flag para verificar se o jogo está ativo

    public Jogo(String arquivoJson) throws IOException {
        this.mapa = new Mapa();
        this.mapa.carregarMapa(arquivoJson);

        try {
            try (Reader reader = new InputStreamReader(new FileInputStream(arquivoJson), "UTF-8")) {
                JsonObject jsonObject = (JsonObject) Jsoner.deserialize(reader);
                this.missao = Missao.carregarMissao(jsonObject); // Carregar a missão usando o JSON carregado
            } catch (IOException | JsonException e) {
                throw new IOException("Erro ao carregar o arquivo JSON da missão", e);
            }

        } catch (Exception e) {
            throw new IOException("Erro ao carregar a missão", e);
        }

        this.toCruz = new ToCruz(20, "Garagem");
        this.jogoAtivo = true;
    }

    public void iniciar() {
        if (missao != null) {
            System.out.println("Missão " + missao.getCodMissao() + " (Versão " + missao.getVersao() + ") iniciada!");
            missao.iniciar();
        } else {
            System.out.println("Erro: Nenhuma missão carregada.");
        }
    }

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
        // Verifica se a divisão desejada é válida
        ArrayUnorderedList<Divisao> divisoesValidas = mapa.obterDivisoesValidas(toCruz.getDivisaoAtual());
    
        boolean divisaoValida = false;
        for (Divisao divisao : divisoesValidas) {
            if (divisao.getNome().equalsIgnoreCase(novaDivisao)) {
                divisaoValida = true;
                break;
            }
        }
    
        if (divisaoValida) {
            toCruz.setDivisaoAtual(novaDivisao);  
            System.out.println("Tó Cruz se moveu para: " + novaDivisao);
        } else {
            System.out.println("Divisão inválida ou não conectada à divisão atual.");
        }
    }

    public void proximaIteracao() {
        System.out.println("\nNova iteração:");
        mapa.moverInimigos();
    }

    // Verifica se a missão foi concluída
    public void verificarEstadoJogo() {
        if (missao.isCompleta()) {
            System.out.println("Missão completada com sucesso!");
            finalizarJogo();
        } else {
            System.out.println("Missão ainda em andamento...");
        }
    }

    public void finalizarJogo() {
        jogoAtivo = false;
        System.out.println("Fim do jogo.");
    }

    public void interagir(String tipoDeInteracao) {
        if (tipoDeInteracao.equalsIgnoreCase("inimigo")) {
            System.out.println("Iniciando combate com inimigos...");
            combaterInimigos();
        } else if (tipoDeInteracao.equalsIgnoreCase("item")) {
            System.out.println("Interagindo com itens...");
            // Lógica para itens, já existente.
        }
    }    

    // Exibe o status atual do jogo
    public void exibirStatus() {
        System.out.println("Status do Jogo:");
        System.out.println("Missão: " + missao);
        System.out.println("Jogador: " + toCruz);
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

    // Método para verificar se o jogo está ativo
    public boolean isJogoAtivo() {
        return jogoAtivo;
    }
}
