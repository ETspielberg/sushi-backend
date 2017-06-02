package unidue.ub.sushiclient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
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
import unidue.ub.media.analysis.Counter;

@RestController
public class SushiController {

	//private static final Logger LOGGER = LoggerFactory.getLogger(SushiController.class);

	@RequestMapping("/fachref/sushi")
	public Process getSushi(@RequestParam(value = "id", defaultValue = "1") String id,
			@RequestParam(value = "type", defaultValue = "update") String type,
			@RequestParam(value = "name", defaultValue = "JR1") String name) {
		Process process = new Process();
		process.setProcess("sushi-backend");
		process.setIdentifier(id);
		process.setMessage("sushi-backend.running");
		process.setStatus("running");
		SushiClient sushiClient = new SushiClient();
		/*
		 * try { Connection.register(process); } catch (JSONException |
		 * IOException e1) { process.addMessage("error.registering.process"); }
		 */
		try {
			List<Counter> counters = new ArrayList<>();
			CounterReportResponse response = sushiClient.getSushi(id, type, name);
			/*
			 * List<unidue.ub.sushiclient.service.Exception> exceptions =
			 * response.getException(); if (exceptions.size() > 0) { for
			 * (unidue.ub.sushiclient.service.Exception exception : exceptions)
			 * { process.addMessage(exception.getMessage()); } }
			 */
			Report report = response.getReport();

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
					for (Metric metric : metrics) {
						counter.setCategory(metric.getCategory().value());
						List<PerformanceCounter> performanceCounters = metric.getInstance();
						for (PerformanceCounter performanceCounter : performanceCounters) {
							switch (performanceCounter.getMetricType().value()) {
							case "ft_ps": {
								counter.setPsRequests(performanceCounter.getCount());
							}
							case "ft_ps_mobile": {
								counter.setPsRequestsMobile(performanceCounter.getCount());
							}
							case "ft_pdf": {
								counter.setPdfRequests(performanceCounter.getCount());
							}
							case "ft_pdf_mobile": {
								counter.setPdfRequestsMobile(performanceCounter.getCount());
							}
							case "ft_html": {
								counter.setHtmlRequests(performanceCounter.getCount());
							}
							case "ft_html_mobile": {
								counter.setHtmlRequestsMobile(performanceCounter.getCount());
							}
							case "ft_epub": {
								counter.setePub(performanceCounter.getCount());
							}
							case "ft_total": {
								counter.setTotalRequests(performanceCounter.getCount());
							}
							case "sectioned_html": {
								counter.setSectionedHtml(performanceCounter.getCount());
							}
							case "toc": {
								counter.setToc(performanceCounter.getCount());
							}
							case "abstract": {
								counter.setAbstractCounter(performanceCounter.getCount());
							}
							case "reference": {
								counter.setReference(performanceCounter.getCount());
							}
							case "data_set": {
								counter.setDataSet(performanceCounter.getCount());
							}
							case "audio": {
								counter.setAudio(performanceCounter.getCount());
							}
							case "video": {
								counter.setVideo(performanceCounter.getCount());
							}
							case "image": {
								counter.setImage(performanceCounter.getCount());
							}
							case "podcast": {
								counter.setPodcast(performanceCounter.getCount());
							}
							case "multimedia": {
								counter.setMultimedia(performanceCounter.getCount());
							}
							case "record_view": {
								counter.setRecordView(performanceCounter.getCount());
							}
							case "result_click": {
								counter.setResultClick(performanceCounter.getCount());
							}
							case "search_reg": {
								counter.setSearchReg(performanceCounter.getCount());
							}
							case "search_fed": {
								counter.setSearchFed(performanceCounter.getCount());
							}
							case "turnaway": {
								counter.setTurnaway(performanceCounter.getCount());
							}
							case "no_license": {
								counter.setNoLicense(performanceCounter.getCount());
							}
							case "other": {
								counter.setOther(performanceCounter.getCount());
							}
							}
						}
					}
					counters.add(counter);
				}
			}
			// register(counters);
		} catch (JsonParseException e) {
			process.addMessage("error.JSONParser");
			process.setStatus("error");
		} catch (JsonMappingException e) {
			process.addMessage("error.JSONMapping");
			process.setStatus("error");
		} catch (MalformedURLException e) {
			process.addMessage("error.MalFormedURL");
			process.setStatus("error");
		} catch (IOException e) {
			e.printStackTrace();
			process.addMessage("error.IO");
			process.setStatus("error");
		}
		return process;
	}

}
