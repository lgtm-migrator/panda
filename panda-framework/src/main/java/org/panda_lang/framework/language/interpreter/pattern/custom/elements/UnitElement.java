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

package org.panda_lang.framework.language.interpreter.pattern.custom.elements;

import org.panda_lang.framework.language.interpreter.pattern.custom.CustomPatternElementBuilder;
import org.panda_lang.framework.design.interpreter.token.TokenRepresentation;

public final class UnitElement extends CustomPatternElementBuilder<TokenRepresentation, UnitElement> {

    private UnitElement(String id) {
        super(id);
    }

    public UnitElement content(String value) {
        super.custom((data, source) -> source.next().getValue().equals(value) ? source.getCurrent() : null);
        return this;
    }


    public static UnitElement create(String id) {
        return new UnitElement(id);
    }

}
