package ch.heigvd.statique.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class ConfigTest {
  private Map<String, Object> empty = new HashMap<>();
  private Map<String, Object> config1 = new HashMap<>() {
    {
      put("key1", "value1");
      put("key2", "value2");
    }
  };
  private Map<String, Object> config2 = new HashMap<>() {
    {
      put("key1", "value1_override");
      put("key3", "value3");
    }
  };
  private Map<String, Object> merged = new HashMap<>() {
    {
      put("key1", "value1_override");
      put("key2", "value2");
      put("key3", "value3");
    }
  };

  /**
   * toRender() should return the config map.
   */
  @Test
  public void testToRender() {
    Config c = new Config();
    assertEquals(empty, c.toRender());

    c = new Config(config1);
    assertEquals(config1, c.toRender());
  }

  /**
   * merge() should merge the configs.
   */
  @Test
  public void testMerge() {
    Config c = new Config();
    Config mergedC = c.merge(new Config(config1));
    assertEquals(empty, c.toRender());
    assertEquals(config1, mergedC.toRender());

    c = new Config(config1);
    mergedC = c.merge(new Config(config2));
    assertEquals(config1, c.toRender());
    assertEquals(merged, mergedC.toRender());
  }
}
