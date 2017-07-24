/*
 * Copyright 2017 michael-simons.eu.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.bootifultodos.wochenplaner;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import static de.bootifultodos.wochenplaner.DemoFeignClient.LOG;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Michael J. Simons, 2017-07-24
 */
@Configuration
@Profile("demo")
public final class DemoHystrixCircuitBreaker {

	static final Logger LOG = LoggerFactory.getLogger(DemoHystrixCircuitBreaker.class);

	@Bean
	FeiertageService feiertageService(final RestTemplate restTemplate) {
		return new FeiertageService(restTemplate);
	}

	@Bean
	DemoCommandLineRunner demoCommandLineRunner(final WochenplanerConfiguration configuration, final FeiertageService feiertageService) {
		return new DemoCommandLineRunner(configuration, feiertageService);
	}
}

@SuppressWarnings({"checkstyle:designforextension"})
class FeiertageService {

	private final RestTemplate restTemplate;

	FeiertageService(final RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@HystrixCommand(fallbackMethod = "defaultFeiertage")
	public List<Feiertag> getFeiertage(
		final int jahr, final int bundesland
	) {
		if (jahr == 2005) {
			throw new RuntimeException("Random error");
		}
		Feiertag[] feiertage = restTemplate.getForEntity(
			"http://feiertage"
			+ "/api/feiertage/{jahr}/{bundesland}",
			Feiertag[].class,
			jahr,
			bundesland
		).getBody();
		// Nutze Resttemplate wie gehabt...
		return Arrays.asList(feiertage);
	}

	private List<Feiertag> defaultFeiertage(
		final int jahr, final int bundesland
	) {
		return new ArrayList<>();
	}
}

@RequiredArgsConstructor
class DemoCommandLineRunner implements CommandLineRunner {

	private final WochenplanerConfiguration configuration;

	private final FeiertageService feiertageService;

	@Override
	public void run(final String... args) throws Exception {
		List<Feiertag> feiertage;
		feiertage = this.feiertageService
			.getFeiertage(LocalDate.now().getYear(),
				configuration.getBundeslandnummer());

		feiertage.forEach(feiertag -> {
			LOG.info("{} ist ein Feiertag: {}",
				feiertag.getDatum(),
				feiertag.getName()
			);
		});

		feiertage = this.feiertageService
			.getFeiertage(2005, configuration.getBundeslandnummer());

		feiertage.forEach(feiertag -> {
			LOG.info("{} ist ein Feiertag: {}",
				feiertag.getDatum(),
				feiertag.getName()
			);
		});
	}
}
