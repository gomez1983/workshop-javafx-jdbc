package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DepartmentFormController implements Initializable {

//------Declara��o dos componentes da tela

	@FXML
	private TextField txtId; // Atributos para a caixa de preenchimento de Id

	@FXML
	private TextField txtName; // Atributos para a caixa de preenchimento de Name

	@FXML
	private Label labelErrorName; // Atributos para o label de erros. Mensagem caso d� algo errado ao preencher o nome
	
	@FXML
	private Button btSave; // Controllers dos bot�es "Save" e "Cancel"
	
	@FXML
	private Button btCancel; // Controllers dos bot�es "Save" e "Cancel"
	
	@FXML
	public void onBtSaveAction() { // Controlador do bot�o "save"
		System.out.println("onBtSaveAction");
	}
	
	@FXML
	public void onBtCancelAction() { // Controlador do bot�o "cancel"
		System.out.println("onBtCancelAction");
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30); // Define a quantidade m�xima de caracteres
	}
		
}


