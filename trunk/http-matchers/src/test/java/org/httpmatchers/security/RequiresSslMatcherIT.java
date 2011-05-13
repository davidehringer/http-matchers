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

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.not;
import static org.httpmatchers.security.RequiresSslMatcher.requiresSsl;
import static org.httpmatchers.Urls.*;
import org.junit.Test;

/**
 * @author David Ehringer
 */
public class RequiresSslMatcherIT {

	@Test
	public void sslCanBeRequired(){
		assertThat(HTTPS_URL, requiresSsl());
	}
	
	@Test
	public void lackOfSslCanBeRequired(){
		assertThat(HTTP_URL, not(requiresSsl()));
	}
}
