package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml")); // Instancia do caminho da view
			Parent parent = loader.load(); // Carrega a view
			Scene mainScene = new Scene(parent); // Cria a cena - Objeto da cena principal e passando o argumento "parent"
			primaryStage.setScene(mainScene); // Seta como a cena principal
			primaryStage.setTitle("Sample JavaFX application"); // Título da cena principal - é o nome que fica na parte superior da janela da aplicação.
			primaryStage.show(); // Mostra a cena principal
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
