package com.mcubes.webtest.enums;

public enum ActionType {
    OPEN_URL, NAVIGATION, CLICK, ENTER_TEXT,
    DEF_VAR, FOREACH_LOOP,
    DELAY
    ;

    public static ActionType from(String type) {
        return ActionType.valueOf(type.toUpperCase());
    }

    //public abstract Action getAction(JSONObject object);

    /*
    public final Category category;

    ActionType(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public static Category categoryOf(String type) {
        return ActionType.from(type).getCategory();
    }

    public enum Category {
        WEB_DRIVER, WEB_ELEMENT, LANG_STATEMENT;
    }
     */
}
