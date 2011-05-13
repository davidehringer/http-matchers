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
package org.httpmatchers.internal.security;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

/**
 * @author David Ehringer
 */
public class SslSpyingDefaultHttpClient extends DefaultHttpClient {

	private final SpyingAllowAllTrustManager trustManager;

	private SslSpyingDefaultHttpClient(ClientConnectionManager conman,
			SpyingAllowAllTrustManager trustManager) {
		super(conman);
		this.trustManager = trustManager;
	}

	public SpyingAllowAllTrustManager getTrustManager() {
		return trustManager;
	}

	public static SslSpyingDefaultHttpClient createSslAwareHttpClient()
			throws NoSuchAlgorithmException, KeyManagementException {
		SpyingAllowAllTrustManager trustManager = new SpyingAllowAllTrustManager();
		SSLContext sslContext = SSLContext.getInstance("SSL");
		sslContext.init(null, new TrustManager[] { trustManager },
				new SecureRandom());

		SSLSocketFactory socketFactory = new SSLSocketFactory(sslContext,
				new AllowAllHostnameVerifier());
		Scheme httpsScheme = new Scheme("https", 443, socketFactory);
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(httpsScheme);

		ClientConnectionManager cm = new SingleClientConnManager(schemeRegistry);

		SslSpyingDefaultHttpClient httpClient = new SslSpyingDefaultHttpClient(
				cm, trustManager);
		return httpClient;
	}
}
