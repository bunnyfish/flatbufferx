package io.flatbufferx.processor.processor;

import com.sun.tools.javac.code.Symbol;
import io.flatbufferx.processor.type.Type;
import io.flatbufferx.processor.type.collection.CollectionType;
import io.flatbufferx.processor.type.field.FieldType;
import io.flatbufferx.processor.type.field.ParameterizedTypeField;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.ArrayList;
import java.util.List;

public class JsonFieldHolder {

    public String[] fieldName;
    public String setterMethod;
    public String getterMethod;
    public boolean shouldParse;
    public boolean shouldSerialize;
    public Type type;
    public Type receiverType;
    public Type returnsType;
    public boolean methodShouldbeList = false;

    public static String getGetter(Element element, Elements elements) {
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

        TypeKind elementTypeKind = element.asType().getKind();

        String elementName = element.getSimpleName().toString();
        String elementNameLowerCase = elementName.toLowerCase();

        List<String> possibleMethodNames = new ArrayList<>();
        possibleMethodNames.add("get" + elementNameLowerCase);
        if (elementTypeKind == TypeKind.BOOLEAN) {
            possibleMethodNames.add("is" + elementNameLowerCase);
            possibleMethodNames.add("has" + elementNameLowerCase);
            possibleMethodNames.add(elementNameLowerCase);
        }

        // Handle the case where variables are named in the form mVariableName instead of just variableName
        if (elementName.length() > 1 && elementName.charAt(0) == 'm' && (elementName.charAt(1) >= 'A' && elementName.charAt(1) <= 'Z')) {
            possibleMethodNames.add("get" + elementNameLowerCase.substring(1));
            if (elementTypeKind == TypeKind.BOOLEAN) {
                possibleMethodNames.add("is" + elementNameLowerCase.substring(1));
                possibleMethodNames.add("has" + elementNameLowerCase.substring(1));
                possibleMethodNames.add(elementNameLowerCase.substring(1));
            }
        }

        List<? extends Element> elementMembers = elements.getAllMembers(enclosingElement);
        List<ExecutableElement> elementMethods = ElementFilter.methodsIn(elementMembers);
        for (ExecutableElement methodElement : elementMethods) {
            if (methodElement.getParameters().size() == 0) {
                String methodNameString = methodElement.getSimpleName().toString();
                String methodNameLowerCase = methodNameString.toLowerCase();

                if (possibleMethodNames.contains(methodNameLowerCase)) {
                    if (methodElement.getParameters().size() == 0) {
                        if (methodElement.getReturnType().toString().equals(element.asType().toString())) {
                            return methodNameString;
                        }
                    }
                }
            }
        }

        return null;
    }

    public static String getSetter(Element element, Elements elements) {
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

        String elementName = element.getSimpleName().toString();
        String elementNameLowerCase = elementName.toLowerCase();

        List<String> possibleMethodNames = new ArrayList<>();
        possibleMethodNames.add("set" + elementNameLowerCase);

        // Handle the case where variables are named in the form mVariableName instead of just variableName
        if (elementName.length() > 1 && elementName.charAt(0) == 'm' && (elementName.charAt(1) >= 'A' && elementName.charAt(1) <= 'Z')) {
            possibleMethodNames.add("set" + elementNameLowerCase.substring(1));
        }

        List<? extends Element> elementMembers = elements.getAllMembers(enclosingElement);
        List<ExecutableElement> elementMethods = ElementFilter.methodsIn(elementMembers);
        for (ExecutableElement methodElement : elementMethods) {
            String methodNameString = methodElement.getSimpleName().toString();
            String methodNameLowerCase = methodNameString.toLowerCase();

            if (possibleMethodNames.contains(methodNameLowerCase)) {
                if (methodElement.getParameters().size() == 1) {
                    if (methodElement.getParameters().get(0).asType().toString().equals(element.asType().toString())) {
                        return methodNameString;
                    }
                }
            }
        }

        return null;
    }

    public String fill(Element element, Elements elements, Types types, String[] fieldNames, TypeMirror typeConverterType, JsonObjectHolder objectHolder, boolean shouldParse, boolean shouldSerialize) {
        if (fieldNames == null || fieldNames.length == 0) {
            String defaultFieldName = element.getSimpleName().toString();

            switch (objectHolder.fieldNamingPolicy) {
                case LOWER_CASE_WITH_UNDERSCORES:
                    defaultFieldName = TextUtils.toLowerCaseWithUnderscores(defaultFieldName);
                    break;
            }

            fieldNames = new String[]{defaultFieldName};
        }
        fieldName = fieldNames;

        this.shouldParse = shouldParse;
        this.shouldSerialize = shouldSerialize;

        setterMethod = getSetter(element, elements);
        getterMethod = getGetter(element, elements);

        type = Type.typeFor(element.asType(), typeConverterType, elements, types);
        return ensureValidType(type, element);
    }

    public String fillWithMethod(Elements elements, Types types, Symbol.MethodSymbol enclosedElement, TypeMirror typeConverterType, JsonObjectHolder objectHolder,
                                 boolean parse, boolean shouldParse, boolean shouldSerialize, boolean methodShouldbeList) {

        receiverType = FieldType.fieldTypeFor(enclosedElement.owner.getQualifiedName().toString());
        if (!enclosedElement.getReturnType().isPrimitive()) {
            returnsType = FieldType.fieldTypeFor(enclosedElement.getReturnType().toString());

        }
        fieldName = new String[]{enclosedElement.getSimpleName().toString()};
        // objectHolder.fieldMap
        this.shouldParse = shouldParse;
        this.shouldSerialize = shouldSerialize;

        // setterMethod = getSetter(element, elements);
        //  getterMethod = getGetter(element, elements);
        //   type=new Ty
        this.methodShouldbeList = methodShouldbeList;
        if (methodShouldbeList) {
            type = Type.typeForMethod(typeConverterType, elements, types, enclosedElement, true);

        } else {
            type = Type.typeForMethod(typeConverterType, elements, types, enclosedElement, false);
        }
        return null;//ensureValidType(type, TypeName.get(enclosedElement.getReturnType()));
    }

    private String ensureValidType(Type type, Element element) {
        if (type == null) {
            return "Type could not be determined for " + element.toString();
        } else {
            if (type instanceof CollectionType) {
                for (Type parameterType : type.parameterTypes) {
                    String errorMessage = ensureValidType(parameterType, element);
                    if (errorMessage != null) {
                        return errorMessage;
                    }
                }
            }

            return null;
        }
    }

    public boolean hasSetter() {
        return !TextUtils.isEmpty(setterMethod);
    }

    public boolean hasGetter() {
        return !TextUtils.isEmpty(getterMethod);
    }

    public boolean isGenericType() {
        return isGenericType(type);
    }

    private boolean isGenericType(Type type) {
        if (type instanceof ParameterizedTypeField) {
            return true;
        } else {
            for (Type parameterizedType : type.parameterTypes) {
                if (isGenericType(parameterizedType)) {
                    return true;
                }
            }
            return false;
        }
    }
}
