package de.jmda.app.xstaffr.client.fx.report;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.jmda.app.xstaffr.common.domain.SearchRequest;
import de.jmda.app.xstaffr.report.RequestsResultsContractsAdapter;
import de.jmda.fx.FXUtil;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public class SearchRequestReporter
{
	private final static Logger LOGGER = LogManager.getLogger(SearchRequestReporter.class);

	public static void runAndShow(Set<SearchRequest> searchRequests)
	{
//		String reportResourceName = "SearchRequestReport.jasper";
		String reportResourceName = "RequestsResultsContracts.jasper";

		// use class loader of SearchRequestAdapter because the report resource is located side by side with the adapter
//		InputStream reportAsStream = SearchRequestAdapter.class.getResourceAsStream(reportResourceName);
		InputStream reportAsStream = RequestsResultsContractsAdapter.class.getResourceAsStream(reportResourceName);
		LOGGER.debug("reportAsStream present: " + (reportAsStream != null));

//		SearchRequestAdapter adapter = new SearchRequestAdapter(new ArrayList<SearchRequest>(searchRequests));
		RequestsResultsContractsAdapter adapter = new RequestsResultsContractsAdapter(new ArrayList<SearchRequest>(searchRequests));
		LOGGER.debug("adapter present: " + (adapter != null));

		JasperPrint jasperPrint;

		try
		{
			jasperPrint = JasperFillManager.fillReport(reportAsStream, new HashMap<>(), adapter);
		}
		catch (JRException e)
		{
			LOGGER.error("Failure filling report, returning!", e);
			Alert alert = new Alert(AlertType.ERROR, "Failure filling report, returning!\n" + e, ButtonType.CANCEL);
			alert.showAndWait();
			return;
		}

		LOGGER.debug("jasperPrint present: " + (jasperPrint != null));

		try
		{
			exportToPDFAndShow(jasperPrint);
		}
		catch (JRException e)
		{
			Alert alert = new Alert(AlertType.ERROR, "Failure exporting report to .pdf, returning!\n" + e, ButtonType.CANCEL);
			alert.showAndWait();
			return;
		}
//		displayInSwingBasedViewer(jasperPrint);
	}

	private static void exportToPDFAndShow(JasperPrint jasperPrint) throws JRException
	{
		String pdfFileName = "SearchRequestReport.pdf";
		JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFileName);
		File pdfFile = new File(pdfFileName);
		FXUtil.getHostServices().showDocument(pdfFile.getAbsolutePath());
	}

	/**
	 * be careful! jasperViewer.setVisible(true) will start swing app and stopping that will terminate fx app!!!
	 * @param jasperPrint
	 */
	private static void displayInSwingBasedViewer(JasperPrint jasperPrint)
	{
		JasperViewer jasperViewer = new JasperViewer(jasperPrint);
		jasperViewer.setVisible(true);
	}
}