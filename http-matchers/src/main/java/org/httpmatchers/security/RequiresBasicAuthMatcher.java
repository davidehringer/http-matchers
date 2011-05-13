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

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.httpmatchers.BaseHttpMatcher;

/**
 * @author David Ehringer
 */
public class RequiresBasicAuthMatcher extends BaseHttpMatcher<String> {

	public RequiresBasicAuthMatcher() {
		super();
	}

	public RequiresBasicAuthMatcher(Credentials credentials) {
		super(credentials);
	}

	public void describeTo(Description description) {
		description.appendText("a URL that requires Basic Authentication");
	}

	@Override
	protected boolean matchesSafely(String url, Description mismatchDescription) {
		try {
			HttpClient httpclient = createHttpClient(url);
			HttpGet get = new HttpGet(url);
			HttpResponse response = null;
			response = httpclient.execute(get);
			if (isUnauthorized(response)
					&& receivedBasicAuthChallenge(response)) {
				return true;
			}

			appendMismatchDescription(mismatchDescription, response);
			return false;
		} catch (Exception e) {
			appendMismatchExceptionDescription(url, mismatchDescription, e);
			return false;
		}
	}

	private boolean isUnauthorized(HttpResponse response) {
		return 401 == response.getStatusLine().getStatusCode();
	}

	private boolean receivedBasicAuthChallenge(HttpResponse response) {
		for (Header header : response.getAllHeaders()) {
			if ("WWW-Authenticate".equalsIgnoreCase(header.getName().trim())
					&& header.getValue().startsWith("basic")) {
				return true;
			}
		}
		return false;
	}

	private void appendMismatchDescription(Description mismatchDescription,
			HttpResponse response) {
		int responseCode = response.getStatusLine().getStatusCode();
		mismatchDescription.appendText("received HTTP response status code "
				+ responseCode
				+ " (expected a 401 and a \"WWW-Authenticate: basic\" header)");
	}

	@Factory
	public static Matcher<String> requiresBasicAuthentication() {
		return new RequiresBasicAuthMatcher();
	}
}
