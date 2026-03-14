package fr.nathan.UI;

import java.lang.classfile.instruction.ThrowInstruction;

import fr.nathan.JeuDeLaVie;
import fr.nathan.cellule.Cellule;
import fr.nathan.pattern.Pattern;
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

    VBox vBoxMain = new VBox();
    HBox hBoxPaterns = new HBox();


    private Button[][] boutons;
    private Button[] boutonsPatterns;
    

    public JeuDeLaVieUI() {
    }

    public static void setJeuStatic(JeuDeLaVie jeuParam) {
        jeu = jeuParam;
    }

    private Button boutonPatternActif;
    private boolean boutonPatternisActif = false;
    private Pattern patternActif = null;

    private void setBoutonPatternActif(Button boutonClique, Pattern pattern) {
        // 1. Si on clique sur le bouton déjà vert : on l'éteint
        if (boutonPatternisActif && boutonClique == boutonPatternActif) {
            boutonClique.setStyle(""); // Reset style
            boutonPatternisActif = false;
            boutonPatternActif = null;
            patternActif = null;
        } 
        // 2. Si on clique sur un NOUVEAU bouton (ou si rien n'était actif)
        else {
            // Éteindre l'ancien bouton s'il existe
            if (boutonPatternActif != null) {
                boutonPatternActif.setStyle("");
            }

            // Allumer le nouveau
            boutonClique.setStyle("-fx-background-color: green; -fx-text-fill: white;");
            boutonPatternActif = boutonClique;
            patternActif = pattern;
            boutonPatternisActif = true;
        }
    }

    private void putPattern(Pattern pattern, int row, int col, Button[][] boutons) {
        patternActif.executePattern(pattern, row, col, jeu, boutons);
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


            // mode manuel
            vBoxMain.getChildren().add(hBox);
            if (jeu.getModeManuel()) {
                vBoxMain.getChildren().add(hBoxPaterns);


                // ajout des clics sur les cellules
                for (int row = 0; row < sizeY; row++) {
                    for (int col = 0; col < sizeX; col++) {

                        final int rowTemp = row;
                        final int colTemp = col;
                        boutons[row][col].setOnAction(e -> {
                            if (!boutonPatternisActif) {
                                if (jeu.getGrilleXY(colTemp, rowTemp).estVivante()) {
                                    boutons[rowTemp][colTemp].setStyle("-fx-background-radius: 0; -fx-background-color: white;");
                                }
                                else {
                                    boutons[rowTemp][colTemp].setStyle("-fx-background-radius: 0; -fx-background-color: pink;");
                                }
                                jeu.inverserEtat(jeu.getGrilleXY(colTemp, rowTemp));
                            }
                            else {
                                putPattern(this.patternActif, rowTemp, colTemp, boutons);
                            }
                        });
                    }
                }


                // ajout des patterns 
                Pattern[] tousLesPatterns = Pattern.values();
                boutonsPatterns = new Button[tousLesPatterns.length];

                
                for (int i = 0; i < tousLesPatterns.length; i++) {
                    boutonsPatterns[i] = new Button(tousLesPatterns[i].toString());
                    hBoxPaterns.getChildren().add(boutonsPatterns[i]);

                    boutonsPatterns[i].setAlignment(Pos.CENTER);
                    boutonsPatterns[i].setMinWidth(80);


                    final Button boutonTemp = boutonsPatterns[i];
                    final Pattern patern = tousLesPatterns[i];
                    boutonsPatterns[i].setOnAction(e -> {
                        this.setBoutonPatternActif(boutonTemp, patern);
                        this.boutonPatternActif = boutonTemp;
                    });
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

            Scene scene = new Scene(vBoxMain);
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
