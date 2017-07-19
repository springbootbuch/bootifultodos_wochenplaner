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

import javax.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * @author Michael J. Simons, 2017-07-19
 */
@SuppressWarnings({"checkstyle:designforextension"})
@Component
@ConfigurationProperties(prefix = "wochenplaner")
@Validated
public class WochenplanerConfiguration {

	/**
	 * Die Nummer des Bundeslandes (amtlicher
	 * Schlüssel), für das Feiertage angezeigt
	 * werden.
	 */
	@NotNull
	private Short bundeslandnummer;

	public Short getBundeslandnummer() {
		return bundeslandnummer;
	}

	public void setBundeslandnummer(final Short bundeslandnummer) {
		this.bundeslandnummer = bundeslandnummer;
	}
}
