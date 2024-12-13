package Interfaces;

/** 
 * Enumeração que representa o estado geral do jogo.
 */
public enum StatusGeral {
    //Referente ao JSON
    /**
     * Indica que a chave 'inimigos' não foi encontrada.
     */
    CHAVE_INIMIGOS_NAO_ENCONTRADA,
    /**
     * Indica que a chave 'itens' não foi encontrada.
     */
    CHAVE_ITENS_NAO_ENCONTRADA,

    //Referente ao Jogador/item
    /**
     * Indica que o jogador tem a vida cheia
     */
    JOGADOR_VIDA_CHEIA,
    /**
     * Indica que o jogador foi curado com sucesso
     */
    JOGADOR_CURA_SUCESSO,
    /**
     * Indica que o jogador já tem o colete no máximo
     */
    JOGADOR_COLETE_MAXIMO,
    /**
     * Indica que o jogador utilizou um colete com sucesso
     */
    JOGADOR_COLETE_SUCESSO,
    /**
     * Indica que o jogador tem a mochila cheia
     */
    MOCHILA_CHEIA,
    /**
     * Indica que a mochila está vazia
     */
    MOCHILA_VAZIA,
    /**
     * Indica que o item foi adicionado à mochila
     */
    ITEM_ADICIONADO_MOCHILA,

    //Referente a Divisão
    /**
     * Indica que a divisão não foi encontrada para inimigo.
     */
    DIVISAO_NAO_ENCONTRADA_INIMIGO,
    /**
     * Indica que a divisão não foi encontrada para item.
     */
    DIVISAO_NAO_ENCONTRADA_ITEM,
    /**
     * Lista os inimigos presentes na divisão.
     */
    LISTAR_INIMIGO,
    /**
     * Lista os itens presentes na divisão.
     */
    LISTAR_ITEM
}
