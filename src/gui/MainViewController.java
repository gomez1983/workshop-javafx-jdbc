package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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

public class MainViewController implements Initializable{

	@FXML
	private MenuItem menuItemSeller;
	
	@FXML
	private MenuItem menuItemDepartment;
	
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	public void onMenuItemSellerAction() { // Na a��o do bot�o, ele ir� imprimir o texto abaixo
		System.out.println("onMenuItemSellerAction");
	}

	@FXML
	public void onMenuItemDepartmentAction() { // Na a��o do bot�o, ele ir� imprimir o texto abaixo
		loadView2("/gui/DepartmentList.fxml"); // Item e caminho indicado. Ser� a janela que ir� aparecer ao clicar no bot�o
	}	
	
	@FXML
	public void onMenuItemAboutAction() { // Na a��o do bot�o, ele ir� abrir o item indicado
		loadView("/gui/About.fxml"); // Item e caminho indicado. Ser� a janela que ir� aparecer ao clicar no bot�o
	}	
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		
	}
	
	private synchronized void loadView(String absoluteName) { // Carregar uma tela nova da app. Com synchronized ele garante que todo o processamento n�o ser� parado
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
		
			Scene mainScene = Main.getMainScene(); // Mostra a view dentro da janela principal
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent(); // getRoot pega o primeiro elemento da main principal. Esse m�todo todo pega a refer�ncia para o VBox da janela principal do APP.
		
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
		}
		catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
			
	}

	private synchronized void loadView2(String absoluteName) { // Carregar uma tela nova da app. Com synchronized ele garante que todo o processamento n�o ser� parado
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
		
			Scene mainScene = Main.getMainScene(); // Mostra a view dentro da janela principal
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent(); // getRoot pega o primeiro elemento da main principal. Esse m�todo todo pega a refer�ncia para o VBox da janela principal do APP.
		
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			DepartmentListController controller = loader.getController();
			controller.setDepartmnetServive(new DepartmentService());
			controller.updateTableView();
		}
		catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
			
	}
	
}
