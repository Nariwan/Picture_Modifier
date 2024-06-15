package PicModi;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.opencv.core.Core;

import static PicModi.Pic_Modi_Menu.refresh_menu;

public class Main extends Application {

    public static Window Window_FX_Var;
    private static Pane Scene_FX_Pane;
    public double d_Window_Height, d_Window_Width;

    private int subtract = 0;

    @Override
    public void start(Stage primaryStage) {

        d_Window_Height = (2*Screen.getPrimary().getBounds().getHeight())/3;
        d_Window_Width = (2*Screen.getPrimary().getBounds().getWidth())/3.5;

        primaryStage.setTitle("Picture Modifier!");
        // OpenCV Startup thing
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Scene_FX_Pane.prefHeight(d_Window_Height);
        Scene_FX_Pane.prefWidth(d_Window_Width);

        Scene scene_FX_Var = new Scene(Scene_FX_Pane, d_Window_Width, d_Window_Height);
        scene_FX_Var.getStylesheets().add("Style.css");


        Pic_Modi_Menu Menu = new Pic_Modi_Menu(Scene_FX_Pane);
        Pic_Modi_Artist Artist = new Pic_Modi_Artist(Window_FX_Var);

        Pic_Modi_Menu.new_Pane_height(Scene_FX_Pane.getHeight());
        Pic_Modi_Menu.new_Pane_width(Scene_FX_Pane.getWidth());

        subtract = Pic_Modi_Menu.getI_x_equal_spacing();

        primaryStage.setScene(scene_FX_Var);
        primaryStage.centerOnScreen();
        primaryStage.sizeToScene();
        primaryStage.setResizable(true);
        primaryStage.show();

        Window_FX_Var = scene_FX_Var.getWindow();
        Window_FX_Var.sizeToScene();

        refresh_menu();
        // If the Windows is resized, call this Function
        primaryStage.heightProperty().addListener((obs, oldVal,  newVal) -> {
            Pic_Modi_Menu.new_Pane_height((double) newVal - subtract);
            refresh_menu();
        });
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            Pic_Modi_Menu.new_Pane_width((double) newVal - subtract);
            refresh_menu();
        });

        Menu.setArtist(Scene_FX_Pane, Artist);
    }

    public static void main(String[] args) {
        Scene_FX_Pane = new Pane();
        launch(args);
    }
}
