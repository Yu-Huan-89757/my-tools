package com.yuhuan;

import com.google.common.collect.Lists;
import com.yuhuan.entity.Constants;
import com.yuhuan.entity.ScriptParam;
import com.yuhuan.entity.StepData;
import com.yuhuan.manager.FreemarkerTemplateManager;
import com.yuhuan.processor.GradlePackagingProcessor;
import com.yuhuan.processor.OapProcessor;
import com.yuhuan.utils.CommonUtils;
import freemarker.cache.ClassTemplateLoader;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

public class ModelUpgradeTool extends Application {
    private static final Logger LOG = LoggerFactory.getLogger(ModelUpgradeTool.class);
    private Label modelCnNameLabel;
    private TextField modelCnNameField;
    private Label modelEnNameLabel;
    private TextField modelEnNameField;
    private Label modelCnDescLabel;
    private TextField modelCnDescField;
    private Label modelEnDescLabel;
    private TextField modelEnDescField;
    private Label modelVersionLabel;
    private TextField modelVersionField;
    private Label deviceSeriesLabel;
    private ComboBox<String> deviceSeriesComboBox;
    private Label upgradeScenarioLabel;
    private ComboBox<String> upgradeScenarioComboBox;
    private Label upgradeTypeLabel;
    private ComboBox<String> upgradeTypeComboBox;
    private Label professionalNetLabel;
    private ComboBox<String> professionalNetComboBox;
    private Label defaultCompareSceneLabel;
    private TextField defaultCompareSceneField;
    private Label compareSceneConfigLabel;
    private ComboBox<String> compareSceneConfigComboBox;
    private Label rollBackLabel;
    private TextField rollBackField;
    private Label mainStepNumLabel;
    private TextField mainStepNumField;
    private Label subStepNumLabel;
    private TextField subStepNumField;
    private TableView<StepData> stepTableView;
    private String modelName;

    private FreemarkerTemplateManager freemarkerTemplateManager;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMdd_HHmm");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmm");

    private final static String PYTHON_DIRECTORY_SUFFIX = "_pythons";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("解耦升级模型包制作工具");

        // 模型信息部分
        GridPane modelInfoPane = new GridPane();
        modelInfoPane.setHgap(10);
        modelInfoPane.setVgap(10);
        modelInfoPane.setPadding(new Insets(10, 10, 10, 10));

        modelCnNameLabel = createLabel("模型名称（中文）");
        modelCnNameField = createTextField("测试模型",250);
        modelEnNameLabel = createLabel("模型名称（英文）");
        modelEnNameField = createTextField( "test model",250);
        modelCnDescLabel = createLabel("模型描述（中文）");
        modelCnDescField = createTextField("测试模型描述",250);
        modelEnDescLabel = createLabel("模型描述（英文）");
        modelEnDescField = createTextField("test model description",250);
        modelVersionLabel = createLabel("模型版本");
        modelVersionField = createTextField("V1", null);
        modelVersionField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!Pattern.matches("^[a-zA-Z0-9_/.]+$", newValue)) {
                modelVersionField.setText(oldValue);
            }
        });
        deviceSeriesLabel = createLabel("支持的设备系列");
        deviceSeriesComboBox = createComboBox(Constants.DEVICE_SERIES);
        upgradeScenarioLabel = createLabel("升级场景");
        upgradeScenarioComboBox = createComboBox(Constants.UPGRADE_SCENARIO);
        upgradeTypeLabel = createLabel("升级类型");
        upgradeTypeComboBox = createComboBox(Constants.UPGRADE_TYPE);
        professionalNetLabel = createLabel("专业网");
        professionalNetComboBox = createComboBox(Constants.PROFESSIONAL_NET);
        defaultCompareSceneLabel = createLabel("默认对比场景");
        defaultCompareSceneField = createTextField("", 250);
        compareSceneConfigLabel = createLabel("对比场景配置");
        compareSceneConfigComboBox = createComboBox(Constants.COMPARE_SCENE_CONFIG);

        rollBackLabel = createLabel("回退步骤数");
        rollBackField = new TextField("3");


        mainStepNumLabel = createLabel("主步骤数");
        mainStepNumField = createTextField("3",null);
        subStepNumLabel = createLabel("每个主步骤包含的子步骤数");
        subStepNumField = createTextField("3",null);

        Button generateButton = new Button("①生成升级步骤");
        generateButton.setOnAction(e -> generateSteps());

        modelInfoPane.add(modelCnNameLabel, 0,0);
        modelInfoPane.add(modelCnNameField, 1, 0);

        modelInfoPane.add(modelEnNameLabel, 2,0);
        modelInfoPane.add(modelEnNameField, 3, 0);

        modelInfoPane.add(modelCnDescLabel, 4,0);
        modelInfoPane.add(modelCnDescField, 5, 0);

        modelInfoPane.add(modelEnDescLabel, 6,0);
        modelInfoPane.add(modelEnDescField, 7, 0);

        modelInfoPane.add(modelVersionLabel, 8,0);
        modelInfoPane.add(modelVersionField, 9, 0);

        modelInfoPane.add(deviceSeriesLabel, 0,1);
        modelInfoPane.add(deviceSeriesComboBox, 1, 1);

        modelInfoPane.add(upgradeScenarioLabel, 2,1);
        modelInfoPane.add(upgradeScenarioComboBox, 3, 1);

        modelInfoPane.add(upgradeTypeLabel, 4,1);
        modelInfoPane.add(upgradeTypeComboBox, 5, 1);

        modelInfoPane.add(professionalNetLabel, 6,1);
        modelInfoPane.add(professionalNetComboBox, 7, 1);

        modelInfoPane.add(defaultCompareSceneLabel, 8,1);
        modelInfoPane.add(defaultCompareSceneField, 9, 1);

        modelInfoPane.add(compareSceneConfigLabel, 0,2);
        modelInfoPane.add(compareSceneConfigComboBox, 1, 2);

        modelInfoPane.add(mainStepNumLabel, 2,2);
        modelInfoPane.add(mainStepNumField, 3, 2);

        modelInfoPane.add(subStepNumLabel, 4,2);
        modelInfoPane.add(subStepNumField, 5, 2);

        modelInfoPane.add(rollBackLabel, 6,2);
        modelInfoPane.add(rollBackField, 7, 2);

        modelInfoPane.add(generateButton, 0,4);

        stepTableView = new TableView<>();
        stepTableView.setPrefHeight(800);
        ScrollPane scrollPane = new ScrollPane(stepTableView);
        // scrollPane.setPrefHeight(600);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        // 升级步骤信息部分
        stepTableView.setEditable(true);
        stepTableView.getColumns().addAll(createColumeByStepdata());

        Button exportSourceButton = new Button("②根据表格内容生成OAP");
        exportSourceButton.setOnAction(e -> exportSourceData());

       Button exportMdoleButton = new Button("指定PYTHON源码目录重新生成OAP");
       exportMdoleButton.setOnAction(e -> exportModelData(null));

        HBox buttonPane = new HBox(10);
        buttonPane.setPadding(new Insets(10));
        buttonPane.getChildren().add(exportSourceButton);
       buttonPane.getChildren().add(exportMdoleButton);

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(modelInfoPane, stepTableView, buttonPane);

        Scene scene = new Scene(root, 1440, 900);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private void exportModelData(String pythonsPath) {
        Path oapsDescPath;
        if(null == pythonsPath) {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setInitialDirectory(new File("."));
            File chooseFile = directoryChooser.showDialog(null);
            if(null == chooseFile) {
                return;
            }
            pythonsPath = chooseFile.getPath();
            oapsDescPath = Paths.get(CommonUtils.substringBeforeSecondLastUnderscore(pythonsPath)+"_"+LocalDateTime.now().format(timeFormatter));
        }else {
            oapsDescPath = Paths.get(pythonsPath.replace(PYTHON_DIRECTORY_SUFFIX, ""));
        }
        if(!oapsDescPath.toFile().exists()) {
            try {
                Files.createDirectory(oapsDescPath);
            } catch (IOException e) {
                LOG.error("创建目标OAP文件夹失败： ", e);
            }
        }

        OapProcessor oapProcessor = new OapProcessor();
        GradlePackagingProcessor gradlePackagingProcessor = new GradlePackagingProcessor();
        try {
            oapProcessor.processorOapFile(Paths.get(pythonsPath));
            gradlePackagingProcessor.compileScripts();
            gradlePackagingProcessor.copyOapFileToCompileDirectory(oapsDescPath);
            LOG.info("模型导出成功");
            showAlert("模型导出成功");
        } catch (IOException | InterruptedException e) {
            LOG.error("模型导出失败： ", e);
            showAlert("模型导出失败：" + e.getMessage());
        } catch (Exception e) {
            LOG.error("模型导出失败：", e);
            showAlert("模型导出失败：" + e.getMessage());
        }
    }

    private List<TableColumn<StepData, String>> createColumeByStepdata() {
        List<TableColumn<StepData, String>> tableColumnList = new ArrayList<>();
        Field[] propertys = getFields(StepData.class);
        Arrays.stream(propertys).forEach(property ->
                tableColumnList.add(createTableColumn(property.getName(), 100)));
        return tableColumnList;
    }

    private static Field[] getFields(Class<?> clazz) {
        Field[] propertys = clazz.getDeclaredFields();
        return propertys;
    }

    private Label createLabel(String text) {
        return new Label(text);
    }

    private TextField createTextField(String defaultText, Integer maxLength) {
        TextField textField = new TextField();
        if(StringUtils.isNotBlank(defaultText)) {
            textField.setText(defaultText);
        }
        if (maxLength != null) {
            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.length() > maxLength) {
                    // 如果输入的文本长度超过最大长度，恢复到上一个有效值
                    textField.setText(oldValue);
                }
            });
        }
        return textField;
    }

    private ComboBox<String> createComboBox(ObservableList<String> items) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(items);
        return comboBox;
    }

    private ComboBox<String> createComboBox(ObservableMap<String, Integer> items) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(items.keySet());
        return comboBox;
    }

    private TableColumn<StepData, String> createTableColumn(String property, int width) {
        TableColumn<StepData, String> column = new TableColumn<>(property);
        column.setCellValueFactory(cellData -> cellData.getValue().getProperty(property));
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setOnEditCommit(event -> {
            StepData stepData = event.getRowValue();
            stepData.getStringProperty(property).setValue(event.getNewValue());
        });
        column.setPrefWidth(width);
        return column;
    }

    private void generateSteps() {
        if (modelCnNameField.getText().isEmpty() || modelEnNameField.getText().isEmpty() 
            || modelCnDescField.getText().isEmpty() || modelEnDescField.getText().isEmpty() 
            || modelVersionField.getText().isEmpty() || deviceSeriesComboBox.getValue() == null 
            || upgradeScenarioComboBox.getValue() == null || upgradeTypeComboBox.getValue() == null 
            || professionalNetComboBox.getValue() == null) {
            showAlert("请先填写完整的模型信息");
            return;
        }
        String mainStepNumStr = mainStepNumField.getText();
        String subStepNumStr = subStepNumField.getText();

        if (mainStepNumStr.isEmpty() || subStepNumStr.isEmpty()) {
            showAlert("请输入主步骤数和子步骤数");
            return;
        }

        int mainStepNum;
        int subStepNum;
        try {
            mainStepNum = Integer.parseInt(mainStepNumStr);
            subStepNum = Integer.parseInt(subStepNumStr);
            if (mainStepNum <= 0 || subStepNum <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            showAlert("主步骤数和子步骤数必须为大于0的整数");
            return;
        }
        stepTableView.getItems().clear();

        ObservableList<StepData> stepDataList = FXCollections.observableArrayList();

        for (int m = 1; m <= mainStepNum; m++) {
            StepData mainStep = new StepData(
                    "主步骤 " + m,
                    "Main Step " + m,
                    String.valueOf(m),
                    "Main",
                    "0:00-6:00",
                    "600",
                    null,
                    "Script",
                    "false",
                    "600",
                    "",
                    "true",
                    "false",
                    null,
                    null,
                    null,
                    m<=2 ?"OUTSIDE_WINDOW":"INSIDE_WINDOW",
                    "",
                    "",
                    ""
            );
            stepDataList.add(mainStep);

            for (int n = 1; n <= subStepNum; n++) {
                StepData subStep = new StepData(
                        "子步骤 " + m + "_" + n,
                        "Sub Step " + m + "_" + n,
                        m + "_" + n,
                        "Sub",
                        "0:00-6:00",
                        "60",
                        null,
                        n==1? "Manual":"Script",
                        "false",
                        "150",
                        "",
                        "true",
                        "false",
                        "upg_" + professionalNetComboBox.getValue() + "_" + modelVersionField.getText() + "_" + m + "_" + n+"_"+LocalDateTime.now().format(dateTimeFormatter),
                        null,
                        "{\"zh-CN\": \"人工执行提示信息\",\"en-US\": \"Manual execute tip info\"}",
                        m<=2 ?"OUTSIDE_WINDOW":"INSIDE_WINDOW",
                        "true",
                        "",
                        "True"
                );
                stepDataList.add(subStep);
            }
        }
        //生成回退步骤
        String rollBackStepNum = rollBackField.getText();
        if(StringUtils.isNotBlank(rollBackStepNum) && Integer.parseInt(rollBackStepNum) > 0) {
            StepData mainStep = new StepData(
                    "回退 " + (mainStepNum+1),
                    "Roll Back " + (mainStepNum+1),
                    (mainStepNum+1)+"",
                    "Main",
                    "0:00-6:00",
                    "600",
                    null,
                    "Script",
                    "false",
                    "600",
                    "",
                    "true",
                    "false",
                    null,
                    null,
                    null,
                    "ROLL_BACK",
                    "",
                    "",
                    ""
            );
            stepDataList.add(mainStep);
            for (int n = 1; n <= Integer.parseInt(rollBackStepNum); n++) {
                StepData subStep = new StepData(
                        "回退 "+(mainStepNum+1)+"_" + n,
                        "Roll Back " + (mainStepNum+1)+"_" + n,
                        (mainStepNum+1) + "_" + n,
                        "Sub",
                        "0:00-6:00",
                        "100",
                        null,
                        n==1? "Manual":"Script",
                        "false",
                        "150",
                        "",
                        "true",
                        "false",
                        "upg_" + professionalNetComboBox.getValue() + "_" + modelVersionField.getText() + "_"+(mainStepNum+1)+"_" + n+"_"+LocalDateTime.now().format(dateTimeFormatter),
                        null,
                        "{\"zh-CN\": \"人工执行提示信息\",\"en-US\": \"Manual execute tip info\"}",
                        "ROLL_BACK",
                        "true",
                        "",
                        "True"
                );
                stepDataList.add(subStep);
            }
        }
        stepTableView.setItems(stepDataList);
    }

    private void exportSourceData() {
        FileChooser fileChooser = new FileChooser();
        modelName = "upgrade_"+deviceSeriesComboBox.getValue().replaceAll(" ", "")
                +"_"+modelVersionField.getText()
                +"_"+ LocalDateTime.now().format(dateTimeFormatter);
        fileChooser.setInitialFileName(modelName);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File file = fileChooser.showSaveDialog(null);
        if(null == file) return;
        String pythonsPath = file.getParent() + File.separator + modelName + PYTHON_DIRECTORY_SUFFIX + File.separator;

        freemarkerTemplateManager = new FreemarkerTemplateManager();
        if(CommonUtils.isJarEnvironment()) {
            freemarkerTemplateManager.setTemplateLoader(new ClassTemplateLoader(getClass(), "/templates"));
        }else {
            freemarkerTemplateManager.setTemplateDirectory("src/main/resources/templates/");
        }
        if (file != null) {
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                Workbook workbook = new XSSFWorkbook();
                Sheet modelSheet = workbook.createSheet("模型信息");
                Sheet stepSheet = workbook.createSheet("升级步骤");

                // 写入模型信息
                Row modelRow = modelSheet.createRow(0);
                modelRow.createCell(0).setCellValue("模型名称");
                modelRow.createCell(1).setCellValue("模型描述");
                modelRow.createCell(2).setCellValue("模型版本");
                modelRow.createCell(3).setCellValue("支持的设备系列");
                modelRow.createCell(4).setCellValue("升级场景");
                modelRow.createCell(5).setCellValue("升级类型");
                modelRow.createCell(6).setCellValue("专业网");
                modelRow.createCell(7).setCellValue("默认对比场景");
                modelRow.createCell(8).setCellValue("对比场景配置");

                modelRow = modelSheet.createRow(1);
                modelRow.createCell(0).setCellValue(modelCnNameField.getText());
                modelRow.createCell(1).setCellValue(modelCnDescField.getText());
                modelRow.createCell(2).setCellValue(modelVersionField.getText());
                modelRow.createCell(3).setCellValue(deviceSeriesComboBox.getValue());
                modelRow.createCell(4).setCellValue(Constants.UPGRADE_SCENARIO.get(upgradeScenarioComboBox.getValue()));
                modelRow.createCell(5).setCellValue(upgradeTypeComboBox.getValue());
                modelRow.createCell(6).setCellValue(professionalNetComboBox.getValue());
                modelRow.createCell(7).setCellValue(defaultCompareSceneField.getText());
                if(StringUtils.isNotBlank(compareSceneConfigComboBox.getValue())) {
                    modelRow.createCell(8).setCellValue(Constants.COMPARE_SCENE_CONFIG.get(compareSceneConfigComboBox.getValue()));
                }

                modelRow = modelSheet.createRow(2);
                modelRow.createCell(0).setCellValue(modelEnNameField.getText());
                modelRow.createCell(1).setCellValue(modelEnDescField.getText());

                // 写入升级步骤信息
                Row stepRow = stepSheet.createRow(0);
                // 写入列名
                Field[] propertys = getFields(StepData.class);
                for (int i = 0; i < propertys.length; i++) {
                    stepRow.createCell(i).setCellValue(propertys[i].getName());
                }

                for (int i = 0; i < stepTableView.getItems().size(); i++) {
                    StepData stepData = stepTableView.getItems().get(i);
                    stepRow = stepSheet.createRow(i + 1);
                    // 写入列数据
                    for (int j = 0; j < propertys.length; j++) {
                        stepRow.createCell(j).setCellValue(
                                stepData.getStringProperty(propertys[j].getName()).getValue());
                    }
                    if(stepData.getLayer().equals("Sub")) {
                        generatePythonFiles(freemarkerTemplateManager, stepData, pythonsPath);
                    }
                }
                workbook.write(outputStream);
                exportModelData(pythonsPath);

            } catch (IOException e) {
                showAlert("数据导出失败：" + e.getMessage());
            }
        }
    }

    private void generatePythonFiles(FreemarkerTemplateManager freemarkerTemplateManager,
                                     StepData stepData, String baseOutputPath) {
        BiConsumer<String, String> consumer = (templateFilePath, outputFileName)->{
            freemarkerTemplateManager.generateFile(templateFilePath,
                    buildScriptParams(stepData),
                    baseOutputPath+stepData.getScriptName()+File.separator+outputFileName);
        };
        consumer.accept("__init__.ftl", "__init__.py");
        consumer.accept("i18n.ftl", ".config"+File.separator+"i18n.json");
        consumer.accept("main.ftl", "main.py");
        consumer.accept("project.ftl", "project.xml");
        consumer.accept("project-config.ftl", "project-config.json");
//        consumer.accept("VERIFY.ftl", "VERIFY");
    }

    private static Map<String, Object> buildScriptParams(StepData stepData) {
        Map<String, Object> data = new HashMap<>();
        data.put("project_name", stepData.getScriptName());
        data.put("project_description", stepData.getScriptName());
        data.put("cost_time", stepData.getReferenceCostTime());
        data.put("expect_result", stepData.getExpectResult());
        // ScriptParam scriptParam1 = new ScriptParam("neighbor_ip", ScriptParam.ScriptParamType.Ip,
        //         "邻居网元IP", "Neighbor ME IP",
        //         "当需要使用邻居网元时填写此属性", "When you need use neighbor ME, fill in it",
        //         true);
        ScriptParam scriptParam2 = new ScriptParam("string_param", ScriptParam.ScriptParamType.String,
                "字符串类型参数", "Param of String type",
                "字符串类型参数描述信息", "Description of String param",
                false);
        // ScriptParam scriptParam3 = new ScriptParam("number_param", ScriptParam.ScriptParamType.Number,
        //         "数值类型参数", "Param of Number type",
        //         "数值类型参数描述信息", "Description of Number param",
        //         true);
        // ScriptParam scriptParam4 = new ScriptParam("password_param", ScriptParam.ScriptParamType.Password,
        //         "密码类型参数", "Param of Password type",
        //         "密码类型参数描述信息", "Description of Password param",
        //         true);
        // data.put("params", Lists.newArrayList(scriptParam1, scriptParam2, scriptParam3, scriptParam4));
        data.put("params", Lists.newArrayList(scriptParam2));
        return data;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }


}