package com.kronos.router.utils;


import javax.lang.model.element.Element;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class TypeUtils {

    private Types types;
    private TypeMirror parcelableType;
    private TypeMirror serializableType;

    public TypeUtils(Types types, Elements elements) {
        this.types = types;
        parcelableType = elements.getTypeElement(Const.PARCELABLE).asType();
        serializableType = elements.getTypeElement(Const.SERIALIZABLE).asType();
    }

    /**
     * Diagnostics out the true java type
     *
     * @param element Raw type
     * @return Type class of java
     */
    public int typeExchange(Element element) {
        TypeMirror typeMirror = element.asType();

        // Primitive
        if (typeMirror.getKind().isPrimitive()) {
            return element.asType().getKind().ordinal();
        }

        switch (typeMirror.toString()) {
            case Const.BYTE:
                return TypeKind.BYTE.ordinal();
            case Const.SHORT:
                return TypeKind.SHORT.ordinal();
            case Const.INTEGER:
                return TypeKind.INT.ordinal();
            case Const.LONG:
                return TypeKind.LONG.ordinal();
            case Const.FLOAT:
                return TypeKind.FLOAT.ordinal();
            case Const.DOUBLE:
                return TypeKind.DOUBLE.ordinal();
            case Const.BOOLEAN:
                return TypeKind.BOOLEAN.ordinal();
            case Const.CHAR:
                return TypeKind.CHAR.ordinal();
            default:
                return 0;
    /*        case Const.STRING:
                return TypeKind.STRING.ordinal();
            default:
                // Other side, maybe the PARCELABLE or SERIALIZABLE or OBJECT.
                if (types.isSubtype(typeMirror, parcelableType)) {
                    // PARCELABLE
                    return TypeKind.PARCELABLE.ordinal();
                } else if (types.isSubtype(typeMirror, serializableType)) {
                    // SERIALIZABLE
                    return TypeKind.SERIALIZABLE.ordinal();
                } else {
                    return TypeKind.OBJECT.ordinal();
                }*/
        }
    }
}