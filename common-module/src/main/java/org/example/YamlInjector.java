package org.example;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

public class YamlInjector {

    private static Map<String, Object> yamlMap;

    static {
        try (InputStream input = YamlInjector.class.getClassLoader().getResourceAsStream("application.yml")) {
            if (input == null) throw new RuntimeException("Missing application.yml");
            yamlMap = new Yaml().load(input);
        } catch (Exception e) {
            throw new RuntimeException("Error loading YAML config", e);
        }
    }

    public static void inject(Object target) {
        for (Field field : target.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(YamlValue.class)) {
                YamlValue annotation = field.getAnnotation(YamlValue.class);
                Object value = getNestedValue(annotation.key());
                if (value != null) {
                    field.setAccessible(true);
                    try {
                        field.set(target, convert(value, field.getType()));
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to inject field: " + field.getName(), e);
                    }
                }
            }
        }
    }

    public static void inject(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers()) && field.isAnnotationPresent(YamlValue.class)) {
                YamlValue annotation = field.getAnnotation(YamlValue.class);
                Object value = getNestedValue(annotation.key());
                if (value != null) {
                    field.setAccessible(true);
                    try {
                        field.set(null, convert(value, field.getType())); // static field → object = null
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to inject static field: " + field.getName(), e);
                    }
                }
            }
        }
    }

    public static Object getNestedValue(String key) {
        String[] parts = key.split("\\.");
        Object current = yamlMap;
        for (String part : parts) {
            if (current instanceof Map<?, ?> map) {
                current = map.get(part);
            } else {
                return null;
            }
        }
        return current;
    }

    private static Object convert(Object value, Class<?> type) {
        if (type == String.class) return value.toString();
        if (type == int.class || type == Integer.class) return Integer.parseInt(value.toString());
        if (type == boolean.class || type == Boolean.class) return Boolean.parseBoolean(value.toString());
        if (type == double.class || type == Double.class) return Double.parseDouble(value.toString());
        return value;
    }
}
