package org.covid.inventory.datajpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.covid.inventory.entity.Orders;
import org.springframework.stereotype.Repository;

@Repository
public class SearchRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	public List<Orders> getSearchResultForDynamicQuery(String qry) {
		TypedQuery<Orders> q = (TypedQuery<Orders>) em.createNativeQuery(qry,Orders.class);
		List<Orders> listOrder = q.getResultList();
		return listOrder;
	}

}
