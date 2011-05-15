// Generated source.
package org.httpmatchers;

public class Specifications {

	public static <T> org.hamcrest.Matcher<T> adheresTo(
			org.httpmatchers.specification.Specification<T> specification) {
		return org.httpmatchers.specification.AdheresTo
				.<T> adheresTo(specification);
	}

	public static <T> org.httpmatchers.specification.Specification<T> specification(
			java.util.List<org.hamcrest.Matcher<T>> matchers) {
		return org.httpmatchers.specification.Specification
				.<T> specification(matchers);
	}

	public static <T> org.httpmatchers.specification.Specification<T> specification(
			org.hamcrest.Matcher<T>... matchers) {
		return org.httpmatchers.specification.Specification
				.<T> specification(matchers);
	}

	public static <T> org.httpmatchers.specification.Specification<T> specificationFrom(
			org.hamcrest.Matcher<T>[] param1) {
		return org.httpmatchers.specification.Specification
				.<T> specificationFrom(param1);
	}
}
