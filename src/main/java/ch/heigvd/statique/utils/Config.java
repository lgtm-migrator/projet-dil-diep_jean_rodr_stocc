package ch.heigvd.statique.utils;

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
   * @param config The config to merge with.
   * @return The merged config.
   */
  public Config merge(Config other) {
    Config merged = new Config(this.config);
    merged.config.putAll(other.config);
    return merged;
  }
}
