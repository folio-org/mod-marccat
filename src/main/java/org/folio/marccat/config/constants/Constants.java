package org.folio.marccat.config.constants;


import java.util.Map;

public class Constants<K> {

  private Constants() {
    throw new AssertionError();
  }

  private interface ConstantType<K> {
  }

  public interface Constant extends ConstantType<String> {
  }

  public interface IntConstant extends ConstantType<Integer> {
  }

  public interface MapConstant extends ConstantType<Map<?, ?>> {
  }

}
