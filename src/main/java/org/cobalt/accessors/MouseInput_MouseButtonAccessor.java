package org.cobalt.accessors;

import java.lang.reflect.Field;
import net.minecraft.client.input.MouseInput;

public class MouseInput_MouseButtonAccessor {

  private static Field buttonField;

  static {
    try {
      buttonField = MouseInput.class.getDeclaredField("button");
      buttonField.setAccessible(true);
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    }
  }

  public static int getButton(MouseInput input) {
    try {
      return (int) buttonField.get(input);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
      return -1;
    }
  }

}
