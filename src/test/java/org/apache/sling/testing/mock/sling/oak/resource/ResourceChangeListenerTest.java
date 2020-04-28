/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sling.testing.mock.sling.oak.resource;

import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class ResourceChangeListenerTest {

    @Rule
    public SlingContext context = new SlingContext(ResourceResolverType.JCR_OAK);

    @Test
    public void testResourceChangeListener() throws InterruptedException, PersistenceException {
        CountDownLatch gate = new CountDownLatch(1);
        ResourceChangeListener listener = changes -> gate.countDown();
        context.registerService(ResourceChangeListener.class, listener, ResourceChangeListener.PATHS, "/");

        ResourceResolver resourceResolver = context.resourceResolver();
        resourceResolver.create(resourceResolver.getResource("/"), "foobar", Collections.emptyMap());
        resourceResolver.commit();

        Assert.assertTrue(gate.await(200, TimeUnit.MILLISECONDS));
    }
}
