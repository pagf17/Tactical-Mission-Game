package Jogo.Printer;

import Interfaces.MapaStatus;

public class MapaPrinter {
    
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
                break;
        }
    }

    public static void erroConectarDivisao(String nome1, String nome2) {
        System.out.println("Erro ao conectar divisão " + nome1 + " com divisão " + nome2 + ".");
    }

    public static void caminhoInexistente(String nome1, String nome2) {
        System.out.println("Caminho inexistente entre divisão " + nome1 + " e divisão " + nome2 + ".");
    }

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
