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

import static org.httpmatchers.security.RequiresBasicAuthentication.requiresBasicAuthentication;
import static org.httpmatchers.security.RequiresSsl.requiresSsl;
import static org.httpmatchers.specification.Specification.specification;

import java.util.Arrays;

import org.junit.Ignore;
import org.junit.Test;

/**
 * @author David Ehringer
 */
public class SpecificationTest {

	@SuppressWarnings("unchecked")
	@Test
	@Ignore
	public void test() {

		specification(//
				requiresBasicAuthentication(), //
				requiresSsl());
		
		specification(Arrays.asList(requiresBasicAuthentication(),
				requiresSsl()));
	}
}
