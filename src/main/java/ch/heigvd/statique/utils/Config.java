package ch.heigvd.statique.utils;

import java.util.HashMap;
import java.util.Map;

public class Config {
  private final Map<String, Object> config;

  /**
   * Config default constructor.
   */
  public Config() {
    this.config = new HashMap<>();
  }

  /**
   * Config constructor with a map.
   *
   * @param config the map to use as config.
   */
  public Config(Map<String, ?> config) {
    // Copy the config map to avoid external modification.
    this.config = new HashMap<>(config);
  }

  /**
   * Config constructor.
   *
   * @param config The configuration map.
   */
  public Config(String index, Map<String, ?> config) {
    this();
    // Copy the config map to avoid external modification.
    this.config.put(index, new HashMap<>(config));
  }

  public Config(String index, Config config) {
    this(index, config.config);
  }

  public Map<String, Object> toRender() {
    // Copy the config map to avoid external modification.
    return this.config;
  }

  public Object put(String index, Config config) {
    return this.config.put(index, config.config);
  }

  public Object put(String index, Object value) {
    return this.config.put(index, value);
  }
}