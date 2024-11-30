import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import Estruturas.ArrayUnorderedList;

public class Inimigo {
    private String name;
    private String division;
    private int damage;
    private int hp;

    public Inimigo(String name, String division, int damage, int hp) {
        this.name = name;
        this.division = division;
        this.damage = damage;
        this.hp = hp;
    }

    public String getName() {
        return this.name;
    }

    public String getDivision() {
        return this.division;
    }

    public int getDamage() {
        return this.damage;
    }

    public int getHp() {
        return this.hp;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    // Metodos Inimigo

    public void attack(ToCruz toCruz) {
        toCruz.takeDamage(this.damage);
    }

    public void takeDamage(int damage) {
        this.hp -= damage;
    }

    public boolean isDead() {
        return this.hp <= 0;
    }

    public static ArrayUnorderedList<Inimigo> carregarInimigos(String arquivoJson) throws IOException {
        ArrayUnorderedList<Inimigo> inimigos = new ArrayUnorderedList<>();

        try (Reader reader = new InputStreamReader(new FileInputStream(arquivoJson), "UTF-8")) {
            JsonObject jsonObject = (JsonObject) Jsoner.deserialize(reader);

            JsonArray inimigosJson = (JsonArray) jsonObject.get("inimigos");

            if (inimigosJson != null) {
                for (Object obj : inimigosJson) {
                    JsonObject inimigoJson = (JsonObject) obj;
                    String nome = (String) inimigoJson.get("nome");
                    int poder = ((Number) inimigoJson.get("poder")).intValue();
                    String divisao = (String) inimigoJson.get("divisao");

                    Inimigo inimigo = new Inimigo(nome, divisao, poder, 100);
                    inimigos.addToRear(inimigo);
                }
            } else {
                System.out.println("Chave 'inimigos' não encontrada no JSON.");
            }

        } catch (Exception e) {
            System.out.println("Erro ao carregar inimigos: " + e.getMessage());
            throw new IOException("Erro ao carregar inimigos", e);
        }

        return inimigos;
    }

    @Override
    public String toString() {
        return "Enemy {" +
                "Name='" + name + '\'' +
                ", Damage=" + damage +
                ", HP=" + hp +
                ", Division='" + division + '\'' +
                '}';
    }
}