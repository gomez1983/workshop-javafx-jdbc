package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

	public static Stage currentStage(ActionEvent event) { //Acessar o Stage onde o controle que recebeu o evento est�
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}

	public static Integer tryParseToInt(String str) {
		try {
			return Integer.parseInt(str); // Converte o n�mero e retorna n�mero
		}
		catch (NumberFormatException e) {
			return null; // Retorna nulo
		}
	}
}
