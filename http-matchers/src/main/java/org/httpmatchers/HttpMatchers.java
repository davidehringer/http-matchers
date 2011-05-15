// Generated source.
package org.httpmatchers;

public class HttpMatchers {

  public static org.hamcrest.Matcher<java.lang.String> isAccessible() {
    return org.httpmatchers.access.UrlIsAccessible.isAccessible();
  }

  public static org.hamcrest.Matcher<java.lang.String> isAccessible(org.httpmatchers.security.Credentials credentials) {
    return org.httpmatchers.access.UrlIsAccessible.isAccessible(credentials);
  }

  public static org.hamcrest.Matcher<java.lang.String> hasCertificateExpiringInDays(org.hamcrest.Matcher<? super java.lang.Integer> daysMatcher) {
    return org.httpmatchers.security.CertificateExpiration.hasCertificateExpiringInDays(daysMatcher);
  }

  public static org.hamcrest.Matcher<org.httpmatchers.security.Credentials> areAuthorizedFor(java.lang.String url) {
    return org.httpmatchers.security.CredentialsAreAuthorized.areAuthorizedFor(url);
  }

  public static org.hamcrest.Matcher<java.lang.String> requiresBasicAuthentication() {
    return org.httpmatchers.security.RequiresBasicAuthentication.requiresBasicAuthentication();
  }

  public static org.hamcrest.Matcher<java.lang.String> requiresSsl() {
    return org.httpmatchers.security.RequiresSsl.requiresSsl();
  }

}
