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

package panda.interpreter.syntax.scope.block.conditional;

import panda.interpreter.parser.Context;
import panda.interpreter.resource.syntax.keyword.Keywords;
import panda.interpreter.syntax.scope.block.BlockParser;
import panda.std.reactive.Completable;
import panda.std.Option;

public final class IfParser extends BlockParser<ConditionalBlock> {

    private static final ConditionalParser CONDITIONAL_PARSER = new ConditionalParser();

    @Override
    public String name() {
        return "if";
    }

    @Override
    public Option<Completable<ConditionalBlock>> parse(Context<?> context) {
        return CONDITIONAL_PARSER
                .parse(SCOPE_PARSER, context, true, true, Keywords.IF)
                .map(Completable::completed);
    }

}
