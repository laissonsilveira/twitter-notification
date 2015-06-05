package br.com.avfinal.mdl.service;

import javafx.collections.ObservableList;
import br.com.avfinal.entity.grid.ResultGrid;

public interface ResultService {

	ObservableList<ResultGrid> calculateAverages(Long idGradeBook);
	
}
