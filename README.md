Break the Dev/Ops divide!  Test drive your middleware configuration and setup.

This is an experimental project in its infancy.  It is a small set of Hamcrest Matchers now and a small DSL that uses them in the future.

For now check out some of the tests for basic examples:
  * [Basic Security](http-matchers/src/test/java/org/httpmatchers/security)
  * [Accessibility/Availability](http-matchers/src/test/java/org/httpmatchers/access)

Example of the URL specification DSL:

```
Credentials adminCredentials = new Credentials("admin", "secret");
Credentials guestCredentials = new Credentials("guest", "password");

Specification<String> specification = 
	specification(
		requiresBasicAuthentication(),
		requiresSsl(),
		canBeAccessedUsing(adminCredentials),
		not(canBeAccessedUsing(guestCredentials)));

assertThat(HTTPS_WITH_BASIC_AUTH_URL, adheresTo(specification));
```
