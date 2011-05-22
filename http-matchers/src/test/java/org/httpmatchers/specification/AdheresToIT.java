/*
 * Copyright 2011 the original author or authors.
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
package org.httpmatchers.specification;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.httpmatchers.Urls.HTTPS_WITH_BASIC_AUTH_URL;
import static org.httpmatchers.Urls.HTTP_URL;
import static org.httpmatchers.security.CanBeAccessedUsingCredentials.canBeAccessedUsing;
import static org.httpmatchers.security.RequiresBasicAuthentication.requiresBasicAuthentication;
import static org.httpmatchers.security.RequiresSsl.requiresSsl;
import static org.httpmatchers.specification.AdheresTo.adheresTo;
import static org.httpmatchers.specification.Specification.specification;

import org.httpmatchers.security.Credentials;
import org.junit.Test;

/**
 * @author David Ehringer
 */
public class AdheresToIT {

	@SuppressWarnings("unchecked")
	@Test
	public void urlAdheresToSpecification() {

		Credentials adminCredentials = new Credentials("admin", "secret");
		Credentials guestCredentials = new Credentials("guest", "password");

		Specification<String> specification = 
			specification(
				requiresBasicAuthentication(), //
				requiresSsl(), //
				canBeAccessedUsing(adminCredentials),//
				not(canBeAccessedUsing(guestCredentials)));

		assertThat(HTTPS_WITH_BASIC_AUTH_URL, adheresTo(specification));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void urlDoesNotAdhereToSpecification() {
		Specification<String> specification = //
		specification(//
				requiresBasicAuthentication(), //
				requiresSsl());

		assertThat(HTTP_URL, not(adheresTo(specification)));
	}
}
