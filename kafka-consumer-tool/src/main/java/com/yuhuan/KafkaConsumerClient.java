package com.yuhuan;

import com.yuhuan.entity.KafkaMessage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KafkaConsumerClient extends Application {
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerClient.class);
    // 定义UI组件为类的成员变量
    private TextArea ipTextArea = new TextArea("10.156.138.6");
    private TextArea portTextArea = new TextArea("9092");
    private TextArea topicTextArea = new TextArea("ranume_ose_grampus_log");
    private ComboBox<Integer> partitionComboBox = new ComboBox<>();
    private DatePicker startDatePicker = new DatePicker();
    private DatePicker endDatePicker = new DatePicker();
    private TextArea offsetTextArea = new TextArea();
    private Button startConsumeButton = new Button("Start Consume");
    private Button stopConsumeButton = new Button("Stop Consume");
    private Button exportButton = new Button("Export to CSV");
    private TableView<KafkaMessage> messagesTableView = new TableView<>();

    private KafkaConsumer<String, String> consumer;
    private ExecutorService executorService;
    private final ObservableList<KafkaMessage> messages = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        // Define UI components
        partitionComboBox.getItems().addAll(Arrays.asList(0, 1, 2, 3, 4));
        VBox layout = new VBox(10, createGridPane());
        layout.setPadding(new Insets(10));
        initializeMessagesTableView();
        Scene scene = new Scene(layout, 1000, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Kafka Consumer Client");

        startConsumeButton.setOnAction(e -> startConsuming());
        stopConsumeButton.setOnAction(e -> stopConsuming());
        exportButton.setOnAction(e -> exportMessages());

        primaryStage.show();
    }

    private void initializeMessagesTableView() {
        // Define table columns
        TableColumn<KafkaMessage, Long> timestampCol = new TableColumn<>("Timestamp");
        timestampCol.setPrefWidth(100);
        TableColumn<KafkaMessage, Integer> partitionCol = new TableColumn<>("Partition");
        TableColumn<KafkaMessage, Long> offsetCol = new TableColumn<>("Offset");
        TableColumn<KafkaMessage, String> keyCol = new TableColumn<>("Key");
        keyCol.setPrefWidth(200);
        TableColumn<KafkaMessage, String> valueCol = new TableColumn<>("Value");
        valueCol.setMinWidth(500);

        // Set column properties
        timestampCol.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        partitionCol.setCellValueFactory(new PropertyValueFactory<>("partition"));
        offsetCol.setCellValueFactory(new PropertyValueFactory<>("offset"));
        keyCol.setCellValueFactory(new PropertyValueFactory<>("key"));
        valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));

        // Add columns to the table view
        messagesTableView.getColumns().addAll(timestampCol, partitionCol, offsetCol, keyCol, valueCol);

        messagesTableView.setPrefHeight(600);
        messagesTableView.setMinHeight(600);

        // Set the item list for the table view
        messagesTableView.setItems(FXCollections.observableArrayList());
    }

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
//        gridPane.setPadding(new Insets(5, 5, 5, 5));
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        // 列约束：IP标签列（列0）占15%宽度
        ColumnConstraints column0 = new ColumnConstraints();
        column0.setPercentWidth(15);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(20);
        gridPane.getColumnConstraints().addAll(column0, column2);

        // 行约束：行0（IP/Port行）固定高度30像素
        RowConstraints row0 = new RowConstraints();
        row0.setMinHeight(30);
        row0.setMaxHeight(30);
        row0.setVgrow(Priority.NEVER);
        RowConstraints row1 = new RowConstraints();
        row1.setMinHeight(30);
        row1.setMaxHeight(30);
        row1.setVgrow(Priority.NEVER);
        RowConstraints row3 = new RowConstraints();
        row3.setMinHeight(30);
        row3.setMaxHeight(30);
        row3.setVgrow(Priority.NEVER);
        gridPane.getRowConstraints().addAll(row0, row1, row3);


        // Labels
        Label ipLabel = new Label("IP:");
        Label portLabel = new Label("Port:");
        Label topicLabel = new Label("Topic:");
        Label partitionLabel = new Label("Partition:");
        Label startTimeLabel = new Label("Start Time:");
        Label endTimeLabel = new Label("End Time:");
        Label offsetLabel = new Label("Offset:");

        offsetTextArea.setPromptText("Enter offsets in the format 'topic-partition:offset,topic-partition:offset'");

        // Add labels to the grid
        gridPane.add(ipLabel, 0, 0);
        gridPane.add(portLabel, 2, 0);
        gridPane.add(topicLabel, 0, 1);
        gridPane.add(partitionLabel, 2, 1);
        gridPane.add(startTimeLabel, 0, 2);
        gridPane.add(endTimeLabel, 2, 2);
        gridPane.add(offsetLabel, 0, 3);

        // Add input fields and components to the grid
        gridPane.add(ipTextArea, 1, 0);
        gridPane.add(portTextArea, 3, 0);
        gridPane.add(topicTextArea, 1, 1);
        gridPane.add(partitionComboBox, 3, 1);
        gridPane.add(startDatePicker, 1, 2);
        gridPane.add(endDatePicker, 3, 2);
        gridPane.add(offsetTextArea, 1, 3);
        gridPane.add(messagesTableView, 0, 4, 4, 1);

        // Buttons
        HBox buttonBox = new HBox(10); // HBox to hold the buttons
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        buttonBox.getChildren().addAll(startConsumeButton, stopConsumeButton, exportButton);

        // Add the HBox of buttons to the last row of the grid
        gridPane.add(buttonBox, 1, 5, 1, 3); // Span across 3 columns

        return gridPane;
    }

    private void startConsuming() {
        if(consumer!=null) {
            showError("kafka连接提示", "kafka已经开启了监听！");
        }
        // Configure Kafka consumer properties
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, ipTextArea.getText()+":"+portTextArea.getText());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "javafx-consumer-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        consumer = new KafkaConsumer<>(props);

        if(!testConnection(consumer)) {
            consumer = null;
            return;
        }
        if (!offsetTextArea.getText().isEmpty()) {
            TopicPartition topicPartition = new TopicPartition(topicTextArea.getText(), partitionComboBox.getValue());
            Collection<TopicPartition> partitions = new ArrayList<>();
            partitions.add(topicPartition);
            consumer.assign(partitions);
            consumer.seek(topicPartition, Long.valueOf(offsetTextArea.getText()));
        }

        consumer.subscribe(Arrays.asList(topicTextArea.getText().split(",")));


        // Set up executor service for consuming in a separate thread
        executorService = Executors.newSingleThreadExecutor();
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                while (!isCancelled()) {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                    System.out.println("records=========================="+records.count());
                    for (ConsumerRecord<String, String> record : records) {
                        KafkaMessage kafkaMessage = new KafkaMessage(
                                record.timestamp(),
                                record.partition(),
                                record.offset(),
                                record.key(),
                                record.value()
                        );
                        System.out.println("messages: " + kafkaMessage);
                        Platform.runLater(() -> messagesTableView.getItems().add(kafkaMessage));
                    }
                    updateTitle("Consuming messages...");
                }
                return null;
            }
        };

        executorService.submit(task);
    }

    private boolean testConnection(KafkaConsumer<String, String> consumer) {
        try {
            List<PartitionInfo> partitionInfos = consumer.partitionsFor(topicTextArea.getText(), Duration.ofSeconds(3));
            if(null != partitionInfos && !partitionInfos.isEmpty()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("连接kafka失败，请检查kafka连接配置是否正确！");
            showError("kafka连接失败", "请检查kafka连接配置是否正确！");
        }
        return false;
    }

    private static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null); // 可以设置为null来隐藏头部文本
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void stopConsuming() {
        if (executorService != null) {
            executorService.shutdownNow();
        }
        if (consumer != null) {
            consumer.close();
        }
    }

    private void exportMessages() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("kafka_messages.csv");
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("Timestamp,Partition,Offset,Key,Value\n");
                for (KafkaMessage message : messages) {
                    writer.write(String.format("%d,%d,%d,%s,%s\n",
                            message.getTimestamp(), message.getPartition(), message.getOffset(),
                            message.getKey() == null ? "" : message.getKey(),
                            message.getValue() == null ? "" : message.getValue()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stop() {
        stopConsuming();
    }

    public static void main(String[] args) {
        launch(args);
    }

}