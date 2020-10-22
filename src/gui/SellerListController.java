package gui;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.SellerService;

public class SellerListController implements Initializable, DataChangeListener {

	private SellerService service;

	@FXML
	private TableView<Seller> tableViewSeller; // Referência para o tableViewSeller

	@FXML
	private TableColumn<Seller, Integer> tableColumnId; // Referência para o Id do tableColumnId. Por isso o Integer

	@FXML
	private TableColumn<Seller, String> tableColumnName; // Referência para o Name do tableColumnId. Por isso o string

	@FXML
	private TableColumn<Seller, String> tableColumnEmail; // Referência para o Email
	
	@FXML
	private TableColumn<Seller, Date> tableColumnBirthDate; // Referência para a data de nascimento
	
	@FXML
	private TableColumn<Seller, Double> tableColumnBaseSalary; // Referência para o Salário
	
	@FXML
	private TableColumn<Seller, Seller> tableColumnEDIT; // Botão de edição
	
	@FXML
	private TableColumn<Seller, Seller> tableColumnREMOVE; // Botão de deletar
	
	@FXML
	private Button btNew; // Referência para o botão new em "Seller"

	private ObservableList<Seller> obsList; // Os departamentos são carregados aqui

	@FXML
	public void onBtNewAction(ActionEvent event) { // Evento que dá condição de acesso ao stage
		Stage parentStage = Utils.currentStage(event); // Referência para o Stage atual
		Seller obj = new Seller(); //O formulário é inicializado vazio.
		createDialogForm(obj,"/gui/SellerForm.fxml", parentStage);
	}

//--Dependência de SellerService-- Inversão de controle	
	public void setSellerServive(SellerService service) {
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
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		
		tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		Utils.formatTableColumnDate(tableColumnBirthDate, "dd//MM/yyyy");
		
		tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		Utils.formatTableColumnDouble(tableColumnBaseSalary, 2);
		
		
		Stage stage = (Stage) Main.getMainScene().getWindow(); // Método para deixar os limites da tabela do tamanho
																// adaptavel ao tamanho da janela do app. É feito um
																// downCasting do Stage para referencia-lo.
		tableViewSeller.prefHeightProperty().bind(stage.heightProperty()); // Método para o table acompanhar a
																				// altura da janela.
	}

// Responsável por acessar o serviço, carregar os departamentos e jogar em "ObservableList"
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Seller> list = service.findAll(); // Recupera os serviços da lista
		obsList = FXCollections.observableArrayList(list); // Instancia os dados originais
		tableViewSeller.setItems(obsList);
		initEditButtons(); // Acrescenta um novo botão com o texto "edit" em cada linha da tabela
		initRemoveButtons(); // Acrescenta um novo botão com o texto "remove" em cada linha da tabela
	}
	
	private void createDialogForm(Seller obj, String absoluteName, Stage parentStage) { // Quando criamos uma janela de diálogo, temos que informar pra ela quem que é o stage que criou essa janela
//		try {
//			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
//			Pane pane = loader.load(); // Carrega o painel
//			
//			
//			SellerFormController controller = loader.getController();
//			controller.setSeller(obj); // Injeta nesse controlador o departamento
//			controller.setSellerService(new SellerService());
//			controller.subscribeDataChangeListener(this); // Se inscreve para receber o evento
//			controller.updateFormData(); // Método para carregar os dados desse objeto no formulário		
//			
//			Stage dialogStage = new Stage();
//			dialogStage.setTitle("Enter Seller data");
//			dialogStage.setScene(new Scene(pane)); // Elemento raiz da cena é "pane"
//			dialogStage.setResizable(false); // Janela não poderá ser redimensionada
//			dialogStage.initOwner(parentStage); // Função que define quem é o stage pai dessa janela
//			dialogStage.initModality(Modality.WINDOW_MODAL); // Ela é modal. Ficará travada
//			dialogStage.showAndWait();
//			
//		}
//		catch (IOException e) {
//			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
//		}
	}

	@Override
	public void onDataChanged() { // Notificação de dados alterados
		updateTableView(); // Função que atualiza os dados da tabela alterados.
	}		
		
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("edit"); // Cria o botão "edit"
	
			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
			}
			setGraphic(button);
			button.setOnAction(
					event -> createDialogForm(obj, "/gui/SellerForm.fxml",Utils.currentStage(event))); // Ao clicar no botão, ele abre um formulário de edição usando o "createDialogForm"
			}
		});
	}
	
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("remove"); // Cria o botão "remove"
		
			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj)); // Função para remover
			}
		});
	}

	private void removeEntity(Seller obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?"); // Criação do alerta para deletar. A resposta desse "Alert" fica armazenada no "result"
		
		if (result.get() == ButtonType.OK) { // Significa que se ele apertar "OK" ele confirmou a deleção.
			if (service == null) {
				throw new IllegalStateException("Service was null"); // Se passar por aqui, significa que o service foi instanciado
			}
			try {
				service.remove(obj); // Resultado da confirmação. Ele removera;
				updateTableView();
			}
		catch (DbIntegrityException e) {
			Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
}