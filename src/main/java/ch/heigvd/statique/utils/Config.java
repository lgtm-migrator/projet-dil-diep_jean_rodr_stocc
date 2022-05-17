package ch.heigvd.statique.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {
  private Map<String, Object> config;

  /**
   * Config default constructor.
   */
  public Config() {
    this(new HashMap<>());
  }

  /**
   * Config constructor.
   *
   * @param config The configuration map.
   */
  public Config(Map<String, Object> config) {
    // Copy the config map to avoid external modification.
    this.config = new HashMap<String, Object>(config);
  }

  /**
   * Construct a config with given key and value in prefixed values.
   *
   * @param config
   * @param prefix
   */
  public Config(Map<String, Object> config, String prefix) {
    this();
    add(config, prefix);
  }

  public void addConfig(Object config, String key) {
    add(config, key);
  }

  /**
   * Get the config for rendering.
   *
   * @return The config map.
   */
  public Map<String, Object> toRender() {
    return config;
  }

  /**
   * Merge this config with another one. This will not affect the original
   * config. If a key is present in both configs, the value of the other config
   * will be used.
   *
   * @param other The config to merge with.
   * @return The merged config.
   */
  public Config merge(Config other) {
    HashMap<String, Object> temp = new HashMap<>();
    temp.put("config", this.config);
    temp.put("page", other.config);
    return new Config(temp);
  }

  private void add(Object config, String key) {
    // Get all prefixes
    List<String> prefixes = Arrays.asList(key.split("\\."));

    var keyMap = this.config;
    var prefixIt = prefixes.iterator();
    String prefix = prefixIt.next();
    while(prefixIt.hasNext()) {
      if (keyMap.containsKey(prefix)) {
        Object tmp = keyMap.get(prefix);
        if (tmp instanceof Map<?, ?>) {
          keyMap = (Map<String, Object>) tmp;
        } else {
          throw new RuntimeException("Cannot add config to a non-map");
        }
      } else {
        Map<String, Object> tmp = new HashMap<>();
        keyMap.put(prefix, tmp);
        keyMap = tmp;
      }

      prefix = prefixIt.next();
    }

    keyMap.put(prefix, config);
  }
}
