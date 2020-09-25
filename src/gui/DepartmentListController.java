package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;

public class DepartmentListController implements Initializable {

	@FXML
	private TableView<Department> tableViewDepartment; //Refer�ncia para o tableViewDepartment
	
	
	@FXML
	private TableColumn<Department, Integer> tableColumnId; //Refer�ncia para o Id do tableColumnId. Por isso o Integer

	@FXML
	private TableColumn<Department, String> tableColumnName; //Refer�ncia para o Name do tableColumnId. Por isso o String

	@FXML
	private Button btNew; // Refer�ncia para o bot�o new em "Department"

	@FXML
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
	}
	
	
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("Id")); // Padr�o para iniciar o comportamento das colunas.
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("Name")); // Padr�o para iniciar o comportamento das colunas.
		
		Stage stage = (Stage) Main.getMainScene().getWindow(); // M�todo para deixar os limites da tabela do tamanho adaptavel ao tamanho da janela do app. � feito um downCasting do Stage para referencia-lo.
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty()); // M�todo para o table acompanhar a altura da janela. 
	}

}
