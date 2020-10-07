package gui;

import java.net.URL;
import java.util.ResourceBundle;

import db.DbException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {

//--- Dependência para o departamento	
	
	private Department entity;
	
	private DepartmentService service;
	
	
//------Declaração dos componentes da tela

	@FXML
	private TextField txtId; // Atributos para a caixa de preenchimento de Id

	@FXML
	private TextField txtName; // Atributos para a caixa de preenchimento de Name

	@FXML
	private Label labelErrorName; // Atributos para o label de erros. Mensagem caso dê algo errado ao preencher o nome
	
	@FXML
	private Button btSave; // Controllers dos botões "Save" e "Cancel"
	
	@FXML
	private Button btCancel; // Controllers dos botões "Save" e "Cancel"
	
	public void setDepartment(Department entity) {
		this.entity = entity;
	}
		
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	@FXML
	public void onBtSaveAction(ActionEvent event) { // Controlador do botão "save"
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData(); // Responsável por pegar os dados das caixas dos form´pularios e instanciar o departamento
			service.saveOrUpdate(entity); // Salva no banco de dados
			Utils.currentStage(event).close(); // Fecha a janela
		}
		catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private Department getFormData() {
		Department obj = new Department();
		obj.setId(Utils.tryParseToInt(txtId.getText())); // Pega o que for digitado na caixa do formulário
		obj.setName(txtName.getText()); // Pega o nome digitado
		
		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) { // Controlador do botão "cancel"
		Utils.currentStage(event).close(); // Fecha a janela
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30); // Define a quantidade máxima de caracteres
	}
		
	public void updateFormData() { // Dados que estiverem dentro do objeto entity
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		
		txtId.setText(String.valueOf(entity.getId())); // Tem que converter o valor do Id (Inteiro) para String.
		txtName.setText(entity.getName());
	}
	
}


