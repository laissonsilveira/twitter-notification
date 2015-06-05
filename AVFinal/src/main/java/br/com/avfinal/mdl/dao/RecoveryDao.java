package br.com.avfinal.mdl.dao;

import java.util.List;

import br.com.avfinal.entity.RecoveryEntity;

public interface RecoveryDao {

	RecoveryEntity save(RecoveryEntity entity);

	List<RecoveryEntity> findRecoveriesBy(Integer bimester, Long idGradebook);

	RecoveryEntity update(RecoveryEntity entity);

	RecoveryEntity findBy(Long idAssessement);

}
