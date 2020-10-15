package model.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private Map<String, String> errors = new HashMap<>(); // Carrega uma cole��o contendo todos os erros poss�veis. Map serve para guardar os erros de cada campo do formul�rio. O primeiro string � o nome do campo e o segundo a msg de erro.
	
	public ValidationException(String msg) {
		super(msg);
	}

	public Map<String, String> getErrors() { // M�todo
		return errors;
	}

	public void addError(String fieldName, String errorMessage)	 { // Permite adicionar um elemento na cole��o
		errors.put(fieldName, errorMessage); // Adiciona a chave e a mensagem de erro em "errors"
	}
}
