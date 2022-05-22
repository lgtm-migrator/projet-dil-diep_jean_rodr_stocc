package ch.heigvd.statique.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.github.jknack.handlebars.Context;

public class ConfigTest {
  private Map<String, Object> empty;
  private Map<String, Object> config1;
  private Map<String, Object> config2;

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
  }

  @Test
  public void testToRender() {
    Config c = new Config();
    assertEquals(empty, c.toRender());

    c = new Config(config1);
    assertEquals(config1, c.toRender());
  }

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
}
