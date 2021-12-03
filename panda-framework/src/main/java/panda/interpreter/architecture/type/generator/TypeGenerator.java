/*
 * Copyright (c) 2021 dzikoysk
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

package panda.interpreter.architecture.type.generator;

import panda.interpreter.FrameworkController;
import panda.interpreter.architecture.module.Module;
import panda.interpreter.architecture.module.TypeLoader;
import panda.interpreter.architecture.type.Kind;
import panda.interpreter.architecture.type.PandaType;
import panda.interpreter.architecture.type.Reference;
import panda.interpreter.architecture.type.State;
import panda.interpreter.architecture.type.Type;
import panda.interpreter.architecture.type.Visibility;
import panda.interpreter.architecture.type.member.method.PandaMethod;
import panda.interpreter.architecture.type.member.parameter.PropertyParameterImpl;
import panda.interpreter.architecture.type.signature.GenericSignature;
import panda.interpreter.architecture.type.signature.Relation;
import panda.interpreter.architecture.type.signature.Signature;
import panda.interpreter.architecture.type.signature.TypedSignature;
import panda.interpreter.source.ClassSource;
import panda.interpreter.source.Location;
import panda.interpreter.token.PandaSnippet;
import panda.std.reactive.Completable;
import panda.utilities.ClassUtils;
import panda.utilities.ReflectionUtils;
import panda.std.Option;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class TypeGenerator {

    final Map<Class<?>, Type> initializedTypes = new HashMap<>();
    final FrameworkController frameworkController;

    public TypeGenerator(FrameworkController frameworkController) {
        this.frameworkController = frameworkController;
    }

    public Type allocate(Class<?> javaType, Type type) {
        initializedTypes.put(javaType, type);
        return type;
    }

    public Reference generate(TypeLoader stdTypeLoader, Module module, String rawName, Class<?> javaType) {
        if (rawName.endsWith("[]")) {
            rawName = rawName.replace("[]", "Array");
        }

        String name = rawName;

        return Option.of(initializedTypes.get(javaType))
                .map(Reference::new)
                .orElse(() -> module.get(name))
                .orElseGet(() -> {
                    Completable<Type> completableType = new Completable<>();
                    Location location = new ClassSource(module, javaType).toLocation();
                    Reference reference = new Reference(completableType, module, name, Visibility.OPEN, Kind.of(javaType), location);

                    TypeVariable<?>[] javaGenerics = javaType.getTypeParameters();
                    Signature[] generics = new Signature[javaGenerics.length];

                    for (int index = 0; index < javaGenerics.length; index++) {
                        generics[index] = new GenericSignature(stdTypeLoader, null, javaGenerics[index].getName(), null, new Signature[0], Relation.DIRECT, PandaSnippet.empty());
                    }

                    Type type = PandaType.builder()
                            .name(name)
                            .module(module)
                            .associatedType(Completable.completed(javaType))
                            .isNative(true)
                            .signature(new TypedSignature(null, reference, generics, Relation.DIRECT, PandaSnippet.empty()))
                            .location(reference.getLocation())
                            .kind(reference.getKind())
                            .state(State.of(javaType))
                            .visibility(Visibility.OPEN)
                            .build();

                    type.addInitializer((typeLoader, initializedType) -> {
                        Class<?> baseClass = javaType.getSuperclass();

                        // Object.class does not have supertype
                        if (baseClass != null) {
                            type.addBase(typeLoader.load(findOrGenerate(typeLoader, module, baseClass)).getSignature());
                        }

                        for (Class<?> javaInterface : javaType.getInterfaces()) {
                            type.addBase(typeLoader.load(findOrGenerate(typeLoader, module, javaInterface)).getSignature());
                        }

                        if (!javaType.equals(Object.class) && type.getBases().isEmpty()) {
                            // type.addAutocast(typeLoader.requireType("panda/panda@::Object"), (originalType, object, resultType) -> object);
                            type.addBase(typeLoader.requireType("panda/panda@::Object").getSignature());
                        }

                        if (!Modifier.isPublic(javaType.getModifiers())) {
                            return;
                        }

                        for (Field field : javaType.getDeclaredFields()) {
                            if (!Modifier.isPublic(field.getModifiers())) {
                                continue;
                            }

                            FieldGenerator generator = new FieldGenerator(this, initializedType, field);
                            initializedType.getFields().declare(field.getName(), () -> generator.generate(typeLoader));
                        }

                        for (Constructor<?> constructor : ReflectionUtils.getByModifier(javaType.getDeclaredConstructors(), Modifier.PUBLIC)) {
                            ConstructorGenerator generator = new ConstructorGenerator(this, initializedType, constructor);
                            initializedType.getConstructors().declare(constructor.toString(), () -> generator.generate(typeLoader));
                        }

                        for (Method method : ReflectionUtils.getByModifier(javaType.getDeclaredMethods(), Modifier.PUBLIC)) {
                            MethodGenerator generator = new MethodGenerator(frameworkController, this, initializedType, method);
                            initializedType.getMethods().declare(method.getName(), () -> generator.generate(typeLoader));
                        }

                        if (javaType.isArray()) {
                            Signature componentType = typeLoader.forJavaType(javaType.getComponentType()).get().getSignature();
                            Signature indexType = typeLoader.forJavaType(int.class).get().getSignature();

                            type.getMethods().declare("set", () -> PandaMethod.builder()
                                    .type(type)
                                    .name("set")
                                    .parameters(Arrays.asList(
                                            new PropertyParameterImpl(0, indexType, "index", false, false),
                                            new PropertyParameterImpl(1, componentType, "value", false, true)
                                    ))
                                    .returnType(type.getSignature())
                                    .customBody((property, stack, instance, arguments) -> {
                                        ((Object[]) Objects.requireNonNull(instance))[(int) arguments[0]] = arguments[1];
                                        return instance;
                                    })
                                    .location(location)
                                    .build());
                            type.getMethods().declare("get", () -> PandaMethod.builder()
                                    .type(type)
                                    .name("get")
                                    .parameters(Collections.singletonList(new PropertyParameterImpl(0, typeLoader.forJavaType(int.class).get().getSignature(), "index", false, false)))
                                    .returnType(componentType)
                                    .customBody((property, stack, instance, arguments) -> ((Object[]) Objects.requireNonNull(instance))[(int) arguments[0]])
                                    .location(location)
                                    .build());

                            TypedSignature typedListSignature = new TypedSignature(
                                    null,
                                    typeLoader.forJavaType(List.class).get().getReference(),
                                    new Signature[] { typeLoader.forJavaType(String.class).get().getSignature() },
                                    Relation.DIRECT,
                                    PandaSnippet.empty()
                            );

                            type.getMethods().declare("toList", () -> PandaMethod.builder()
                                    .type(type)
                                    .name("toList")
                                    .parameters(Collections.emptyList())
                                    .returnType(typedListSignature)
                                    .customBody((property, stack, instance, arguments) -> Arrays.asList((Object[]) Objects.requireNonNull(instance)))
                                    .location(location)
                                    .build());
                        }
                    });

                    completableType.complete(allocate(javaType, type));
                    return type.getReference();
                });
    }

    Type findOrGenerate(TypeLoader typeLoader, Module module, Class<?> javaType) {
        if (javaType.isPrimitive()) {
            javaType = ClassUtils.getNonPrimitiveClass(javaType);
        }

        Option<Type> typeValue = typeLoader.forJavaType(javaType);

        if (typeValue.isDefined()) {
            return typeValue.get();
        }

        Type type = initializedTypes.get(javaType);

        if (type != null) {
            return type;
        }

        return generate(typeLoader, module, javaType.getSimpleName(), javaType).fetchType();
    }

}
