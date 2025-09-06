package com.yuhuan.entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

public class StepData {
    private final StringProperty cnStepName;
    private final StringProperty enStepName;
    private final StringProperty serialNumber;
    private final StringProperty layer;
    private final StringProperty timePlan;
    private final StringProperty referenceCostTime;
    private final StringProperty parentScriptName;
    private final StringProperty isManual;
    private final StringProperty isSerial;
    private final StringProperty timeOut;
    private final StringProperty canSkip;
    private final StringProperty failureSkip;
    private final StringProperty isOCardUpAlone;
    private final StringProperty scriptName;
    private final StringProperty isComparison;
    private final StringProperty tip;
    private final StringProperty segFlag;
    private final StringProperty enableRollBack;
    private final StringProperty buttonFlag;
    private final StringProperty expectResult;

    public StepData(String cnStepName, String enStepName, String serialNumber, String layer, String timePlan, String referenceCostTime, String parentScriptName, String isManual, String isSerial, String timeOut, String canSkip, String failureSkip, String isOCardUpAlone, String scriptName, String isComparison, String tip, String segFlag, String enableRollBack, String buttonFlag, String expectResult) {
        this.cnStepName = new SimpleStringProperty(cnStepName);
        this.enStepName = new SimpleStringProperty(enStepName);
        this.serialNumber = new SimpleStringProperty(serialNumber);
        this.layer = new SimpleStringProperty(layer);
        this.timePlan = new SimpleStringProperty(timePlan);
        this.referenceCostTime = new SimpleStringProperty(referenceCostTime);
        this.parentScriptName = new SimpleStringProperty(parentScriptName);
        this.isManual = new SimpleStringProperty(isManual);
        this.isSerial = new SimpleStringProperty(isSerial);
        this.timeOut = new SimpleStringProperty(timeOut);
        this.canSkip = new SimpleStringProperty(canSkip);
        this.failureSkip = new SimpleStringProperty(failureSkip);
        this.isOCardUpAlone = new SimpleStringProperty(isOCardUpAlone);
        this.scriptName = new SimpleStringProperty(scriptName);
        this.isComparison = new SimpleStringProperty(isComparison);
        this.tip = new SimpleStringProperty(tip);
        this.segFlag = new SimpleStringProperty(segFlag);
        this.enableRollBack = new SimpleStringProperty(enableRollBack);
        this.buttonFlag = new SimpleStringProperty(buttonFlag);
        this.expectResult = new SimpleStringProperty(expectResult);
    }

    public String getCnStepName() {
        return cnStepName.get();
    }

    public StringProperty cnStepNameProperty() {
        return cnStepName;
    }

    public String getEnStepName() {
        return enStepName.get();
    }

    public StringProperty enStepNameProperty() {
        return enStepName;
    }

    public String getSerialNumber() {
        return serialNumber.get();
    }

    public StringProperty serialNumberProperty() {
        return serialNumber;
    }

    public String getLayer() {
        return layer.get();
    }

    public StringProperty layerProperty() {
        return layer;
    }

    public String getTimePlan() {
        return timePlan.get();
    }

    public StringProperty timePlanProperty() {
        return timePlan;
    }

    public String getReferenceCostTime() {
        return referenceCostTime.get();
    }

    public StringProperty referenceCostTimeProperty() {
        return referenceCostTime;
    }

    public String getParentScriptName() {
        return parentScriptName.get();
    }

    public StringProperty parentScriptNameProperty() {
        return parentScriptName;
    }

    public String getIsManual() {
        return isManual.get();
    }

    public StringProperty isManualProperty() {
        return isManual;
    }

    public String getIsSerial() {
        return isSerial.get();
    }

    public StringProperty isSerialProperty() {
        return isSerial;
    }

    public String getTimeOut() {
        return timeOut.get();
    }

    public StringProperty timeOutProperty() {
        return timeOut;
    }

    public String getCanSkip() {
        return canSkip.get();
    }

    public StringProperty canSkipProperty() {
        return canSkip;
    }

    public String getFailureSkip() {
        return failureSkip.get();
    }

    public StringProperty failureSkipProperty() {
        return failureSkip;
    }

    public String getIsOCardUpAlone() {
        return isOCardUpAlone.get();
    }

    public StringProperty isOCardUpAloneProperty() {
        return isOCardUpAlone;
    }

    public String getScriptName() {
        return scriptName.get();
    }

    public StringProperty scriptNameProperty() {
        return scriptName;
    }

    public String getIsComparison() {
        return isComparison.get();
    }

    public StringProperty isComparisonProperty() {
        return isComparison;
    }

    public String getTip() {
        return tip.get();
    }

    public StringProperty tipProperty() {
        return tip;
    }

    public StringProperty segFlagProperty() {
        return segFlag;
    }

    public StringProperty enableRollBackProperty() {
        return enableRollBack;
    }

    public StringProperty buttonFlagProperty() {
        return buttonFlag;
    }

    public StringProperty expectResultProperty() {
        return expectResult;
    }

    public String getExpectResult() {
        return expectResult.get();
    }

    public StringProperty getStringProperty(String propertyName) {
        try {
            String methodName = propertyName + "Property";
            return (StringProperty)StepData.class.getDeclaredMethod(methodName).invoke(this);
        } catch (Exception e) {
            return null;
        }
    }

    public ObservableValue<String> getProperty(String propertyName) {
        try {
            String methodName = propertyName + "Property";
            return (ObservableValue<String>) StepData.class.getDeclaredMethod(methodName).invoke(this);
        } catch (Exception e) {
            return null;
        }
    }

}
