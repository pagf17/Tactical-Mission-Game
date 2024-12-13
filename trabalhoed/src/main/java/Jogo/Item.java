package Jogo;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;

import Estruturas.ArrayUnorderedList;
import Jogo.Printer.PrinterGeral;
import Interfaces.StatusGeral;

public class Item {
    private Divisao localizacao;

    public Item(Divisao localizacao) {
        this.localizacao = localizacao;
    }

    public Divisao getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(Divisao localizacao) {
        this.localizacao = localizacao;
    }

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

    @Override
    public String toString() {
        return "Item [ Localização: " + localizacao + "]";
    }
}
