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

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.httpmatchers.BaseHttpMatcher;
import org.httpmatchers.access.UrlIsAccessbileMatcher;

/**
 * @author David Ehringer
 */
public class CredentialAreAuthorizedMatcher extends
		BaseHttpMatcher<Credentials> {

	private final String url;

	public CredentialAreAuthorizedMatcher(String url) {
		this.url = url;
	}

	public void describeTo(Description description) {
		description.appendText("credentials are authorized for " + url);
	}

	@Override
	protected boolean matchesSafely(Credentials credentials,
			Description mismatchDescription) {
		if (UrlIsAccessbileMatcher.isAccessible(credentials).matches(url)) {
			return true;
		}
		mismatchDescription.appendText(credentials + " are not authorized");
		return false;
	}

	@Factory
	public static Matcher<Credentials> areAuthorizedFor(String url) {
		return new CredentialAreAuthorizedMatcher(url);
	}
}
