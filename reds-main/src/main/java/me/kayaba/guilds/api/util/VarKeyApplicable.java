package me.kayaba.guilds.api.util;

import me.kayaba.guilds.enums.*;

import java.util.*;

public interface VarKeyApplicable<T> extends Cloneable {

    Map<VarKey, String> getVars();


    T vars(Map<VarKey, String> vars);


    T setVar(VarKey varKey, String value);


    T setVar(VarKey varKey, Integer value);


    T setVar(VarKey varKey, Double value);


    boolean isChanged();
}
