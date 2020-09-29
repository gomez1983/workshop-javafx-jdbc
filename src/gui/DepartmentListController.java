package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable {

	private DepartmentService service;

	@FXML
	private TableView<Department> tableViewDepartment; // Referência para o tableViewDepartment

	@FXML
	private TableColumn<Department, Integer> tableColumnId; // Referência para o Id do tableColumnId. Por isso o Integer

	@FXML
	private TableColumn<Department, String> tableColumnName; // Referência para o Name do tableColumnId. Por isso o
																// String

	@FXML
	private Button btNew; // Referência para o botão new em "Department"

	private ObservableList<Department> obsList; // Os departamentos são carregados aqui

	@FXML
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
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
	}
}