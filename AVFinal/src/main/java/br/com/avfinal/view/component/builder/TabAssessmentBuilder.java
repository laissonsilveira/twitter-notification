package br.com.avfinal.view.component.builder;

import javafx.util.Builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import br.com.avfinal.view.component.tabs.TabAssessment;

@Component
public class TabAssessmentBuilder implements Builder<TabAssessment> {

	@Autowired
	private ApplicationContext context;

	@Override
	public TabAssessment build() {
		return context.getBean(TabAssessment.class);
	}
}