package com.kronos.autoregister;

public class AutoRegisterConfig {
    public String REGISTER_PACKAGE_NAME = "";
    public String REGISTER_CLASS_NAME = "";
    public String REGISTER_FUNCTION_NAME = "";
    public String REGISTER_CLASS_FUNCTION_NAME = "";
    public String REGISTER_CUSTOM_CLASS_NAME = "";

    public void transform() {
        if (REGISTER_CLASS_NAME != null && !REGISTER_CLASS_NAME.isEmpty()) {
            Constant.REGISTER_CLASS_CONST = REGISTER_CLASS_NAME;
        }
        if (REGISTER_PACKAGE_NAME != null && !REGISTER_PACKAGE_NAME.isEmpty()) {
            Constant.REGISTER_PACKAGE_CONST = REGISTER_PACKAGE_NAME;
        }
        if (REGISTER_FUNCTION_NAME != null && !REGISTER_FUNCTION_NAME.isEmpty()) {
            Constant.REGISTER_FUNCTION_NAME_CONST = REGISTER_FUNCTION_NAME;
        }
        if (REGISTER_CLASS_FUNCTION_NAME != null && !REGISTER_CLASS_FUNCTION_NAME.isEmpty()) {
            Constant.REGISTER_CLASS_FUNCTION_CONST = REGISTER_CLASS_FUNCTION_NAME;
        }
    }

}
