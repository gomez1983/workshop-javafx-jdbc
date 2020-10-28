package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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

//--- Dependência para o departamento	
	
	private Seller entity;
	
	private SellerService service;
	
	private DepartmentService departmentService;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>(); // Permite que outros objetos se inscrevam nessa lista para receber os eventos.
	
//------Declaração dos componentes da tela

	@FXML
	private TextField txtId; // Atributos para a caixa de preenchimento de Id

	@FXML
	private TextField txtName; // Atributos para a caixa de preenchimento de Name

	@FXML
	private TextField txtEmail; // Atributos para a caixa de preenchimento de Email
	
	@FXML
	private DatePicker dpBirthDate; // Atributos para a caixa de preenchimento da data de nascimento
	
	@FXML
	private TextField txtBaseSalary; // Atributos para a caixa de preenchimento do valor de salário base
	
	@FXML
	private ComboBox<Department> comboBoxDepartment; // Atributo para um Combobox. Os objetos vão ser do tipo Deparment. O nome será ComboBox
	
	@FXML
	private Label labelErrorName; // Atributos para o label de erros. Mensagem caso dê algo errado ao preencher o nome
	
	@FXML
	private Label labelErrorEmail; // Atributos para o label de erros. Mensagem caso dê algo errado ao preencher o email
	
	@FXML
	private Label labelErrorBirthDate; // Atributos para o label de erros. Mensagem caso dê algo errado ao preencher a data de nascimento
	
	@FXML
	private Label labelErrorBaseSalary; // Atributos para o label de erros. Mensagem caso dê algo errado ao preencher o valor do salário base
	
	@FXML
	private Button btSave; // Controllers dos botões "Save" e "Cancel"
	
	@FXML
	private Button btCancel; // Controllers dos botões "Save" e "Cancel"
	
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
	public void onBtSaveAction(ActionEvent event) { // Controlador do botão "save"
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData(); // Responsável por pegar os dados das caixas dos formúlarios e instanciar o departamento
			service.saveOrUpdate(entity); // Salva no banco de dados
			notifyDataChangeListeners();// Faz a notificação
			Utils.currentStage(event).close(); // Fecha a janela
		}
		catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		}
		catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void notifyDataChangeListeners() { // Executa o método "onDataChanged" da interface "DataChangeListener"
		for (DataChangeListener listener : dataChangeListeners) { // Um for para cada DataChangeListener listener pertencente a lista dataChangeListeners...
			listener.onDataChanged(); // ... faz este método para o evento
		}
		
	}

	private Seller getFormData() { // Pega os dados preenchidos no formulário e carrega um objeto com esses dados e o retorna para "obj"
		Seller obj = new Seller();
		
		ValidationException exception = new ValidationException("Validation error"); // Instanciou a sessão
		
		obj.setId(Utils.tryParseToInt(txtId.getText())); // Pega o que for digitado na caixa do formulário
		
		if (txtName.getText() == null || txtName.getText().trim().equals("")) { // Se o nome for "nulo" ou for igual a espaços nulos (string vazio), a exceção é ativada.
			exception.addError("name", "Field can't be empty");
		}
		obj.setName(txtName.getText()); // Pega o nome digitado
		
		if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) { // Se campo for "nulo" ou for igual a espaços nulos (string vazio), a exceção é ativada.
			exception.addError("email", "Field can't be empty");
		}
		obj.setEmail(txtEmail.getText()); // Pega o email digitado
		
		
		if (dpBirthDate.getValue() == null) {
			exception.addError("birthDate", "Field can't be empty");
		}
		else {
		Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault())); // Converte a data escolhida no horário do computador do usuário, para o instant que é uma data independente de localidade.
		obj.setBirthDate(Date.from(instant)); //Converte o instant para date
		}
		
		if (txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals("")) { // Se o nome for "nulo" ou for igual a espaços nulos (string vazio), a exceção é ativada.
			exception.addError("baseSalary", "Field can't be empty");
		}
		
		obj.setBaseSalary(Utils.tryParseToDouble(txtBaseSalary.getText()));
		
		obj.setDepartment(comboBoxDepartment.getValue()); // Pega o departamento do ComboBox e joga para o "obj"
		
		if (exception.getErrors().size() > 0) { // Testa se a coleção de erros tem ao menos 1 erro. Se for verdade, lança a exceção.
			throw exception;
		}
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
		Constraints.setTextFieldMaxLength(txtName, 70); // Define a quantidade máxima de caracteres
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
		txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary())); // O baseSalary é um valor Double. Por isso, precisamos converter a string em Double.
		
		if (entity.getBirthDate() != null) { // Só converte a data se ela for diferente de nulo.
			dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault())); // Mostra a data no formato local do PC que está acessando.
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
	
	private void setErrorMessages(Map<String, String> errors) { // Percorre a coleção, carrega os erros e preenche os erros nas caixas de texto do programa.
		Set<String> fields = errors.keySet();
		
		labelErrorName.setText((fields.contains("name") ? errors.get("name") : "")); 
		labelErrorEmail.setText((fields.contains("email") ? errors.get("email") : ""));
		labelErrorBirthDate.setText((fields.contains("birthDate") ? errors.get("birthDate") : ""));
		labelErrorBaseSalary.setText((fields.contains("baseSalary") ? errors.get("baseSalary") : ""));
					
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