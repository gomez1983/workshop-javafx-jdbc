package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {

//--- Depend�ncia para o departamento	
	
	private Seller entity;
	
	private SellerService service;
	
	private DepartmentService departmentService;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>(); // Permite que outros objetos se inscrevam nessa lista para receber os eventos.
	
//------Declara��o dos componentes da tela

	@FXML
	private TextField txtId; // Atributos para a caixa de preenchimento de Id

	@FXML
	private TextField txtName; // Atributos para a caixa de preenchimento de Name

	@FXML
	private TextField txtEmail; // Atributos para a caixa de preenchimento de Email
	
	@FXML
	private DatePicker dpBirthDate; // Atributos para a caixa de preenchimento da data de nascimento
	
	@FXML
	private TextField txtBaseSalary; // Atributos para a caixa de preenchimento do valor de sal�rio base
	
	@FXML
	private ComboBox<Department> comboBoxDepartment; // Atributo para um Combobox. Os objetos v�o ser do tipo Deparment. O nome ser� ComboBox
	
	@FXML
	private Label labelErrorName; // Atributos para o label de erros. Mensagem caso d� algo errado ao preencher o nome
	
	@FXML
	private Label labelErrorEmail; // Atributos para o label de erros. Mensagem caso d� algo errado ao preencher o email
	
	@FXML
	private Label labelErrorBirthDate; // Atributos para o label de erros. Mensagem caso d� algo errado ao preencher a data de nascimento
	
	@FXML
	private Label labelErrorBaseSalary; // Atributos para o label de erros. Mensagem caso d� algo errado ao preencher o valor do sal�rio base
	
	@FXML
	private Button btSave; // Controllers dos bot�es "Save" e "Cancel"
	
	@FXML
	private Button btCancel; // Controllers dos bot�es "Save" e "Cancel"
	
	private ObservableList<Department> obsList;
	
	public void setSeller(Seller entity) {
		this.entity = entity;
	}
		
	public void setServices(SellerService service, DepartmentService departmentService) {
		this.service = service;
		this.departmentService = departmentService;
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

	private Seller getFormData() {
		Seller obj = new Seller();
		
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
		Constraints.setTextFieldMaxLength(txtName, 70); // Define a quantidade m�xima de caracteres
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
		
		initializeComboBoxDepartment();
	}
		
	public void updateFormData() { // Dados que estiverem dentro do objeto entity
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		
		txtId.setText(String.valueOf(entity.getId())); // Joga o objeto da entidade (entity) dentro de txtId. Tem que converter o valor do Id (Inteiro) para String.
		txtName.setText(entity.getName()); // joga o objeto getName dentro de txtName.
		txtEmail.setText(entity.getEmail());
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary())); // O baseSalary � um valor Double. Por isso, precisamos converter a string em Double.
		
		if (entity.getBirthDate() != null) { // S� converte a data se ela for diferente de nulo.
			dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault())); // Mostra a data no formato local do PC que est� acessando.
		}
		if(entity.getDepartment() == null) {
			comboBoxDepartment.getSelectionModel().selectFirst();
		}
		else {
			comboBoxDepartment.setValue(entity.getDepartment());
		}
		
	}
	
	public void loadAssociatedObjects() {
		if (departmentService == null) {
			throw new IllegalStateException("DepartmentService was null");
		}
		
		List<Department> list = departmentService.findAll(); // Carrega os departamentos do banco de dados
		obsList = FXCollections.observableArrayList(list); //Joga os departamentos dentro da lista
		comboBoxDepartment.setItems(obsList);
	}
	
	private void setErrorMessages(Map<String, String> errors) { // Percorre a cole��o, carrega os erros e preenche os erros nas caixas de texto do programa.
		Set<String> fields = errors.keySet();
		
		if(fields.contains("name")) { // Se existir a chave "name"...
			labelErrorName.setText(errors.get("name")); // Pega a mensagem correspondente ao campo name e seta ela em "labelErrorName".
		}
	}
	
	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxDepartment.setCellFactory(factory);
		comboBoxDepartment.setButtonCell(factory.call(null));
	}
}