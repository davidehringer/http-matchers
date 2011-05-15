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
package org.httpmatchers.security;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.httpmatchers.Urls.HTTP_URL;
import static org.httpmatchers.Urls.HTTP_WITH_BASIC_AUTH_URL;
import static org.httpmatchers.security.CanBeAccessedUsingCredentials.canBeAccessedUsing;

import org.junit.Test;

/**
 * @author David Ehringer
 */
public class CanBeAccessedUsingCredentialsIT {

	@Test
	public void credentialsAreAuthorizedForUrlRequiringBasicAuth() {
		Credentials credentials = new Credentials("admin", "secret");
		assertThat(HTTP_WITH_BASIC_AUTH_URL, canBeAccessedUsing(credentials));
	}

	@Test
	public void credentialsAreNotAuthorizedForUrlRequiringBasicAuth() {
		// TODO this doesn't work if the URL is bad
		Credentials credentials = new Credentials("hacker", "guess");
		assertThat(HTTP_WITH_BASIC_AUTH_URL, not(canBeAccessedUsing(credentials)));
	}

	@Test
	public void credentialsAreAlwaysAuthorizedForUrlsNotRequiringBasicAuth() {
		Credentials credentials = new Credentials("someone", "random");
		assertThat(HTTP_URL, canBeAccessedUsing(credentials));
	}
}
