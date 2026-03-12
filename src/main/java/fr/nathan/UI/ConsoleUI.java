package fr.nathan.UI;

import fr.nathan.JeuDeLaVie;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ConsoleUI implements Observateur{
    public static JeuDeLaVie jeu;
    

    public ConsoleUI() {
    }

    public static void setJeuStatic(JeuDeLaVie jeuParam) {
        jeu = jeuParam;
    }

    public void actualise() {
        for(int i = 0; i < 50; i++) {
            System.out.println();
        }
        jeu.afficheGrille();
        
    }

    
}
