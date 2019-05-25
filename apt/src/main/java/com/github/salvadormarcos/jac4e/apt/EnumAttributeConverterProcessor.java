package com.github.salvadormarcos.jac4e.apt;

import com.github.salvadormarcos.jac4e.core.BaseEnumAttributeConverter;
import com.github.salvadormarcos.jac4e.core.EnumAttributeConverter;
import com.github.salvadormarcos.jac4e.core.exception.EnumMetadataException;
import com.squareup.javapoet.*;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class EnumAttributeConverterProcessor extends AbstractProcessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton( EnumAttributeConverter.class.getCanonicalName() );
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<TypeElement> annotatedEnuns = roundEnv.getElementsAnnotatedWith(EnumAttributeConverter.class).stream()
                .filter(e -> e.getKind() == ElementKind.ENUM)
                .map(e -> (TypeElement) e)
                .collect(Collectors.toSet());

        try {
            for (TypeElement enumType : annotatedEnuns) {
                EnumAttributeConverter opts = enumType.getAnnotation(EnumAttributeConverter.class);

                TypeElement valueType = findAttributeTypeElementByName(enumType, opts.attributeName());

                createConverterJavaFile(enumType, valueType, opts).writeTo(processingEnv.getFiler());
            }
        } catch (Exception e){
            throw new RuntimeException("Failed to generate the AttributeConverter of enums", e);
        }

        return true;
    }

    private TypeElement findAttributeTypeElementByName(TypeElement enumType, String attributeName) {
        if (StringUtils.isBlank(attributeName)) {
            attributeName = BaseEnumAttributeConverter.DEFAULT_ATTRIBUTE_NAME;
        }

        for (Element element : enumType.getEnclosedElements()) {
            final String fieldName = element.getSimpleName().toString();

            if (element.getKind() != ElementKind.FIELD || !fieldName.equals(attributeName)) {
                continue;
            }

            TypeMirror type = element.asType();

            if (type.getKind().isPrimitive()) {
                return processingEnv.getTypeUtils().boxedClass( processingEnv.getTypeUtils().getPrimitiveType(type.getKind()) );
            }

            return (TypeElement) processingEnv.getTypeUtils().asElement(type);
        }

        throw new EnumMetadataException("The enum \"" + enumType.getQualifiedName() + "\" does not have the \"" + attributeName + "\" attribute!");
    }

    private JavaFile createConverterJavaFile(TypeElement enumType, TypeElement attributeType, EnumAttributeConverter opts) {
        final ClassName enumTypeClassName = ClassName.get(enumType);
        final ClassName attributeTypeClassName = ClassName.get(attributeType);
        final String packageName = enumTypeClassName.packageName();

        final String converterName = String.join("", enumTypeClassName.simpleNames()) + "AttributeConverter";

        TypeSpec.Builder builder = TypeSpec.classBuilder(converterName)
                .addModifiers(Modifier.PUBLIC)
                .superclass(toParameterizedTypeName(BaseEnumAttributeConverter.class, enumTypeClassName, attributeTypeClassName))
                .addSuperinterface(toParameterizedTypeName(AttributeConverter.class, enumTypeClassName, attributeTypeClassName));

        addJpaConverterAnnotation(builder, opts);

        addConverterConstructor(builder, enumTypeClassName, attributeTypeClassName, opts);

        return JavaFile.builder(packageName, builder.build()).build();
    }

    private ParameterizedTypeName toParameterizedTypeName(Class<?> parameterizedType, TypeName... types) {
        return ParameterizedTypeName.get(ClassName.get(parameterizedType), types);
    }

    private void addJpaConverterAnnotation(TypeSpec.Builder converterBuilder, EnumAttributeConverter opts) {
        AnnotationSpec.Builder builder = AnnotationSpec.builder(Converter.class);

        if (opts.autoApply()) {
            builder.addMember("autoApply", "$L", true);
        }

        converterBuilder.addAnnotation(builder.build());
    }

    private void addConverterConstructor(TypeSpec.Builder converterBuilder, ClassName enumTypeClassName, ClassName attributeTypeClassName, EnumAttributeConverter opts) {
        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("super($T.class, $T.class)", enumTypeClassName, attributeTypeClassName);

        if (StringUtils.isNotBlank(opts.attributeName())) {
            builder.addStatement("setAttributeName($S)", opts.attributeName());
        }

        if (opts.errorIfValueNotPresent()) {
            builder.addStatement("setErrorIfValueNotPresent($L)", opts.errorIfValueNotPresent());
        }

        converterBuilder.addMethod(builder.build());
    }

}
