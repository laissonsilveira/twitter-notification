package br.com.avfinal.view.component.builder;

import br.com.avfinal.view.component.tabs.TabReport;
import javafx.util.Builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class TabReportBuilder implements Builder<TabReport> {

	@Autowired
	private ApplicationContext context;

	@Override
	public TabReport build() {
		return context.getBean(TabReport.class);
	}
}