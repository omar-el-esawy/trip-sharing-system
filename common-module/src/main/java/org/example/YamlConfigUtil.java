package org.example;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class YamlConfigUtil {

    /**
     * Loads a YAML file from the classpath and returns its contents as a Map.
     * @param resourceName the YAML file name (e.g., "application.yml")
     * @return the parsed YAML as a Map, or null if not found
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> loadYaml(String resourceName) {
        try (InputStream in = YamlConfigUtil.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (in != null) {
                Yaml yaml = new Yaml();
                return yaml.load(in);
            }
        } catch (Exception e) {
            System.err.println("Warning: Could not load YAML config: " + resourceName + " (" + e.getMessage() + ")");
        }
        return null;
    }

    /**
     * Gets a nested map by key from a YAML config map.
     * @param yamlMap the root YAML map
     * @param key the key to extract
     * @return the nested map, or null if not found
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getSection(Map<String, Object> yamlMap, String key) {
        if (yamlMap == null) return null;
        Object section = yamlMap.get(key);
        if (section instanceof Map) {
            return (Map<String, Object>) section;
        }
        return null;
    }
}
