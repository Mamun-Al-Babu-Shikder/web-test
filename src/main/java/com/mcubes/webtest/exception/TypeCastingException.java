package com.mcubes.webtest.exception;

import com.mcubes.webtest.enums.VariableType;

public class TypeCastingException extends RuntimeException {

    public TypeCastingException(String message) {
        super(message);
    }

    public TypeCastingException(String varName, VariableType varType) {
        super("Failed to cast the value of variable [name='%s'] to type '%s'".formatted(varName, varType.getType()));
    }

    public TypeCastingException(String varName, String varTypeName) {
        super("Failed to cast the value of variable [name='%s'] to type '%s'".formatted(varName, varTypeName));
    }
}
