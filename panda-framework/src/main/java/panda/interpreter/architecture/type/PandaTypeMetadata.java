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

package panda.interpreter.architecture.type;

import panda.interpreter.architecture.module.Module;
import panda.interpreter.architecture.type.signature.Signature;
import panda.interpreter.architecture.type.signature.TypedSignature;
import panda.interpreter.source.ClassSource;
import panda.interpreter.source.Location;
import panda.std.reactive.Completable;

import java.util.ArrayList;
import java.util.List;

public class PandaTypeMetadata<BUILDER extends PandaTypeMetadata<BUILDER, ?>, TYPE extends PandaType> {

    protected String name;
    protected Signature signature;
    protected Module module;
    protected Location location;
    protected TypeScope typeScope;
    protected Completable<? extends Class<?>> associatedType;
    protected List<TypedSignature> bases = new ArrayList<>();
    protected String kind = Kind.TYPE;
    protected State state = State.DEFAULT;
    protected Visibility visibility = Visibility.OPEN;
    protected boolean isNative;

    protected PandaTypeMetadata() { }

    public BUILDER name(String name) {
        this.name = name;
        return getThis();
    }

    public BUILDER signature(Signature signature) {
        this.signature = signature;
        return getThis();
    }

    public BUILDER module(Module module) {
        this.module = module;
        return getThis();
    }

    public BUILDER typeScope(TypeScope typeScope) {
        this.typeScope = typeScope;
        return getThis();
    }

    public BUILDER location(Location location) {
        this.location = location;
        return getThis();
    }

    public BUILDER location(Class<?> javaType) {
        return location(new ClassSource(module, javaType).toLocation());
    }

    public BUILDER associatedType(Completable<? extends Class<?>> associatedType) {
        this.associatedType = associatedType;
        return getThis();
    }

    public BUILDER bases(List<TypedSignature> bases) {
        this.bases.addAll(bases);
        return getThis();
    }

    public BUILDER kind(String kind) {
        this.kind = kind;
        return getThis();
    }

    public BUILDER state(State state) {
        this.state = state;
        return getThis();
    }

    public BUILDER visibility(Visibility visibility) {
        this.visibility = visibility;
        return getThis();
    }

    public BUILDER isNative(boolean isNative) {
        this.isNative = isNative;
        return getThis();
    }

    @SuppressWarnings("unchecked")
    public TYPE build() {
        return (TYPE) new PandaType(this);
    }

    @SuppressWarnings("unchecked")
    protected BUILDER getThis() {
        return (BUILDER) this;
    }

}
