package br.com.avfinal.mdl.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;

import br.com.avfinal.entity.BaseEntity;

/**
 * @author Laisson R. Silveira <br>
 *         Florianópolis - Jan 14, 2014 <br>
 *         <a href="mailto:laisson.silveira@hotmail.com.br">laisson.silveira@hotmail.com.br</a>
 */

public class GenericDAO<T extends BaseEntity> implements IGenericDAO<T> {

	@PersistenceContext
	private EntityManager em;

	public GenericDAO() {}

	private Class<T> entityClass;

	public GenericDAO(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	@Override
	public T save(T entity) {
		entity = em.merge(entity);
		return entity;
	}

	@Override
	public T update(T entity) {
		entity = em.merge(entity);
		return entity;
	}

	@Override
	public void delete(T entity) {
		em.remove(em.merge(entity));
	}

	@Override
	public T findById(Long id) {
		T entity = em.find(entityClass, id);
		return entity;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> findAll() {
		CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
		cq.select(cq.from(entityClass));
		return em.createQuery(cq).getResultList();
	}

	protected TypedQuery<T> createQuery(String hql) {
		return em.createQuery(hql, entityClass);
	}

	protected TypedQuery<?> createQuery(Class<?> clazz, String hql) {
		return em.createQuery(hql, clazz);
	}

	protected Query createQueryGeneric(String hql) {
		return em.createQuery(hql);
	}

	@SuppressWarnings({ "unchecked", "hiding" })
	protected <T> T getSingleResult(Query query) {
		query.setMaxResults(1);
		List<T> list = query.getResultList();
		if ((list == null) || list.isEmpty() || (list.get(0) == null) || "null".equals(list.get(0))) {
			return null;
		}
		return list.get(0);
	}

	@SuppressWarnings("hiding")
	protected <T> List<T> getResultList(TypedQuery<T> query) {
		List<T> list = query.getResultList();
		if ((list == null) || list.isEmpty()) {
			return null;
		}
		return list;
	}

}
