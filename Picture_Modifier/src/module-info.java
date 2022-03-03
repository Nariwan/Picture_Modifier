module DataPickerModule {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires opencv;


    opens Picker to javafx.controls;
    exports Picker;
}