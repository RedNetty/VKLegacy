package me.kayaba.guilds.impl.util.converter;

public class EnumToNameConverterImpl<E extends Enum> extends AbstractConverter<E, String> {
    @Override
    public String convert(E e) {
        return e.name();
    }
}
