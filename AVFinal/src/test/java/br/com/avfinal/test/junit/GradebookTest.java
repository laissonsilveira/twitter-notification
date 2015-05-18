package br.com.avfinal.test.junit;

import java.util.Date;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import br.com.avfinal.entity.FrequencyEntity;
import br.com.avfinal.entity.GradebookEntity;
import br.com.avfinal.entity.StudentEntity;
import br.com.avfinal.mdl.service.FrequencyService;
import br.com.avfinal.mdl.service.GradebookService;
import br.com.avfinal.mdl.service.StudentService;
import br.com.avfinal.mdl.service.impl.FrequencyServiceImpl;
import br.com.avfinal.mdl.service.impl.GradebookServiceImpl;
import br.com.avfinal.mdl.service.impl.StudentServiceImpl;

@Ignore
public class GradebookTest {

	static final Logger logger = Logger.getLogger(GradebookTest.class);

	public GradebookTest() {
		BasicConfigurator.configure();
	}

	@Test
	public void getFrequency() {
		FrequencyService service = new FrequencyServiceImpl();
		FrequencyEntity entity = service.findBy(2L, new Date());
		Assert.assertNotNull(entity);
	}

	@Test
	public void getGradebook() {
		GradebookService service = new GradebookServiceImpl();
		List<GradebookEntity> list = null;
		list = service.findAll();
		for (GradebookEntity entity : list) {
			logger.info(entity.getId() + " " + entity.getTurma());
		}
		Assert.assertNotNull(list);
	}

	@Test
	public void getStudent() {
		StudentService service = new StudentServiceImpl();
		final List<StudentEntity> allStudents = service.findAll();

		Assert.assertNotNull(allStudents);

		for (StudentEntity studentEntity : allStudents) {
			logger.info(studentEntity.getName());
		}
	}

}
