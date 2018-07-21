package de.jmda.app.xstaffr.common.domain.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.derby.jdbc.ClientDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;

public class DBUtils
{
	private final static Logger LOGGER = LogManager.getLogger(DBUtils.class);

	public File DEFAULT_BACKUP_FILE = new File("backup.full.xml");

//	private Class<?> jdbcDriver;
//	private String jdbcURL;
//	private String jdbcUser;
//	private String jdbcPassword;

	private IDatabaseConnection dbConnection;

	public DBUtils(Class<?> jdbcDriver, String jdbcURL, String jdbcUser, String jdbcPassword) throws Exception
	{
		super();

		if (jdbcDriver == null) throw new IllegalArgumentException("jdbcDriver must not be null");
		if (jdbcURL == null) throw new IllegalArgumentException("jdbcURL must not be null");
		if (jdbcUser == null) throw new IllegalArgumentException("jdbcUser must not be null");
		if (jdbcPassword == null) throw new IllegalArgumentException("jdbcPassword must not be null");

//		this.jdbcDriver = jdbcDriver;
//		this.jdbcURL = jdbcURL;
//		this.jdbcUser = jdbcUser;
//		this.jdbcPassword = jdbcPassword;

		Connection jdbcConnection = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPassword);
		dbConnection = new DatabaseConnection(jdbcConnection);
		DatabaseConfig databaseConfig = dbConnection.getConfig();
		databaseConfig.setProperty(DatabaseConfig.FEATURE_ALLOW_EMPTY_FIELDS, Boolean.TRUE);
		LOGGER.debug("databaseConfig.getProperty(DatabaseConfig.FEATURE_ALLOW_EMPTY_FIELDS): " + databaseConfig.getProperty(DatabaseConfig.FEATURE_ALLOW_EMPTY_FIELDS));
	}

	public void backup(File file) throws Exception
	{
		LOGGER.debug("starting backup with " + file.getAbsolutePath());
		// full database export
//		IDataSet fullDataSet = dbConnection.createDataSet();
		Writer writer = new FileWriter(file);
		IDataSet fullDataSet =
				new FilteredDataSet(new DatabaseSequenceFilter(dbConnection), dbConnection.createDataSet());
//		FlatXmlDataSet.write(fullDataSet, writer, "Cp1252");
//		FlatXmlDataSet.write(fullDataSet, writer, "windows-1252");
		FlatXmlDataSet.write(fullDataSet, writer, StandardCharsets.UTF_8.name());
		LOGGER.debug("finished backup with " + file.getAbsolutePath());
	}

	public void restore(File file) throws Exception
	{
		LOGGER.debug("starting restore from " + file.getAbsolutePath());
		// Clean Insert = alles Loeschen und neu aufbauen
		IDataSet dataSet = new FlatXmlDataSetBuilder().build(new FileInputStream(file));
		DatabaseOperation.CLEAN_INSERT.execute(dbConnection, dataSet);
//		DatabaseOperation.DELETE_ALL.execute(dbConnection, dataSet);
		LOGGER.debug("finished restore from " + file.getAbsolutePath());
	}

	public void backup()  throws Exception { backup( DEFAULT_BACKUP_FILE); }
	public void restore() throws Exception { restore(DEFAULT_BACKUP_FILE); }

	public static void main(String[] args) throws Exception
	{
		DBUtils dbUtils =
				new DBUtils(ClientDriver.class, "jdbc:derby://localhost:1527/db/derby/xstaffr", "xstaffr", "xstaffr");
		dbUtils.backup();
		dbUtils.restore();
	}
}