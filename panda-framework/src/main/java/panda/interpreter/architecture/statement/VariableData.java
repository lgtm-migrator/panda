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

package panda.interpreter.architecture.statement;

import panda.interpreter.architecture.type.signature.Signature;
import panda.interpreter.architecture.type.signature.Signed;
import panda.std.reactive.Completable;

/**
 * Represents basic data about variable
 */
public interface VariableData extends Signed {

    boolean interfereSignature(Signature signature);

    boolean awaitsSignature();

    Completable<Signature> getSignatureReference();

    /**
     * Check if variable is nillable (accepts null values)
     *
     * @return true if variable is nillable
     */
    boolean isNillable();

    /**
     * Check if variable is mutable
     *
     * @return true if variable is mutable
     */
    boolean isMutable();

    /**
     * Get name of parameter
     *
     * @return the name
     */
    String getName();

}
