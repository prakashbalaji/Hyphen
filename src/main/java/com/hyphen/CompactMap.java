package com.hyphen;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static com.hyphen.Hyphen.fold;
import static java.util.Arrays.asList;

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
        return fold(asList(kv), 
                (f, acc) -> {
                    acc.put(f.getKey(), f.getValue()); 
                    return acc;
                }, 
                new HashMap<String, Object>());
        
    }
}
