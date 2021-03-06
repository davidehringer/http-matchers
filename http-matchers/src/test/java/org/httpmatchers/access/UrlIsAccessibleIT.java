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
package org.httpmatchers.access;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.httpmatchers.Urls.HTTP_URL;
import static org.httpmatchers.Urls.HTTPS_URL;
import static org.httpmatchers.Urls.HTTPS_2_URL;
import static org.httpmatchers.Urls.HTTPS_WITH_BASIC_AUTH_URL;
import static org.httpmatchers.access.UrlIsAccessible.isAccessible;

import org.httpmatchers.security.Credentials;
import org.junit.Test;

/**
 * @author David Ehringer
 */
public class UrlIsAccessibleIT {

	@Test
	public void anUrlCanBeAccessedUsingGet() {
		assertThat(HTTP_URL, isAccessible());
	}

	@Test
	public void anSslUrlCanBeAccessedUsingGet() {
		assertThat(HTTPS_URL, isAccessible());
	}

	@Test
	public void anyPortCanBeUsedForAnSslUrl() {
		assertThat(HTTPS_2_URL, isAccessible());
	}

	@Test
	public void httpsCanBeInAnyCase() {
		assertThat("https://localhost:8443/", isAccessible());
		assertThat("hTtPs://localhost:8443/", isAccessible());
		assertThat("HTTPS://localhost:8443/", isAccessible());
	}
	
	@Test
	public void credentialsCanBeProvidedToAccessPagesRequiringAuthentication(){
		Credentials credentials = new Credentials("admin", "secret");
		assertThat(HTTPS_WITH_BASIC_AUTH_URL, isAccessible(credentials));
	}
	
	@Test
	public void http401StatusCodesAreHandledWhenCredentialsAreNotProvided(){
		assertThat(HTTPS_WITH_BASIC_AUTH_URL, not(isAccessible()));
	}
}
