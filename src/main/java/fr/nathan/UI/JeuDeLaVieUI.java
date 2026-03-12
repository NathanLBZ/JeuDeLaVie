package fr.nathan.UI;

import fr.nathan.JeuDeLaVie;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class JeuDeLaVieUI extends Application implements Observateur{
    public static JeuDeLaVie jeu;
    
    private GridPane gridPane;
    private Button[][] boutons;

    public JeuDeLaVieUI() {
    }

    public static void setJeuStatic(JeuDeLaVie jeuParam) {
        jeu = jeuParam;
    }

    @Override
    public void start(Stage stage) throws Exception {
        try {
            int sizeX = jeu.getXmax(); // nombre de colonnes
            int sizeY = jeu.getYmax(); // nombre de lignes

            gridPane = new GridPane();
            boutons = new Button[sizeY][sizeX];

            for (int i = 0; i < sizeY; i++) {
                for (int j = 0; j < sizeX; j++) {

                    Button cell = new Button();
                    cell.setPrefSize(20, 20);
                    
                    if (jeu.getGrilleXY(j, i).estVivante()) {
                        cell.setStyle("-fx-background-color: black;");
                    }
                    else {
                        cell.setStyle("-fx-background-color: white;");
                    }

                    boutons[i][j] = cell;
                    gridPane.add(cell, j, i); // colonne, ligne
                    

                }
            }

            Scene scene = new Scene(gridPane);
            stage.setScene(scene);
            stage.setTitle("Le Jeu de la Vie");
            stage.show();

        } 
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            throw e;
        }
    }



    // affichage
    public void actualise() {
        if (boutons == null) {
            System.out.println("[UI] boutons is null, skipping update");
            return; // L'interface n'est pas encore initialisée
        }
        
        System.out.println("[UI] Updating display...");
        Platform.runLater(() -> {
            int sizeX = jeu.getXmax(); // nombre de colonnes
            int sizeY = jeu.getYmax(); // nombre de lignes

            for (int i = 0; i < sizeY; i++) {
                for (int j = 0; j < sizeX; j++) {
                    
                    if (jeu.getGrilleXY(i, j).estVivante()) {
                        boutons[i][j].setStyle("-fx-background-color: black;");
                    }
                    else {
                        boutons[i][j].setStyle("-fx-background-color: white;");
                    }
                }
            }
        });
    }

    
}
