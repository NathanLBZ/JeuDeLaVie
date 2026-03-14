package fr.nathan.UI;

import fr.nathan.JeuDeLaVie;
import fr.nathan.cellule.Cellule;
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
            labelGen = new Label("Génération\n" + jeu.getNbGen());
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
                    boutonPause.setText("STOP");
                }
            });

            
            sliderVitesse.valueProperty().addListener((observable, oldValue, newValue) -> {
                double cooldown = newValue.doubleValue(); 

                jeu.setCooldown((int) cooldown);
                labelSlider.setText("Délai : " + jeu.getCooldown() + " ms");
            });


            for (int row = 0; row < sizeY; row++) {
                for (int col = 0; col < sizeX; col++) {
                    Button cell = new Button();

                    boutons[row][col] = cell;
                    boutons[row][col].setPrefSize(10, 10);
                    boutons[row][col].setMinSize(10, 10);
                    boutons[row][col].setMaxSize(10, 10);

                    gridPane.add(boutons[row][col], col, row);
                }
            }


            // ajouter des clics sur les cellules
            if (jeu.getModeManuel()) {
                for (int row = 0; row < sizeY; row++) {
                    for (int col = 0; col < sizeX; col++) {

                        final int rowTemp = row;
                        final int colTemp = col;
                        boutons[row][col].setOnAction(e -> {
                            if (jeu.getGrilleXY(colTemp, rowTemp).estVivante()) {
                                boutons[rowTemp][colTemp].setStyle("-fx-background-radius: 0; -fx-background-color: white;");
                            }
                            else {
                                boutons[rowTemp][colTemp].setStyle("-fx-background-radius: 0; -fx-background-color: pink;");
                            }
                            jeu.inverserEtat(jeu.getGrilleXY(colTemp, rowTemp));
                        });
                    }
                }
            }
            
            
            
            sliderVitesse.setOrientation(Orientation.VERTICAL);
            sliderVitesse.setPrefHeight(30);
            sliderVitesse.setPrefHeight(screenHeight * 0.2);
            
            boutonPause.setText("Démarrer");
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

            //affichage de la premiere gen
            actualise();

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

            jeu.nextGen();
            this.labelGen.setText("Génération\n" + jeu.getNbGen());

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
