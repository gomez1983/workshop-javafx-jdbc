package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable {

	private DepartmentService service;

	@FXML
	private TableView<Department> tableViewDepartment; // Refer�ncia para o tableViewDepartment

	@FXML
	private TableColumn<Department, Integer> tableColumnId; // Refer�ncia para o Id do tableColumnId. Por isso o Integer

	@FXML
	private TableColumn<Department, String> tableColumnName; // Refer�ncia para o Name do tableColumnId. Por isso o
																// String

	@FXML
	private Button btNew; // Refer�ncia para o bot�o new em "Department"

	private ObservableList<Department> obsList; // Os departamentos s�o carregados aqui

	@FXML
	public void onBtNewAction(ActionEvent event) { // Evento que d� condi��o de acesso ao stage
		Stage parentStage = Utils.currentStage(event); // Refer�ncia para o Stage atual
		Department obj = new Department(); //O formul�rio � inicializado vazio.
		createDialogForm(obj,"/gui/DepartmentForm.fxml", parentStage);
	}

//--Depend�ncia de DepartmentService-- Invers�o de controle	
	public void setDepartmentServive(DepartmentService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();

	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("Id")); // Padr�o para iniciar o comportamento das
																				// colunas.
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("Name")); // Padr�o para iniciar o comportamento
																					// das colunas.

		Stage stage = (Stage) Main.getMainScene().getWindow(); // M�todo para deixar os limites da tabela do tamanho
																// adaptavel ao tamanho da janela do app. � feito um
																// downCasting do Stage para referencia-lo.
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty()); // M�todo para o table acompanhar a
																				// altura da janela.
	}

// Respons�vel por acessar o servi�o, carregar os departamentos e jogar em "ObservableList"
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Department> list = service.findAll(); // Recupera os servi�os da lista
		obsList = FXCollections.observableArrayList(list); // Instancia os dados originais
		tableViewDepartment.setItems(obsList);
	}
	
	private void createDialogForm(Department obj, String absoluteName, Stage parentStage) { // Quando criamos uma janela de di�logo, temos que informar pra ela quem que � o stage que criou essa janela
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load(); // Carrega o painel
			
			
			DepartmentFormController controller = loader.getController();
			controller.setDepartment(obj); // Injeta nesse controlador o departamento
			controller.updateFormData(); // M�todo para carregar os dados desse objeto no formul�rio		
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Department data");
			dialogStage.setScene(new Scene(pane)); // Elemento raiz da cena � "pane"
			dialogStage.setResizable(false); // Janela n�o poder� ser redimensionada
			dialogStage.initOwner(parentStage); // Fun��o que define quem � o stage pai dessa janela
			dialogStage.initModality(Modality.WINDOW_MODAL); // Ela � modal. Ficar� travada
			dialogStage.showAndWait();
			
		}
		catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
	
}