import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;

import Estruturas.ArrayUnorderedList;

public class Item {
    private String tipo;
    private String localizacao;

    public Item(String tipo, String localizacao) {
        this.tipo = tipo;
        this.localizacao = localizacao;
    }

    public String getTipo() {
        return tipo;
    }

    public String getlocalizacao() {
        return localizacao;
    }

    public void settipo(String tipo) {
        this.tipo = tipo;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String use(ToCruz toCruz) {
        if (tipo.equalsIgnoreCase("kit de vida")) {
            toCruz.addKitMochila(this);
        } else if (tipo.equalsIgnoreCase("Colete")) {
            toCruz.usarColete();
        }
        return "Item descartado.";
    }

    protected static ArrayUnorderedList<Item> carregarItens(JsonObject jsonObject) {
        ArrayUnorderedList<Item> itens = new ArrayUnorderedList<>();

        JsonArray itensJson = (JsonArray) jsonObject.get("itens");
        if (itensJson != null) {
            for (Object obj : itensJson) {
                JsonObject itemJson = (JsonObject) obj;

                String divisao = (String) itemJson.get("divisao");
                String tipo = (String) itemJson.get("tipo");

                Item item = new Item(tipo, divisao);
                itens.addToRear(item);
            }
        } else {
            System.out.println("Chave 'itens' não encontrada no JSON.");
        }

        return itens;
    }

    @Override
    public String toString() {
        return "Item {" +
                "Tipo :'" + tipo + '\'' +
                ", Localização :'" + localizacao + '\'' +
                '}';
    }
}
