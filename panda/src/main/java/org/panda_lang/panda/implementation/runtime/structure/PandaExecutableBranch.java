package org.panda_lang.panda.implementation.runtime.structure;

import org.panda_lang.core.runtime.element.WrapperInstance;
import org.panda_lang.core.runtime.structure.ExecutableBranch;

public class PandaExecutableBranch implements ExecutableBranch {

    private final WrapperInstance wrapperInstance;
    private final ExecutableBranch parent;
    private ExecutableBranch child;

    public PandaExecutableBranch(WrapperInstance wrapperInstance) {
        this(wrapperInstance, null);
    }

    public PandaExecutableBranch(WrapperInstance wrapperInstance, ExecutableBranch parent) {
        this.wrapperInstance = wrapperInstance;
        this.parent = parent;
    }

    @Override
    public ExecutableBranch getChild() {
        return child;
    }

    @Override
    public ExecutableBranch getParent() {
        return parent;
    }

    @Override
    public WrapperInstance getWrapperInstance() {
        return wrapperInstance;
    }

}
