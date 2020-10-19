package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable, DataChangeListener {

	private DepartmentService service;

	@FXML
	private TableView<Department> tableViewDepartment; // Referência para o tableViewDepartment

	@FXML
	private TableColumn<Department, Integer> tableColumnId; // Referência para o Id do tableColumnId. Por isso o Integer

	@FXML
	private TableColumn<Department, String> tableColumnName; // Referência para o Name do tableColumnId. Por isso o string

	@FXML
	private TableColumn<Department, Department> tableColumnEDIT;
	
	@FXML
	private Button btNew; // Referência para o botão new em "Department"

	private ObservableList<Department> obsList; // Os departamentos são carregados aqui

	@FXML
	public void onBtNewAction(ActionEvent event) { // Evento que dá condição de acesso ao stage
		Stage parentStage = Utils.currentStage(event); // Referência para o Stage atual
		Department obj = new Department(); //O formulário é inicializado vazio.
		createDialogForm(obj,"/gui/DepartmentForm.fxml", parentStage);
	}

//--Dependência de DepartmentService-- Inversão de controle	
	public void setDepartmentServive(DepartmentService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();

	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("Id")); // Padrão para iniciar o comportamento das
																				// colunas.
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("Name")); // Padrão para iniciar o comportamento
																					// das colunas.

		Stage stage = (Stage) Main.getMainScene().getWindow(); // Método para deixar os limites da tabela do tamanho
																// adaptavel ao tamanho da janela do app. É feito um
																// downCasting do Stage para referencia-lo.
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty()); // Método para o table acompanhar a
																				// altura da janela.
	}

// Responsável por acessar o serviço, carregar os departamentos e jogar em "ObservableList"
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Department> list = service.findAll(); // Recupera os serviços da lista
		obsList = FXCollections.observableArrayList(list); // Instancia os dados originais
		tableViewDepartment.setItems(obsList);
		initEditButtons(); // Acrescenta um novo botão com o texto "edit" em cada linha da tabela
	}
	
	private void createDialogForm(Department obj, String absoluteName, Stage parentStage) { // Quando criamos uma janela de diálogo, temos que informar pra ela quem que é o stage que criou essa janela
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load(); // Carrega o painel
			
			
			DepartmentFormController controller = loader.getController();
			controller.setDepartment(obj); // Injeta nesse controlador o departamento
			controller.setDepartmentService(new DepartmentService());
			controller.subscribeDataChangeListener(this); // Se inscreve para receber o evento
			controller.updateFormData(); // Método para carregar os dados desse objeto no formulário		
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Department data");
			dialogStage.setScene(new Scene(pane)); // Elemento raiz da cena é "pane"
			dialogStage.setResizable(false); // Janela não poderá ser redimensionada
			dialogStage.initOwner(parentStage); // Função que define quem é o stage pai dessa janela
			dialogStage.initModality(Modality.WINDOW_MODAL); // Ela é modal. Ficará travada
			dialogStage.showAndWait();
			
		}
		catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() { // Notificação de dados alterados
		updateTableView(); // Função que atualiza os dados da tabela alterados.
	}		
		
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("edit");
	
			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
			}
			setGraphic(button);
			button.setOnAction(
					event -> createDialogForm(obj, "/gui/DepartmentForm.fxml",Utils.currentStage(event))); // Ao clicar no botão, ele abre um formulário de edição usando o "createDialogForm"
			}
		});
	}
}