package eu.trentorise.smartcampus.parcheggiausiliari.activity;

import java.util.ArrayList;
import java.util.List;

import eu.trentorise.smartcampus.parcheggiausiliari.model.Parking;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Street;

public interface AddGeoPoints {
	public void addgeopoints(List<Parking> parks);
	public void addStreets(List<Street> streets);

}
