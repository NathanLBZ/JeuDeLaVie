package fr.nathan.UI;

import fr.nathan.JeuDeLaVie;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class JeuDeLaVieUI extends Application implements Observateur{
    public static JeuDeLaVie jeu;
    private int nbGen = 1;
    
    HBox hBox;
    VBox vBox;
    HBox hBoxSlider;
    private GridPane gridPane;
    Label labelGen;
    Label labelSlider;
    Button boutonPause;
    Slider sliderVitesse;


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


            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            double screenWidth = screenBounds.getWidth();
            double screenHeight = screenBounds.getHeight();
            

            // Composants de la fenêtre
            gridPane = new GridPane();
            boutons = new Button[sizeY][sizeX];
            labelGen = new Label("Génération\n" + nbGen++);
            labelSlider = new Label("Délai : " + jeu.getCooldown() + " ms");
            hBox = new HBox();
            vBox = new VBox();
            boutonPause = new Button();
            hBoxSlider = new HBox();
            sliderVitesse = new Slider(50, 200, jeu.getCooldown());
            
            boutonPause.setOnAction(e -> {
                jeu.getModPause().changeMode();
                
                if (jeu.isPaused()) {
                    boutonPause.setText("PLAY");
                }
                else {
                    boutonPause.setText("PAUSE");
                }
            });

            sliderVitesse.valueProperty().addListener((observable, oldValue, newValue) -> {
                double cooldown = newValue.doubleValue(); 

                jeu.setCooldown((int) cooldown);
                labelSlider.setText("Délai : " + jeu.getCooldown() + " ms");
            });
            
            
            
            sliderVitesse.setOrientation(Orientation.VERTICAL);
            sliderVitesse.setPrefHeight(30);
            sliderVitesse.setPrefHeight(screenHeight * 0.2);
            
            boutonPause.setText("PAUSE");
            boutonPause.setAlignment(Pos.CENTER);

            labelGen.setAlignment(Pos.CENTER);

            boutonPause.setAlignment(Pos.CENTER);
            boutonPause.setMinWidth(80);



            // structure de la fenêtre
            hBoxSlider.getChildren().add(sliderVitesse);
            hBoxSlider.getChildren().add(labelSlider);

            
            vBox.getChildren().add(labelGen);
            vBox.getChildren().add(boutonPause);
            vBox.getChildren().add(hBoxSlider);

            hBox.getChildren().add(gridPane);
            hBox.getChildren().add(vBox);
            HBox.setMargin(vBox, new Insets(10, 10, 10, 10));
            
            
            
            
            jeu.attacheObservateur(this);

            // initialisation de la première génération
            for (int row = 0; row < sizeY; row++) {
                for (int col = 0; col < sizeX; col++) {
                    Button cell = new Button();
                    cell.setPrefSize(10, 10);
                    cell.setMinSize(10, 10);
                    cell.setMaxSize(10, 10);
                    
                    if (jeu.getGrilleXY(col, row).estVivante()) {
                        cell.setStyle("-fx-background-radius: 0; -fx-background-color: pink;");
                    }
                    else {
                        cell.setStyle("-fx-background-radius: 0; -fx-background-color: white;");
                    }

                    boutons[row][col] = cell;
                    gridPane.add(cell, col, row); // colonne, ligne
                    

                }
            }

            Scene scene = new Scene(hBox);
            stage.setScene(scene);
            stage.setTitle("Le Jeu de la Vie");
            stage.show();

        } 
        catch (Exception e) {
            System.err.println(e.getMessage());
            throw e;
        }
    }



    // affichage
    @Override
    public void actualise() {

        if (this.boutons == null) {
            return; 
        }

        javafx.application.Platform.runLater(() -> {
            int sizeX = jeu.getXmax();
            int sizeY = jeu.getYmax();

            this.labelGen.setText("Génération\n" + nbGen++);

            for (int row = 0; row < sizeY; row++) {
                for (int col = 0; col < sizeX; col++) {
                    if (jeu.getGrilleXY(col, row).estVivante()) {
                        this.boutons[row][col].setStyle("-fx-background-radius: 0; -fx-background-color: pink;");
                    } 
                    else {
                        this.boutons[row][col].setStyle("-fx-background-radius: 0; -fx-background-color: white;");
                    }
                }
            }
        });
    }
    
}
