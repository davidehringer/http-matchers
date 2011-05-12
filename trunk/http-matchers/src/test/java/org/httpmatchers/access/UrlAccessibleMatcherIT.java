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
import static org.httpmatchers.access.UrlIsAccessbileMatcher.isAccessible;

import org.junit.Test;

/**
 * @author David Ehringer
 */
public class UrlAccessibleMatcherIT {

	@Test
	public void anUrlCanBeAccessedUsingGet() {
		assertThat("http://localhost:8080", isAccessible());
	}

	@Test
	public void anSslUrlCanBeAccessedUsingGet() {
		assertThat("https://localhost:8443/", isAccessible());
	}

	@Test
	public void anyPortCanBeUsedForAnSslUrl() {
		assertThat("https://localhost:9443/", isAccessible());
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
		assertThat("https://localhost:8443/secure", isAccessible(credentials));
	}
	
	@Test
	public void http401StatusCodesAreHandledWhenCredentialsAreNotProvided(){
		assertThat("https://localhost:8443/secure", not(isAccessible()));
	}
}
