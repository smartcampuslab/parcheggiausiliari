package eu.trentorise.smartcampus.parcheggiausiliari.util.constants;

/**
 * Interface containing all services URLS as static final fields
 * @author Michele Armellini
 */
public class Parcheggi_Services {

	public static final String HOST = "https://vas-dev.smartcampuslab.it/";

	public static final String PARKLIST = "/parkings/";

	public static final String PARKLISTINRADIUS = "/parkings?lat=45.888528&lon=11.036985&radius=0.013";

	public static final String STREETLIST = "/streets/";

	public static final String STREETLISTINRADIUS = "/streets?lat=45.888528&lon=11.036985&radius=0.001";

	// aggiungere id via in fondo
	public static final String STREETLOGLIST = "/log/street/";

	// aggiungere idVia?num num = max Risultati
	public static final String STREETLOGLISTMAX = "/log/street/";

	// aggiungere id park in fondo
	public static final String PARKLOGLIST = "/log/parking/";

	// aggiungere idPark?num num = max Risultati
	public static final String PARKLOGLISTMAX = "/log/parking/";

	// aggiungere idAusiliario
	public static final String AUSLOGLIST = "/log/user/";

	// aggiungere idAus?num num = max risultati
	public static final String AUSLOGLISTMAX = "/log/user/";

	// come body prende oggetto Parking
	public static final String UPDATEPARK = "/parkings/";

	// come body prende oggetto Street
	public static final String UPDATESTREET = "/streets/";

}
