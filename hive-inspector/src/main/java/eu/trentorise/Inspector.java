package eu.trentorise;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class Inspector {

	private static final Logger logger = Logger.getLogger(Inspector.class);

	private static final String HIVE_HOST = "cosmos.lab.fi-ware.org";
	private static final String HIVE_PORT = "10000";
	private static final String HIVE_USER = "mirko.perillo";
	private static final String HIVE_PWD = "";

	private static final String TMP_TABLE = "sc_data";
	private static final String STADIO_ENTITY = "Ospedale___Rovereto";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		logger.info("Start data proccesor");
		try {
			Class.forName("org.apache.hadoop.hive.jdbc.HiveDriver");
			String connectionString = String.format(
					"jdbc:hive://%s:%s/default", HIVE_HOST, HIVE_PORT);
			Connection con = DriverManager.getConnection(connectionString,
					HIVE_USER, HIVE_PWD);
			logger.info("Connected to " + connectionString);
			Statement stmt = con.createStatement();

			ResultSet resultSet = null;

			// delete temp table
			// stmt.executeQuery("drop table " + TMP_TABLE);
			// logger.info("Dropped " + TMP_TABLE);

			stmt.executeQuery(String
					.format("create external table IF NOT EXISTS %s (date_f string, time_f string, entity_f string, type_f string, attr_f string, fake_f string, value_f int) row format delimited fields terminated by '|' location '%s'",
							TMP_TABLE, "/user/mirko.perillo/sc/dataset1/"));
			logger.info("CREATED " + TMP_TABLE);

			// resultSet = stmt.executeQuery("describe extended " + TMP_TABLE);
			// while (resultSet.next()) {
			// logger.info(resultSet.getString(1));
			// }

			// query park
			// resultSet = stmt
			// .executeQuery(String
			// .format("SELECT entity_f,time_f, value_f FROM %s WHERE entity_f='%s'",
			// TMP_TABLE, STADIO_ENTITY));
			// while (resultSet.next()) {
			// logger.info(resultSet.getString(1) + " "
			// + resultSet.getString(2) + " " + resultSet.getInt(3));
			// }

			// query bike
			resultSet = stmt
					.executeQuery(String
							.format("SELECT entity_f,AVG(value_f) as media FROM %s WHERE entity_f NOT LIKE 'street@%%' AND entity_f NOT LIKE 'parking@%%' GROUP BY entity_f",
									TMP_TABLE));
			while (resultSet.next()) {
				logger.info(resultSet.getString(1) + " "
						+ resultSet.getFloat(2));
			}
			// resultSet = stmt.executeQuery("SELECT COUNT(*) FROM " +
			// TMP_TABLE);
			// while (resultSet.next()) {
			// logger.info("COUNT " + resultSet.getInt(1));
			// }
		} catch (Exception e) {
			e.printStackTrace();
			logger.fatal("Exception executing processor: " + e.getMessage());
			System.exit(1);
		}
		logger.info("End processing");
	}

}
