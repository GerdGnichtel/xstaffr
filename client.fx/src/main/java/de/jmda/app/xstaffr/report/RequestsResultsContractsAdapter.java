package de.jmda.app.xstaffr.report;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.jmda.app.xstaffr.common.domain.Contract;
import de.jmda.app.xstaffr.common.domain.Customer;
import de.jmda.app.xstaffr.common.domain.Project;
import de.jmda.app.xstaffr.common.domain.SearchRequest;
import de.jmda.app.xstaffr.common.domain.SearchResult;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class RequestsResultsContractsAdapter implements JRDataSource
{
	private final static Logger LOGGER = LogManager.getLogger(RequestsResultsContractsAdapter.class);

	private enum FIELD
	{
		  CUSTOMER__ID
		, CUSTOMER__NAME
		, PROJECT__ID
		, PROJECT__NAME
		, REQUESTER__ID
		, REQUESTER__NAME
		, REQUEST__ID
		, REQUEST__RECEIPT
		, REQUEST__INCEPTION
		, REQUEST__EXPIRATION
		, REQUEST__ROLENAME
		, REQUEST__HOURLY_RATE
		, REQUEST__TEXT
		, RESULT__ID
		, RESULT__RECEIPT
		, RESULT__HOURLY_RATE
		, RESULT__RATING_INTERNAL
		, RESULT__RATING_BY_CUSTOMER
		, RESULT__TEXT
		, RESULT__FORWARD_TO_REQUESTER
		, RESULT__FORWARD_TO_CUSTOMER
		, RESULT__FEEDBACK_FROM_CUSTOMER
		, RESULT__FEEDBACK_TO_SUPPLIER
		, CANDIDATE__ID
		, CANDIDATE__LASTNAME
		, CANDIDATE__FIRSTNAME
		, CANDIDATE__UNIQUENAME
		, CANDIDATE__TEXT
		, SUPPLIER__ID
		, SUPPLIER__NAME
		, SUPPLIER__TEXT
		, CONTRACT__ID
		, CONTRACT__INCEPTION
		, CONTRACT__EXPIRATION
		, CONTRACT__HOURLY_RATE
		, CONTRACT__TEXT
	};

	private class Data
	{
		Long       CUSTOMER__ID;
		String     CUSTOMER__NAME;
		Long       PROJECT__ID;
		String     PROJECT__NAME;
		Long       REQUESTER__ID;
		String     REQUESTER__NAME;
		Long       REQUEST__ID;
		LocalDate  REQUEST__RECEIPT;
		LocalDate  REQUEST__INCEPTION;
		LocalDate  REQUEST__EXPIRATION;
		String     REQUEST__ROLENAME;
		BigDecimal REQUEST__HOURLY_RATE;
		String     REQUEST__TEXT;
		Long       RESULT__ID;
		LocalDate  RESULT__RECEIPT;
		BigDecimal RESULT__HOURLY_RATE;
		String     RESULT__RATING_INTERNAL;
		String     RESULT__RATING_BY_CUSTOMER;
		String     RESULT__TEXT;
		LocalDate  RESULT__FORWARD_TO_REQUESTER;
		LocalDate  RESULT__FORWARD_TO_CUSTOMER;
		LocalDate  RESULT__FEEDBACK_FROM_CUSTOMER;
		LocalDate  RESULT__FEEDBACK_TO_SUPPLIER;
		Long       CANDIDATE__ID;
		String     CANDIDATE__FIRSTNAME;
		String     CANDIDATE__LASTNAME;
		String     CANDIDATE__UNIQUENAME;
		String     CANDIDATE__TEXT;
		Long       SUPPLIER__ID;
		String     SUPPLIER__NAME;
		String     SUPPLIER__TEXT;
		Long       CONTRACT__ID;
		LocalDate  CONTRACT__INCEPTION;
		LocalDate  CONTRACT__EXPIRATION;
		BigDecimal CONTRACT__HOURLY_RATE;
		String     CONTRACT__TEXT;
	}

	private List<Data> dataItems = new ArrayList<>();

	public RequestsResultsContractsAdapter(List<SearchRequest> requests)
	{
		this();

		if (requests == null) throw new IllegalArgumentException("searchRequests must not be null");

		requests.sort((item1, item2) -> compare(item1, item2));

		boolean dataCreated = false;

		for (SearchRequest request : requests)
		{
			dataCreated = false;

//			Data dataItem = new Data();
//			populateData(dataItem, request);
//			dataItems.add(dataItem);

			for (SearchResult result : request.getSearchResults())
			{
				dataCreated = false;

//				if (dataCreated)
//				{
//					dataItem = new Data();
////					populateData(dataItem, request, result);
//					dataItems.add(dataItem);
//				}
//
//				populateData(dataItem, request, result);
//
//				boolean createDataForNextContract = false;

				for (Contract contract : result.getContracts())
				{
					dataCreated = false;

//					if (createDataForNextContract)
//					{
//						dataItem = new Data();
//						populateData(dataItem, request, result, contract);
//						dataItems.add(dataItem);
//					}

					Data dataItem = new Data();
					populateData(dataItem, request, result, contract);
					dataItems.add(dataItem);
					dataCreated = true;
				}

				if (dataCreated == false)
				{
					Data dataItem = new Data();
					populateData(dataItem, request, result);
					dataItems.add(dataItem);
					dataCreated = true;
				}
			}

			if (dataCreated == false)
			{
				Data dataItem = new Data();
				populateData(dataItem, request);
				dataItems.add(dataItem);
				dataCreated = true;
			}
		}
	}

	private RequestsResultsContractsAdapter()
	{
		Field[] declaredFields = Data.class.getDeclaredFields();
		FIELD[] fieldEnumValues = FIELD.values();

		if (declaredFields.length != fieldEnumValues.length)
		{
			LOGGER.warn(
					  "non matching number of declared fields in " + Data.class.getName() + " "
					+ "(" + declaredFields.length + ") and defined values in "
					+ FIELD.class.getName() + " (" + fieldEnumValues.length + ")");
		}

		for (FIELD field : fieldEnumValues)
		{
			if (isInDeclaredFields(field, declaredFields) == false)
			{
				LOGGER.warn(FIELD.class.getName() + "." + field.name() + " is not in declared fields of " + Data.class.getName());
			}
		}

		for (Field field : declaredFields)
		{
			if (isInFieldEnumValues(field, fieldEnumValues) == false)
			{
				LOGGER.warn(Data.class.getName() + "." + field.getName() + " is not in  " + FIELD.class.getName() + " values");
			}
		}
	}

	private boolean isInDeclaredFields(FIELD field, Field[] declaredFields)
	{
		for (Field dedclaredField : declaredFields)
		{
			if (dedclaredField.getName().equals(field.name())) return true;
		}
		return false;
	}

	private boolean isInFieldEnumValues(Field field, FIELD[] fieldEnumValues)
	{
		for (FIELD fieldEnumValue : fieldEnumValues)
		{
			if (fieldEnumValue.name().equals(field.getName())) return true;
		}
		return false;
	}

	private void populateData(Data data, SearchRequest request)
	{
		data.CUSTOMER__ID         = request.getProject().getCustomer().getId();
		data.CUSTOMER__NAME       = request.getProject().getCustomer().getName();
		data.PROJECT__ID          = request.getProject().getId();
		data.PROJECT__NAME        = request.getProject().getName();
		data.REQUESTER__ID        = request.getRequester().getId();
		data.REQUESTER__NAME      = request.getRequester().getName();
		data.REQUEST__ID          = request.getId();
		data.REQUEST__ROLENAME    = request.getRolename();
		data.REQUEST__HOURLY_RATE = request.getHourlyRate();
		data.REQUEST__RECEIPT     = request.getReceipt();
		data.REQUEST__INCEPTION   = request.getInception();
		data.REQUEST__EXPIRATION  = request.getExpiration();
		data.REQUEST__TEXT        = request.getText();
	}

	private void populateData(Data data, SearchRequest request, SearchResult result)
	{
		populateData(data, request);

		data.RESULT__ID                     = result.getId();
		data.RESULT__RECEIPT                = result.getReceipt();
		data.RESULT__HOURLY_RATE            = result.getHourlyRate();
		data.RESULT__RATING_INTERNAL        = result.getRatingInternal();
		data.RESULT__RATING_BY_CUSTOMER     = result.getRatingByCustomer();
		data.RESULT__FORWARD_TO_REQUESTER   = result.getForwardToRequester();
		data.RESULT__FORWARD_TO_CUSTOMER    = result.getForwardToCustomer();
		data.RESULT__FEEDBACK_FROM_CUSTOMER = result.getFeedbackFromCustomer();
		data.RESULT__FEEDBACK_TO_SUPPLIER   = result.getFeedbackToSupplier();
		data.CANDIDATE__ID                  = result.getCandidate().getId();
		data.CANDIDATE__FIRSTNAME           = result.getCandidate().getFirstName();
		data.CANDIDATE__LASTNAME            = result.getCandidate().getLastName();
		data.CANDIDATE__UNIQUENAME          = result.getCandidate().getUniqueName();
		data.CANDIDATE__TEXT                = result.getCandidate().getText();
		data.SUPPLIER__ID                   = result.getSupplier().getId();
		data.SUPPLIER__NAME                 = result.getSupplier().getName();
		data.SUPPLIER__TEXT                 = result.getSupplier().getText();
	}

	private void populateData(Data data, SearchRequest request, SearchResult result, Contract contract)
	{
		populateData(data, request, result);

		data.CONTRACT__ID          = contract.getId();
		data.CONTRACT__INCEPTION   = contract.getInception();
		data.CONTRACT__EXPIRATION  = contract.getExpiration();
		data.CONTRACT__HOURLY_RATE = contract.getHourlyRate();
		data.CONTRACT__TEXT        = contract.getText();
	}

	private int rowIndex = -1; // start at -1, first call to next() will set rowIndex to first valid row (0)

	/** method is called <b>before</b> next row will be processed */
	@Override public boolean next() throws JRException
	{
		LOGGER.debug("rowIndex " + rowIndex);
		if (rowIndex < (dataItems.size() - 1))
		{
			rowIndex++;
			return true;
		}
		return false;
	}

	@Override public Object getFieldValue(JRField jrField) throws JRException
	{
		return getFieldValue(jrField.getName(), dataItems.get(rowIndex));
	}

	private Object getFieldValue(String jrFieldName, Data dataItem) throws JRException
	{
		Field field;
		try
		{
			field = Data.class.getDeclaredField(jrFieldName);
		}
		catch (NoSuchFieldException | SecurityException e)
		{
			throw new JRException("failure getting field on " + Data.class.getName() + " object for " + jrFieldName, e);
		}

		Object result;
		try
		{
			result = field.get(dataItem);
		}
		catch (IllegalArgumentException | IllegalAccessException e)
		{
			throw new JRException("failure getting value of field on " + Data.class.getName() + " object for " + jrFieldName, e);
		}

		return result;
	}

	public String toCSV() throws JRException
	{
		StringBuffer result = new StringBuffer();

		// produce column header line
		for (FIELD field : FIELD.values())
		{
			result.append(field.name() + ";");
		}
		result.append("\n");

		// produce row lines
		for (Data dataItem : dataItems)
		{
			// produce row columns
			for (FIELD field : FIELD.values())
			{
				// wrap field values into "s to support multi line data
				result.append("\"" + getFieldValue(field.name(), dataItem) + "\";");
			}
			result.append("\n");
		}

		return result.toString();
	}

	private static int compare(SearchRequest item1, SearchRequest item2)
	{
		Project project1 = item1.getProject();
		Project project2 = item2.getProject();
		Customer customer1 = project1.getCustomer();
		Customer customer2 = project2.getCustomer();

		int customerNameComparison = customer1.getName().compareTo(customer2.getName());

		if (customerNameComparison == 0)
		{
			int projectNameComparison = project1.getName().compareTo(project2.getName());

			if (projectNameComparison == 0)
			{
				LocalDate requestReceipt1 = item1.getReceipt();
				LocalDate requestReceipt2 = item2.getReceipt();

				int requestReceiptComparison = requestReceipt1.compareTo(requestReceipt2);

				if (requestReceiptComparison == 0)
				{
					return item1.getRolename().compareTo(item2.getRolename());
				}
				else return requestReceiptComparison;
			}
			else return projectNameComparison;
		}
		else return customerNameComparison;
	}
}