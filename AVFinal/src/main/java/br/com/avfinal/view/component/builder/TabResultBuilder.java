package br.com.avfinal.view.component.builder;

import javafx.util.Builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import br.com.avfinal.view.component.tabs.TabResult;

@Component
public class TabResultBuilder implements Builder<TabResult> {

	@Autowired
	private ApplicationContext context;

	@Override
	public TabResult build() {
		return context.getBean(TabResult.class);
	}
}