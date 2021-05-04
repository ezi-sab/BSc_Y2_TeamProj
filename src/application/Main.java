package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import view.ViewManager;

public class Main extends Application {

	private static ViewManager viewManager = new ViewManager();

	@Override
	public void start(Stage primaryStage) {
		
		try {
			primaryStage = viewManager.getMainStage();
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		launch(args);	
	}
	
}
