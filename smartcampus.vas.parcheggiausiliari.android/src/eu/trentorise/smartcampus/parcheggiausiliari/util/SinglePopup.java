package eu.trentorise.smartcampus.parcheggiausiliari.util;

import eu.trentorise.smartcampus.parcheggiausiliari.model.GeoObject;

/**
 * interface used to open only one popup at a time
 * @author Michele Armellini
 *
 */
public interface SinglePopup {
	public abstract void openPopup(GeoObject obj);
}
