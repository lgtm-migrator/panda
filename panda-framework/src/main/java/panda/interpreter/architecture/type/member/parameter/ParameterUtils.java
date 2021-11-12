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

package panda.interpreter.architecture.type.member.parameter;

import panda.interpreter.architecture.expression.Expression;
import panda.interpreter.architecture.statement.AbstractPropertyFramedScope;
import panda.interpreter.architecture.type.Type;
import panda.interpreter.architecture.type.member.MemberFrameImpl;
import panda.interpreter.architecture.type.signature.Signature;
import panda.interpreter.runtime.PandaRuntimeException;
import panda.utilities.text.Joiner;
import panda.std.reactive.Completable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public final class ParameterUtils {

    public static final PropertyParameter[] PARAMETERLESS = new PropertyParameter[0];

    private ParameterUtils() { }

    public static void assignValues(MemberFrameImpl<? extends AbstractPropertyFramedScope> instance, Object[] parameterValues) {
        if (instance.getMemorySize() < parameterValues.length) {
            throw new PandaRuntimeException("Incompatible number of parameters");
        }

        List<? extends PropertyParameter> parameters = instance.getFramedScope().getParameters();

        for (int index = 0; index < parameterValues.length; index++) {
            Object value = parameterValues[index];

            if (value == null) {
                PropertyParameter parameter = parameters.get(index);

                if (!parameter.isNillable()) {
                    throw new PandaRuntimeException("Cannot assign null to  the '" + parameter.getName() + "' parameter without nil modifier");
                }
            }

            instance.set(index, value);
        }
    }

    public static Signature[] toTypes(Expression... expressions) {
        Signature[] types = new Signature[expressions.length];

        for (int index = 0; index < types.length; index++) {
            Expression expression = expressions[index];
            types[index] = expression.getSignature();
        }

        return types;
    }

    public static Class<?>[] parametersToClasses(List<? extends PropertyParameter> parameters) {
        return parametersToClasses(parameters.stream());
    }

    public static Class<?>[] parametersToClasses(PropertyParameter[] parameters) {
        return parametersToClasses(Arrays.stream(parameters));
    }

    private static Class<?>[] parametersToClasses(Stream<? extends PropertyParameter> parameterStream) {
        return parameterStream
                .map(PropertyParameter::getSignature)
                .map(Signature::getKnownType)
                .map(Type::getAssociated)
                .map(Completable::get)
                .toArray(Class[]::new);
    }

    public static String toString(List<? extends PropertyParameter> parameters) {
        return Joiner.on(", ")
                .join(parameters, parameter -> parameter.getSignature() + " " + parameter.getName())
                .toString();
    }

}
