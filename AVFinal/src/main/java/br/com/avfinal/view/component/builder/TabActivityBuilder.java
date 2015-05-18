package br.com.avfinal.view.component.builder;

import javafx.util.Builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import br.com.avfinal.view.component.tabs.TabActivity;

@Component
public class TabActivityBuilder implements Builder<TabActivity> {

	@Autowired
	private ApplicationContext context;

	@Override
	public TabActivity build() {
		return context.getBean(TabActivity.class);
	}
}