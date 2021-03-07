package application;
	
import javafx.application.Application;

import javafx.stage.Stage;
//import javafx.scene.Scene;
//import javafx.scene.layout.BorderPane;
import view.ViewManager;



public class Main extends Application {

	ViewManager viewManager = new ViewManager();

	@Override
	public void start(Stage primaryStage) {
		try {
			//BorderPane root = new BorderPane();
			//Scene scene = new Scene(root,400,400);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			//primaryStage.setScene(scene);
			viewManager.playSound("Space Shooter/src/view/resources/sounds/spaceinvaders1.mp3");
			ViewManager manager = new ViewManager();
			primaryStage = manager.getMainStage();
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
