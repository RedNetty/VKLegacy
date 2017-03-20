package me.kayaba.guilds.impl.util.converter;

public class ToStringConverterImpl extends AbstractConverter<Object, String> {
    @Override
    public String convert(Object o) {
        return o.toString();
    }
}
