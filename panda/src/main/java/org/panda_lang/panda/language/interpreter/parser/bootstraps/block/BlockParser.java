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

package org.panda_lang.panda.language.interpreter.parser.bootstraps.block;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.statement.Block;
import org.panda_lang.framework.design.architecture.statement.Cell;
import org.panda_lang.framework.design.architecture.statement.Scope;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Channel;
import org.panda_lang.framework.design.interpreter.parser.pipeline.HandleResult;
import org.panda_lang.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.framework.design.interpreter.parser.pipeline.PipelineComponents;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.SourceStream;
import org.panda_lang.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.framework.language.interpreter.parser.ScopeParser;
import org.panda_lang.framework.language.interpreter.parser.pipeline.PandaChannel;
import org.panda_lang.framework.language.interpreter.token.PandaSourceStream;
import org.panda_lang.panda.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.language.interpreter.parser.PandaPriorities;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.ParserBootstrap;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Component;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Local;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Src;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.data.LocalData;
import org.panda_lang.panda.language.interpreter.parser.loader.Registrable;

import java.util.function.Supplier;

@Registrable(pipeline = Pipelines.SCOPE_LABEL, priority = PandaPriorities.CONTAINER_BLOCK)
public class BlockParser extends ParserBootstrap {

    private static final ScopeParser CONTAINER_PARSER = new ScopeParser();

    @Override
    protected BootstrapInitializer initialize(Context context, BootstrapInitializer initializer) {
        return initializer.pattern("<*declaration> body:~{");
    }

    @Override
    protected Boolean customHandle(ParserHandler handler, Context context, Channel channel, Snippet source) {
        HandleResult<BlockSubparser> result = context.getComponent(Components.PIPELINE)
                .getPipeline(PandaPipelines.BLOCK)
                .handle(context, channel, source);

        return result.isFound();
    }

    @Autowired(order = 1)
    void parse(Context context, LocalData local, @Component Scope parent, @Src("*declaration") Snippet declaration) throws Exception {
        SourceStream declarationStream = new PandaSourceStream(declaration);
        Channel channel = new PandaChannel();

        HandleResult<BlockSubparser> handleResult = context
                .getComponent(Components.PIPELINE)
                .getPipeline(PandaPipelines.BLOCK)
                .handle(context, channel, declarationStream.toSnippet());

        BlockSubparser blockParser = handleResult.getParser().orElseThrow((Supplier<? extends Exception>) () -> {
            if (handleResult.getFailure().isPresent()) {
                throw handleResult.getFailure().get();
            }

            throw PandaParserFailure.builder("Unknown block", context)
                    .withStreamOrigin(declaration)
                    .build();
        });


        Context delegatedContext = local.allocated(context.fork())
                .withComponent(PipelineComponents.CHANNEL, channel);

        if (!parent.getCells().isEmpty()) {
            Cell cell = parent.getCells().get(parent.getCells().size() - 1);

            if (cell.isBlock()) {
                delegatedContext.withComponent(BlockComponents.PREVIOUS_BLOCK, (Block) cell.getStatement());
            }
        }

        BlockData blockData = blockParser.parse(delegatedContext, declaration);

        if (blockData == null || blockData.getBlock() == null) {
            throw PandaParserFailure.builder(blockParser.getClass().getSimpleName() + " cannot parse current block", context)
                    .withStreamOrigin(declaration)
                    .build();
        }

        local.allocated(blockData.getBlock());

        if (blockData.isUnlisted()) {
            return;
        }

        context.getComponent(Components.SCOPE).addStatement(blockData.getBlock());
    }

    @Autowired(order = 2)
    void parseContent(@Local Context blockContext, @Local Scope block, @Nullable @Src("body") Snippet body) throws Exception {
        if (body != null) {
            CONTAINER_PARSER.parse(blockContext, block, body);
        }
    }

}
