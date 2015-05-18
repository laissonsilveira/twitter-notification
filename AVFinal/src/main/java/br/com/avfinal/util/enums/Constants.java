package br.com.avfinal.util.enums;

public enum Constants {

	// BD
	MSG_ERROR_DB("Erro com banco de dados. Contate seu administrador."),
	MSG_ERROR_DB_CONSTRAINT_INTEGRITY("Esse registro está relacionado com alguma informação do sistema."),

	// Calculos
	CONST_CALC_MEDIA_BIMONTHLY(1.7D),
	CONST_CALC_EXAM_FINAL(1.3D),
	CONST_POINTS_MINIMO_FINAL(14D),
	CONST_MEDIA_BIMONTHLY_MINIMA(7D),

	// Msgs
	MSG_FAILURE_REMOVE("Este registro não pode ser removido!"),
	MSG_SUCESS_REMOVE("Registro removido com sucesso!"),
	MSG_SUCESS_EDIT("Registro editado com sucesso!"),
	MSG_SUCESS_SAVE("Registro salvo com sucesso!"),
	MSG_ERROR_SELECT_DAILY("Selecione um Diário!"),
	MSG_ERROR_SELECT_STUDENT("Nenhum aluno selecionado para presença!"),
	MSG_ERROR_SELECT_DATE("Selecione uma Data!"),
	MSG_ERROR_SELECTED_DATE("Data selecionada deve ser menor ou igual a data atual!"),
	MSG_ERROR_STUDENT_EXISTS("Já existe um aluno com as mesmas informações (Nome e Matrícula)!"),
	MSG_QUESTION_REMOVE("Deseja excluir este registro?"),
	MSG_VALIDATE_USER("Usuário ou senha inválidos!"),
	MSG_ERROR_GENERIC("Erro interno, contate o administrador do sistema."),
	MSG_FIELD_REQUIRED("Campo Obrigatório!"),
	MSG_FIELD_INVALID("Campo Inválido"),
	MSG_FIELD_REQUIRED_GENERAL("Foram encontrados alguns erros. Corrija-os para prosseguir!"),

	// Caminho fxml
	PATH_FXML_GRADEBOOK("/fxml/Gradebook.fxml"),
	PATH_FXML_RECOVERY("/fxml/RecoveryModal.fxml"),
	PATH_FXML_HOMESCREEN("/fxml/HomeScreen.fxml"),
	PATH_FXML_LOGIN("/fxml/Login.fxml"),
	PATH_FXML_ASSESSMENT_MODAL("/fxml/AssessmentModal.fxml"),
	PATH_FXML_NEW_ASSESSMENT_MODAL("/fxml/NewAssessmentModal.fxml"),
	PATH_FXML_TAB_FREQUENCY("/fxml/TabFrequency.fxml"),
	PATH_FXML_TAB_ASSESSMENT("/fxml/TabAssessment.fxml"),
	PATH_FXML_TAB_ACTIVITY("/fxml/TabActivity.fxml"),
	PATH_FXML_TAB_RESULT("/fxml/TabResult.fxml"),
	PATH_FXML_TAB_REPORT("/fxml/TabReport.fxml"),
	PATH_FXML_WAIT_PROGRESS("/fxml/WaitProgress.fxml"),

	// Imagens
	// PATH_ICON_APP ("/images/avFinal_32.png"),
	PATH_ICON_EDIT_GRID("/images/edit_grid.png"),
	PATH_ICON_REMOVE_GRID("/images/remove_grid.png"),
	PATH_IMAGE_LOGO_REPORT("/images/logo_sc.jpg"),

	// Messages Bundle I18n
	PATH_I18N_APP("locales.avFinal-i18n"),
	PATH_MESSAGES_VALIDATION("locales.ValidationMessages"),

	// Titulos
	TITLE_RECOVERY("Recuperação"),
	TITLE_NEW_ASSESSMENT("Nova Avaliação"),
	TITLE_LOGIN("Login"),
	TITLE_APP("AVFinal"),
	TITLE_MODAL_ASSESSMENT("Selecionar Avaliação"),
	TITLE_GRADEBOOK("Diário de Classe"),
	TITLE_REPORT("Relatório Teste"),

	// Table
	IS_REQUIRED_VALUE("isRequired"),
	STAGE_PARENT("stageParent"),
	TIPO_ASSESSMENT("typeAvaliacao"),
	IS_CANCELED("isCanceled"),

	// Report
	JASPER_COVER("/jasper/cover.jasper"),
	JASPER_FREQUENCY("/jasper/frequency.jasper"),
	JASPER_ASSESSMENT("/jasper/assessment.jasper"),
	JASPER_ACTIVITY("/jasper/activity.jasper");

	public static final String YES = "S";
	public static final String NO = "N";
	public static final String LACK = "F";
	public static final String ATTENDANCE = "C";

	Constants(String value) {
		setValues(value);
	}

	Constants(Double value) {
		setValues(value);
	}

	private Object values;

	public final String valuesToString() {
		if (values instanceof String) {
			return String.valueOf(values);
		}
		return "";
	}

	public final Double valuesToDouble() {
		if (values instanceof Double) {
			return ((Double) values);
		}
		return 0D;
	}

	public Object getValues() {
		return values;
	}

	private void setValues(Object values) {
		this.values = values;
	}

}