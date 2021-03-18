package application;
	
import javafx.application.Application;
<<<<<<< HEAD
import javafx.stage.Stage;
//import javafx.scene.Scene;
//import javafx.scene.layout.BorderPane;
import view.viewManager;
=======

import javafx.stage.Stage;
//import javafx.scene.Scene;
//import javafx.scene.layout.BorderPane;
import view.ViewManager;
>>>>>>> master



public class Main extends Application {
<<<<<<< HEAD
=======

	// ViewManager viewManager = new ViewManager();

>>>>>>> master
	@Override
	public void start(Stage primaryStage) {
		try {
			//BorderPane root = new BorderPane();
			//Scene scene = new Scene(root,400,400);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			//primaryStage.setScene(scene);
<<<<<<< HEAD
			viewManager manager = new viewManager();
=======
			// viewManager.playSound("Space Shooter/src/view/resources/sounds/spaceinvaders1.mp3");
			ViewManager manager = new ViewManager();
>>>>>>> master
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
