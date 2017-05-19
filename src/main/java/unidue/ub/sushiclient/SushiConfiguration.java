package unidue.ub.sushiclient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class SushiConfiguration {
	
	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		// this package must match the package in the <generatePackage> specified in
		// pom.xml
		marshaller.setContextPath("sushi.wsdl");
		return marshaller;
	}

	@Bean
	public SushiClient quoteClient(Jaxb2Marshaller marshaller) {
		SushiClient client = new SushiClient();
		client.setDefaultUri("localhost");
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}
}
