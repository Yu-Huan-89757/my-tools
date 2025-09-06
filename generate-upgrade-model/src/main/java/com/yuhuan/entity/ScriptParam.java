package com.yuhuan.entity;

public class ScriptParam {
    private String name;
    private ScriptParamType type;
    private String displayNameZH;
    private String displayNameEN;
    private String descriptionZH;
    private String descriptionEN;
    private boolean isOptional;

    public ScriptParam(String name, ScriptParamType type, String displayNameZH,
                       String displayNameEN, String descriptionZH, String descriptionEN,
                       boolean isOptional) {
        this.name = name;
        this.type = type;
        this.displayNameZH = displayNameZH;
        this.displayNameEN = displayNameEN;
        this.descriptionZH = descriptionZH;
        this.descriptionEN = descriptionEN;
        this.isOptional = isOptional;
    }

    public static enum ScriptParamType{
        String,Number,Ip,Password,Switch,Enum
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ScriptParamType getType() {
        return type;
    }

    public void setType(ScriptParamType type) {
        this.type = type;
    }

    public String getDisplayNameZH() {
        return displayNameZH;
    }

    public void setDisplayNameZH(String displayNameZH) {
        this.displayNameZH = displayNameZH;
    }

    public String getDisplayNameEN() {
        return displayNameEN;
    }

    public void setDisplayNameEN(String displayNameEN) {
        this.displayNameEN = displayNameEN;
    }

    public String getDescriptionZH() {
        return descriptionZH;
    }

    public void setDescriptionZH(String descriptionZH) {
        this.descriptionZH = descriptionZH;
    }

    public String getDescriptionEN() {
        return descriptionEN;
    }

    public void setDescriptionEN(String descriptionEN) {
        this.descriptionEN = descriptionEN;
    }

    public boolean isOptional() {
        return isOptional;
    }

    public void setOptional(boolean optional) {
        isOptional = optional;
    }
}
