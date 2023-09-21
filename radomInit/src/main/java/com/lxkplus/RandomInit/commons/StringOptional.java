package com.lxkplus.RandomInit.commons;

import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Consumer;
import java.util.function.Function;


/**
 * if null,then fail and do nothing
 * if not null, then next
 */
public final class StringOptional {

    private String object;

    StringOptional(String obj) {
        this.object = obj;
    }

    static public StringOptional of(String obj) {
        return new StringOptional(obj);
    }

    static public StringOptional stringOf(Object obj) {
        if (obj == null) {
            return new StringOptional(null);
        }
        return new StringOptional(obj.toString());
    }

    public StringOptional ifNotEmpty(Consumer<String> action) {
        if (object != null) {
            action.accept(object);
        }
        return this;
    }

    public StringOptional ifEmpty(Consumer<String> action) {
        if (StringUtils.isEmpty(object)) {
            action.accept(object);
        }
        object = null;
        return this;
    }

    public StringOptional ifNotBlank(Consumer<String> action) {
        if (StringUtils.isNotBlank(object)) {
            action.accept(object);
        }
        return this;
    }

    public StringOptional append(String... str) {
        if (object == null) {
            return this;
        }
        StringBuilder sb = new StringBuilder(object);
        for (String s : str) {
            if (s != null) {
                sb.append(s);
            }
        }
        object = sb.toString();
        return this;
    }

    public StringOptional trim(String str) {
        if (object == null) {
            return this;
        }
        object = StringUtils.strip(object, str);
        return this;
    }

    public StringOptional trim() {
        if (object == null) {
            return this;
        }
        object = StringUtils.strip(object);
        return this;
    }

    public StringOptional strip() {
        if (object == null) {
            return this;
        }
        object = object.strip();
        return this;
    }
    public StringOptional trimToNull() {
        if (object == null) {
            return this;
        }
        object = StringUtils.trimToNull(object);
        return this;
    }

    public StringOptional trimToEmpty() {
        if (object == null) {
            return this;
        }
        object = StringUtils.trimToEmpty(object);
        return this;
    }

    public StringOptional match(String regex) {
        if (object == null) {
            return this;
        }
        if (!object.matches(regex)) {
            object = null;
        }
        return this;
    }

    public StringOptional replaceAll(String regex, String statement) {
        if (object == null) {
            return this;
        }
        object = object.replaceAll(regex, statement);
        return this;
    }

    public StringOptional addFix(String pre, String suf) {
        if (object == null) {
            return this;
        }
        object = pre + object + suf;
        return this;
    }

    public StringOptional addFix(String fix) {
        if (object == null) {
            return this;
        }
        object = fix + object + fix;
        return this;
    }

    public StringOptional ifBlank(Consumer<String> action) {
        if (object == null) {
            return this;
        }
        if (StringUtils.isBlank(object)) {
            action.accept(object);
        }
        return this;
    }

    public String get() {
        return object;
    }

    public StringOptional next(Consumer<String> action) {
        if (object == null) {
            return null;
        }
        action.accept(object);
        return this;
    }

    public StringOptional contains(String str) {
        boolean contains = StringUtils.contains(object, str);
        if (!contains) {
            object = null;
        }
        return this;
    }

    public StringOptional splitAndFirst() {
        if (object == null) {
            return this;
        }
        String[] split = object.split("\\s+", 2);
        if (split.length >= 1) {
            object = split[0];
        }
        else {
            object = null;
        }
        return this;
    }

    public StringOptional split(String regex, int index) {
        if (object == null) {
            return this;
        }
        String[] split = object.split(regex);
        if (split.length > index) {
            object = split[index];
        }
        else {
            object = null;
        }
        return this;
    }

    public StringOptional removeEnd(String end) {
        if (object == null) {
            return this;
        }
        object = StringUtils.removeEnd(object, end);
        return this;
    }

    public StringOptional removeStart(String end) {
        if (object == null) {
            return this;
        }
        object = StringUtils.removeStart(object, end);
        return this;
    }

    public StringOptional deleteWhitespace() {
        if (object == null) {
            return this;
        }
        object = StringUtils.deleteWhitespace(object);
        return this;
    }

    public StringOptional reverse() {
        if (object == null) {
            return this;
        }
        object = StringUtils.reverse(object);
        return this;
    }

    public StringOptional upperCase() {
        if (object == null) {
            return this;
        }
        object = StringUtils.upperCase(object);
        return this;
    }

    public StringOptional lowerCase() {
        if (object == null) {
            return this;
        }
        object = StringUtils.lowerCase(object);
        return this;
    }

    public StringOptional appendIfMissing(String end) {
        if (object == null) {
            return this;
        }
        object = StringUtils.appendIfMissing(object, end);
        return this;
    }

    public StringOptional equalsAny(String... str) {
        if (object == null) {
            return this;
        }
        for (String s : str) {
            if(object.equals(s)) {
                return this;
            }
        }
        object = null;
        return this;
    }

    public StringOptional equalsIgnoreCase(String str) {
        if (object == null || str == null) {
            return this;
        }
        boolean b = StringUtils.equalsIgnoreCase(object, str);
        if (!b) {
            object = null;
        }
        return this;
    }

    public StringOptional subString(int start, int end) {
        if (object == null) {
            return this;
        }
        object = StringUtils.substring(object, start, end);
        return this;
    }

    public StringOptional subString(int start) {
        if (object == null) {
            return this;
        }
        object = StringUtils.substring(object, start);
        return this;
    }

    public StringOptional removeEndIgnoreCase(String end) {
        if (object == null) {
            return this;
        }
        object = StringUtils.removeEndIgnoreCase(object, end);
        return this;
    }

    public StringOptional map(Function<String, String> mapper) {
        if (object == null) {
            return this;
        }
        object = mapper.apply(object);
        return this;
    }

    public StringOptional nullMap(Function<String, String> mapper) {
        if (object == null) {
            object = mapper.apply(null);
        }
        return this;
    }

    public StringOptional emptyMap(Function<String, String> mapper) {
        if (StringUtils.isEmpty(object)) {
            object = mapper.apply(null);
        }
        return this;
    }


    public StringOptional blackMap(Function<String, String> mapper) {
        if (StringUtils.isBlank(object)) {
            object = mapper.apply(object);
        }
        return this;
    }

    public StringOptional lowerCamelToLowerUnderscore() {
        if (object == null) {
            return this;
        }
        object = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, object);
        return this;
    }

    public StringOptional lowerUnderscoreToLowerCamel() {
        if (object == null) {
            return this;
        }
        object = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, object);
        return this;
    }

}
