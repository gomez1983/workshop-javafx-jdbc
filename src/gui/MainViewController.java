package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;
import model.services.SellerService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemSeller;

	@FXML
	private MenuItem menuItemDepartment;

	@FXML
	private MenuItem menuItemAbout;

	@FXML
	public void onMenuItemSellerAction() { 
		loadView("/gui/SellerList.fxml", (SellerListController controller) -> {
			controller.setSellerServive(new SellerService());
			controller.updateTableView(); // Ação de inicialização do controller DepartmentListController
		});
	}

	@FXML
	public void onMenuItemDepartmentAction() { 
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
			controller.setDepartmentServive(new DepartmentService());
			controller.updateTableView(); // Ação de inicialização do controller DepartmentListController
		});
	}

	@FXML
	public void onMenuItemAboutAction() { // Na ação do botão, ele irá abrir o item indicado
		loadView("/gui/About.fxml", x -> {
		}); // Item e caminho indicado. Será a janela que irá aparecer ao clicar no botão
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {

	}

	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) { // Carregar uma tela
																						// não será parado
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();

			Scene mainScene = Main.getMainScene(); // Mostra a view dentro da janela principal
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent(); // getRoot pega o primeiro elemento
		
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			T controller = loader.getController();
			initializingAction.accept(controller);
			
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}

	}

}
