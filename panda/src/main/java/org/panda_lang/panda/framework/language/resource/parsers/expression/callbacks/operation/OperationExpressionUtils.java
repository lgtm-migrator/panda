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

package org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.operation;

import org.panda_lang.panda.framework.design.interpreter.pattern.progressive.ProgressivePatternElement;
import org.panda_lang.panda.framework.design.interpreter.pattern.progressive.ProgressivePattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.progressive.ProgressivePatternResult;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.resource.syntax.operator.Operators;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;

public class OperationExpressionUtils {

    public static final Token[] OPERATORS = new Token[]{
            Operators.ADDITION,
            Operators.SUBTRACTION,
            Operators.DIVISION,
            Operators.MULTIPLICATION,

            Operators.BITWISE_AND,
            Operators.BITWISE_NOT,
            Operators.BITWISE_OR,
            Operators.BITWISE_XOR,
            Operators.BITWISE_LEFT_SHIFT,
            Operators.BITWISE_RIGHT_SHIFT
    };

    public static final ProgressivePattern OPERATION_PATTERN = new ProgressivePattern(Separators.getOpeningSeparators(), OPERATORS);

    public static boolean isOperationExpression(Tokens source) {
        return isOperationExpression(OPERATION_PATTERN.extract(source));
    }

    public static boolean isOperationExpression(ProgressivePatternResult source) {
        if (source.size() % 2 == 0) {
            return false;
        }

        int expression = 0;
        int operators = 0;

        for (ProgressivePatternElement element : source.getElements()) {
            if (element.isExpression()) {
                expression++;
            }

            if (element.isOperator()) {
                operators++;
            }

            if (operators > 0 && expression > 1) {
                return true;
            }
        }

        return false;
    }

}
