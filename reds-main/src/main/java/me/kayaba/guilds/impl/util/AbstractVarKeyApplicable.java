package me.kayaba.guilds.impl.util;

import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.enums.*;

import java.util.*;

@SuppressWarnings("unchecked")
public class AbstractVarKeyApplicable<T extends VarKeyApplicable> implements VarKeyApplicable<T> {
    protected final Map<VarKey, String> vars = new HashMap<>();
    protected int varsHashCode = vars.hashCode();

    @Override
    public Map<VarKey, String> getVars() {
        return vars;
    }

    @Override
    public T vars(Map<VarKey, String> vars) {
        this.vars.clear();
        this.vars.putAll(vars);
        return (T) this;
    }

    @Override
    public T setVar(VarKey varKey, String value) {
        vars.put(varKey, value);
        return (T) this;
    }

    @Override
    public T setVar(VarKey varKey, Integer value) {
        return setVar(varKey, String.valueOf(value));
    }

    @Override
    public T setVar(VarKey varKey, Double value) {
        return setVar(varKey, String.valueOf(value));
    }

    @Override
    public boolean isChanged() {
        if (varsHashCode == vars.hashCode()) {
            return false;
        }

        varsHashCode = vars.hashCode();
        return true;
    }
}
