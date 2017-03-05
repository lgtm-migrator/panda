/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.panda.language.structure.prototype.structure.method.parameter;

import org.panda_lang.panda.implementation.structure.Variable;
import org.panda_lang.panda.implementation.structure.PandaVariable;
import org.panda_lang.panda.language.structure.prototype.ClassPrototype;

public class Parameter {

    private final ClassPrototype parameterType;
    private final String parameterName;

    public Parameter(ClassPrototype parameterType, String parameterName) {
        this.parameterType = parameterType;
        this.parameterName = parameterName;
    }

    public String getParameterName() {
        return parameterName;
    }

    public ClassPrototype getParameterType() {
        return parameterType;
    }

    public Variable toVariable() {
        return new PandaVariable(parameterType, parameterName);
    }

}
