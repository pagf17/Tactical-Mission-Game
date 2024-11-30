import Estruturas.ArrayUnorderedList;

public class App {
    public static void main(String[] args) throws Exception {
        ToCruz toCruz = new ToCruz(20, "Cruz");
        Mapa mapa = new Mapa();

        mapa.carregarMapa("missao.json");

        System.out.println(mapa);
        mapa.encontrarCaminho("Garagem", "Laboratório");

        ArrayUnorderedList<Inimigo> inimigos = Inimigo.carregarInimigos("missao.json");

        System.out.println("Inimigos carregados:");
        for (Inimigo inimigo : inimigos) {
            System.out.println(inimigo);
        }


        if (!inimigos.isEmpty()) {
            Inimigo primeiroInimigo = inimigos.removeFirst(); 
            System.out.println("\nPrimeiro inimigo encontrado: " + primeiroInimigo);

            System.out.println("\nO jogador Tó Cruz ataca o inimigo!");
            toCruz.attack(primeiroInimigo);

            System.out.println("\nEstado do inimigo após o ataque:");
            System.out.println(primeiroInimigo);

            if (!primeiroInimigo.isDead()) {
                System.out.println("\nO inimigo contra-ataca!");
                primeiroInimigo.attack(toCruz);

                System.out.println("\nEstado do jogador após o contra-ataque:");
                System.out.println(toCruz);
            } else {
                System.out.println("\nO inimigo foi derrotado!");
            }
        }

        System.out.println("\nEstado final do jogador:");
        System.out.println(toCruz);
    }
}
