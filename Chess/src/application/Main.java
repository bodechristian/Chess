package application;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	public static FXMLLoader fxmlLoader;
	
	@Override
	public void start(Stage primaryStage) throws IOException {		
		//Saving the controller in static variable to make it accesible from anywhere (idk if needed yet)
		URL location = getClass().getResource("Chessboard.fxml");	
		fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(location);
		fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());		
		Parent root = (Parent) fxmlLoader.load(location.openStream());
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.sizeToScene();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
