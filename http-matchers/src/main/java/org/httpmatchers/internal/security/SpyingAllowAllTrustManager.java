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

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.net.ssl.X509TrustManager;

/**
 * @author David Ehringer
 */
public class SpyingAllowAllTrustManager implements X509TrustManager {
	
	private boolean checkForTrustedCertOccured = false;
	private Date certValidUntil;

	public void checkClientTrusted(X509Certificate[] certificates,
			String authType) throws CertificateException {
		checkForTrustedCertOccured = true;
	}

	public void checkServerTrusted(X509Certificate[] certificates,
			String authType) throws CertificateException {
		checkForTrustedCertOccured = true;
		// TODO how to handle multiple certs?
		for (X509Certificate cert : certificates) {
			System.out.println("Certificate: " + cert);
			certValidUntil = cert.getNotAfter();
		}
	}

	public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[0];
	}

	public boolean checkForTrustedCertHasOccured() {
		return checkForTrustedCertOccured;
	}

	public Date getCheckCertValidUntilDate() {
		return certValidUntil;
	}
}
