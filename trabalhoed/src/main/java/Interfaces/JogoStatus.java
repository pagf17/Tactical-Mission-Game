package Interfaces;

/**
 * Enumeração que representa o estado do jogo.
 */
public enum JogoStatus {
    
    //Referente ao Jogador
    /**
     * Indica que o jogador está no início de seu turno.
     */
    INICIO_TURNO_JOGADOR,
    /**
     * Indica que o jogador está no fim de seu turno.
     */
    FIM_TURNO_JOGADOR,
    /**
     * Indica que o jogador foi derrotado.
     */
    JOGADOR_DERROTADO,
    /**
     * Indica que o jogador se moveu.
     */
    JOGADOR_MOVEU,
    /**
     * Indica que o jogador foi atacado.
     */
    JOGADOR_ATACADO,
    /**
     * Indica que o jogador atacou.
     */
    ATAQUE_JOGADOR,
    /**
     * Indica que o movimento foi bloqueado por um inimigo.
     */
    MOVIMENTO_BLOQUEADO_POR_INIMIGO,

    //Referente aos Inimigos
    /**
     * Indica que há inimigos presentes.
     */
    INIMIGOS_PRESENTES,
    /**
     * Indica que o turno do inimigo foi iniciado.
     */
    INICIO_TURNO_INIMIGO,
    /**
     * Indica que o turno do inimigo foi finalizado.
     */
    FIM_TURNO_INIMIGO,
    /**
     * Indica que os inimigos se moveram.
     */
    MOVER_INIMIGOS, 
    /**
     * Indica que existem inimigos na sala do objetivo.
     */
    INIMIGOS_PRESENTES_OBJETIVO,
    /**
     * Indica que o inimigo atacou o jogador.
     */
    ATAQUE_INIMIGO,
    /**
     * Indica que não existem inimigos.
     */
    SEM_INIMIGOS,
    /**
     * Indica que se iniciou um confronto.
     */
    LUTA_INICIADA,
    /**
     * Indica que o inimigo foi eliminado.
     */
    INIMIGO_ELIMINADO,  
    /**
     * Indica que todos os inimigos foram eliminados.
     */
    TODOS_INIMIGOS_ELIMINADOS,
    /**
     * Indica que o inimigo se moveu para a sala do jogador.
     */
    INIMIGO_MOVEU_PARA_JOGADOR,
    /**
     * Indica que existem inimigos.
     */
    ALERTA_INIMIGOS,

    //Referente aos itens
    /**
     * Indica que há itens na divisão atual.
     */
    ITENS_PRESENTES,
    /**
     * Indica que o jogador adicionou um kit à mochila.
     */
    KIT_ADD_MOCHILA,
    /**
     * Indica que o item é inválido.
     */
    ITEM_INVALIDO,
    /**
     * Indica que a mochila está vazia.
     */
    MOCHILA_VAZIA,
    /**
     * Indica que há itens disponiveis.
     */
    ITENS_DISPONIVEIS,

    //Referente ao Objetivo/Divisões
    /**
     * Indica que o objetivo foi encontrado.
     */
    OBJETIVO_ENCONTRADO,
    /**
     * Indica que o objetivo foi alcançado.
     */
    OBJETIVO_ALCANCADO,
    /**
     * Indica que o objetivo foi extraído.
     */
    OBJETIVO_EXTRAIDO,
    /**
     * Indica que a divisão é inválida.
     */
    DIVISAO_INVALIDA,
    /**
     * Indica que deve procurar uma saída.
     */
    PROCURE_SAIDA_VALIDA,
    /**
     * Indica que deve continuar a explorar.
     */
    CONTINUAR_EXPLORACAO,
    /**
     * Indica a divisão atual.
     */
    DIVISAO_ATUAL,
    /**
     * Indica que a entrada foi escolhida (MODO AUTOMATICO).
     */
    ENTRADA_ESCOLHIDA,
    /**
     * Indica que a entrada é inválida (MODO AUTOMATICO). 
     */
    ENTRADA_INVALIDA,
}
