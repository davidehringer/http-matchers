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
package org.httpmatchers.specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

/**
 * @author David Ehringer
 */
public class Specification<T> extends TypeSafeDiagnosingMatcher<T> {

	private final List<Matcher<T>> components = new ArrayList<Matcher<T>>();

	public Specification(List<Matcher<T>> components) {
		this.components.addAll(components);
	}

	public void describeTo(Description description) {
		description.appendText("a specification consisting of [");
		Iterator<Matcher<T>> iter = components.iterator();
		while (iter.hasNext()) {
			Matcher<T> component = iter.next();
			component.describeTo(description);
			if (iter.hasNext()) {
				description.appendText(", ");
			}
		}
		description.appendText("]");
	}

	@Override
	protected boolean matchesSafely(T item, Description mismatchDescription) {
		List<Matcher<T>> failedComponents = testComponents(item);
		if(failedComponents.isEmpty()){
			return true;
		}
		addFailuresToMismatchDescription(failedComponents, mismatchDescription);
		return false;
	}

	private List<Matcher<T>> testComponents(T item) {
		List<Matcher<T>> failedComponentDescriptions = new ArrayList<Matcher<T>>();
		for (Matcher<T> component : components) {
			if (!component.matches(item)) {
				failedComponentDescriptions.add(component);
			}
		}
		return failedComponentDescriptions;
	}

	private void addFailuresToMismatchDescription(
			List<Matcher<T>> failedComponents, Description description) {
		// TODO this is basically a duplicate of above
		description.appendText("the following specification components failed [");
		Iterator<Matcher<T>> iter = failedComponents.iterator();
		while (iter.hasNext()) {
			Matcher<T> component = iter.next();
			component.describeTo(description);
			if (iter.hasNext()) {
				description.appendText(", ");
			}
		}
		description.appendText("]");
	}
	// http://mail.openjdk.java.net/pipermail/coin-dev/2009-March/000217.html

	@Factory
	public static <T> Specification<T> specification(Matcher<T>... matchers) {
		return specificationFrom(matchers);
	}

	@Factory
	public static <T> Specification<T> specificationFrom(Matcher<T>[] matchers) {
		return new Specification<T>(Arrays.asList(matchers));
	}

	@Factory
	public static <T> Specification<T> specification(List<Matcher<T>> matchers) {
		return new Specification<T>(matchers);
	}
}
