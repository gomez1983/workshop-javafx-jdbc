package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml")); // Instancia do caminho da view
			ScrollPane scrollPane = loader.load(); // Carrega a view
			
			scrollPane.setFitToHeight(true); // Faz a barra de menu ficar na altura da tela
			scrollPane.setFitToWidth(true); // Faz a barra de menu ficar na largura da tela
			
			Scene mainScene = new Scene(scrollPane); // Cria a cena - Objeto da cena principal e passando o argumento "parent"
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
