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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers.logical;

import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.framework.design.architecture.type.Type;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.rpn.RPNOperationAction;

public final class AndOperator extends OrOperation {

    @Override
    public RPNOperationAction<Boolean> of(ModuleLoader loader, Expression a, Expression b) {
        return new RPNOperationAction<Boolean>() {
            @Override
            public Boolean get(ProcessStack stack, Object instance) throws Exception {
                Boolean aValue = a.evaluate(stack, instance);

                if (!aValue) {
                    return false;
                }

                return b.evaluate(stack, instance);
            }

            @Override
            public Type returnType(ModuleLoader loader) {
                return AndOperator.super.requiredType(loader);
            }
        };
    }
}
