package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

	public static Stage currentStage(ActionEvent event) { //Acessar o Stage onde o controle que recebeu o evento est�
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}
}
