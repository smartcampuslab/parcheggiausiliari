package eu.trentorise.smartcampus.parcheggiausiliari.util.constants;

/**
 * Interface containing all services URLS as static final fields
 * @author Michele Armellini
 */
public class Parcheggi_Services {

	public static final String HOST = "https://tn.smartcommunitylab.it/";

	public static final String PARKLIST = "/parkings/";

	public static final String STREETLIST = "/streets/";

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
