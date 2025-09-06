package com.yuhuan;

import com.sun.javafx.collections.ObservableListWrapper;
import com.yuhuan.Handler.TableKeyEventHandler;
import com.yuhuan.convert.ConvertData;
import com.yuhuan.entity.ColumnAndDataType;
import com.yuhuan.entity.DataBase;
import com.yuhuan.entity.Table;
import com.yuhuan.helper.SqlHelper;
import com.yuhuan.utils.CsvUtils;
import com.yuhuan.utils.ResultSetUtils;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DbLight extends Application {
    private TextField urlField;
    private TextField portField;
    private TextField usernameField;
    private PasswordField passwordField;
    private Button connectButton;
    private Button historyButton;
    private TextField dbSearchField;
    private TextField tbSearchField;
    private ListView<DataBase> dbListView;
    private ListView<Table> tableListView;
    private TextArea queryArea;
    private Button executeButton;
    private TextField searchAll;
    private Button searchButton;
    private Button columnsSetButton;
    private Button exportButton;
    private TableView<List<Object>> queryResultArea;
    private Connection connection;

    private DirectoryChooser directoryChooser;

    private ObservableList<DataBase> dataBaseObservableList;
    private ObservableList<Table> tableObservableList;
    private List<ColumnAndDataType> columnAndDataTypes = new ArrayList<>();
    private String selectedTableName;
    private List<String> allColumns = new ArrayList<>();
    private List<String> selectedColumns = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Database Connection Tool");

        // 创建UI组件
        Label urlLabel = new Label("URL:");
        urlField = new TextField();
        urlField.setText("10.239.69.189");
        portField = new TextField();
        portField.setText("4508");
        Label usernameLabel = new Label("Username:");
        usernameField = new TextField();
        usernameField.setText("postgres");
        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();
        passwordField.setText("Cloud123");
        connectButton = new Button("Connect");
        historyButton = new Button("History");
        Label dbLabel = new Label("DataBase:");
        dbSearchField = new TextField();
        Label tbLabel = new Label("Table:");
        tbSearchField = new TextField();
        dbListView = new ListView<>();
        tableListView = new ListView<>();
        Label queryLabel = new Label("SQL Query:");
        queryArea = new TextArea();
        queryArea.setWrapText(true);
        executeButton = new Button("Execute");
        searchAll = new TextField();
        searchButton = new Button("Search");
        columnsSetButton = new Button("|||");
        exportButton = new Button("Export");
        queryResultArea = new TableView<>();
        queryResultArea.getSelectionModel().setCellSelectionEnabled(true);
        queryResultArea.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        directoryChooser = new DirectoryChooser();

        // 创建布局
        GridPane gridPane = new GridPane();
        setGridPane(urlLabel, usernameLabel, passwordLabel, dbLabel, tbLabel, queryLabel, gridPane);

        // 创建场景
        Scene scene = new Scene(gridPane, 1800, 1000);
        primaryStage.setScene(scene);
        primaryStage.show();

        setListeners(primaryStage);
    }

    private void setGridPane(Label urlLabel, Label usernameLabel, Label passwordLabel,
                             Label dbLabel, Label tbLabel,
                             Label queryLabel, GridPane gridPane) {
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setGridLinesVisible(false);
        gridPane.setHgap(10); // 设置水平间距s
        gridPane.setVgap(10); // 设置垂直间距

        setGridPaneRowAndCoulumnSize(gridPane);
        setGridPaneNode(urlLabel, usernameLabel, passwordLabel, dbLabel, tbLabel, queryLabel, gridPane);
    }

    private void setGridPaneNode(Label urlLabel, Label usernameLabel, Label passwordLabel, Label dbLabel, Label tbLabel, Label queryLabel, GridPane gridPane) {
        gridPane.add(urlLabel, 0, 0, 1, 1);
        gridPane.add(urlField, 1, 0, 2, 1);
        gridPane.add(portField,3,0,1,1);
        gridPane.add(usernameLabel, 0, 1, 1, 1);
        gridPane.add(usernameField, 1, 1, 3, 1);
        gridPane.add(passwordLabel, 0, 2, 1, 1);
        gridPane.add(passwordField, 1, 2, 3, 1);
        gridPane.add(connectButton, 1, 3, 1, 1);
        gridPane.add(historyButton,2,3,1,1);
        gridPane.add(dbLabel, 0, 4, 1, 1);
        gridPane.add(dbSearchField, 1, 4, 1, 1);
        gridPane.add(tbLabel, 2, 4, 1, 1);
        gridPane.add(tbSearchField, 3, 4, 1, 1);
        gridPane.add(dbListView, 0, 5, 2, 1);
        gridPane.add(tableListView, 2, 5, 2, 1);
        gridPane.add(queryLabel, 4, 0, 1, 1);
        gridPane.add(queryArea, 5, 0, 5, 2);
        gridPane.add(executeButton, 5, 2, 1, 1);
        gridPane.add(searchAll, 6, 2, 1, 1);
        gridPane.add(searchButton, 7, 2, 1, 1);
        gridPane.add(columnsSetButton, 8, 2, 1, 1);
        gridPane.add(exportButton, 9, 2, 1, 1);
        gridPane.add(queryResultArea, 4, 3, 6, 3);
    }

    private static void setGridPaneRowAndCoulumnSize(GridPane gridPane) {
        RowConstraints rc1 = new RowConstraints();
        rc1.setPercentHeight(3);
        RowConstraints rc2 = new RowConstraints();
        rc2.setPercentHeight(85);
        gridPane.getRowConstraints().addAll(rc1, rc1, rc1, rc1, rc1, rc2);

        ColumnConstraints cc1 = new ColumnConstraints();
        cc1.setPercentWidth(5);
        ColumnConstraints cc2 = new ColumnConstraints();
        cc2.setPercentWidth(6);
        ColumnConstraints cc3 = new ColumnConstraints();
        cc3.setPercentWidth(54);
        ColumnConstraints cc4 = new ColumnConstraints();
        cc4.setPercentWidth(3);
        gridPane.getColumnConstraints().addAll(cc1, cc2, cc1, cc2, cc2, cc1, cc3, cc1, cc4, cc1);
    }

    private void setListeners(Stage primaryStage) {
        //数据库连接
        connectButton.setOnAction(e -> connectToDatabase());
        //全表数据搜索
        searchButton.setOnAction(e -> searchData());
        //数据库搜索
        dbSearchField.textProperty().addListener((observable, oldValue, newValue) -> searchDataBase(newValue));
        //表搜索
        tbSearchField.textProperty().addListener((observable, oldValue, newValue) -> searchTable(newValue));
        //双击数据库，默认展示该数据库所有表
        dbListView.setOnMouseClicked(this::selectDbEvent);
        //双击表，默认查询该表前50行数据
        tableListView.setOnMouseClicked(this::selectTableEvent);
        //sql执行
        executeButton.setOnAction(e -> executeSql());
        //支持复制单元格数据
        queryResultArea.setOnKeyPressed(new TableKeyEventHandler());
        //列定制
        columnsSetButton.setOnAction(e -> setTableColumns());
        //数据导出按钮
        exportButton.setOnAction(e -> exportTableData(primaryStage));
    }

    private void setTableColumns() {
        if(columnAndDataTypes.isEmpty() || selectedTableName == null || allColumns.isEmpty()) {
            showDialog(Alert.AlertType.WARNING, "列定制", "列定制失败", "可能未选择数据库表？");
        }
        Stage columnsStage = new Stage();
        VBox vBox = new VBox(5);
        vBox.setPadding(new Insets(10));
        vBox.setMaxHeight(600);

        HBox hBoxButton1 = new HBox(10);
        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton selectAllBtn = new RadioButton("select all");
//        selectAllBtn.setSelected(true);
        selectAllBtn.setToggleGroup(toggleGroup);
        RadioButton cancelAllBtn = new RadioButton("cancel all");
        cancelAllBtn.setToggleGroup(toggleGroup);
        toggleGroup.selectToggle(selectAllBtn);

        hBoxButton1.getChildren().addAll(selectAllBtn, cancelAllBtn);
        vBox.getChildren().add(hBoxButton1);

        ScrollPane scrollPane = new ScrollPane();
        VBox vBoxColumns = new VBox(5);
        vBoxColumns.setPadding(new Insets(10));
        vBoxColumns.setMaxHeight(600);

        for (int i = 0; i < allColumns.size(); i++) {
            CheckBox checkBox = new CheckBox(allColumns.get(i));
            checkBox.setPadding(new Insets(3));
            checkBox.setSelected(selectedColumns.contains(allColumns.get(i)));
            vBoxColumns.getChildren().add(checkBox);
        }
        scrollPane.setContent(vBoxColumns);
        vBox.getChildren().add(scrollPane);

        HBox hBoxButton2 = new HBox(10);
        Button confirmBtn = new Button("confirm");
        Button cancelBtn = new Button("cancel");
        hBoxButton2.getChildren().addAll(confirmBtn, cancelBtn);
        vBox.getChildren().add(hBoxButton2);

        Scene columnScene = new Scene(vBox, 300, 600);
        columnsStage.setScene(columnScene);
        columnsStage.setTitle("Columns Customize");
//        columnsStage.setAlwaysOnTop(true);
        columnsStage.show();

        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            RadioButton radioButton = (RadioButton) newValue;
            selectOrCancelAllColumns(vBoxColumns,radioButton.getText().equals("select all"));
        });

        confirmBtn.setOnAction(e -> {
            selectedColumns = vBoxColumns.getChildren().stream().map(node -> {
                if (node instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) node;
                    boolean selected = checkBox.isSelected();
                    if (selected) {
                        return checkBox.getText();
                    }
                }
                return null;
            }).filter(Objects::nonNull).collect(Collectors.toList());
            if(selectedColumns.size()==0) {
                showDialog(Alert.AlertType.WARNING, "列定制", "列定制失败", "请至少选择1列！");
                return;
            }
            String queryTablesDataString = SqlHelper.getQueryTablesDataSql(selectedTableName, selectedColumns);
            queryArea.setText(queryTablesDataString);
            executeSql();
            columnsStage.close();
        });
        cancelBtn.setOnAction(e -> columnsStage.close());
    }

    private void selectOrCancelAllColumns(VBox vBoxColumns, boolean flag) {
        vBoxColumns.getChildren().forEach(node -> {
            if(node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                checkBox.setSelected(flag);
            }
        });
    }

    private void exportTableData(Stage primaryStage) {
        ObservableList<TableColumn<List<Object>, ?>> columns = queryResultArea.getColumns();
        if(columns.isEmpty()) {
            showDialog(Alert.AlertType.INFORMATION, "数据导出结果", "数据导出失败", "获取表格列名失败，可能未选择数据库表？");
            return;
        }
        ObservableList<List<Object>> tableData = queryResultArea.getItems();
        if(tableData.isEmpty()) {
            showDialog(Alert.AlertType.INFORMATION, "数据导出结果", "数据导出失败", "当前表格数据为空！");
            return;
        }

        File file = directoryChooser.showDialog(primaryStage);
        String tableName = getSelectTableName();
        String fileName = file.getPath()+ File.separator + tableName + "-" + System.currentTimeMillis() + ".csv";

        String[] exportColumns = queryResultArea.getColumns()
                .stream()
                .map(TableColumnBase::getText).toArray(String[]::new);
        CsvUtils.exportCsvTableWithFullPath(fileName, tableData, exportColumns);
        showDialog(Alert.AlertType.INFORMATION, "数据导出", "数据导出成功", "文件保存位置："+fileName);
    }

    private String getSelectTableName() {
        String tableName;
        ObservableList<Table> selectedTables = tableListView.getSelectionModel().getSelectedItems();
        if(selectedTables.isEmpty()) {
            tableName = "table";
        }else {
            tableName = selectedTables.get(0).getTableName();
        }
        return tableName;
    }

    private void searchDataBase(String newValue) {
        if (StringUtils.isBlank(newValue)) {
            dbListView.setItems(dataBaseObservableList);
        }
        if (!dataBaseObservableList.isEmpty()) {
            List<DataBase> filterDataBaseList = dataBaseObservableList.stream().filter(dataBase ->
                            dataBase.getDatname().toLowerCase().contains(newValue.toLowerCase()))
                    .collect(Collectors.toList());
            dbListView.setItems(new ObservableListWrapper<>(filterDataBaseList));
        }
    }

    private void searchTable(String newValue) {
        if (StringUtils.isBlank(newValue)) {
            tableListView.setItems(tableObservableList);
        }
        if (!tableObservableList.isEmpty()) {
            List<Table> filterTableList = tableObservableList.stream().filter(table ->
                            table.getTableName().toLowerCase().contains(newValue.toLowerCase()))
                    .collect(Collectors.toList());
            tableListView.setItems(new ObservableListWrapper<>(filterTableList));
        }
    }

    private void selectTableEvent(MouseEvent event) {
        if (2 == event.getClickCount()) {//双击表
            selectedTableName = tableListView.getSelectionModel().getSelectedItems().get(0).getTableName();
            //初始化列名
            columnAndDataTypes = executeQuery(SqlHelper.getTableColumnsSql(selectedTableName), ColumnAndDataType.class);
            allColumns = columnAndDataTypes.stream().map(ColumnAndDataType::getColumnName).collect(Collectors.toList());
            selectedColumns = new ArrayList<>(allColumns);
            String queryTablesDataString = SqlHelper.getQueryTablesDataSql(selectedTableName, allColumns);
            queryArea.setText(queryTablesDataString);
            executeSql();
        }
    }

    private void selectDbEvent(MouseEvent event) {
        if (2 == event.getClickCount()) {//双击数据库
            //关闭原连接
            closeConn();
            String dbName = dbListView.getSelectionModel().getSelectedItems().get(0).getDatname();
            try {
                connection = DriverManager.getConnection(getUrl() + dbName, usernameField.getText(), passwordField.getText());
            } catch (SQLException e) {
                showDialog(Alert.AlertType.WARNING, "数据库连接结果", "数据库连接失败", "请检查填写的数据库IP、端口、用户名等信息是否正确");
            }
            tbSearchField.setText("");
            if (tableObservableList != null) {
                tableObservableList.clear();
            }
            List<Table> tables = executeQuery(SqlHelper.getQueryTablesSql(dbName), Table.class);
            tableObservableList = new ObservableListWrapper<>(tables);
            tableListView.setItems(tableObservableList);
        }
    }

    private String getUrl() {
        return "jdbc:postgresql://" + urlField.getText() + ":" + portField.getText() + "/";
    }

    private void searchData() {
        String searchAllText = searchAll.getText();
        if (StringUtils.isBlank(searchAllText)) {
            return;
        }
        //如果未选择过数据库表，则提示
        if (null == selectedTableName) {
            showDialog(Alert.AlertType.WARNING, "数据查找结果", "数据查找失败", "请先选择一个数据库表");
        }
        queryArea.setText(SqlHelper.getSearchAllSqlString(selectedTableName, columnAndDataTypes, searchAllText));
        executeSql();
    }

    private void executeSql() {
        queryResultArea.getItems().clear();
        queryResultArea.getColumns().clear();
        Pair<List<String>, List<List<Object>>> pair = executeQuery(queryArea.getText());
        queryResultArea.setItems(ConvertData.convertToTableData(queryResultArea, pair));
    }

    private void connectToDatabase() {
        String url = getUrl();
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            // 建立数据库连接
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to database!");
            List<DataBase> dbList = executeQuery(SqlHelper.QUERY_ALL_DATABASES, DataBase.class);
            if (dataBaseObservableList != null) {
                dataBaseObservableList.clear();
            }
            if (tableObservableList != null) {
                tableObservableList.clear();
            }
            dbSearchField.setText("");
            tbSearchField.setText("");
            dataBaseObservableList = new ObservableListWrapper<>(dbList);
            dbListView.setItems(dataBaseObservableList);
        } catch (SQLException e) {
            showDialog(Alert.AlertType.ERROR, "数据库连接结果", "数据库连接失败,请检查填写的数据库IP、端口、用户名等信息是否正确", "错误信息："+e.toString());
        }
    }

    private void showDialog(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    private void closeConn() {
        if (null != connection) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private <T> List<T> executeQuery(String query, Class<T> clazz) {
        System.out.println("query: " + query);
        List<T> result = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            result = ResultSetUtils.parseResultSetToList(resultSet, clazz);
            // 关闭连接
            resultSet.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private Pair<List<String>, List<List<Object>>> executeQuery(String query) {
        System.out.println("query: " + query);
        Pair<List<String>, List<List<Object>>> result = null;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            result = ResultSetUtils.parseResultSetToList(resultSet);
            // 关闭连接
            resultSet.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        closeConn();
    }
}
