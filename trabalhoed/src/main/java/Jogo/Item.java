package Jogo;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import Estruturas.ArrayUnorderedList;
import Jogo.Printer.PrinterGeral;
import Interfaces.StatusGeral;

/**
 * Classe que representa um item
 */
public class Item {
    private Divisao localizacao;

    /**
     * Construtor de Item
     * 
     * @param localizacao Divisão onde está o item.
     */
    public Item(Divisao localizacao) {
        this.localizacao = localizacao;
    }

    /**
     * Método que devolve a localização do item.
     * 
     * @return a Divisão onde está o item.
     */
    public Divisao getLocalizacao() {
        return localizacao;
    }

    /**
     * Método que define a localização do item.
     * 
     * @param localizacao Divisão para associar o item.
     */
    public void setLocalizacao(Divisao localizacao) {
        this.localizacao = localizacao;
    }

    /**
     * Método para carregar itens a partir de um ficheiro JSON
     * Este método lê os 'itens' do ficheiro JSON, depois cria instâncias de
     * 'Item' ('Kit' ou 'Colete') para cada um deles, adicionando-os posteriormente 
     * à lista de itens da divisão correspondente.
     * 
     * @param jsonObject o ficheiro JSON.
     * @param mapa o mapa que contêm as divisões onde os itens vão ser colocados.
     * @return a lista de itens carregados.
     */
    public static ArrayUnorderedList<Item> carregarItens(JsonObject jsonObject, Mapa mapa) {
        ArrayUnorderedList<Item> itens = new ArrayUnorderedList<>();
    
        JsonArray itensJson = (JsonArray) jsonObject.get("itens");
        if (itensJson != null) {
            for (Object obj : itensJson) {
                JsonObject itemJson = (JsonObject) obj;
                String nomeDivisao = (String) itemJson.get("divisao"); 
                Divisao divisao = mapa.obterDivisao(nomeDivisao);  
    
                if (divisao != null) {
                    String tipo = (String) itemJson.get("tipo");
    
                    if ("kit de vida".equalsIgnoreCase(tipo)) {
                        int pontosRecuperados = ((Number) itemJson.get("pontosRecuperados")).intValue();
                        itens.addToRear(new Kit(divisao, pontosRecuperados));  
                    } else if ("colete".equalsIgnoreCase(tipo)) {
                        int pontosExtra = ((Number) itemJson.get("pontosExtra")).intValue();
                        itens.addToRear(new Colete(divisao, pontosExtra));  
                    }
                } else {
                    PrinterGeral.print(StatusGeral.DIVISAO_NAO_ENCONTRADA_ITEM, nomeDivisao);
                }
            }
        } else {
            PrinterGeral.print(StatusGeral.CHAVE_ITENS_NAO_ENCONTRADA, null);
        }
        return itens;
    }    

    /**
     * Método que retorna a representação em String do item.
     * 
     * @return Representação em String do item.
     */
    @Override
    public String toString() {
        return "Item [ Localização: " + localizacao + "]";
    }
}
