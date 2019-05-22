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

package org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.extractor;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.DescriptivePattern;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;

public class TokenExtractor {

    private final DescriptivePattern pattern;

    public TokenExtractor(DescriptivePattern pattern) {
        this.pattern = pattern;
    }

    public ExtractorResult extract(ParserData data, SourceStream source) {
        return new ExtractorWorker(pattern, data, source).extract();
    }

}
