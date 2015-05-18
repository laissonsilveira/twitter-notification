package br.com.avfinal.mdl.dao;

import java.util.List;

import br.com.avfinal.entity.BaseEntity;

/**
 * @author Laisson R. Silveira <br>
 *         Florianópolis - Jan 14, 2014 <br>
 *         <a href="mailto:laisson.silveira@hotmail.com.br">laisson.silveira@hotmail.com.br</a>
 */
public interface IGenericDAO<T extends BaseEntity> {

    public T save(T entity);

    public T update(T entity);

    public void delete(T entity);

    public T findById(Long id);
    
    public List<T> findAll();

}
