/*
 * Copyright (c) 2015-2019 Dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.panda_lang.panda.utilities.autodata.data.repository;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

final class RepositoryProxyInvocationHandler implements InvocationHandler {

    private final Map<String, RepositoryProxyMethod> generatedFunctions = new HashMap<>();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
        RepositoryProxyMethod function = generatedFunctions.get(method.getName());

        if (function == null) {
            return null; // or throw?
        }

        return function.apply(args);
    }

    protected void addFunctions(Map<String, RepositoryProxyMethod> functions) {
        generatedFunctions.putAll(functions);
    }

}
