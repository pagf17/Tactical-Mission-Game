package Jogo.Printer;

import Interfaces.StatusGeral;

/**
 * Classe responsável por imprimir mensagens na consola referentes a situações gerais do jogo.
 */
public class PrinterGeral {
    
    /**
     * Método que imprime mensagens na consola levando em conta o status recebido.
     * 
     * @param status o estado do jogo, representado por um enum "StatusGeral".
     * @param info é uma informação adicional para as mensagens como o nome de um inimigo, o nome de um item, etc.
     */
    public static void print(StatusGeral status, String info) {
        switch (status) {
            //Referente ao JSON
            case CHAVE_INIMIGOS_NAO_ENCONTRADA:
                System.out.println("Chave 'inimigos' não encontrada no arquivo JSON.");
                break;
            case CHAVE_ITENS_NAO_ENCONTRADA:
                System.out.println("Chave 'itens' não encontrada no arquivo JSON.");
                break;

            //Referente ao Jogador/item
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
            case MOCHILA_CHEIA:
                System.out.println("Mostrila cheia. Não é possível adicionar mais kits.");
                break;
            case ITEM_ADICIONADO_MOCHILA:
                System.out.println("Kit adicionado à mochila.");
                break;

            //Referente a Divisão
            case DIVISAO_NAO_ENCONTRADA_INIMIGO:
                System.out.println("Erro: Divisão não foi encontrada para: " + info);
                break;
            case DIVISAO_NAO_ENCONTRADA_ITEM:
                System.out.println("Erro: Divisão não foi encontrada para: " + info);
                break;
            case LISTAR_INIMIGO:
                System.out.println(info);
                break;
            case LISTAR_ITEM:
                System.out.println(info);
                break;
            default:
                System.out.println("Status desconhecido.");
                break;
        }
    }

    /**
     * Método que imprime uma mensagem quando um inimigo é movido para uma divisão.
     * 
     * @param nome Nome do inimigo. 
     * @param divisao Divisão para onde o inimigo foi movido.
     */
    public static void moverInimigo(String nome, String divisao) {
        System.out.println(nome + " moveu-se para a divisão " + divisao);
    }

    /**
     * Método que imprime informações sobre a missão.
     * 
     * @param codMissao Código da missão em curso.
     * @param versao Versão da missão.
     * @param objetivo Objetivo da missão.
     */
    public static void infoMissao (String codMissao, int versao, String objetivo) {
        System.out.println("Missão " + codMissao + " (Versão " + versao + ") iniciada!");
        System.out.println("Objetivo: " + objetivo);
        System.out.println("Depois, extraia-o e saia por uma das divisões de entrada/saída.");
    }

    /**
     * Método que imprime informação sobre uma missão concluida.
     * 
     * @param codMissao Código da missão.
     * @param versao Versão da missão.
     */
    public static void terminarMissao(String codMissao, int versao) {
        System.out.println("Missão " + codMissao + " (Versão " + versao + ") completada com sucesso!");
        System.out.println("O jogo terminou. Parabéns!");
    }

    /**
     * Método que imprime uma mensagem de conclusão de objetivo.
     * 
     * @param tipo Tipo de objetivo.
     * @param divisaoAlvo Divisão onde o objetivo foi concluído.
     */
    public static void concluirObjetivo(String tipo, String divisaoAlvo) {
        System.out.println("Objetivo concluído: " + tipo + " na divisão " + divisaoAlvo);
    }
}
