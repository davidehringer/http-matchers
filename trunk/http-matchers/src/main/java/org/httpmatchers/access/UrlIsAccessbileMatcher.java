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

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

/**
 * @author David Ehringer
 */
public class UrlIsAccessbileMatcher extends TypeSafeDiagnosingMatcher<String> {

	private final Credentials credentials;

	public UrlIsAccessbileMatcher() {
		this.credentials = null;
	}

	public UrlIsAccessbileMatcher(Credentials credentials) {
		this.credentials = credentials;
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
			e.printStackTrace();
			appendMismatchExceptionDescription(url, mismatchDescription, e);
			return false;
		}
	}

	private HttpClient createHttpClient(String url)
			throws NoSuchAlgorithmException, KeyManagementException {
		DefaultHttpClient httpClient;
		if (isSsl(url)) {
			httpClient = createSslAwareHttpClient();
		} else {
			httpClient = new DefaultHttpClient();
		}
		handleCredentials(httpClient);
		return httpClient;
	}

	private boolean isSsl(String url) {
		return url.trim().toLowerCase().startsWith("https");
	}

	private DefaultHttpClient createSslAwareHttpClient()
			throws NoSuchAlgorithmException, KeyManagementException {
		SSLContext sslContext = SSLContext.getInstance("SSL");
		sslContext.init(null,
				new TrustManager[] { new SpyingAllowAllTrustManager() },
				new SecureRandom());

		SSLSocketFactory socketFactory = new SSLSocketFactory(sslContext,
				new AllowAllHostnameVerifier());
		Scheme httpsScheme = new Scheme("https", 8443, socketFactory);
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(httpsScheme);

		ClientConnectionManager cm = new SingleClientConnManager(schemeRegistry);

		return new DefaultHttpClient(cm);
	}

	private void handleCredentials(DefaultHttpClient httpClient) {
		if (credentials != null) {
			httpClient.getCredentialsProvider().setCredentials(
					new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
					new UsernamePasswordCredentials(credentials.getUsername(),
							credentials.getPassword()));
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

	private void appendMismatchExceptionDescription(String url,
			Description mismatchDescription, Exception e) {
		mismatchDescription.appendText(url + " cannot be accessed due to \""
				+ e.getMessage() + "\" (" + e + ")");
	}

	@Factory
	public static Matcher<String> isAccessible() {
		return new UrlIsAccessbileMatcher();
	}

	@Factory
	public static Matcher<String> isAccessible(Credentials credentials) {
		return new UrlIsAccessbileMatcher(credentials);
	}
}
