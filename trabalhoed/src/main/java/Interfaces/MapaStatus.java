package Interfaces;

/**
 * Enumeração que representa o estado do mapa.
 */
public enum MapaStatus {
    /**
     * Indica que o não foi encontrada divisão para inimigos.
     */
    DIVISAO_NAO_ENCONTRADA_INIMIGO,
    /**
     * Indica exibição de missão
     */
    MOSTRAR_MISSAO,
    /**
     * Indica exibição de caminho até ao objetivo
     */
    CAMINHO_PARA_OBJETIVO,
    /**
     * Indica exibição de caminho até ao kit
     */
    CAMINHO_PARA_KIT,
    /**
     * Indica que a chave 'edificio' não foi encontrada.
     */
    CHAVE_EDIFICIO_NAO_ENCONTRADA,
    /**
     * Indica que a chave 'ligacao' não foi encontrada.
     */
    CHAVE_LIGACAO_NAO_ENCONTRADA,
}
