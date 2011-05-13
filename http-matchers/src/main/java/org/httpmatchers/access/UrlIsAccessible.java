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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.httpmatchers.internal.BaseHttpMatcher;
import org.httpmatchers.security.Credentials;

/**
 * @author David Ehringer
 */
public class UrlIsAccessible extends BaseHttpMatcher<String> {

	public UrlIsAccessible() {
		super();
	}

	public UrlIsAccessible(Credentials credentials) {
		super(credentials);
	}

	public void describeTo(Description description) {
		description
				.appendText("to be able to access the URL using an HTTP GET");
	}

	@Override
	protected boolean matchesSafely(String url, Description mismatchDescription) {
		try {
			HttpClient httpclient = createHttpClient(url);
			HttpGet get = new HttpGet(url);
			HttpResponse response = httpclient.execute(get);
			return verifyStatusCode(mismatchDescription, response);
		} catch (Exception e) {
			appendMismatchExceptionDescription(url, mismatchDescription, e);
			return false;
		}
	}

	private boolean verifyStatusCode(Description mismatchDescription,
			HttpResponse response) {
		int statusCode = response.getStatusLine().getStatusCode();
		if (200 == statusCode) {
			return true;
		}
		mismatchDescription.appendText("HTTP status code was " + statusCode);
		return false;
	}

	@Factory
	public static Matcher<String> isAccessible() {
		return new UrlIsAccessible();
	}

	@Factory
	public static Matcher<String> isAccessible(Credentials credentials) {
		return new UrlIsAccessible(credentials);
	}
}
