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

package panda.interpreter.architecture.type.signature;

import org.jetbrains.annotations.Nullable;
import panda.interpreter.architecture.module.TypeLoader;
import panda.interpreter.architecture.type.Type;
import panda.interpreter.token.Snippetable;
import panda.std.Option;
import panda.std.Pair;
import panda.utilities.text.Joiner;

public final class GenericSignature extends AbstractSignature<Pair<String, Signature>> {

    private final TypeLoader typeLoader;

    public GenericSignature(TypeLoader typeLoader, @Nullable Signature parent, String identifier, Signature signature, Signature[] generics, Relation relation, Snippetable source) {
        super(parent, new Pair<>(identifier, signature), generics, relation, source);
        this.typeLoader = typeLoader;
    }

    @Override
    public Signature apply(Signed context) {
        Option<Pair<GenericSignature, Signature>> result = context.getSignature().findGeneric(this);

        if (result.isPresent()) {
            return result.get().getSecond();
        }

        return new GenericSignature(typeLoader, getParent().getOrNull(), getLocalIdentifier(), getSubject().getSecond(), applyGenerics(context), getRelation(), getSource());
    }

    @Override
    public boolean isGeneric() {
        return true;
    }

    @Override
    public GenericSignature toGeneric() {
        return this;
    }

    public boolean isWildcard() {
        return getLocalIdentifier().equals("*");
    }

    public boolean hasWildcardDescription() {
        return getSubject().getSecond() != null;
    }

    public Option<Signature> getWildcardDescription() {
        return Option.of(getSubject().getSecond());
    }

    public String getLocalIdentifier() {
        return getSubject().getFirst();
    }

    public Option<Signature> getAny() {
        Signature signature = getSubject().getSecond();
        return Option.when(signature != null && Relation.ANY == signature.getRelation(), signature);
    }

    public Option<Signature> getAlso() {
        Signature signature = getSubject().getSecond();
        return Option.when(signature != null && Relation.ALSO == signature.getRelation(), signature);
    }

    @Override
    public Type toType() {
        return getAny().map(Signature::toType)
                .orElse(() -> getAlso().map(Signature::toType))
                .orElseGet(() -> typeLoader.requireType("panda/panda@::Object"));
    }

    @Override
    public String toString() {
        return getRelation() + " : abstract " + getLocalIdentifier() + (getGenerics().length == 0 ? "" : "<" + Joiner.on(", ").join(getGenerics()) + ">");
    }

}
