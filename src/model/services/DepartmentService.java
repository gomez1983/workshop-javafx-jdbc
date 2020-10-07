package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {

	private DepartmentDao dao = DaoFactory.createDepartmentDao();
	
	public List<Department> findAll() {
		return dao.findAll();
	}
	
	public void saveOrUpdate(Department obj) { // Verifica se tem que inserir um novo departamento ou atualizar o departamento existente 
		if (obj.getId() == null) { // Se o Id for nulo, significa que ele não existe e que você está inserindo um novo no banco de dados.
			dao.insert(obj);
		}
		else {
			dao.update(obj); // Atualiza
		}
	}
}
