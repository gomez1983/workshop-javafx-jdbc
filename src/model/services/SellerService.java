package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService {

	private SellerDao dao = DaoFactory.createSellerDao();
	
	public List<Seller> findAll() { // Recupera todos os vendedores
		return dao.findAll();
	}
	
	public void saveOrUpdate(Seller obj) { // Verifica se tem que inserir um novo vendedor ou atualizar o departamento existente 
		if (obj.getId() == null) { // Se o Id for nulo, significa que ele n�o existe e que voc� est� inserindo um novo no banco de dados.
			dao.insert(obj);
		}
		else {
			dao.update(obj); // Atualiza
		}
	}
	
	public void remove(Seller obj) { //M�todo para remover um vendedor do Banco de Dados.
		dao.deleteById(obj.getId());
	}
	
}
