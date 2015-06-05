package br.com.avfinal.view.component.builder;

import javafx.util.Builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import br.com.avfinal.view.component.tabs.TabFrequency;

@Component
public class TabFrequencyBuilder implements Builder<TabFrequency> {

	@Autowired
	private ApplicationContext context;

	@Override
	public TabFrequency build() {
		return context.getBean(TabFrequency.class);
	}
}