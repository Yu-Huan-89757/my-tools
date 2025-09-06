@echo off
set CURRENT_DIR=%~dp0
java -Xmx512m --module-path "%CURRENT_DIR%\javafx-sdk-23.0.2\lib" --add-modules javafx.controls,javafx.fxml -jar database-light-1.0-SNAPSHOT-shaded.jar