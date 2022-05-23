package ch.heigvd.statique.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a configuration of the website or of a page. It
 * encapsulates a Map of key/value pairs. Every Map that are passed in any
 * function of this class would be copied to a new Map to avoid any external
 * changes.
 */
public class Config {
  /**
   * The config Map.
   */
  private final Map<String, Object> config;

  /**
   * Default config constructor.
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
   * Config constructor from a map with a given key. The given map would be
   * placed at given key in the config map.
   *
   * @param key    the key to use for the config.
   * @param config The configuration map.
   */
  public Config(String key, Map<?, ?> config) {
    this();
    // Copy the config map to avoid external modification.
    this.config.put(key, config);
  }

  /**
   * Config constructor from another config with a given key. The given config's
   * map would be placed at given key in the new config map.
   *
   * @param key    the key to use for the config.
   * @param config The configuration map.
   */
  public Config(String key, Config config) {
    this(key, config.config);
  }

  /**
   * To get the config Map, a copy is made.
   *
   * @return A copy of the map.
   */
  public Map<String, Object> toRender() {
    // Copy the config map to avoid external modification.
    return new HashMap<>(this.config);
  }

  /**
   * Put a new value in the config.
   *
   * @param key   The key to use.
   * @param value The value to add.
   * @return The previous value associated with the key, or null if there was no
   *         mapping for the key.
   */
  public Object put(String key, Object value) {
    return this.config.put(key, value);
  }

  /**
   * Put a copy of the map in the config.
   *
   * @param key   The key to use.
   * @param value The map to add.
   * @return The previous value associated with the key, or null if there was no
   *         mapping for the key.
   */
  public Object put(String key, Map<?, ?> value) {
    value = new HashMap<>(value);
    if (!this.config.containsKey(key)) {
      return this.config.put(key, value);
    }

    Object previous = this.config.get(key);

    if (!(previous instanceof Map)) {
      return this.config.put(key, value);
    }

    ((Map<Object, Object>) previous).putAll(value);

    return previous;
  }

  /**
   * Put a copy of the config in the config.
   *
   * @param key    The key to use.
   * @param config The config to add.
   * @return The previous value associated with the key, or null if there was no
   *         mapping for the key.
   */
  public Object put(String key, Config config) {
    return this.put(key, config.config);
  }

  /**
   * Put all element of the map in the config.
   * 
   * @param config The map to copy.
   */
  public void putAll(Map<String, ?> config) {
    this.config.putAll(new HashMap<>(config));
  }

  /**
   * Put all element of the config in the config.
   * 
   * @param config The config to copy.
   */
  public void putAll(Config config) {
    this.putAll(config.config);
  }
}
