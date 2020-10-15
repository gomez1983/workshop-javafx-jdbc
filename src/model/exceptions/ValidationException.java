package model.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private Map<String, String> errors = new HashMap<>(); // Carrega uma coleção contendo todos os erros possíveis. Map serve para guardar os erros de cada campo do formulário. O primeiro string é o nome do campo e o segundo a msg de erro.
	
	public ValidationException(String msg) {
		super(msg);
	}

	public Map<String, String> getErrors() { // Método
		return errors;
	}

	public void addError(String fieldName, String errorMessage)	 { // Permite adicionar um elemento na coleção
		errors.put(fieldName, errorMessage); // Adiciona a chave e a mensagem de erro em "errors"
	}
}
