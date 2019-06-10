package org.folio.marccat.annotation;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.Random;

/**
 * @author: Christian Chiama
 * @date: 02/06/2019 02:05
 * <br/>
 * <br/>
 * @project: folio/mod-marccat 2019
 * <br/>
 * <br/>
 * @packageName: org.folio.marccat.annotation
 * @className: AnnotatedResourcePath
 * <br/>
 **/
public class AnnotatedResourcePath extends AnnotationElement {

  private static final String QUALIFIER_STRING = "java.lang.String";
  private static final String SEED = "ABCDEFGHJKLMNOPRSTUVYZabcdefghjklmnoprstuvyz";

  AnnotatedResourcePath(Element element) {
    super(element);
  }

  @Override
  public boolean isTypeValid(Elements elements, Types types) {
    TypeMirror elementType = element.asType();
    TypeMirror string = elements.getTypeElement(QUALIFIER_STRING).asType();
    return types.isSameType(elementType, string);
  }

  @Override
  public String getRandomValue() {
    StringBuilder builder = new StringBuilder();
    Random random = new Random();
    for (int i = 0; i < 10; i++) {
      int randIndex = random.nextInt(SEED.length());
      builder.append(SEED.charAt(randIndex));
    }
    return "\"" + builder.toString() + "\"";
  }

}
