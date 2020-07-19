/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.panda;

import org.panda_lang.framework.language.resource.syntax.PandaSyntax;
import org.panda_lang.panda.bootstrap.PandaBootstrap;
import org.panda_lang.panda.language.interpreter.messenger.formatters.Formatters;
import org.panda_lang.panda.language.interpreter.messenger.layouts.TranslatorLayouts;
import org.panda_lang.panda.language.interpreter.messenger.mappers.StacktraceMapper;
import org.panda_lang.panda.language.resource.syntax.PandaParsers;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.assignation.AssignationParsers;
import org.slf4j.Logger;

/**
 * Simplify creation of Panda instance
 */
public final class PandaFactory {

    /**
     * Create default instance of Panda
     *
     * @param logger logger to use by framework
     * @return a new instance of panda
     */
    public Panda createPanda(Logger logger) {
        return PandaBootstrap.initializeBootstrap(logger)
                // load syntax
                .withSyntax(new PandaSyntax())

                // initialize messenger
                .initializeMessenger()
                    .addLayouts(TranslatorLayouts.LAYOUTS)
                    .addDataFormatters(Formatters.FORMATTERS)
                    .addDataMapper(new StacktraceMapper())
                    .collect()

                // load pipelines
                .initializePipelines()
                    .collect()

                // load parsers and expressions subparsers
                .initializeParsers()
                    .loadParsers(PandaParsers.PARSERS)
                    .loadParsers(AssignationParsers.SUBPARSERS)
                    .loadDefaultExpressionSubparsers()
                    .collect()

                // load models
                .create();
    }

}
