package org.folio.marccat.annotation;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static org.folio.marccat.config.constants.Global.EMPTY_STRING;

public class AnnotationElement {

  public Element element;
  private Name qualifiedClassName;
  private Name simpleClassName;
  private Name elementName;
  private TypeMirror elementType;

  public AnnotationElement(Element element) {
    this.element = element;
    elementName = element.getSimpleName();
    simpleClassName = element.getEnclosingElement().getSimpleName();
    TypeElement enclosingElement = ((TypeElement) element.getEnclosingElement());
    qualifiedClassName = enclosingElement.getQualifiedName();
    elementType = element.asType();
  }

  public Name getQualifiedClassName() {
    return qualifiedClassName;
  }

  public Name getSimpleClassName() {
    return simpleClassName;
  }

  public Name getElementName() {
    return elementName;
  }

  public TypeMirror getElementType() {
    return elementType;
  }

  public Element getElement() {
    return element;
  }

  @Override
  public String toString() {
    return "Qualified class name : " + qualifiedClassName.toString() + "\n"
      + "Simple class name : " + simpleClassName.toString() + "\n"
      + "Element name : " + elementName.toString() + "\n"
      + "Element type : " + elementType.toString() + "\n";
  }

  public boolean isTypeValid(Elements elements, Types types){return false;}

  public String getRandomValue(){ return EMPTY_STRING; };
}
