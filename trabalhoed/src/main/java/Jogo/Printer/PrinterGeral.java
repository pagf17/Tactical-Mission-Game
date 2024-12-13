package Jogo.Printer;

import Interfaces.StatusGeral;

public class PrinterGeral {
    
    public static void print(StatusGeral status, String info) {
        switch (status) {
            case CHAVE_INIMIGOS_NAO_ENCONTRADA:
                System.out.println("Chave 'inimigos' não encontrada no arquivo JSON.");
                break;
            case CHAVE_ITENS_NAO_ENCONTRADA:
                System.out.println("Chave 'itens' não encontrada no arquivo JSON.");
                break;
            case DIVISAO_NAO_ENCONTRADA_INIMIGO:
                System.out.println("Erro: Divisão não foi encontrada para: " + info);
                break;
            case DIVISAO_NAO_ENCONTRADA_ITEM:
                System.out.println("Erro: Divisão não foi encontrada para: " + info);
                break;
            case MOCHILA_VAZIA:
                System.out.println("Erro: Mochila vazia.");
                break;
            case JOGADOR_VIDA_CHEIA:
                System.out.println("Erro: Tó Cruz já está com 100 de vida e não pode usar o kit.");
                break;
            case JOGADOR_CURA_SUCESSO:
                System.out.println("Tó Cruz usou o kit e recuperou " + info + " de vida.");
                break;
            case JOGADOR_COLETE_MAXIMO:
                System.out.println("Tó Cruz já atingiu o limite máximo de vida (125) e não pode usar o colete balístico.");
                break;
            case JOGADOR_COLETE_SUCESSO:
                System.out.println("Tó Cruz usou o colete balístico e recuperou " + info + " de vida extra.");
                break;
            default:
                break;
        }
    }

    public static void moverInimigo(String nome, String divisao) {
        System.out.println(nome + " moveu-se para a divisão " + divisao);
    }

    public static void infoMissao (String codMissao, int versao, String objetivo) {
        System.out.println("Missão " + codMissao + " (Versão " + versao + ") iniciada!");
        System.out.println("Objetivo: " + objetivo);
        System.out.println("Depois, extraia-o e saia por uma das divisões de entrada/saída.");
    }

    public static void terminarMissao(String codMissao, int versao) {
        System.out.println("Missão " + codMissao + " (Versão " + versao + ") completada com sucesso!");
        System.out.println("O jogo terminou. Parabéns!");
    }

    public static void concluirObjetivo(String tipo, String divisaoAlvo) {
        System.out.println("Objetivo concluído: " + tipo + " na divisão " + divisaoAlvo);
    }
}
