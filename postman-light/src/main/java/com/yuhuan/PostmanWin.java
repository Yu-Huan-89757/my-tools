package com.yuhuan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuhuan.api.ICallApi;
import com.yuhuan.entity.SshInfo;
import com.yuhuan.helper.HttpMethod;
import com.yuhuan.impl.PostmanCallApiImpl;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PostmanWin extends Application {

    private static final String[] HTTP_METHOD_TYPE = new String[]{"GET", "POST", "PUT", "DELETE"};

    private HBox sshBox;
    private TextField ip;
    private TextField username;
    private TextField password;

    private HBox urlBox;
    private TextField urlField;
    private ComboBox<String> methodField;
    private TextArea requestBodyField;
    private TextArea responseBodyField;
    private TextArea logField;

    private static final ObjectMapper OM = new ObjectMapper();

    public static void main(String[] args) {
        launch(args);
    }

    private static String getUrl(String ip, String method){
        return "https://"+ ip +":28001/api/"+ method;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 顶层布局
        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(10);

        sshBox = new HBox();
        Label ipLabel = new Label("ip: ");
        ip = new TextField();
        ip.setText("10.156.138.6");
        Label usernameLabel = new Label("username: ");
        username = new TextField();
        username.setText("admin");
        Label passwordLabel = new Label("password: ");
        password = new TextField();
        password.setText("Zenap_123");

        // 请求方法选择框
        Label methodLabel = new Label("Method:");
        methodField = new ComboBox<>();
        methodField.getItems().addAll(HTTP_METHOD_TYPE);
        methodField.setValue(HTTP_METHOD_TYPE[0]);
        sshBox.getChildren().addAll(ipLabel, ip, usernameLabel, username, passwordLabel, password, methodLabel, methodField);

        // URL输入框
        urlBox = new HBox();
        urlField = new TextField();
        urlField.setPrefWidth(700);
        urlField.setPromptText("{serviceName}/v1/method");
        urlField.setText("umebn-inv-basic/v1/mes/ex_querier");

        urlBox.getChildren().addAll(new Label("URL:/api/"), urlField);

        //请求header
        Label headersLabel = new Label("Headers (Key:Value):");
        HBox headerPane = new HBox();
        ListView<TextField> headerList = new ListView<>();
        headerList.setPrefHeight(100);
        headerList.setPrefWidth(680);
        Button addHeaderBtn = new Button("Add Header");
        addHeaderBtn.setOnAction(e -> headerList.getItems().add(new TextField()));
        headerList.getItems().add(new TextField("Content-Type:application/json;charset=utf-8"));
        headerPane.getChildren().addAll(headerList, addHeaderBtn);

        // 请求体区域
        requestBodyField = new TextArea();
        requestBodyField.setPrefRowCount(5);
        requestBodyField.setText("[ {\n" +
                "  \"key\" : \"ifGateway\",\n" +
                "  \"value\" : [ true ]\n" +
                "} ]");


        // 响应区域
        responseBodyField = new TextArea();
        responseBodyField.setEditable(false);
        responseBodyField.setPrefRowCount(30);
        responseBodyField.setWrapText(true);

        logField = new TextArea();
        logField.setEditable(false);
        logField.setPrefRowCount(5);
        logField.setWrapText(true);

        // 按钮区域
        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> {
            formatRequestField();//格式化请求体
            responseBodyField.setText("");
            logField.setText("");

            ObservableList<TextField> headers = headerList.getItems();
            Map<String, String> headerMap = new HashMap<>();
            if(!headers.isEmpty()) {
                headerMap = headers.stream()
                        .map(TextField::getText)
                        .filter(StringUtils::isNotBlank)
                        .map(text -> text.split(":"))
                        .collect(Collectors.toMap(keyValue -> keyValue[0], keyValue -> keyValue[1], (e1, e2) -> e1));

            }


            SshInfo sshInfo = new SshInfo(ip.getText(), username.getText(), password.getText());
            ICallApi iCallApi = new PostmanCallApiImpl(sshInfo, logField);
            String selectedMethodType = methodField.getSelectionModel().getSelectedItem();
            String res = "";
            try {
                res = iCallApi.doRequest(
                        HttpMethod.valueOf(selectedMethodType),
                        getUrl(ip.getText(), urlField.getText()),
                        requestBodyField.getText(), headerMap);
                if(StringUtils.isBlank(res)) {
                    responseBodyField.setText("响应为空");
                    return;
                }
                Object resObj = OM.readValue(res, Object.class);
                responseBodyField.setText(OM.writerWithDefaultPrettyPrinter().writeValueAsString(resObj));
            } catch (JsonProcessingException ex) {
                responseBodyField.setText("JSON格式化发生异常，显示原始响应数据：\n" + res);
            } catch (IllegalArgumentException illegalArgumentException) {
                responseBodyField.setText("请求发生异常，检查下URL和HTTP Method设置");
            } catch (Exception exception) {
                responseBodyField.setText("请求发生异常，" + exception.getMessage());
            }
        });

        // 添加到布局中
        root.getChildren().addAll(
                sshBox,
                urlBox,
                headersLabel, headerPane,
                new Label("Request Body:"), requestBodyField,
                sendButton,
                new Label("Response Body:"), responseBodyField,
                new Label("Log Info:"), logField
        );

        // 创建场景和窗口
        Scene scene = new Scene(root, 800, 800);
        primaryStage.setTitle("My-UME-Postman");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void formatRequestField() {
        Object reqObj = null;
        if(StringUtils.isNotBlank(requestBodyField.getText())) {
            try {
                reqObj = OM.readValue(requestBodyField.getText(), Object.class);
                requestBodyField.setText(OM.writerWithDefaultPrettyPrinter().writeValueAsString(reqObj));
            } catch (JsonProcessingException e) {
                logField.setText(e.toString());
                throw new RuntimeException(e);
            }
        }

    }
}
