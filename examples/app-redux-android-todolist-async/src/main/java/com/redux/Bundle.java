package com.redux;

import com.google.common.base.MoreObjects;

import java.util.HashMap;

import static com.google.common.base.Preconditions.checkNotNull;

public class Bundle {
    private final HashMap<String, Object> data;

    public Bundle() {
        data = new HashMap<>();
    }


    public Bundle put(String key, boolean value) {
        data.put(key, value);
        return this;
    }

    public boolean getBoolean(String key) {
        return ((Boolean) safeGet(key));
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        final Object result = data.get(key);
        if (result == null) {
            return defaultValue;
        }
        return ((Boolean) result);
    }

    public Bundle put(String key, String value) {
        data.put(key, value);
        return this;
    }

    public String getString(String key) {
        return ((String) safeGet(key));
    }

    public Bundle put(String key, int value) {
        data.put(key, value);
        return this;
    }

    public int getInt(String key) {
        return ((int) safeGet(key));
    }

    private Object safeGet(String key) {
        return checkNotNull(data.get(key), "Not element for " + key);
    }

    @Override public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("data", data)
                .toString();
    }
}
