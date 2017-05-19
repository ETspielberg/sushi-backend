package unidue.ub.sushiclient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import unidue.ub.sushiclient.service.CounterReportResponse;
import unidue.ub.sushiclient.service.Identifier;
import unidue.ub.sushiclient.service.Metric;
import unidue.ub.sushiclient.service.PerformanceCounter;
import unidue.ub.sushiclient.service.Report;
import unidue.ub.sushiclient.service.Report.Customer;
import unidue.ub.sushiclient.service.ReportItem;

import unidue.ub.monitor.Process;
import unidue.ub.monitor.connector.Connection;
import unidue.ub.services.data.Counter;
import static unidue.ub.services.data.connector.CounterConnector.register;

@RestController
public class SushiController {

	@RequestMapping("/fachref/sushi")
	public Process greeting(@RequestParam(value = "id", defaultValue = "1") String id,
			@RequestParam(value = "type", defaultValue = "update") String type) {
		Process process = new Process();
		process.setProcess("sushi-backend");
		process.setIdentifier(id);
		process.setMessage("sushi-backend.running");
		process.setStatus("running");
		SushiClient sushiClient = new SushiClient();
		try {
			Connection.register(process);
		} catch (JSONException | IOException e1) {
			process.addMessage("error.registering.process");
		}
		try {
			List<Counter> counters = new ArrayList<>();
			CounterReportResponse response = sushiClient.getSushi(id, type);
			List<unidue.ub.sushiclient.service.Exception> exceptions = response.getException();
			if (exceptions.size() > 0) {
				for (unidue.ub.sushiclient.service.Exception exception : exceptions) {
					process.addMessage(exception.getMessage());
				}
			}
			List<Report> reports = response.getReport().getReport();

			for (Report report : reports) {
				List<Customer> customers = report.getCustomer();
				for (Customer customer : customers) {
					List<ReportItem> reportItems = customer.getReportItems();
					for (ReportItem reportItem : reportItems) {
						Counter counter = new Counter();
						counter.setFullName(reportItem.getItemName());
						counter.setPublisher(reportItem.getItemPublisher());
						List<Identifier> identifiers = reportItem.getItemIdentifier();
						for (Identifier identifier : identifiers) {
							switch (identifier.getType().value()) {
							case "Online_ISSN": {
								counter.setOnlineISSN(identifier.getValue());
							}
							case "Print_ISSN": {
								counter.setPrintISSN(identifier.getValue());
							}
							case "EISSN": {
								counter.seteISSN(identifier.getValue());
							}
							case "ISSN": {
								counter.seteISSN(identifier.getValue());
							}
							case "ISBN": {
								counter.setISBN(identifier.getValue());
							}
							case "Online_ISBN": {
								counter.setOnlineISBN(identifier.getValue());
							}
							case "Print_ISBN": {
								counter.setPrintISBN(identifier.getValue());
							}
							case "DOI": {
								counter.setDoi(identifier.getValue());
							}
							case "Proprietary": {
								counter.setProprietary(identifier.getValue());
							}
							}

						}
						List<Metric> metrics = reportItem.getItemPerformance();
						for (Metric metric: metrics) {
							counter.setCategory(metric.getCategory().value());
							List<PerformanceCounter> performanceCounters = metric.getInstance();
							for (PerformanceCounter performanceCounter : performanceCounters) {
								switch (performanceCounter.getMetricType().value()) {
								case "ft_ps" : {
									counter.setPsRequests(performanceCounter.getCount());
								}
								case "ft_ps_mobile" : {
									counter.setPsRequestsMobile(performanceCounter.getCount());
								}
								case "ft_pdf" : {
									counter.setPdfRequests(performanceCounter.getCount());
								}
								case "ft_pdf_mobile" : {
									counter.setPdfRequestsMobile(performanceCounter.getCount());
								}
								case "ft_html" : {
									counter.setHtmlRequests(performanceCounter.getCount());
								}
								case "ft_html_mobile" : {
									counter.setHtmlRequestsMobile(performanceCounter.getCount());
								}
								case "ft_epub" : {
									counter.setePub(performanceCounter.getCount());
								}
								case "ft_total" : {
									counter.setTotalRequests(performanceCounter.getCount());
								}
								case "sectioned_html" : {
									counter.setSectionedHtml(performanceCounter.getCount());
								}
								case "toc" : {
									counter.setToc(performanceCounter.getCount());
								}
								case "abstract" : {
									counter.setAbstractCounter(performanceCounter.getCount());
								}
								case "reference" : {
									counter.setReference(performanceCounter.getCount());
								}
								case "data_set" : {
									counter.setDataSet(performanceCounter.getCount());
								}
								case "audio" : {
									counter.setAudio(performanceCounter.getCount());
								}
								case "video" : {
									counter.setVideo(performanceCounter.getCount());
								}
								case "image" : {
									counter.setImage(performanceCounter.getCount());
								}
								case "podcast" : {
									counter.setPodcast(performanceCounter.getCount());
								}
								case "multimedia" : {
									counter.setMultimedia(performanceCounter.getCount());
								}
								case "record_view" : {
									counter.setRecordView(performanceCounter.getCount());
								}
								case "result_click" : {
									counter.setResultClick(performanceCounter.getCount());
								}
								case "search_reg" : {
									counter.setSearchReg(performanceCounter.getCount());
								}
								case "search_fed" : {
									counter.setSearchFed(performanceCounter.getCount());
								}
								case "turnaway" : {
									counter.setTurnaway(performanceCounter.getCount());
								}
								case "no_license" : {
									counter.setNoLicense(performanceCounter.getCount());
								}
								case "other" : {
									counter.setOther(performanceCounter.getCount());
								}
								}
							}
						}
						counters.add(counter);
					}
				}
			}
			register(counters);
		} catch (JsonParseException e) {
			process.setMessage("error.JSONParser");
		} catch (JsonMappingException e) {
			process.setMessage("error.JSONMapping");
		} catch (MalformedURLException e) {
			process.setMessage("error.MalFormedURL");
		} catch (IOException e) {
			process.setMessage("error.IO");
		}
		return process;
	}

}
