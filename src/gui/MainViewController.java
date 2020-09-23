package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

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
		System.out.println("onMenuItemDepartmentAction");
	}	
	
	@FXML
	public void onMenuItemAboutAction() { // Na a��o do bot�o, ele ir� imprimir o texto abaixo
		System.out.println("onMenuItemAboutAction");
	}	
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		
	}

}
