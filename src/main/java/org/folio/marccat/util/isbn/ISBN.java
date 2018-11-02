package org.folio.marccat.util.isbn;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Christian Chiama
 */

// TODO da rivedere un attimo la logica dell annotation
@Documented
@Constraint(validatedBy = {})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(ISBN.List.class)
public @interface ISBN {

  String value() default "";

  String message() default "";

  Class <?>[] groups() default {};

  Class <? extends Payload>[] payload() default {};

  Type type() default Type.ISBN13;

  enum Type {
    ISBN10,
    ISBN13
  }

  @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
  @Retention(RUNTIME)
  @Documented
  @interface List {
    ISBN[] value();
  }
}
