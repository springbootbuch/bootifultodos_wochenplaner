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

import de.bootifultodos.wochenplaner.FeiertageClient.DefaultFeiertageClient;
import java.util.ArrayList;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Michael J. Simons, 2017-07-22
 */
@FeignClient(
	name = "feiertage",
	fallback = DefaultFeiertageClient.class
)
public interface FeiertageClient {
	@GetMapping("/api/feiertage/{jahr}/{bundesland}")
	List<Feiertag> getFeiertage(
		@PathVariable int jahr,
		@PathVariable int bundesland
	);

	final class DefaultFeiertageClient 
		implements FeiertageClient {

		@Override
		public List<Feiertag> getFeiertage(
			final int jahr, 
			final int bundesland
		) {
			return new ArrayList<>();
		}
	}
}
