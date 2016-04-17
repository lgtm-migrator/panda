package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Essence;
import org.panda_lang.panda.core.statement.Constructor;
import org.panda_lang.panda.core.statement.Executable;
import org.panda_lang.panda.core.statement.Method;
import org.panda_lang.panda.core.statement.Vial;

import java.util.ArrayList;
import java.util.List;

public class ListEssence extends ObjectEssence {

    static {
        Vial vial = new Vial("List");
        vial.group("panda.lang");
        vial.constructor(new Constructor() {
            @Override
            public Essence execute(Alice alice) {
                return new ListEssence();
            }
        });
        vial.method(new Method("add", new Executable() {
            @Override
            public Essence execute(Alice alice) {
                ListEssence list = alice.getValueOfInstance();
                list.getList().add(alice.getValueOfFactor(0));
                return null;
            }
        }));
    }

    private final List<Essence> list;

    public ListEssence() {
        this.list = new ArrayList<>();
    }

    public List<Essence> getList() {
        return list;
    }

    @Override
    public Object getJavaValue() {
        return list;
    }

    @Override
    public String toString() {
        StringBuilder node = new StringBuilder();
        for (Essence o : list) {
            if (node.length() != 0) {
                node.append(", ");
            }
            node.append(o);
        }
        return node.toString();
    }

}
