package unidue.ub.sushiclient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import unidue.ub.services.settings.Sushiprovider;
import unidue.ub.sushiclient.service.CounterReportResponse;
import unidue.ub.sushiclient.service.CustomerReference;
import unidue.ub.sushiclient.service.Range;
import unidue.ub.sushiclient.service.ReportDefinition;
import unidue.ub.sushiclient.service.ReportDefinition.Filters;
import unidue.ub.sushiclient.service.ReportRequest;
import unidue.ub.sushiclient.service.Requestor;

public class SushiClient extends WebServiceGatewaySupport {

	public CounterReportResponse getSushi(String id, String type) throws JsonParseException, JsonMappingException, MalformedURLException, IOException {
			// collect the data from the settings repository
			ObjectMapper mapper = new ObjectMapper();
			Sushiprovider provider = mapper.readValue(new URL("localhost:11200/settings/sushiprovider/" + id),
					Sushiprovider.class);
			ReportRequest request = new ReportRequest();

			// create the CustomerReference object
			CustomerReference reference = new CustomerReference();
			reference.setID(provider.getSushiCustomerReferenceID());

			// create the Requestor object
			Requestor requestor = new Requestor();
			requestor.setID(provider.getSushiRequestorID());
			requestor.setName(provider.getSushiRequestorName());

			// create the ReportDefinition
			ReportDefinition definition = new ReportDefinition();
			definition.setRelease(String.valueOf(provider.getSushiRelease()));

			// createFilters
			Filters filters = new Filters();
			Range range = new Range();
			LocalDate today = LocalDate.now();
			LocalDate start = today;
			LocalDate end = today;
			switch (type) {
			case "update": {
				start = today.minusMonths(2);
				end = today.minusMonths(1);

			}
			case "initial": {
				start = today.withDayOfMonth(1).withMonth(0).withYear(2000);
				if (today.getDayOfMonth() >= 15)
					end = today.minusMonths(1).withDayOfMonth(15);
				else
					end = today.minusMonths(2).withDayOfMonth(15);
			}
			}
			try {
				range.setBegin(DatatypeFactory.newInstance().newXMLGregorianCalendar(start.toString()));
				range.setEnd(DatatypeFactory.newInstance().newXMLGregorianCalendar(end.toString()));
			} catch (DatatypeConfigurationException e) {
				e.printStackTrace();
			}

			filters.setUsageDateRange(range);
			definition.setFilters(filters);

			// build the request
			request.setCustomerReference(reference);
			request.setRequestor(requestor);
			request.setReportDefinition(definition);

			CounterReportResponse response = (CounterReportResponse) getWebServiceTemplate()
					.marshalSendAndReceive(provider.getSushiURL(), request);
			return response;
	}
}
