package com.hyphen;

import java.util.HashMap;
import java.util.Map;

public class CompactMap {

    public static class KeyValue {
        private final String key;
        private final Object value;

        public KeyValue(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        public static KeyValue kv(String key, Object value){
            return new KeyValue(key,value);
        };

        public String getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }
    }
    public static Map<String,Object> m(KeyValue...kv){
        Map<String, Object> result = new HashMap<>();
        for (KeyValue keyValue : kv) {
            result.put(keyValue.getKey(),keyValue.getValue());
        }
        return result;
    }
}
