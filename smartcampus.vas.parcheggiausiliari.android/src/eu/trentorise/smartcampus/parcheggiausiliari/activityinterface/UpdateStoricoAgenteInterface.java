package eu.trentorise.smartcampus.parcheggiausiliari.activityinterface;

import java.util.List;
import java.util.Map;

import eu.trentorise.smartcampus.parcheggiausiliari.model.LogObject;

public interface UpdateStoricoAgenteInterface {

	void addStorico(List<LogObject> storico);
	void refreshLog();
}
