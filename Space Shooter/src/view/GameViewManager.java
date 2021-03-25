/*package view;

import java.io.IOException;

import view.Controller;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Ship;

public class GameViewManager {

    private AnchorPane gamePane;
    private Scene gameScene;
    private Stage gameStage;

    private static final int GAME_WIDTH = 2000;//changed from 600
    private static final int GAME_HEIGHT = 800;
    
    private Stage menuStage;
    private ImageView ship;

    public GameViewManager() {
    	initializeStage();
        //createKeyListeners();
    }
    
    
     private void createKeyListeners() {
        gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
            	if(event.getCode() == KeyCode.LEFT) {
            		
            	}
            	else if (event.getCode() == KeyCode.RIGHT) {
            		
            	}

            }
        });

        gameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {

            }
        });
    } 
    
    private void initializeStage() {
    	gamePane = new AnchorPane();
    	gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
    	gameStage = new Stage();
    	gameStage.setScene(gameScene);
    }
    
    public void createNewGame(Stage menuStage, Ship chosenShip) throws Exception {
    	this.menuStage = menuStage;
    	this.menuStage.hide();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/StarShooter.fxml"));
        Parent root = loader.load();
        gameStage.setTitle("Space Shooter");
        Controller controller = loader.getController();
        root.setOnKeyPressed(controller);
        double sceneWidth = controller.getBoardWidth();
        double sceneHeight = controller.getBoardHeight();
        gameStage.setScene(new Scene(root, sceneWidth, sceneHeight));
        gameStage.show();
        root.requestFocus();{
    	gameStage.show();
        }
    }
        
    
} */