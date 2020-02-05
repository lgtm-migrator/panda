/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.framework.language.architecture.module;

import io.vavr.collection.Stream;
import io.vavr.control.Option;
import org.panda_lang.framework.design.architecture.module.ReferencesMap;
import org.panda_lang.framework.design.architecture.type.DynamicClass;
import org.panda_lang.framework.design.architecture.type.Reference;
import org.panda_lang.utilities.commons.ClassUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

final class PandaReferencesMap extends HashMap<String, Reference> implements ReferencesMap {

    private final Map<DynamicClass, String> associatedClasses = new HashMap<>();

    @Override
    public boolean put(Reference reference) {
        if (associatedClasses.containsKey(reference.getAssociatedClass()) || containsKey(reference.getName())) {
            return false;
        }

        super.put(reference.getName(), reference);
        associatedClasses.put(reference.getAssociatedClass(), reference.getName());
        return true;
    }

    @Override
    public int countUsedPrototypes() {
        return Stream.ofAll(entrySet())
                .count(entry -> entry.getValue().isInitialized());
    }

    @Override
    public Option<Reference> forClass(Class<?> associatedClass) {
        return get(associatedClass)
                .flatMap(this::forName)
                .orElse(() -> associatedClass.isPrimitive() ? forClass(ClassUtils.getNonPrimitiveClass(associatedClass)) : Option.none());
    }

    private Option<String> get(Class<?> associatedClass) {
        return Stream.ofAll(associatedClasses.entrySet())
                .find(entry -> entry.getKey().fetchImplementation().equals(associatedClass))
                .map(Entry::getValue);
    }

    @Override
    public Option<Reference> forName(CharSequence typeName) {
        return Option.of(get(typeName.toString()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<Entry<String, Reference>> getPrototypes() {
        Object sharedSet = entrySet(); // due to javac 1.8 bug
        return new HashSet<>((Collection<? extends Entry<String, Reference>>) sharedSet);
    }

}
