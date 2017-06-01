package unidue.ub.sushiclient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapMessage;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import unidue.ub.services.settings.Sushiprovider;
import unidue.ub.sushiclient.service.CounterReportResponse;
import unidue.ub.sushiclient.service.CustomerReference;
import unidue.ub.sushiclient.service.ObjectFactory;
import unidue.ub.sushiclient.service.Range;
import unidue.ub.sushiclient.service.ReportDefinition;
import unidue.ub.sushiclient.service.ReportDefinition.Filters;
import unidue.ub.sushiclient.service.ReportRequest;
import unidue.ub.sushiclient.service.Requestor;

public class SushiClient extends WebServiceGatewaySupport {

	private static final Logger LOGGER = LoggerFactory.getLogger(SushiClient.class);

	public CounterReportResponse getSushi(String id, String type, String name)
			throws JsonParseException, JsonMappingException, MalformedURLException, IOException {
		// collect the data from the settings repository
		ObjectMapper mapper = new ObjectMapper();
		Sushiprovider provider = mapper.readValue(new URL("http://localhost:11300/sushiprovider/" + id),
				Sushiprovider.class);
		ObjectFactory factory = new ObjectFactory();
		ReportRequest request = factory.createReportRequest();

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
		definition.setName(name);

		// createFilters
		Filters filters = new Filters();
		Range range = new Range();
		LocalDateTime today = LocalDateTime.now();

		LocalDateTime start = today;
		LocalDateTime end = today;
		switch (type) {
		case "update": {
			start = today.minusMonths(2);
			end = today.minusMonths(1);

		}
		case "initial": {
			start = today.withDayOfMonth(1).withMonth(1).withYear(2000);
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
		request.setID(provider.getSushiRequestorID());
		try {
			request.setCreated(DatatypeFactory.newInstance().newXMLGregorianCalendar(LocalDateTime.now().toString()));
		} catch (DatatypeConfigurationException e1) {
			e1.printStackTrace();
		}

		WebServiceTemplate webServiceTemplate = getWebServiceTemplate();
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("unidue.ub.sushiclient.service");
		webServiceTemplate.setMarshaller(marshaller);
		webServiceTemplate.setUnmarshaller(marshaller);

		StreamResult result = new StreamResult(System.out);
		marshaller.marshal(request, result);

		CounterReportResponse response = factory.createCounterReportResponse();
		webServiceTemplate.marshalSendAndReceive(provider.getSushiURL(), request,
				new WebServiceMessageCallback() {
					public void doWithMessage(WebServiceMessage message) {
						((SoapMessage) message).setSoapAction("SushiService:GetReportIn");
						LOGGER.info(message.toString());
					}
				});
		return response;
	}
}
