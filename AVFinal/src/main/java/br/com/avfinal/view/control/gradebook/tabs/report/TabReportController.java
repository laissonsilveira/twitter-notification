package br.com.avfinal.view.control.gradebook.tabs.report;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.avfinal.entity.FrequencyEntity;
import br.com.avfinal.entity.GradebookEntity;
import br.com.avfinal.entity.report.FrequencyReport;
import br.com.avfinal.mdl.service.FrequencyService;
import br.com.avfinal.util.enums.Bimester;
import br.com.avfinal.util.enums.Constants;
import br.com.avfinal.view.component.MessageDisplay;
import br.com.avfinal.view.component.listCell.BimesterListCell;
import br.com.avfinal.view.component.tabs.TabReport;
import br.com.avfinal.view.control.IGenericController;
import br.com.avfinal.view.control.gradebook.GradebookController;

@Component
public class TabReportController implements IGenericController {

	private static final Logger LOG = Logger.getLogger(TabReportController.class);

	@Autowired
	private Stage stagePrimary;
	@Autowired
	private GradebookController controllerParent;
	@Autowired
	private FrequencyService frequencyService;

	private TabReport view;

	public void initializeController(TabReport tabReport) {
		view = tabReport;
		initializeController();
	}

	@Override
	public void initializeController() {
		if (view != null) {
			initComponet();
			initConfig();
			initValues();
		} else {
			LOG.fatal("View não carregada");
			new MessageDisplay(stagePrimary.getScene().getRoot(), Constants.MSG_ERROR_GENERIC.valuesToString());
		}
	}

	@Override
	public void initConfig() {
		view.getCmbBoxBimester().setCellFactory(param -> new BimesterListCell());
		view.getCmbBoxBimester().setItems(FXCollections.observableArrayList(Bimester.values()));
	}

	@Override
	public void initValues() {
		view.getCmbBoxBimester().setValue(Bimester.PRIMEIRO);
	}

	@Override
	public void initComponet() {}

	public void openReport() {

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("REPORT_LOCALE", new Locale("pt", "BR"));
			parameters.put("LOGO_SC", getClass().getResource(Constants.PATH_IMAGE_LOGO_REPORT.valuesToString()));
			parameters.put("BIMESTER", view.getCmbBoxBimester().getSelectionModel().getSelectedItem().getNumber() + "º BIMESTRE");
			putParameterGradebook(parameters);

			JasperPrint coverReport = getCoverReport(parameters), frequencyReport = null, assessmentReport = null, activityReport = null;

			if (view.getCheckFrequency().isSelected()) {
				frequencyReport = getFrequencyReport(parameters);
				addPagesInReport(coverReport, frequencyReport);
			}

			if (view.getCheckAssessment().isSelected()) {
				assessmentReport = getAssessmentReport(parameters);
				addPagesInReport(coverReport, assessmentReport);
			}

			if (view.getCheckActivity().isSelected()) {
				activityReport = getActivityReport(parameters);
				addPagesInReport(coverReport, activityReport);
			}

			final File PDFFile = createPDFFile(coverReport);
			if (PDFFile != null) {
				Desktop.getDesktop().open(PDFFile);
			}
			// JasperViewer.viewReport(coverReport, false);

		} catch (JRException | IOException e) {
			LOG.error(e.getMessage(), e);
			new MessageDisplay(view, Constants.MSG_ERROR_GENERIC.valuesToString()).showErrorMessage();
		}

	}

	private File createPDFFile(JasperPrint coverReport) throws JRException {
		File file = null;
		// Preferences prefs = Preferences.userNodeForPackage(StartAvFinal.class);
		// String pathReport = prefs.get("pathReport", null);

		// if (pathReport != null) {
		// file = new File(pathReport);
		// } else {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		file = directoryChooser.showDialog(stagePrimary);

		if (file != null) {
			final String pdfFile = file.getPath().concat("/relatorio_AVFinal.pdf");
			// prefs.put("pathReport", pdfFile);
			file = new File(pdfFile);

			if (file.exists()) {
				file.delete();
			}

			JasperExportManager.exportReportToPdfFile(coverReport, file.getPath());
			// } else {
			// prefs.remove("pathReport");
		}
		// }

		// if (file == null) {
		// throw new NullPointerException("É necessário informar uma caminho!");
		// }

		return file;
	}

	private void putParameterGradebook(Map<String, Object> parameters) {
		parameters.put("SHIFT", controllerParent.getGradebookEntity().getTurno());
		parameters.put("TEACHER", controllerParent.getGradebookEntity().getProfessor());
		parameters.put("SCHOOL_UNIT", controllerParent.getGradebookEntity().getUnidEscolar());
		parameters.put("MUNICIPALITY", controllerParent.getGradebookEntity().getMunicipio());
		parameters.put("DISCIPLINE", controllerParent.getGradebookEntity().getDisciplina());
		parameters.put("CLASS", controllerParent.getGradebookEntity().getTurma());
		parameters.put("COURSE", controllerParent.getGradebookEntity().getCurso());
		parameters.put("SERIES", controllerParent.getGradebookEntity().getSerie());
	}

	private void addPagesInReport(JasperPrint coverReport, JasperPrint otherReport) {
		if (otherReport == null)
			return;

		final List<JRPrintPage> pages = otherReport.getPages();
		if ((pages != null) && !pages.isEmpty()) {
			for (JRPrintPage page : pages) {
				coverReport.addPage(page);
			}
		}
	}

	private JasperPrint getActivityReport(Map<String, Object> parameters) throws JRException, IOException {
		URL jasperActivity = getClass().getResource(Constants.JASPER_ACTIVITY.valuesToString());

		if (jasperActivity == null) {
			throw new FileNotFoundException("Jasper not found!");
		}

		List<GradebookEntity> activities = null;

		if (activities == null) {
			return null;
		}

		JRBeanCollectionDataSource dsActivity = new JRBeanCollectionDataSource(activities);

		return JasperFillManager.fillReport(jasperActivity.openStream(), parameters, dsActivity);
	}

	private JasperPrint getAssessmentReport(Map<String, Object> parameters) throws JRException, IOException {
		URL jasperAssessment = getClass().getResource(Constants.JASPER_ASSESSMENT.valuesToString());

		if (jasperAssessment == null) {
			throw new FileNotFoundException("Jasper not found!");
		}

		List<GradebookEntity> assessments = new ArrayList<GradebookEntity>();

		if (assessments == null) {
			return null;
		}

		JRBeanCollectionDataSource dsAssessment = new JRBeanCollectionDataSource(assessments);

		return JasperFillManager.fillReport(jasperAssessment.openStream(), parameters, dsAssessment);
	}

	private JasperPrint getFrequencyReport(Map<String, Object> parameters) throws JRException, IOException {
		URL jasperFrequency = getClass().getResource(Constants.JASPER_FREQUENCY.valuesToString());

		if (jasperFrequency == null) {
			throw new FileNotFoundException("Jasper not found!");
		}

		// List<GradebookEntity> frequencies = new ArrayList<GradebookEntity>();
		// final ObservableList<StudentEntity> students = controllerParent.getListStudentsGradebook();

		List<FrequencyReport> frequenciesReport = new ArrayList<FrequencyReport>();

		final Integer bimester = view.getCmbBoxBimester().getSelectionModel().getSelectedItem().getNumber();
		final List<FrequencyEntity> frequencies = frequencyService.findByGradebook(controllerParent.getGradebookEntity().getId(), bimester);

		if (frequencies == null) {
			return null;
		}

		for (FrequencyEntity frequencyEntity : frequencies) {
			frequenciesReport.add(new FrequencyReport(frequencyEntity.getStudent().getName(), frequencyEntity.getStudent().getMatriculation(),
					frequencyEntity.getFrequencyDate().getDatFrequency(), frequencyEntity.getFlgFrequencyReport01()));
		}

		JRBeanCollectionDataSource dsFrequency = new JRBeanCollectionDataSource(frequenciesReport);

		return JasperFillManager.fillReport(jasperFrequency.openStream(), parameters, dsFrequency);
	}

	private JasperPrint getCoverReport(Map<String, Object> parameters) throws JRException, IOException {
		URL jasperCover = getClass().getResource(Constants.JASPER_COVER.valuesToString());

		if (jasperCover == null) {
			throw new FileNotFoundException("Jasper not found!");
		}

		List<GradebookEntity> gradebooks = new ArrayList<GradebookEntity>();
		gradebooks.add(controllerParent.getGradebookEntity());

		JRBeanCollectionDataSource dsCover = new JRBeanCollectionDataSource(gradebooks);

		return JasperFillManager.fillReport(jasperCover.openStream(), parameters, dsCover);
	}

}
