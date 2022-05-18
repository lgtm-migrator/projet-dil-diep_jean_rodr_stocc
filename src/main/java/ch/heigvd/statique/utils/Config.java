package ch.heigvd.statique.utils;

import java.util.HashMap;
import java.util.Map;

public class Config extends HashMap<String, Object> {

  /**
   * Config default constructor.
   */
  public Config() {
    super();
  }

  /**
   * Config constructor.
   *
   * @param config The configuration map.
   */
  public Config(Map<String, ?> config) {
    this();

    putAll(config);
  }

  /**
   * Construct a config with given key and value in prefixed values.
   *
   * @param config
   * @param prefix
   */
  public Config(Map<String, Object> config, String prefix) {
    this();
    put(prefix, new Config(config));
  }

  public Config(String key, Object value) {
    this();
    put(key, value);
  }

  @Override
  public Object put(String key, Object value) {
    String[] prefixes = key.split("\\.", 2);

    if (prefixes.length == 1) {
      return super.put(key, value);
    }

    if (!this.containsKey(prefixes[0])) {
      this.put(prefixes[0], new Config(prefixes[1], value));
      return null;
    }

    if (!(this.get(prefixes[0]) instanceof Config)) {
      Object tmp = this.get(prefixes[0]);
      this.put(prefixes[0], new Config(prefixes[1], value));
      return tmp;
    }

    return ((Config) this.get(prefixes[0])).put(prefixes[1], value);
  }

  @Override
  public void putAll(Map<? extends String, ?> m) {
    var it = m.entrySet().iterator();

    while (it.hasNext()) {
      var entry = it.next();
      var key = entry.getKey();
      var value = entry.getValue();

      if (value instanceof Map<?, ?>) {
        var tmp = (Map<String, ?>) value;
        put(key, new Config(tmp));
      } else {
        put(key, value);
      }
    }
  }

  public Object get(String key) {
    String[] prefixes = key.split("\\.", 2);

    if (prefixes.length == 1) {
      return super.get(key);
    }

    if (!this.containsKey(prefixes[0])) {
      return null;
    }

    if (!(this.get(prefixes[0]) instanceof Config)) {
      return null;
    }

    return ((Config) this.get(prefixes[0])).get(prefixes[1]);
  }

  public boolean containsKey(String key) {
    String[] prefixes = key.split("\\.", 2);

    if (prefixes.length == 1) {
      return super.containsKey(key);
    }

    if (!this.containsKey(prefixes[0])) {
      return false;
    }

    if (!(this.get(prefixes[0]) instanceof Config)) {
      return false;
    }

    return ((Config) this.get(prefixes[0])).containsKey(prefixes[1]);
  }

  public Object remove(String key) {
    String[] prefixes = key.split("\\.", 2);

    if (prefixes.length == 1) {
      return super.remove(key);
    }

    if (!this.containsKey(prefixes[0])) {
      return null;
    }

    if (!(this.get(prefixes[0]) instanceof Config)) {
      return null;
    }

    return ((Config) this.get(prefixes[0])).remove(prefixes[1]);
  }
}
