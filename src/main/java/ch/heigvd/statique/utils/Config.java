package ch.heigvd.statique.utils;

import java.util.ArrayList;
import java.util.HashMap;
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
    ArrayList<Object> prefixes = new ArrayList<Object>(prefix.split("\\."));

    Map<String, Object> topMap = new HashMap<>(config);
    for (String subPrefix: prefixes) {
      Map<String, Object> tmp = new HashMap<>();
      tmp.put(subPrefix, topMap);
      topMap = tmp;
    }
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
}
