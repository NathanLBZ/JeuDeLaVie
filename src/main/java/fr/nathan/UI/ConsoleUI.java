package fr.nathan.UI;

import fr.nathan.JeuDeLaVie;

public class ConsoleUI implements Observateur{
    public static JeuDeLaVie jeu;
    

    public ConsoleUI() {
    }

    public static void setJeuStatic(JeuDeLaVie jeuParam) {
        jeu = jeuParam;
    }

    @Override
    public void actualise() {
        System.out.println("Génération : " + jeu.getNbGen());
        System.out.println("Cellules vivantes : " + jeu.nbCellulesVivantes());
    }
}