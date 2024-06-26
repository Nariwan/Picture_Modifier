package PicModi;


// Imported Libraries

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;


public class Pic_Modi_Menu {

    // Some variables, IntelliJ might gonna nag of some,
    // but that's okay.

    public static boolean bool_DataPickMenuCreated = false;
    public static Group DataGroup_For_FXPane;
    public static FileChooser fch_DataChoose;
    private static File file_DataVar;
    private static Button Button_PickFile, Button_For_Saving_The_Pic, Button_For_Exit, Button_Refresh_Pic;
    private static Label sPath_Label, ThresDescription_Label, CheckBox_Normalise_Label, Norm_Min_Label, Norm_Max_Label, lbl_Save_Name, lbl_Live_Preview, lbl_Equal_Hist, lbl_Feedback;
    public static boolean bool_HasPic;


    public static TextField txtfld_Save_Name_Giver;
    public static Slider Slider_Threshold, Slider_Norm_Min, Slider_Norm_Max;
    private static CheckBox Chkbx_Normalise, chkbx_Live_preview, chkbx_SW_Pic, chkbx_Equal_Hist;
    private static float f_Y_Pos;
    private static final int i_x_equal_spacing = 15;
    private static final int i_y_equal_spacing = 10;

    private boolean b_Live_Preview = false;
    private static int i_Button_Pref_Width = 120;
    private static int i_Menu_Obj_Pref_Height = 30;
    private static float f_Label_Pref_Width=0;
    private static double d_Button_X_Pos;
    private static double d_Window_Width;
    private static Pic_Modi_Artist Artist;
    private static double pane_width, pane_height;
    private String str_Last_Path;

    // Constructor of the Chooser
    public Pic_Modi_Menu(Pane in_Pane){

        // Did the layout, window, already got created?
        if(!bool_DataPickMenuCreated){

            // Desc. for Input Label
            ThresDescription_Label = new Label();
            str_Last_Path = System.getProperty("user.home");

            // Setup for the B/W CheckBox
            chkbx_SW_Pic = new CheckBox();

            // Slider to pick the Threshold
            Slider_Threshold = new Slider();
            Slider_Threshold.setMax(255.0);
            Slider_Threshold.setMin(0.0);
            Slider_Threshold.setValue(255.0/2);
            Slider_Threshold.setBlockIncrement(0.1f);

            // Event Handler for the B/W Checkbox
            chkbx_SW_Pic.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
                Artist.switch_B_SW_Pic();
                if(bool_HasPic){
                    Artist.Image_Refresh(file_DataVar, b_Live_Preview);
                }
                if(Artist.isB_SW_Pic()){
                    DataGroup_For_FXPane.getChildren().add(lbl_Equal_Hist);
                    DataGroup_For_FXPane.getChildren().add(chkbx_Equal_Hist);
                } else {
                    DataGroup_For_FXPane.getChildren().remove(lbl_Equal_Hist);
                    DataGroup_For_FXPane.getChildren().remove(chkbx_Equal_Hist);
                }
            });

            // Event Handler for the Threshold Slider
            Slider_Threshold.valueProperty().addListener((observable, oldValue, newValue) -> {
                if(bool_HasPic){
                    Artist.Image_Refresh(file_DataVar, b_Live_Preview);
                }
                ThresDescription_Label.setText("Threshold : "+ String.format("%.2f",Slider_Threshold.getValue()));
            });
            // Setup the Text first
            ThresDescription_Label.setText("Threshold : "+ String.format("%.2f",Slider_Threshold.getValue()));

            // ------------------------------------------------------------------------------------------------

            // Desc. for the CheckBox of Live_Preview
            lbl_Live_Preview = new Label("Live Preview");

            // Setup for the CheckBox of Live_Preview
            chkbx_Live_preview = new CheckBox();

            // Event Handler for the Checkbox for Live_Preview
            chkbx_Live_preview.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
                b_Live_Preview = !b_Live_Preview;
                if(bool_HasPic) {
                    Artist.Image_Refresh(file_DataVar, b_Live_Preview);
                }
                if (b_Live_Preview) {
                    DataGroup_For_FXPane.getChildren().remove(Button_Refresh_Pic);
                } else {
                    DataGroup_For_FXPane.getChildren().add(Button_Refresh_Pic);
                }
            });

            // ------------------------------------------------------------------------------------------------

            // Desc. for the CheckBox of Live_Preview
            lbl_Equal_Hist = new Label("Equal. Histogram");

            // Setup for the CheckBox of Live_Preview
            chkbx_Equal_Hist = new CheckBox();

            // Event Handler for the Checkbox for Live_Preview
            chkbx_Equal_Hist.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
                Artist.switch_B_equal_hist();
                if(bool_HasPic) {
                    Artist.Image_Refresh(file_DataVar, b_Live_Preview);
                }
            });

            // ------------------------------------------------------------------------------------------------

            // Desc. for the CheckBox
            CheckBox_Normalise_Label = new Label("Normalise");

            // Setup for the CheckBox
            Chkbx_Normalise = new CheckBox();

            // Event Handler for the Checkbox
            Chkbx_Normalise.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
                Artist.switch_B_Normalise_Picture();
                if(bool_HasPic){
                    Artist.Image_Refresh(file_DataVar, b_Live_Preview);
                }
                if(Artist.isB_Normalise_Picture()){
                    DataGroup_For_FXPane.getChildren().add(Norm_Max_Label);
                    DataGroup_For_FXPane.getChildren().add(Norm_Min_Label);
                    DataGroup_For_FXPane.getChildren().add(Slider_Norm_Min);
                    DataGroup_For_FXPane.getChildren().add(Slider_Norm_Max);
                } else {
                    DataGroup_For_FXPane.getChildren().remove(Norm_Max_Label);
                    DataGroup_For_FXPane.getChildren().remove(Norm_Min_Label);
                    DataGroup_For_FXPane.getChildren().remove(Slider_Norm_Min);
                    DataGroup_For_FXPane.getChildren().remove(Slider_Norm_Max);
                }
            });

            // ------------------------------------------------------------------------------------------------

            // Desc. for Normalise_Min_Value
            Norm_Min_Label = new Label("Min :");

            // Slider to set the Min_Norm Value
            Slider_Norm_Min = new Slider();
            Slider_Norm_Min.setMax(254.0);
            Slider_Norm_Min.setMin(0.0);
            Slider_Norm_Min.setValue(100.0);
            Slider_Norm_Min.setBlockIncrement(0.1f);

            // Event Handler for the Min Norm Slider
            Slider_Norm_Min.valueProperty().addListener((observable, oldValue, newValue) -> {
                if(Slider_Norm_Min.getValue() >= Slider_Norm_Max.getValue()){
                    Slider_Norm_Max.setValue(Slider_Norm_Min.getValue()+1);
                }
                if(bool_HasPic){
                    Artist.Image_Refresh(file_DataVar, b_Live_Preview);
                }
                Norm_Min_Label.setText("Min : "+ String.format("%.2f",Slider_Norm_Min.getValue()));
            });
            Norm_Min_Label.setText("Min : "+ String.format("%.2f",Slider_Norm_Min.getValue()));

            // ------------------------------------------------------------------------------------------------

            // Desc. for Normalise_Max_Value
            Norm_Max_Label = new Label("Max :");
            Norm_Max_Label.setTextFill(Color.YELLOW);

            // Slider to set the Max_Norm Value
            Slider_Norm_Max = new Slider();
            Slider_Norm_Max.setMax(255.0);
            Slider_Norm_Max.setMin(1.0);
            Slider_Norm_Max.setValue(101.0);
            Slider_Norm_Max.setBlockIncrement(0.1f);

            // Event Handler for the Max Norm Slider
            Slider_Norm_Max.valueProperty().addListener((observable, oldValue, newValue) -> {
                if(Slider_Norm_Min.getValue() >= Slider_Norm_Max.getValue()){
                    Slider_Norm_Min.setValue(Slider_Norm_Max.getValue()-1);
                }
                if(bool_HasPic){
                    Artist.Image_Refresh(file_DataVar, b_Live_Preview);
                }
                Norm_Max_Label.setText("Max : "+ String.format("%.2f",Slider_Norm_Max.getValue()));
            });
            Norm_Max_Label.setText("Max : "+ String.format("%.2f",Slider_Norm_Max.getValue()));

            // ------------------------------------------------------------------------------------------------


            /*
                The FileChooser Object gets created.
                After this some restrictions which kind of
                Data is allowed.
            */
            fch_DataChoose = new FileChooser();

            fch_DataChoose.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("*", "*.jpg","*.jpeg","*.JPG","*.JPEG","*.png","*.gif","*.bmp","*.tiff"),
                    new FileChooser.ExtensionFilter("JPG", "*.jpg","*.JPG"),
                    new FileChooser.ExtensionFilter("JPEG", "*.jpeg","*.JPEG"),
                    new FileChooser.ExtensionFilter("PNG", "*.png"),
                    new FileChooser.ExtensionFilter("GIF", "*.gif"),
                    new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                    new FileChooser.ExtensionFilter("TIFF", "*.tiff")
            );

            fch_DataChoose.setInitialDirectory(new File(str_Last_Path));

            // ------------------------------------------------------------------------------------------------

            /*
                Creation, and setup, for the exit button.
             */
            Button_For_Exit = new Button();
            Button_For_Exit.setText("Exit");

            /*
                If clicked -> Exit
             */
            Button_For_Exit.setOnAction(event -> System.exit(0));

            // ------------------------------------------------------------------------------------------------

             /*
                Here the button with the refresh the Image.
                It only appears, if Live Preview is not active
             */
            Button_Refresh_Pic = new Button();
            Button_Refresh_Pic.setText("Refresh");

            /*
                Once clicked, it calls the Just_Modify method with
                the current Data Path, to modify it.
             */
            Button_Refresh_Pic.setOnAction(event -> {
                // Wenn der Button gedrückt wird, erscheint das Dateiauswahlfenster.
                if (bool_HasPic) {
                    Artist.Just_Modify(Imgcodecs.imread(file_DataVar.getAbsolutePath()));
                }
            });

            // ------------------------------------------------------------------------------------------------


             /*
                Here the button with the modify function
                gets created, and arranged in the Pane.
             */
            Button_For_Saving_The_Pic = new Button();
            Button_For_Saving_The_Pic.setText("Save");

            /*
                What happens, if the Modify button gets clicked?
                Well..
                It checks again, if there's a picture available ( Might be
                redundant, but just to make sure. ), and then it starts the
                entire modifying process.
             */
            Button_For_Saving_The_Pic.setOnAction(event -> {

                // Wenn der Button gedrückt wird, erscheint das Dateiauswahlfenster.
                if (!bool_HasPic) {
                    lbl_Feedback.setText("NO Picture!");
                    Platform.runLater(() -> {
                        try {
                            Thread.sleep(1000);
                            lbl_Feedback.setText("");
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } else {
                    if(Artist.Yay(file_DataVar) == 0)
                    {
                        lbl_Feedback.setText("Pic saved!");
                        Platform.runLater(() -> {
                            try {
                                Thread.sleep(1000);
                                lbl_Feedback.setText("");
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    } else {
                        lbl_Feedback.setText("Not saved!");
                        Platform.runLater(() -> {
                            try {
                                Thread.sleep(1000);
                                lbl_Feedback.setText("");
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }
                }
            });

            // ------------------------------------------------------------------------------------------------

            lbl_Feedback = new Label("");
            lbl_Feedback.setTextFill(Color.GREEN);

            // ------------------------------------------------------------------------------------------------

            /*
                The button and boolean, for the File picker.
             */
            bool_HasPic = false;
            Button_PickFile = new Button();
            Button_PickFile.setText("Pick Image");

            /*
                Now we get to the function of the Button, what happens
                when it got clicked?
                Well it opens a window in which the user can pick a file,
                if nothing got chosen, it returns "No file."
                If it has something, it sets the sPath_Label to the
                File-Path, and also remembers the path.
             */
            Button_PickFile.setOnAction(event -> {

                file_DataVar = fch_DataChoose.showOpenDialog(Main.Window_FX_Var);
                lbl_Feedback.setText("");
                if(file_DataVar == null && sPath_Label.getText().equals("NO FILE!!")) {
                    System.out.println("No File!");
                    sPath_Label.setText("NO FILE!!");
                    bool_HasPic = false;
                } else if(file_DataVar == null && !sPath_Label.getText().equals("NO FILE!!")) {
                    file_DataVar = new File(sPath_Label.getText());
                    bool_HasPic = true;
                    Artist.Just_Modify(Imgcodecs.imread(file_DataVar.getAbsolutePath()));
                    Resize_Pic_Place();
                } else {
                    //System.out.println(DataVar);
                    sPath_Label.setText(file_DataVar.toString());
                    sPath_Label.setLayoutX(Button_PickFile.getLayoutX() - d_Window_Width - i_x_equal_spacing);
                    sPath_Label.setLayoutY(Button_PickFile.getLayoutY());

                    str_Last_Path = file_DataVar.getParent();
                    fch_DataChoose.setInitialDirectory(new File(str_Last_Path));

                    Artist.Just_Modify(Imgcodecs.imread(file_DataVar.getAbsolutePath()));
                    bool_HasPic = true;
                    Resize_Pic_Place();
                }
                });

            // ------------------------------------------------------------------------------------------------

            // The input field of how to name the Output File
            txtfld_Save_Name_Giver = new TextField("Save_Name.png");

            // ------------------------------------------------------------------------------------------------

            /*
                Here's the Label which tells the user, if a File got
                chosen, and which file it is.
             */
            sPath_Label = new Label();
            sPath_Label.setText("NO FILE!!");
            sPath_Label.setTextFill(Color.YELLOW);


            // ------------------------------------------------------------------------------------------------
            // All the former created objects are being put into Group.
            DataGroup_For_FXPane = new Group();

            bool_DataPickMenuCreated = true;
        }

        // In the end we add the DataGroup to the Pane.
        in_Pane.getChildren().add(DataGroup_For_FXPane);

    }


    /*
        Just to add the children back into the group.
     */
    private static void children_add(Group in_this_group){
        in_this_group.getChildren().add(Button_PickFile);
        in_this_group.getChildren().add(Button_For_Saving_The_Pic);
        in_this_group.getChildren().add(Button_For_Exit);
        in_this_group.getChildren().add(lbl_Feedback);
        in_this_group.getChildren().add(sPath_Label);
        in_this_group.getChildren().add(Slider_Threshold);
        in_this_group.getChildren().add(txtfld_Save_Name_Giver);
        in_this_group.getChildren().add(ThresDescription_Label);
        in_this_group.getChildren().add(Chkbx_Normalise);
        in_this_group.getChildren().add(CheckBox_Normalise_Label);
        in_this_group.getChildren().add(lbl_Live_Preview);
        in_this_group.getChildren().add(chkbx_Live_preview);
        in_this_group.getChildren().add(Button_Refresh_Pic);
        in_this_group.getChildren().add(chkbx_SW_Pic);
    }

    // Just to remove the children from the group.
    private static void children_remove(Group in_this_group){
        in_this_group.getChildren().remove(Button_PickFile);
        in_this_group.getChildren().remove(Button_For_Saving_The_Pic);
        in_this_group.getChildren().remove(Button_For_Exit);
        in_this_group.getChildren().remove(lbl_Feedback);
        in_this_group.getChildren().remove(sPath_Label);
        in_this_group.getChildren().remove(Slider_Threshold);
        in_this_group.getChildren().remove(txtfld_Save_Name_Giver);
        in_this_group.getChildren().remove(ThresDescription_Label);
        in_this_group.getChildren().remove(Chkbx_Normalise);
        in_this_group.getChildren().remove(CheckBox_Normalise_Label);
        in_this_group.getChildren().remove(lbl_Live_Preview);
        in_this_group.getChildren().remove(chkbx_Live_preview);
        in_this_group.getChildren().remove(Button_Refresh_Pic);
        in_this_group.getChildren().remove(chkbx_SW_Pic);
    }

    // Once the Panel is resized, this Function
    // calls the Functions to refreshe the menu.
    public static void refresh_menu(){
        if(bool_DataPickMenuCreated) {
            children_remove(DataGroup_For_FXPane);
        }
        Window_Is_resized();
        children_add(DataGroup_For_FXPane);
    }

    /*
        Resizes, and relocates, the elements in the window,
        once the window gets resized.
        First time doing something like this.. there might be better ways
     */
    private static void Window_Is_resized(){
        if(pane_width != Screen.getPrimary().getBounds().getWidth()) {
            d_Window_Width = pane_width / 4;
        } else {
            d_Window_Width = pane_width;
        }

        i_Button_Pref_Width = (int)(pane_width*0.1);
        if(i_Button_Pref_Width > 120) {i_Button_Pref_Width = 120;}
        d_Button_X_Pos = pane_width - i_Button_Pref_Width - i_x_equal_spacing;

        f_Y_Pos = i_Menu_Obj_Pref_Height/4.0f;
        f_Label_Pref_Width = (float)(pane_width*0.1);

        if(f_Label_Pref_Width < 115){
            if(f_Label_Pref_Width < 80){
                f_Label_Pref_Width=80;
            } else {
                f_Label_Pref_Width=115;
            }
        }

        // Desc. for Input Label
        ThresDescription_Label.setPrefWidth(f_Label_Pref_Width);
        ThresDescription_Label.setPrefHeight(i_Menu_Obj_Pref_Height);
        ThresDescription_Label.setLayoutX(10);
        ThresDescription_Label.setLayoutY(f_Y_Pos);

        // Setup for the B/W CheckBox
        chkbx_SW_Pic.setPrefWidth(5);
        chkbx_SW_Pic.setPrefHeight(5);
        chkbx_SW_Pic.setLayoutX(ThresDescription_Label.getLayoutX()+f_Label_Pref_Width+i_x_equal_spacing);
        chkbx_SW_Pic.setLayoutY(ThresDescription_Label.getLayoutX()+5);

        // Slider to pick the Threshold
        Slider_Threshold.setPrefWidth(285);
        Slider_Threshold.setPrefHeight(i_Menu_Obj_Pref_Height);
        Slider_Threshold.setLayoutX(chkbx_SW_Pic.getLayoutX()+10+i_x_equal_spacing);
        Slider_Threshold.setLayoutY(f_Y_Pos);

        // ------------------------------------------------------------------------------------------------

        // Desc. for the CheckBox of Live_Preview
        lbl_Live_Preview.setPrefWidth(f_Label_Pref_Width);
        lbl_Live_Preview.setPrefHeight(i_Menu_Obj_Pref_Height);
        lbl_Live_Preview.setLayoutX(Slider_Threshold.getLayoutX()+285+i_x_equal_spacing);
        lbl_Live_Preview.setLayoutY(Slider_Threshold.getLayoutY());

        // Setup for the CheckBox of Live_Preview
        chkbx_Live_preview.setPrefWidth(5);
        chkbx_Live_preview.setPrefHeight(5);
        chkbx_Live_preview.setLayoutX(lbl_Live_Preview.getLayoutX()+f_Label_Pref_Width+i_x_equal_spacing-10);
        chkbx_Live_preview.setLayoutY(chkbx_SW_Pic.getLayoutY());

        // ------------------------------------------------------------------------------------------------

        // Desc. for the CheckBox of Live_Preview
        lbl_Equal_Hist.setPrefWidth(f_Label_Pref_Width);
        lbl_Equal_Hist.setPrefHeight(i_Menu_Obj_Pref_Height);
        lbl_Equal_Hist.setLayoutX(lbl_Live_Preview.getLayoutX());
        lbl_Equal_Hist.setLayoutY(lbl_Live_Preview.getLayoutY()+i_Menu_Obj_Pref_Height+i_y_equal_spacing);

        // Setup for the CheckBox of Live_Preview
        chkbx_Equal_Hist.setPrefWidth(5);
        chkbx_Equal_Hist.setPrefHeight(5);
        chkbx_Equal_Hist.setLayoutX(lbl_Equal_Hist.getLayoutX()+f_Label_Pref_Width+i_x_equal_spacing-10);
        chkbx_Equal_Hist.setLayoutY(lbl_Equal_Hist.getLayoutY()+5);

        // ------------------------------------------------------------------------------------------------

        // Desc. for the CheckBox
        CheckBox_Normalise_Label.setPrefWidth(f_Label_Pref_Width);
        CheckBox_Normalise_Label.setPrefHeight(i_Menu_Obj_Pref_Height);
        CheckBox_Normalise_Label.setLayoutX(10);
        CheckBox_Normalise_Label.setLayoutY(f_Y_Pos +i_Menu_Obj_Pref_Height+i_y_equal_spacing);

        // Setup for the CheckBox
        Chkbx_Normalise.setPrefWidth(5);
        Chkbx_Normalise.setPrefHeight(5);
        Chkbx_Normalise.setLayoutX(CheckBox_Normalise_Label.getLayoutX()+f_Label_Pref_Width+i_x_equal_spacing);
        Chkbx_Normalise.setLayoutY(CheckBox_Normalise_Label.getLayoutY()+5);

        // ------------------------------------------------------------------------------------------------

        // Desc. for Normalise_Min_Value
        Norm_Min_Label.setPrefWidth(100);
        Norm_Min_Label.setPrefHeight(i_Menu_Obj_Pref_Height);
        Norm_Min_Label.setLayoutX(Chkbx_Normalise.getLayoutX()+15+i_x_equal_spacing);
        Norm_Min_Label.setLayoutY(f_Y_Pos +30);

        // Slider to set the Min_Norm Value
        Slider_Norm_Min.setPrefWidth(170);
        Slider_Norm_Min.setLayoutX(Norm_Min_Label.getLayoutX()+100+i_x_equal_spacing);
        Slider_Norm_Min.setLayoutY(Norm_Min_Label.getLayoutY()+5);

        // Event Handler for the Min Norm Slider

        // ------------------------------------------------------------------------------------------------

        // Desc. for Normalise_Max_Value
        Norm_Max_Label.setPrefWidth(100);
        Norm_Max_Label.setPrefHeight(i_Menu_Obj_Pref_Height);
        Norm_Max_Label.setLayoutX(Norm_Min_Label.getLayoutX());
        Norm_Max_Label.setLayoutY(Norm_Min_Label.getLayoutY()+i_Menu_Obj_Pref_Height);

        // Slider to set the Max_Norm Value
        Slider_Norm_Max.setPrefWidth(170);
        Slider_Norm_Max.setLayoutX(Norm_Max_Label.getLayoutX()+100+i_x_equal_spacing);
        Slider_Norm_Max.setLayoutY(Norm_Max_Label.getLayoutY()+5);

        // ------------------------------------------------------------------------------------------------

            /*
                Creation, and setup, for the exit button.
             */
        Button_For_Exit.setPrefWidth(i_Button_Pref_Width);
        Button_For_Exit.setPrefHeight(i_Menu_Obj_Pref_Height);
        Button_For_Exit.setLayoutX(d_Button_X_Pos);
        Button_For_Exit.setLayoutY(f_Y_Pos +(4* i_Menu_Obj_Pref_Height)+i_x_equal_spacing);

        // ------------------------------------------------------------------------------------------------

            /*
                Creation, and setup, for the Feedback Label.
             */

        lbl_Feedback.setPrefWidth(f_Label_Pref_Width);
        lbl_Feedback.setPrefHeight(i_Menu_Obj_Pref_Height);
        lbl_Feedback.setLayoutX(d_Button_X_Pos);
        lbl_Feedback.setLayoutY(Button_For_Exit.getLayoutY() + 2* i_Menu_Obj_Pref_Height -i_x_equal_spacing);

        // ------------------------------------------------------------------------------------------------

             /*
                Here the button with the refresh the Image.
                It only appears, if Live Preview is not active
             */
        Button_Refresh_Pic.setPrefWidth(i_Button_Pref_Width);
        Button_Refresh_Pic.setPrefHeight(i_Menu_Obj_Pref_Height);
        Button_Refresh_Pic.setLayoutX(d_Button_X_Pos);
        Button_Refresh_Pic.setLayoutY(Button_For_Exit.getLayoutY()- i_Menu_Obj_Pref_Height -i_x_equal_spacing);

        // ------------------------------------------------------------------------------------------------


             /*
                Here the button with the modify function
                gets created, and arranged in the Pane.
             */
        Button_For_Saving_The_Pic.setPrefWidth(i_Button_Pref_Width);
        Button_For_Saving_The_Pic.setPrefHeight(i_Menu_Obj_Pref_Height);
        Button_For_Saving_The_Pic.setLayoutX(d_Button_X_Pos);
        Button_For_Saving_The_Pic.setLayoutY(Button_Refresh_Pic.getLayoutY()- i_Menu_Obj_Pref_Height -i_x_equal_spacing);

        // ------------------------------------------------------------------------------------------------

            /*
                The button and boolean, for the File picker.
             */
        Button_PickFile.setPrefWidth(i_Button_Pref_Width);
        Button_PickFile.setPrefHeight(i_Menu_Obj_Pref_Height);
        Button_PickFile.setLayoutX(d_Button_X_Pos);
        Button_PickFile.setLayoutY(Button_For_Saving_The_Pic.getLayoutY()- i_Menu_Obj_Pref_Height -i_x_equal_spacing);

            /*
                Now we get to the function of the Button, what happens
                when it got clicked?
                Well it opens a window in which the user can pick a file,
                if nothing got chosen, it returns "No file."
                If it has something, it sets the sPath_Label to the
                File-Path, and also remembers the path.
             */
            if(file_DataVar == null && sPath_Label.getText().equals("NO FILE!!")) {
                sPath_Label.setText("NO FILE!!");
                bool_HasPic = false;
            } else if(file_DataVar == null && !sPath_Label.getText().equals("NO FILE!!")) {
                Resize_Pic_Place();
            } else {
                sPath_Label.setText(file_DataVar.toString());
                sPath_Label.setLayoutX(Button_PickFile.getLayoutX() - d_Window_Width - i_x_equal_spacing);
                sPath_Label.setLayoutY(Button_PickFile.getLayoutY());
                Resize_Pic_Place();
            }

        // ------------------------------------------------------------------------------------------------

        // The input field of how to name the Output File
        txtfld_Save_Name_Giver.setPrefWidth(3*i_Button_Pref_Width);
        txtfld_Save_Name_Giver.setPrefHeight(i_Menu_Obj_Pref_Height);
        txtfld_Save_Name_Giver.setLayoutX(Button_PickFile.getLayoutX() - (3*i_Button_Pref_Width) - i_x_equal_spacing);
        txtfld_Save_Name_Giver.setLayoutY(Button_For_Saving_The_Pic.getLayoutY());

        // ------------------------------------------------------------------------------------------------

            /*
                Here's the Label which tells the user, if a File got
                chosen, and which file it is.
             */
        sPath_Label.setPrefWidth(3*i_Button_Pref_Width);
        sPath_Label.setPrefHeight(i_Menu_Obj_Pref_Height);
        sPath_Label.setLayoutX(Button_PickFile.getLayoutX() - (3*i_Button_Pref_Width) - i_x_equal_spacing);
        sPath_Label.setLayoutY(Button_PickFile.getLayoutY());
    }

    /*
        A short Function which just resizes the Picture
     */
    private static void Resize_Pic_Place(){
        if(bool_HasPic) {
            Artist.getShow_Image().setFitHeight(pane_height - Artist.getShow_Image().getY() - 20);
            Artist.getShow_Image().setFitWidth(pane_width - 40 - i_Button_Pref_Width);
        }
    }

    public void setArtist(Pane in_Pane, Pic_Modi_Artist arti){
        Artist = arti;
        Artist.setScene_pane(in_Pane);
        Artist.setChild_group(DataGroup_For_FXPane);
        Artist.setSld_thres(Slider_Threshold);
        Artist.setSld_norm_min(Slider_Norm_Min);
        Artist.setSld_norm_max(Slider_Norm_Max);
        Artist.setTxt_info(txtfld_Save_Name_Giver);
        Artist.setI_sub_from_imgview(i_Button_Pref_Width);
    }
    public static void new_Pane_height(double new_Val){
        pane_height = new_Val;
    }
    public static void new_Pane_width(double new_Val){
        pane_width = new_Val;
    }
    public static int getI_x_equal_spacing(){
        return i_x_equal_spacing;
    }
}
