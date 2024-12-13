package Jogo.Printer;

import Interfaces.JogoStatus;

/**
 * Classe responsável por imprimir mensagens na consola referentes ao jogo.
 */
public class JogoPrinter {
    
    /**
     * Método que imprime mensagens na tela levando em conta o status recebido.
     * 
     * @param status o estado do jogo, representado por um enum "JogoStatus".
     * @param info é uma informação adicional para as mensagens como a divisão atual, inimigo atacado, etc.
     */
    public static void printJogo(JogoStatus status, String info) {
        switch(status) {

            //Referente ao Jogador
            case INICIO_TURNO_JOGADOR:
                System.out.println("\n=== Turno de Tó Cruz ===");
                break;
            case FIM_TURNO_JOGADOR:
                System.out.println("Fim do turno de Tó Cruz.");
                break;
            case JOGADOR_DERROTADO:
                System.out.println("Tó Cruz foi derrotado! Fim de jogo!");
                break;
            case JOGADOR_MOVEU:
                System.out.println("Tó Cruz moveu-se para a divisão " + info + ".");
                break;
            case JOGADOR_ATACADO:
                System.out.println("Tó Cruz foi atacado por " + info + "!");    
                break;
            case ATAQUE_JOGADOR:
                System.out.println("Tó Cruz atacou " + info + "!");
                break;
            case MOVIMENTO_BLOQUEADO_POR_INIMIGO:
                System.out.println("Tó Cruz não pode mover-se enquanto houver inimigos!");
                break;
            
            //Referente aos Inimigos
            case INIMIGOS_PRESENTES:
                System.out.println("Inimigos presentes na divisão: " + info);
                break;
            case INICIO_TURNO_INIMIGO:
                System.out.println("\n=== Turno dos inimigos ===");
                break;
            case FIM_TURNO_INIMIGO:
                System.out.println("Fim do turno dos inimigos.");
                break;
            case MOVER_INIMIGOS:
                System.out.println("Os inimigos estão a mudar de divisão.");
                break;
            case INIMIGOS_PRESENTES_OBJETIVO:
                System.out.println("Inimigos presentes na divisão! Elimine-os primeiro!");
                break;
            case ATAQUE_INIMIGO:
                System.out.println(info +" atacou Tó Cruz!");
                break;
            case SEM_INIMIGOS:
                System.out.println("Não há inimigos para atacar nesta divisão.");
                break;
            case LUTA_INICIADA:
                System.out.println("Combate iniciado!");
                break;
            case INIMIGO_ELIMINADO:
                System.out.println(info + " foi eliminado!");
                break;
            case TODOS_INIMIGOS_ELIMINADOS:
                System.out.println("Todos os inimigos foram eliminados!");
                break;
            case INIMIGO_MOVEU_PARA_JOGADOR:
                System.out.println(info + " moveu-se para a divisão de Tó Cruz.");
                break;
            case ALERTA_INIMIGOS:
                System.out.println("Aviso: Inimigos presentes na divisão!");
                break;
            
            //Referente aos itens
            case ITENS_PRESENTES:
                System.out.println("Itens presentes na divisão: " + info);
                break;
            case KIT_ADD_MOCHILA:
                System.out.println("Kit de primeiros socorros adicionado à mochila.");
                break;
            case ITEM_INVALIDO:
                System.out.println("Item inválido.");
                break;
            case MOCHILA_VAZIA:
                System.out.println("A mochila está vazia.");
                break;
            case ITENS_DISPONIVEIS:
                System.out.println("Aviso: Itens disponiveis na divisão!");
                break;

            //Referente ao Objetivo/Divisões
            case OBJETIVO_ENCONTRADO:
                System.out.println("\nAtenção! Objetivo encontrado nesta divisão!");
                break;
            case OBJETIVO_ALCANCADO:
                System.out.println("Objetivo resgatado! Vá para a saída!");
                break;
            case OBJETIVO_EXTRAIDO:
                System.out.println("Objetivo extraído com sucesso! Parabéns!");
                break;
            case DIVISAO_INVALIDA:
                System.out.println("Divisão inválida.");
                break;
            case PROCURE_SAIDA_VALIDA:
                System.out.println("Procure uma saída válida.");
                break;
            case CONTINUAR_EXPLORACAO:
                System.out.println("Continue a explorar!");
                break;
            case DIVISAO_ATUAL:
                System.out.println("\nVocê está na divisão " + info + ".");
                break;
            case ENTRADA_ESCOLHIDA: //Modo automático
                System.out.println("Entrada escolhida: " + info);
                break;
            case ENTRADA_INVALIDA: //Modo automático
                System.out.println("Não foi encontrada uma entrada válida.");
                break;
            
            default:
                System.out.println("Status desconhecido.");
        }
    }

    /**
     * Método que imprime o status atuais do jogo, como a missão e informações sobre o jogador.
     * 
     * @param info1 informação sobre a missão atual.
     * @param info2 informação sobre o jogador.
     */
    public static void printStatus(String info1, String info2) {
        System.out.println("Status do jogo: ");
        System.out.println("Missao: " + info1);
        System.out.println("Tó Cruz: " + info2);
    }
}
