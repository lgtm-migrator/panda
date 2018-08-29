/*
 * Copyright (c) 2016-2018 Dzikoysk
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

package org.panda_lang.panda.framework.language.interpreter.pattern.lexical;

import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.extractor.processed.WildcardProcessor;

public class LexicalPatternBuilder<T> {

    private LexicalPatternElement pattern;
    private WildcardProcessor<T> wildcardProcessor;

    public LexicalPatternBuilder<T> compile(String pattern) {
        LexicalPatternCompiler compiler = new LexicalPatternCompiler();
        LexicalPatternElement compiledPattern = compiler.compile(pattern);

        if (compiledPattern == null) {
            throw new RuntimeException("Cannot compile pattern: " + pattern);
        }

        this.pattern = compiledPattern;
        return this;
    }

    public LexicalPattern<T> build() {
        return new LexicalPattern<>(pattern, wildcardProcessor);
    }

    public LexicalPatternBuilder<T> setWildcardProcessor(WildcardProcessor<T> wildcardProcessor) {
        this.wildcardProcessor = wildcardProcessor;
        return this;
    }

}
