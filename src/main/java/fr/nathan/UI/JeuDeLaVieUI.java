package fr.nathan.UI;


import fr.nathan.JeuDeLaVie;
import fr.nathan.pattern.Pattern;
import fr.nathan.visiteur.VisiteurClassique;
import fr.nathan.visiteur.VisiteurCovid;
import fr.nathan.visiteur.VisiteurHighLife;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class JeuDeLaVieUI extends Application implements Observateur{
    public static JeuDeLaVie jeu;
    
    private VBox vBoxMain = new VBox();
    private HBox hBox;
    private VBox vBox;
    private GridPane gridPane;
    private Label labelGen;
    private Label labelSlider;
    private Button boutonPause;
    private Button boutonReset;
    private Label labelErrorReset;
    private Label labelPatternAlert = new Label("");
    private ComboBox<String> modesDeJeu = new ComboBox<>();

    private Slider sliderVitesse;

    private Button[][] boutonsGrille;
    private Button[] boutonsPatterns;
    
    private Button boutonPatternActif;
    private boolean boutonPatternisActif = false;
    private Pattern patternActif = null;

    public JeuDeLaVieUI() {
    }

    public static void setJeuStatic(JeuDeLaVie jeuParam) {
        jeu = jeuParam;
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        try {
            
            
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            double screenHeight = screenBounds.getHeight();
            
            
            // Composants de la fenêtre
            gridPane = new GridPane();
            vBoxMain = new VBox();
            vBox = new VBox();
            hBox = new HBox();
            
            labelGen = new Label("Génération\n" + jeu.getNbGen());
            boutonPause = new Button();
            sliderVitesse = new Slider(50, 200, jeu.getCooldown());
            labelSlider = new Label("Délai : " + jeu.getCooldown() + " ms");
            
            boutonsGrille = new Button[jeu.getYmax()][jeu.getXmax()];

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

            modesDeJeu.setOnAction(e -> {
                String mode = modesDeJeu.getValue();
                
                if (mode == "Classique") {
                    jeu.setVisiteur(new VisiteurClassique(jeu));
                }
                else if (mode == "High Life") {
                    jeu.setVisiteur(new VisiteurHighLife(jeu));
                }
                else if (mode == "Virus") {
                    jeu.setVisiteur(new VisiteurCovid(jeu));
                }
            });
            
            
            int sizeX = jeu.getXmax(); // nombre de colonnes
            int sizeY = jeu.getYmax(); // nombre de lignes
            for (int row = 0; row < sizeY; row++) {
                for (int col = 0; col < sizeX; col++) {
                    Button cell = new Button();
                    
                    boutonsGrille[row][col] = cell;
                    boutonsGrille[row][col].setPrefSize(10, 10);
                    boutonsGrille[row][col].setMinSize(10, 10);
                    boutonsGrille[row][col].setMaxSize(10, 10);
                    
                    gridPane.add(boutonsGrille[row][col], col, row);
                }
            }
            
            
            // mode manuel
            vBoxMain.getChildren().add(hBox);
            if (jeu.getModeManuel()) {
                HBox hBoxPaterns = new HBox();
                vBoxMain.getChildren().add(hBoxPaterns);
                
                
                // ajout des clics sur les cellules
                for (int row = 0; row < sizeY; row++) {
                    for (int col = 0; col < sizeX; col++) {
                        
                        final int rowTemp = row;
                        final int colTemp = col;
                        boutonsGrille[row][col].setOnAction(e -> {
                            if (!boutonPatternisActif) {
                                if (jeu.getGrilleXY(colTemp, rowTemp).estVivante()) {
                                    boutonsGrille[rowTemp][colTemp].setStyle("-fx-background-radius: 0; -fx-background-color: white;");
                                }
                                else {
                                    boutonsGrille[rowTemp][colTemp].setStyle("-fx-background-radius: 0; -fx-background-color: pink;");
                                }
                                jeu.inverserEtat(jeu.getGrilleXY(colTemp, rowTemp));
                            }
                            else {
                                if ((patternActif.hauteurPattern() + rowTemp) < sizeY && (patternActif.largeurPattern() + colTemp) < sizeX) {
                                    putPattern(this.patternActif, rowTemp, colTemp, boutonsGrille);
                                    labelPatternAlert.setText("");
                                }
                                else {
                                    labelPatternAlert.setText("Pas assez\nde place\npour placer\nle pattern");
                                    labelPatternAlert.setStyle("-fx-text-fill: red;");
                                }
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


                // gestion du bouton reset
                boutonReset = new Button();
                boutonReset.setText("RESET");
                labelErrorReset = new Label();

                boutonReset.setOnAction(e -> {
                    if (boutonPause.getText() == "Démarrer" || boutonPause.getText() == "PLAY") {
                        jeu.resetJeu();
                        actualise();
                        labelErrorReset.setText("");
                    }
                    else {
                        labelErrorReset.setText("Mettez le jeu\nen pause");
                        labelErrorReset.setStyle("-fx-text-fill: red;");
                    }
                });
            }

            // ajout des différents modes de jeu
            modesDeJeu.getItems().addAll("Classique", "High Life", "Virus");
            modesDeJeu.setValue("Classique");
            
            
            
            sliderVitesse.setOrientation(Orientation.VERTICAL);
            sliderVitesse.setPrefHeight(30);
            sliderVitesse.setPrefHeight(screenHeight * 0.2);
            
            boutonPause.setText("Démarrer");
            boutonPause.setAlignment(Pos.CENTER);
            
            labelGen.setAlignment(Pos.CENTER);
            
            boutonPause.setAlignment(Pos.CENTER);
            boutonPause.setMinWidth(80);
            
            
            
            // structure de la fenêtre
            
            vBox.getChildren().add(labelGen);
            vBox.getChildren().add(boutonPause);
            if (jeu.getModeManuel()) {
                vBox.getChildren().add(boutonReset);
            }
            vBox.getChildren().add(sliderVitesse);
            vBox.getChildren().add(labelSlider);
            if (jeu.getModeManuel()) {
                vBox.getChildren().add(labelErrorReset);
            }
            if (jeu.getModeManuel()) {
                vBox.getChildren().add(labelPatternAlert);
            }
            vBox.getChildren().add(modesDeJeu);

            
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
        
        if (this.boutonsGrille == null) {
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
                        this.boutonsGrille[row][col].setStyle("-fx-background-radius: 0; -fx-background-color: pink;");
                    } 
                    else {
                        this.boutonsGrille[row][col].setStyle("-fx-background-radius: 0; -fx-background-color: white;");
                    }
                }
            }
        });
    }
    
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
}
