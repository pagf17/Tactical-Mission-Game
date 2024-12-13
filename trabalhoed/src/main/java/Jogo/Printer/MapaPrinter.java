package Jogo.Printer;

import Interfaces.MapaStatus;

/**
 * Classe responsável por imprimir mensagens na consola referentes ao mapa do jogo.
 */
public class MapaPrinter {
    
    /**
     * Método que imprime mensagens na tela levando em conta o status recebido.
     * 
     * @param status o estado do jogo, representado por um enum "MapaStatus".
     * @param info é uma informação adicional para as mensagens como o nome de uma inimigo, o caminho para um objetivo, etc.
     */
    public static void printMapa(MapaStatus status, String info) {
        switch (status) {
            case DIVISAO_NAO_ENCONTRADA_INIMIGO:
                System.out.println("Divisão não encontrada para " + info + ".");
                break;
            case MOSTRAR_MISSAO:
                System.out.println("Missão: " + info);
                break;
            case CAMINHO_PARA_OBJETIVO:
                System.out.println("Caminho para objetivo: " + info);
                break;
            case CAMINHO_PARA_KIT:
                System.out.println("Caminho para kit: " + info);
                break;
            case CHAVE_EDIFICIO_NAO_ENCONTRADA:
                System.out.println("Chave 'edifício' não encontrada no ficheiro JSON.");
                break;
            case CHAVE_LIGACAO_NAO_ENCONTRADA:
                System.out.println("Chave 'ligacoes' não encontrada no ficheiro JSON.");
                break;
            default:
                System.out.println("Status desconhecido.");
                break;
        }
    }

    /**
     * Método que imprime uma mensagem de erro ao conectar duas divisões.
     * 
     * @param nome1 Nome da primeira divisão.
     * @param nome2 Nome da segunda divisão.
     */
    public static void erroConectarDivisao(String nome1, String nome2) {
        System.out.println("Erro ao conectar divisão " + nome1 + " com divisão " + nome2 + ".");
    }

    /**
     * Método que imprime uma mensagem caso não encontre um caminho entre duas divisões.
     * 
     * @param nome1 Nome da primeira divisão.
     * @param nome2 Nome da segunda divisão.
     */
    public static void caminhoInexistente(String nome1, String nome2) {
        System.out.println("Caminho inexistente entre divisão " + nome1 + " e divisão " + nome2 + ".");
    }

    /**
     * Método que imprime informações sobre uma divisão, como ligações, itens e inimigos.
     * 
     * @param nome Nome da divisão.
     * @param ligacoes Ligações da divisão.
     * @param itens Itens disponíveis na divisão.
     * @param inimigos Inimigos presentes na divisão.
     */
    public static void verMapa(String nome, String ligacoes, String itens, String inimigos) {
        System.out.println("\nDivisao " + nome + ":");
        System.out.println("Ligações: " + ligacoes);
        if (!itens.isEmpty()) {
            System.out.println("Itens: " + itens);
        }
        if (!inimigos.isEmpty()) {
            System.out.println("Inimigos: " + inimigos);
        }
    }
}
