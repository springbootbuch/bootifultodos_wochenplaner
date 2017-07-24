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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Michael J. Simons, 2017-07-24
 */
@SuppressWarnings({"checkstyle:designforextension"})
@Service
@RequiredArgsConstructor
public class FeiertageService {

	private final RestTemplate restTemplate;

	@HystrixCommand(fallbackMethod = "defaultFeiertage")
	public List<Feiertag> getFeiertage(
		final int jahr, final int bundesland
	) {
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
