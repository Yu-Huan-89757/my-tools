package com.yuhuan.convert;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Pair;

import java.util.List;

public class ConvertData {
    public static ObservableList<List<Object>> convertToTableData(TableView<List<Object>> tableView,
                                                                  Pair<List<String>, List<List<Object>>> pair) {
        List<String> columns = pair.getKey();
        List<List<Object>> rowDatas = pair.getValue();

        final ObservableList<List<Object>> observableList = FXCollections.observableArrayList();
        for (int i = 0; i < columns.size(); i++) {
            TableColumn<List<Object>, Object> tableColumn = new TableColumn<>(columns.get(i));
            final int finalI = i;
            tableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get(finalI)));
            tableView.getColumns().add(tableColumn);
        }
        rowDatas.forEach(row-> observableList.add(row));
        return observableList;
    }
}
