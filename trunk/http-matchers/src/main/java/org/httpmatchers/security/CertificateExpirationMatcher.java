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

import java.util.Date;

import org.apache.http.client.methods.HttpGet;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.httpmatchers.access.BaseHttpMatcher;
import org.httpmatchers.access.SslSpyingDefaultHttpClient;

/**
 * @author David Ehringer
 */
public class CertificateExpirationMatcher extends BaseHttpMatcher<String> {

	public final Matcher<? super Integer> daysMatcher;

	public CertificateExpirationMatcher(Matcher<? super Integer> daysMatcher) {
		super();
		this.daysMatcher = daysMatcher;
	}

	public void describeTo(Description description) {
		description.appendText("a URL with an SSL certificate expiring in a number of days with ");
		description.appendDescriptionOf(daysMatcher);
	}

	@Override
	protected boolean matchesSafely(String url, Description mismatchDescription) {
		try {
			SslSpyingDefaultHttpClient httpClient = SslSpyingDefaultHttpClient
					.createSslAwareHttpClient();
			HttpGet get = new HttpGet(url);
			httpClient.execute(get);
			SpyingAllowAllTrustManager trustManager = httpClient
					.getTrustManager();
			if (trustManager.checkForTrustedCertHasOccured()) {
				Date expirationDate = trustManager.getCheckCertValidUntilDate();
				int daysUntilExpiration = daysUntil(expirationDate);
				if(daysMatcher.matches(daysUntilExpiration)){
					return true;
				}

				mismatchDescription.appendText("the certificate expires in ");
				mismatchDescription.appendValue(daysUntilExpiration);
				mismatchDescription.appendText(" days");
				return false;
			}

			mismatchDescription
					.appendText("no check for a trusted certificate occured");
			return false;
		} catch (Exception e) {
			appendMismatchExceptionDescription(url, mismatchDescription, e);
			return false;
		}
	}

	private int daysUntil(Date date) {
		Date today = new Date();
		return (int) ((date.getTime() - today.getTime()) / (1000 * 60 * 60 * 24));
	}

	@Factory
	public static Matcher<String> hasCertificateExpiringInDays(
			Matcher<? super Integer> daysMatcher) {
		return new CertificateExpirationMatcher(daysMatcher);
	}
}
