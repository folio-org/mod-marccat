package org.folio.marccat.annotation;


import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;


@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Process.class)
public class AnnotationEngine extends AbstractProcessor {

  private static final String MARCCAT_ANOTATION_SUFFIX = "_MarccatAnnotation";
  private static final String TARGET_STATEMENT_FORMAT = "target.%1$s = %2$s";
  private static final String CONST_PARAM_TARGET_NAME = "target";

  private static final char CHAR_DOT = '.';

  private static final List<Class<? extends Annotation>> ANNOTATION_TYPES = Collections.singletonList(ResourcePath.class);

  private Messager messager;
  private Types typesUtil;
  private Elements elementsUtil;
  private Filer filer;

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    messager = processingEnv.getMessager();
    typesUtil = processingEnv.getTypeUtils();
    elementsUtil = processingEnv.getElementUtils();
    filer = processingEnv.getFiler();
  }


  @Override
  public Set<String> getSupportedAnnotationTypes() {
    Set<String> annotations = new LinkedHashSet<>();
    for (Class<? extends Annotation> annotation : ANNOTATION_TYPES) {
      annotations.add(annotation.getCanonicalName());
    }
    return annotations;
  }


  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }


  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    Map<String, List<AnnotationElement>> annotatedElementMap = new LinkedHashMap<>();

    for (Element element : roundEnv.getElementsAnnotatedWith(ResourcePath.class)) {
      AnnotationElement annotationElement = new AnnotationElement(element);
      messager.printMessage(Diagnostic.Kind.NOTE, annotationElement.toString());
      if (!annotationElement.isTypeValid(elementsUtil, typesUtil)) {
        messager.printMessage(Diagnostic.Kind.ERROR, annotationElement.getSimpleClassName().toString() + "#"
          + element.getSimpleName() + " is not in valid type String");
      }
      addAnnotatedElement(annotatedElementMap, annotationElement);
    }

    if (annotatedElementMap.size() == 0) {
      return true;
    }

    try {
      for (Map.Entry<String, List<AnnotationElement>> entry : annotatedElementMap.entrySet()) {
        MethodSpec constructor = createConstructor(entry.getValue());
        TypeSpec binder = createClass(getClassName(entry.getKey()), constructor);
        JavaFile javaFile = JavaFile.builder(getPackage(entry.getKey()), binder).build();
        javaFile.writeTo(filer);
      }

    } catch (IOException e) {
      messager.printMessage(Diagnostic.Kind.ERROR, "Error on creating java file");
    }

    return true;
  }

  private MethodSpec createConstructor(List<AnnotationElement> randomElements) {
    AnnotationElement firstElement = randomElements.get(0);
    MethodSpec.Builder builder = MethodSpec.constructorBuilder()
      .addModifiers(Modifier.PUBLIC)
      .addParameter(TypeName.get(firstElement.getElement().getEnclosingElement().asType()), CONST_PARAM_TARGET_NAME);
    for (AnnotationElement randomElement : randomElements) {
      addStatement(builder, randomElement);
    }
    return builder.build();
  }


  private void addStatement(MethodSpec.Builder builder, AnnotationElement randomElement) {
    builder.addStatement(String.format(
      TARGET_STATEMENT_FORMAT,
      randomElement.getElementName().toString(),
      randomElement.getRandomValue())
    );
  }

  private TypeSpec createClass(String className, MethodSpec constructor) {
    return TypeSpec.classBuilder(className + MARCCAT_ANOTATION_SUFFIX)
      .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
      .addMethod(constructor)
      .build();
  }

  private String getPackage(String qualifier) {
    return qualifier.substring(0, qualifier.lastIndexOf(CHAR_DOT));
  }


  private String getClassName(String qualifier) {
    return qualifier.substring(qualifier.lastIndexOf(CHAR_DOT) + 1);
  }

  private void addAnnotatedElement(Map<String, List<AnnotationElement>> map, AnnotationElement randomElement) {
    String qualifier = randomElement.getQualifiedClassName().toString();
    map.computeIfAbsent(qualifier, k -> new ArrayList<>());
    map.get(qualifier).add(randomElement);
  }

}
