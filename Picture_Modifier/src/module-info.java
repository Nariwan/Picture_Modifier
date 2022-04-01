module DataPickerModule {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires opencv;
    requires java.desktop;


    opens Picker to javafx.controls;
    exports Picker;
}