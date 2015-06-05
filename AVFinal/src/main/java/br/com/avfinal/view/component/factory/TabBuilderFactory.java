package br.com.avfinal.view.component.factory;

import javafx.fxml.JavaFXBuilderFactory;
import javafx.util.Builder;
import javafx.util.BuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import br.com.avfinal.view.component.builder.TabActivityBuilder;
import br.com.avfinal.view.component.builder.TabAssessmentBuilder;
import br.com.avfinal.view.component.builder.TabFrequencyBuilder;
import br.com.avfinal.view.component.builder.TabReportBuilder;
import br.com.avfinal.view.component.builder.TabResultBuilder;
import br.com.avfinal.view.component.tabs.TabActivity;
import br.com.avfinal.view.component.tabs.TabAssessment;
import br.com.avfinal.view.component.tabs.TabFrequency;
import br.com.avfinal.view.component.tabs.TabReport;
import br.com.avfinal.view.component.tabs.TabResult;

@Component
public class TabBuilderFactory implements BuilderFactory {

	@Autowired
	private ApplicationContext context;

	private JavaFXBuilderFactory defaultBuilderFactory = new JavaFXBuilderFactory();

	@Override
	public Builder<?> getBuilder(Class<?> type) {
		Builder<?> builder;
		if (type == TabFrequency.class) {
			builder = context.getBean(TabFrequencyBuilder.class);
		} else if (type == TabAssessment.class) {
			builder = context.getBean(TabAssessmentBuilder.class);
		} else if (type == TabActivity.class) {
			builder = context.getBean(TabActivityBuilder.class);
		} else if (type == TabResult.class) {
			builder = context.getBean(TabResultBuilder.class);
		} else if (type == TabReport.class) {
			builder = context.getBean(TabReportBuilder.class);
		} else {
			builder = defaultBuilderFactory.getBuilder(type);
		}
		return builder;
	}

}