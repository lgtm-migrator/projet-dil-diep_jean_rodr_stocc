package ch.heigvd.statique.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ConfigTest {
  private Map<String, Object> empty;
  private Map<String, Object> config1;
  private Map<String, Object> config2;
  private Map<String, Object> merged;

  /**
   * Generate the maps to test.
   */
  @BeforeEach
  private void setUpMap() {
    empty = new HashMap<>();
    config1 = new HashMap<>() {
      {
        put("key1", "value1");
        put("key2", "value2");
      }
    };
    config2 = new HashMap<>() {
      {
        put("key1", "value1_over");
        put("key3", "value3");
      }
    };
    merged = new HashMap<>();
    merged.putAll(config1);
    merged.putAll(config2);
  }

  /**
   * Test the toRender method.
   */
  @Test
  public void testToRender() {
    Config c = new Config();
    assertEquals(empty, c.toRender());

    c = new Config(config1);
    assertEquals(config1, c.toRender());
  }

  /**
   * Test to create a new config with a given key and a given map.
   */
  @Test
  public void testConstructHashMapPrefixed() {
    Map<String, Object> prefixedConfig = new HashMap<>() {
      {
        put("prefix1", config1);
      }
    };
    Config c = new Config("prefix1", config1);
    assertEquals(prefixedConfig, c.toRender());
  }

  /**
   * Test to create a new config with a given key and a given config.
   */
  @Test
  public void testConstructConfigPrefixed() {
    Map<String, Object> prefixedConfig = new HashMap<>() {
      {
        put("prefix1", config1);
      }
    };

    Config c = new Config("prefix1", new Config(config1));
    assertEquals(prefixedConfig, c.toRender());
  }

  /**
   * Test to add a new map to a config.
   */
  @Test
  public void testPutNewHashMap() {
    Map<String, Object> prefixedConfig = new HashMap<>() {
      {
        put("prefix1", config1);
        put("prefix2", config2);
      }
    };

    Config c = new Config("prefix1", config1);
    c.put("prefix2", config2);
    assertEquals(prefixedConfig, c.toRender());
  }

  /**
   * Test to add a config to another config.
   */
  @Test
  public void testPutNewConfig() {
    Map<String, Object> prefixedConfig = new HashMap<>() {
      {
        put("prefix1", config1);
        put("prefix2", config2);
      }
    };

    Config c = new Config("prefix1", config1);
    c.put("prefix2", new Config(config2));
    assertEquals(prefixedConfig, c.toRender());
  }

  /**
   * Test to add a new value to a config.
   */
  @Test
  public void testPut() {
    Map<String, Object> prefixedConfig = new HashMap<>() {
      {
        put("key1", "value1");
        put("key2", "value2");
        put("key3", "value3");
      }
    };

    Config c = new Config();
    c.put("key1", "value1");
    c.put("key2", "value2");
    c.put("key3", "value3");

    assertEquals(prefixedConfig, c.toRender());
  }

  /**
   * Test to add a new map that overrides some values but not all of them.
   */
  @Test
  public void testPutOverride() {
    Map<String, Object> keyMerge = new HashMap<>();
    keyMerge.put("key", merged);

    Config c = new Config("key", config1);
    c.put("key", config2);

    assertEquals(keyMerge, c.toRender());
  }

  /**
   * Test to put all elements of a config into another config.
   */
  @Test
  public void testPutAllConfig() {

    Config c = new Config(config1);
    c.putAll(new Config(config2));

    assertEquals(merged, c.toRender());
  }
}
