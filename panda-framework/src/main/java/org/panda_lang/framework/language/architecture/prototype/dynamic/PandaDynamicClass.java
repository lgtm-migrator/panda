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

package org.panda_lang.framework.language.architecture.prototype.dynamic;

import javassist.ClassPool;
import javassist.CtClass;
import org.panda_lang.framework.PandaFrameworkException;
import org.panda_lang.framework.design.architecture.prototype.DynamicClass;
import org.panda_lang.framework.design.architecture.prototype.PrototypeModels;
import org.panda_lang.framework.language.architecture.prototype.PrototypeClass;
import org.panda_lang.utilities.commons.ClassPoolUtils;
import org.panda_lang.utilities.commons.ClassUtils;
import org.panda_lang.utilities.commons.ObjectUtils;
import org.panda_lang.utilities.commons.PackageUtils;
import org.panda_lang.utilities.commons.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public final class PandaDynamicClass implements DynamicClass {

    private static final AtomicInteger ID = new AtomicInteger();
    private static final DynamicClassGenerator GENERATOR = new DynamicClassGenerator();

    protected final String module;
    protected final String model;

    protected String name;
    protected final Collection<DynamicClass> interfaces = new ArrayList<>();
    protected DynamicClass superclass = null;
    protected boolean frozen;
    protected boolean changed;

    protected Class<?> structure;
    protected Class<?> implementation;

    public PandaDynamicClass(String name, String module, String model) {
        if (module == null) {
            throw new IllegalArgumentException("Module cannot be null");
        }

        if (model == null) {
            throw new IllegalArgumentException("Model cannot be null");
        }

        this.name = name;
        this.model = model;
        this.module = StringUtils.replace(module, ":", ".");
        this.changed = true;
    }

    public PandaDynamicClass(Class<?> clazz, String customName, String module) {
        this(customName, module, PrototypeModels.of(clazz));
        this.structure = clazz;
        this.implementation = clazz;
        this.frozen = true;
    }

    public PandaDynamicClass(Class<?> clazz) {
        this(clazz, clazz.getSimpleName(), PackageUtils.toString(clazz.getPackage(), StringUtils.EMPTY));
    }

    private void recreate() throws DynamicClassException {
        if (frozen || changed) {
            return;
        }

        String className = module + "." + name + ID.incrementAndGet();
        CtClass generatedClass;

        if (superclass != null) {
            generatedClass = ClassPool.getDefault().makeClass(name, DynamicClassUtils.get(superclass.fetchStructure()));
        }
        else {
            generatedClass = ClassPool.getDefault().makeInterface(name, DynamicClassUtils.get(PrototypeClass.class));
        }

        if (structure != null) {
            generatedClass.addInterface(DynamicClassUtils.get(structure));
        }

        if (superclass != null) {
            if (superclass.isInterface()) {

            }
            CtClass ctSuperclass = DynamicClassUtils.get(superclass.fetchStructure());

            this.structure = ClassPoolUtils.toClass(generatedClass);
        }

    }

    private DynamicClass update(Runnable action) {
        action.run();
        this.changed = true;
        return this;
    }

    @Override
    public DynamicClass append(Class<?> clazz) {
        return update(() -> {
            DynamicClass dynamicClass = new PandaDynamicClass(clazz);

            if (dynamicClass.isClass()) {
                extendClass(dynamicClass);
            }
            else if (dynamicClass.isInterface()) {
                implementInterface(dynamicClass);
            }
            else {
                throw new PandaFrameworkException("Unsupported model " + model);
            }
        });
    }

    @Override
    public DynamicClass extendClass(DynamicClass superclass) {
        if (this.superclass != null) {
            throw new IllegalStateException("Class cannot extend more than one class");
        }

        return update(() -> this.superclass = superclass);
    }

    @Override
    public DynamicClass implementInterface(DynamicClass interfaceClass) {
        return update(() -> interfaces.add(interfaceClass));
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, module, model);
    }

    @Override
    public boolean equals(Object o) { // lgtm [java/unchecked-cast-in-equals]
        DynamicClass that = ObjectUtils.cast(o);
        return that != null && model.equals(that.getModule()) && name.equals(that.getSimpleName()) && model.equals(that.getModel());
    }

    @Override
    public boolean isAssignableFrom(Class<?> cls) {
        return ClassUtils.isAssignableFrom(implementation, cls);
    }

    @Override
    public boolean isAssignableTo(Class<?> cls) {
        return ClassUtils.isAssignableFrom(cls, implementation);
    }

    @Override
    public boolean isClass() {
        return PrototypeModels.CLASS.equals(model);
    }

    @Override
    public boolean isInterface() {
        return PrototypeModels.INTERFACE.equals(model);
    }

    @Override
    public Class<?> fetchImplementation() {
        return implementation;
    }

    @Override
    public Class<?> fetchStructure() {
        return structure;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public String getModule() {
        return module;
    }

    @Override
    public String getSimpleName() {
        return name;
    }

    @Override
    public String getName() {
        return getModule() + "." + getSimpleName();
    }

}
