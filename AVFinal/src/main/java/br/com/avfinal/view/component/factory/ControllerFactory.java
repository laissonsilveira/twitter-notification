package br.com.avfinal.view.component.factory;

import javafx.util.Callback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ControllerFactory implements Callback<Class<?>, Object> {

	@Autowired
	private ApplicationContext context;

	@Override
	public Object call(Class<?> param) {
		return context.getBean(param);
	}

}
