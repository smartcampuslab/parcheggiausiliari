package eu.trentorise.smartcampus.parcheggiausiliari.activityinterface;

import java.util.List;

import eu.trentorise.smartcampus.parcheggiausiliari.model.Street;
import eu.trentorise.smartcampus.parcheggiausiliari.model.StreetLog;

public interface UpdateStoricoStreetInterface {


	void addStoricoStreet(List<StreetLog> storico);

}
