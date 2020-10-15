package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.exceptions.ValidationException;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {

//--- Depend�ncia para o departamento	
	
	private Department entity;
	
	private DepartmentService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>(); // Permite que outros objetos se inscrevam nessa lista para receber os eventos.
	
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
	
	public void setDepartment(Department entity) {
		this.entity = entity;
	}
		
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	@FXML
	public void onBtSaveAction(ActionEvent event) { // Controlador do bot�o "save"
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData(); // Respons�vel por pegar os dados das caixas dos form�larios e instanciar o departamento
			service.saveOrUpdate(entity); // Salva no banco de dados
			notifyDataChangeListeners();// Faz a notifica��o
			Utils.currentStage(event).close(); // Fecha a janela
		}
		catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		}
		catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void notifyDataChangeListeners() { // Executa o m�todo "onDataChanged" da interface "DataChangeListener"
		for (DataChangeListener listener : dataChangeListeners) { // Um for para cada DataChangeListener listener pertencente a lista dataChangeListeners...
			listener.onDataChanged(); // ... faz este m�todo para o evento
		}
		
	}

	private Department getFormData() {
		Department obj = new Department();
		
		ValidationException exception = new ValidationException("Validation error"); // Instanciou a sess�o
		
		obj.setId(Utils.tryParseToInt(txtId.getText())); // Pega o que for digitado na caixa do formul�rio
		
		if (txtName.getText() == null || txtName.getText().trim().equals("")) { // Se o nome for "nulo" ou for igual a espa�os nulos (string vazio), a exce��o � ativada.
			exception.addError("name", "Field can't be empty");
		}
		obj.setName(txtName.getText()); // Pega o nome digitado
		
		if (exception.getErrors().size() > 0) { // Testa se a cole��o de erros tem ao menos 1 erro. Se for verdade, lan�a a exce��o.
			throw exception;
		}
		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) { // Controlador do bot�o "cancel"
		Utils.currentStage(event).close(); // Fecha a janela
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30); // Define a quantidade m�xima de caracteres
	}
		
	public void updateFormData() { // Dados que estiverem dentro do objeto entity
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		
		txtId.setText(String.valueOf(entity.getId())); // Tem que converter o valor do Id (Inteiro) para String.
		txtName.setText(entity.getName());
	}
	
	private void setErrorMessages(Map<String, String> errors) { // Percorre a cole��o, carrega os erros e preenche os erros nas caixas de texto do programa.
		Set<String> fields = errors.keySet();
		
		if(fields.contains("name")) { // Se existir a chave "name"...
			labelErrorName.setText(errors.get("name")); // Pega a mensagem correspondente ao campo name e seta ela em "labelErrorName".
		}
		
	}
	
}


