package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import view.ViewManager;

public class Main extends Application {
	
	private static ViewManager viewManager = new ViewManager();
	
	/**
	 * Sets the primary stage to main stage from viewManager.
	 * @override start method from inbuilt javafx start()
	 * @param primaryStage stage will be set to the main stage and shows the stage.
	 */
	@Override
	public void start(Stage primaryStage) {
		
		try {
			primaryStage = viewManager.getMainStage();
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	* Launches the main method and calls the game stage.
	* @param args is passed as argument to launch method.
	*/
	public static void main(String[] args) {
		launch(args);	
	}
	
}
