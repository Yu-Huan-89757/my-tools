@echo off
set CURRENT_DIR=%~dp0
chcp 65001
java -Dfile.encoding=UTF-8 -Xmx512m --module-path "%CURRENT_DIR%\javafx-sdk-23.0.2\lib" --add-modules javafx.controls,javafx.fxml -jar generate-upgrade-model-1.0-SNAPSHOT-shaded.jar