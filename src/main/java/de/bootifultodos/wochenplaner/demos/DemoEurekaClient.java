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
package de.bootifultodos.wochenplaner.demos;

import com.netflix.discovery.EurekaClient;
import de.bootifultodos.wochenplaner.Feiertag;
import de.bootifultodos.wochenplaner.WochenplanerConfiguration;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Used only for demonstrating purpose and runs only in profile "demo".
 *
 * @author Michael J. Simons, 2017-07-21
 */
@Component
@Profile("demo")
@RequiredArgsConstructor
public final class DemoEurekaClient implements CommandLineRunner {

	static final Logger LOG = LoggerFactory.getLogger(DemoEurekaClient.class);

	private final WochenplanerConfiguration configuration;

	private final EurekaClient eurekaClient;

	@Override
	public void run(final String... args) throws Exception {
		String homepageUrl = eurekaClient
			.getNextServerFromEureka("feiertage", false)
			.getHomePageUrl();
		LOG.info("Homepage-URL ist {}", homepageUrl);

		final RestTemplate restTemplate = new RestTemplate();
		Feiertag[] feiertage = restTemplate.getForEntity(
			homepageUrl
				+ "/api/feiertage/{jahr}/{bundesland}",
			Feiertag[].class,
			LocalDate.now().getYear(),
			configuration.getBundeslandnummer()
		).getBody();

		for (Feiertag feiertag : feiertage) {
			LOG.info("{} ist ein Feiertag: {}",
				feiertag.getDatum(),
				feiertag.getName()
			);
		}
	}
}
