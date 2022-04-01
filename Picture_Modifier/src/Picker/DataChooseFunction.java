package Picker;


// Imported Libraries

import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Locale;

import static Picker.Main.Scene_FX_Pane;


public class DataChooseFunction {

    // Some variables, IntelliJ might gonna nag of some,
    // but that's okay.

    private static boolean b_DataPickCriteria = false;
    private static Group DataGroup_For_FXPane;
    private static FileChooser DataChoose;
    private static File DataVar;
    private static Button Button_PickFile, Button_For_Saving_The_Pic, Button_For_Exit, Button_Refresh_Pic;
    private static Label sPath_Label, ThresDescription_Label, CheckBox_Label, Norm_Min_Label, Norm_Max_Label, NameDesc_Label, lbl_Live_Preview, lbl_Equal_Hist;
    private static boolean HasPic;
    private static boolean b_Is_ImageView_Setup = false;
    private static boolean b_Normalise_Picture = false;
    private static boolean b_Live_Preview = false;
    private static boolean b_SW_Pic = false;
    private static boolean b_equal_hist = false;

    private static InputStream Stream_Input;
    private static ImageView Show_Image;

    public static TextField NameGiver_txtField;
    public static Slider Slider_Threshold, Slider_Norm_Min, Slider_Norm_Max;
    private static CheckBox Chkbx_Normalise, chkbx_Live_preview, chkbx_SW_Pic, chkbx_Equal_Hist;
    private static double NameGiver_txtField_Width;
    private static int Y_Pos = 20;

    // Constructor of the Chooser
    public DataChooseFunction(){

        // Did the layout, window, already got created?
        if(!b_DataPickCriteria){

            NameGiver_txtField_Width = Scene_FX_Pane.getWidth()/4;

            // Desc. for Input Label
            ThresDescription_Label = new Label();
            ThresDescription_Label.setPrefWidth(145);
            ThresDescription_Label.setLayoutX(10);
            ThresDescription_Label.setLayoutY(Y_Pos);

            // Setup for the B/W CheckBox
            chkbx_SW_Pic = new CheckBox();
            chkbx_SW_Pic.setLayoutX(ThresDescription_Label.getLayoutX()+145);
            chkbx_SW_Pic.setLayoutY(Y_Pos);
            chkbx_SW_Pic.setPrefWidth(5);
            chkbx_SW_Pic.setPrefHeight(5);

            // Slider to pick the Threshold
            Slider_Threshold = new Slider();
            Slider_Threshold.setPrefWidth(285);
            Slider_Threshold.setLayoutX(chkbx_SW_Pic.getLayoutX()+30);
            Slider_Threshold.setLayoutY(Y_Pos);
            Slider_Threshold.setMax(255.0);
            Slider_Threshold.setMin(0.0);
            Slider_Threshold.setValue(255.0/2);
            Slider_Threshold.setBlockIncrement(0.1f);

            // Event Handler for the B/W Checkbox
            chkbx_SW_Pic.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
                b_SW_Pic = !b_SW_Pic;
                if(HasPic){
                    Image_Refresh(DataVar);
                }
                if(b_SW_Pic){
                    DataGroup_For_FXPane.getChildren().add(lbl_Equal_Hist);
                    DataGroup_For_FXPane.getChildren().add(chkbx_Equal_Hist);
                } else {
                    DataGroup_For_FXPane.getChildren().remove(lbl_Equal_Hist);
                    DataGroup_For_FXPane.getChildren().remove(chkbx_Equal_Hist);
                }
            });

            // Event Handler for the Threshold Slider
            Slider_Threshold.valueProperty().addListener((observable, oldValue, newValue) -> {
                if(HasPic){
                    Image_Refresh(DataVar);
                }
                ThresDescription_Label.setText("Threshold : "+ String.format("%.2f",Slider_Threshold.getValue()));
            });
            // Setup the Text first
            ThresDescription_Label.setText("Threshold : "+ String.format("%.2f",Slider_Threshold.getValue()));

            // ------------------------------------------------------------------------------------------------

            // ------------------------------------------------------------------------------------------------

            // Desc. for the CheckBox
            CheckBox_Label = new Label("Normalise");
            CheckBox_Label.setPrefWidth(80);
            CheckBox_Label.setLayoutX(10);
            CheckBox_Label.setLayoutY(Y_Pos+30);

            // Setup for the CheckBox
            Chkbx_Normalise = new CheckBox();
            Chkbx_Normalise.setLayoutX(90);
            Chkbx_Normalise.setLayoutY(Y_Pos+30);
            Chkbx_Normalise.setPrefWidth(5);
            Chkbx_Normalise.setPrefHeight(5);

            // Event Handler for the Checkbox
            Chkbx_Normalise.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
                b_Normalise_Picture = !b_Normalise_Picture;
                if(HasPic){
                    Image_Refresh(DataVar);
                }
                if(b_Normalise_Picture){
                    DataGroup_For_FXPane.getChildren().add(Norm_Max_Label);
                    DataGroup_For_FXPane.getChildren().add(Norm_Min_Label);
                    DataGroup_For_FXPane.getChildren().add(Slider_Norm_Min);
                    DataGroup_For_FXPane.getChildren().add(Slider_Norm_Max);
                    lbl_Live_Preview.setLayoutX(Slider_Norm_Max.getLayoutX()+190);
                    chkbx_Live_preview.setLayoutX(lbl_Live_Preview.getLayoutX()+100);
                    if(b_SW_Pic) {
                        lbl_Equal_Hist.setLayoutX(chkbx_Live_preview.getLayoutX() + 40);
                        chkbx_Equal_Hist.setLayoutX(lbl_Equal_Hist.getLayoutX() + 150);
                    }
                } else {
                    DataGroup_For_FXPane.getChildren().remove(Norm_Max_Label);
                    DataGroup_For_FXPane.getChildren().remove(Norm_Min_Label);
                    DataGroup_For_FXPane.getChildren().remove(Slider_Norm_Min);
                    DataGroup_For_FXPane.getChildren().remove(Slider_Norm_Max);
                    lbl_Live_Preview.setLayoutX(Chkbx_Normalise.getLayoutX()+40);
                    chkbx_Live_preview.setLayoutX(lbl_Live_Preview.getLayoutX()+100);
                    if(b_SW_Pic) {
                        lbl_Equal_Hist.setLayoutX(chkbx_Live_preview.getLayoutX()+40);
                        chkbx_Equal_Hist.setLayoutX(lbl_Equal_Hist.getLayoutX()+150);
                    }
                }
            });

            // ------------------------------------------------------------------------------------------------

            // Desc. for Normalise_Min_Value
            Norm_Min_Label = new Label("Min :");
            Norm_Min_Label.setPrefWidth(100);
            Norm_Min_Label.setLayoutX(130);
            Norm_Min_Label.setLayoutY(Y_Pos+30);

            // Slider to set the Min_Norm Value
            Slider_Norm_Min = new Slider();
            Slider_Norm_Min.setPrefWidth(170);
            Slider_Norm_Min.setLayoutX(225);
            Slider_Norm_Min.setLayoutY(Y_Pos+30);
            Slider_Norm_Min.setMax(254.0);
            Slider_Norm_Min.setMin(0.0);
            Slider_Norm_Min.setValue(100.0);
            Slider_Norm_Min.setBlockIncrement(0.1f);

            // Event Handler for the Min Norm Slider
            Slider_Norm_Min.valueProperty().addListener((observable, oldValue, newValue) -> {
                if(Slider_Norm_Min.getValue() >= Slider_Norm_Max.getValue()){
                    Slider_Norm_Max.setValue(Slider_Norm_Min.getValue()+1);
                }
                if(HasPic){
                    Image_Refresh(DataVar);
                }
                Norm_Min_Label.setText("Min : "+ String.format("%.2f",Slider_Norm_Min.getValue()));
            });
            Norm_Min_Label.setText("Min : "+ String.format("%.2f",Slider_Norm_Min.getValue()));

            // ------------------------------------------------------------------------------------------------

            // Desc. for Normalise_Max_Value
            Norm_Max_Label = new Label("Max :");
            Norm_Max_Label.setPrefWidth(100);
            Norm_Max_Label.setLayoutX(400);
            Norm_Max_Label.setLayoutY(Y_Pos+30);
            Norm_Max_Label.setTextFill(Color.YELLOW);

            // Slider to set the Max_Norm Value
            Slider_Norm_Max = new Slider();
            Slider_Norm_Max.setPrefWidth(170);
            Slider_Norm_Max.setLayoutX(500);
            Slider_Norm_Max.setLayoutY(Y_Pos+30);
            Slider_Norm_Max.setMax(255.0);
            Slider_Norm_Max.setMin(1.0);
            Slider_Norm_Max.setValue(101.0);
            Slider_Norm_Max.setBlockIncrement(0.1f);

            // Event Handler for the Max Norm Slider
            Slider_Norm_Max.valueProperty().addListener((observable, oldValue, newValue) -> {
                if(Slider_Norm_Min.getValue() >= Slider_Norm_Max.getValue()){
                    Slider_Norm_Min.setValue(Slider_Norm_Max.getValue()-1);
                }
                if(HasPic){
                    Image_Refresh(DataVar);
                }
                Norm_Max_Label.setText("Max : "+ String.format("%.2f",Slider_Norm_Max.getValue()));
            });
            Norm_Max_Label.setText("Max : "+ String.format("%.2f",Slider_Norm_Max.getValue()));

            // ------------------------------------------------------------------------------------------------

            // Desc. for the CheckBox of Live_Preview
            lbl_Live_Preview = new Label("Live Preview");
            lbl_Live_Preview.setPrefWidth(100);
            lbl_Live_Preview.setLayoutX(Chkbx_Normalise.getLayoutX()+40);
            lbl_Live_Preview.setLayoutY(Y_Pos+30);

            // Setup for the CheckBox of Live_Preview
            chkbx_Live_preview = new CheckBox();
            chkbx_Live_preview.setLayoutX(lbl_Live_Preview.getLayoutX()+100);
            chkbx_Live_preview.setLayoutY(Y_Pos+30);
            chkbx_Live_preview.setPrefWidth(5);
            chkbx_Live_preview.setPrefHeight(5);

            // Event Handler for the Checkbox for Live_Preview
            chkbx_Live_preview.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
                b_Live_Preview = !b_Live_Preview;
                if(HasPic) {
                    Image_Refresh(DataVar);
                }
                if (b_Live_Preview) {
                    DataGroup_For_FXPane.getChildren().remove(Button_Refresh_Pic);
                } else {
                    DataGroup_For_FXPane.getChildren().add(Button_Refresh_Pic);
                }
            });
            // ------------------------------------------------------------------------------------------------

            // The input field of how to name the Output File
            NameGiver_txtField = new TextField("Test.png");
            NameGiver_txtField.setPrefWidth(NameGiver_txtField_Width);
            NameGiver_txtField.setLayoutX(Scene_FX_Pane.getWidth()/2-NameGiver_txtField_Width/2);
            NameGiver_txtField.setLayoutY(Y_Pos-5);

            // Desc. for Save-Name Label
            NameDesc_Label = new Label("Save Name");
            NameDesc_Label.setPrefWidth(95);
            NameDesc_Label.setLayoutY(Y_Pos);
            NameDesc_Label.setLayoutX(NameGiver_txtField.getLayoutX()-115);

            // ------------------------------------------------------------------------------------------------

            /*
                The FileChooser Object gets created.
                After this some restrictions which kind of
                Data is allowed.
            */
            DataChoose = new FileChooser();

            DataChoose.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("*", "*.jpg","*.jpeg","*.JPG","*.JPEG","*.png","*.gif","*.bmp","*.tiff"),
                    new FileChooser.ExtensionFilter("JPG", "*.jpg","*.JPG"),
                    new FileChooser.ExtensionFilter("JPEG", "*.jpeg","*.JPEG"),
                    new FileChooser.ExtensionFilter("PNG", "*.png"),
                    new FileChooser.ExtensionFilter("GIF", "*.gif"),
                    new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                    new FileChooser.ExtensionFilter("TIFF", "*.tiff")
            );

            DataChoose.setInitialDirectory(new File(System.getProperty("user.home")));


            /*
                Here's the Label which tells the user, if a File got
                chosen, and which file it is.
             */
            sPath_Label = new Label();
            sPath_Label.setText("NO FILE!!");
            sPath_Label.setTextFill(Color.YELLOW);
            sPath_Label.setLayoutX(10);
            sPath_Label.setLayoutY(Y_Pos+60);

            // ------------------------------------------------------------------------------------------------

            /*
                The button, and boolean, for the File picker.
             */
            HasPic = false;
            Button_PickFile = new Button();
            Button_PickFile.setText("Pick Image");
            Button_PickFile.setPrefWidth(130);
            Button_PickFile.setLayoutX((NameGiver_txtField.getLayoutX()+NameGiver_txtField_Width+20));
            Button_PickFile.setLayoutY(Y_Pos-5);

            /*
                Now we get to the function of the Button, what happens
                when it got clicked?
                Well it opens a window in which the user can pick a file,
                if nothing got chosen, it returns "No file."
                If it has something, it sets the sPath_Label to the
                File-Path, and also remembers the path.
             */
            Button_PickFile.setOnAction(event -> {

                DataVar = DataChoose.showOpenDialog(Main.Window_FX_Var);
                if(DataVar == null && sPath_Label.getText().equals("NO FILE!!")) {
                    System.out.println("No File!");
                    sPath_Label.setText("NO FILE!!");
                    HasPic = false;
                } else if(DataVar == null && !sPath_Label.getText().equals("NO FILE!!")) {
                    DataVar = new File(sPath_Label.getText());
                    HasPic = true;
                    Just_Modify(Imgcodecs.imread(DataVar.getAbsolutePath()));
                    Resize_Pic_Place();
                } else {
                    //System.out.println(DataVar);
                    sPath_Label.setText(DataVar.toString());
                    sPath_Label.setLayoutX(10);
                    Just_Modify(Imgcodecs.imread(DataVar.getAbsolutePath()));
                    HasPic = true;
                    Resize_Pic_Place();
                }
                });

            // ------------------------------------------------------------------------------------------------

            /*
                Here the button with the modify function
                gets created, and arranged in the Pane.
             */
            Button_For_Saving_The_Pic = new Button();
            Button_For_Saving_The_Pic.setText("Save");
            Button_For_Saving_The_Pic.setPrefWidth(100);
            Button_For_Saving_The_Pic.setLayoutX(Scene_FX_Pane.getWidth()-240);
            Button_For_Saving_The_Pic.setLayoutY(Y_Pos-5);

            /*
                What happens, if the Modify button gets clicked?
                Well..
                It checks again, if there's a picture available ( Might be
                redundant, but just to make sure. ), and then it starts the
                entire modifying process.
             */
            Button_For_Saving_The_Pic.setOnAction(event -> {

                // Wenn der Button gedrückt wird, erscheint das Dateiauswahlfenster.
                if (!HasPic) {
                    System.out.println("No Pic.");
                } else {
                    Yay(DataVar);
                }
            });

            // ------------------------------------------------------------------------------------------------

            /*
                Creation, and setup, for the exit button.
             */
            Button_For_Exit = new Button();
            Button_For_Exit.setText("Exit");
            Button_For_Exit.setPrefWidth(100);
            Button_For_Exit.setLayoutX(Scene_FX_Pane.getWidth()-120);
            Button_For_Exit.setLayoutY(Y_Pos-5);

            /*
                If clicked -> Exit
             */
            Button_For_Exit.setOnAction(event -> System.exit(0));

             /*
                Here the button with the refresh the Image.
                It only appears, if Live Preview is not active
             */
            Button_Refresh_Pic = new Button();
            Button_Refresh_Pic.setText("Refresh");
            Button_Refresh_Pic.setPrefWidth(100);
            Button_Refresh_Pic.setLayoutX(Button_PickFile.getLayoutX()+150);
            Button_Refresh_Pic.setLayoutY(Y_Pos-5);

            /*
                Once clicked, it calls the Just_Modify method with
                the current Data Path, to modify it.
             */
            Button_Refresh_Pic.setOnAction(event -> {
                // Wenn der Button gedrückt wird, erscheint das Dateiauswahlfenster.
                if (HasPic) {
                    Just_Modify(Imgcodecs.imread(DataVar.getAbsolutePath()));
                }
            });
            // ------------------------------------------------------------------------------------------------

            // Desc. for the CheckBox of Live_Preview
            lbl_Equal_Hist = new Label("Equalise Histogram");
            lbl_Equal_Hist.setPrefWidth(145);
            lbl_Equal_Hist.setLayoutX(chkbx_Live_preview.getLayoutX()+40);
            lbl_Equal_Hist.setLayoutY(Y_Pos+30);

            // Setup for the CheckBox of Live_Preview
            chkbx_Equal_Hist = new CheckBox();
            chkbx_Equal_Hist.setLayoutX(lbl_Equal_Hist.getLayoutX()+150);
            chkbx_Equal_Hist.setLayoutY(Y_Pos+30);
            chkbx_Equal_Hist.setPrefWidth(5);
            chkbx_Equal_Hist.setPrefHeight(5);

            // Event Handler for the Checkbox for Live_Preview
            chkbx_Equal_Hist.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
                b_equal_hist = !b_equal_hist;
                if(HasPic) {
                    Image_Refresh(DataVar);
                }
            });

            // ------------------------------------------------------------------------------------------------

            // All the former created objects are being put into Group.
            DataGroup_For_FXPane = new Group();
            DataGroup_For_FXPane.getChildren().add(Button_PickFile);
            DataGroup_For_FXPane.getChildren().add(Button_For_Saving_The_Pic);
            DataGroup_For_FXPane.getChildren().add(Button_For_Exit);
            DataGroup_For_FXPane.getChildren().add(sPath_Label);
            DataGroup_For_FXPane.getChildren().add(Slider_Threshold);
            DataGroup_For_FXPane.getChildren().add(NameGiver_txtField);
            DataGroup_For_FXPane.getChildren().add(ThresDescription_Label);
            DataGroup_For_FXPane.getChildren().add(NameDesc_Label);
            DataGroup_For_FXPane.getChildren().add(Chkbx_Normalise);
            DataGroup_For_FXPane.getChildren().add(CheckBox_Label);
            DataGroup_For_FXPane.getChildren().add(lbl_Live_Preview);
            DataGroup_For_FXPane.getChildren().add(chkbx_Live_preview);
            DataGroup_For_FXPane.getChildren().add(Button_Refresh_Pic);
            DataGroup_For_FXPane.getChildren().add(chkbx_SW_Pic);

            b_DataPickCriteria = true;
        }

        // In the end we add the DataGroup to the Pane.
        Scene_FX_Pane.getChildren().add(DataGroup_For_FXPane);


    }

    /*
        The Yay Method! ( I keep calling them Functions )
        The Method reads the File into the TestMat and checks
        if the TestMat is empty ( Just to make sure there's no
        NULL-Mat )
        Then it starts the Modify_And_Save method.

        Why 'Yay'? Well, for some reason it made me go "Yay" after
        I made it work.
     */
    private static void Yay(File Data){
        Mat TestMat;
        TestMat = Imgcodecs.imread(Data.getAbsolutePath());
        if(!TestMat.empty()){
            Just_Modify(TestMat);
            Save_Modified_Image(TestMat);
        }
    }
    /*
        Copy of Yay, just for Threshold refresh
    */
    private static void Image_Refresh(File Data){
        if(b_Live_Preview) {
            Mat TestMat;
            TestMat = Imgcodecs.imread(Data.getAbsolutePath());
            if (!TestMat.empty()) {
                Just_Modify(TestMat);
            }
        }
    }

    /*
    At first a new Mat is being created.
    Then we set the SaveFileName via testing the String in the NameGiver
    Input field, for format error. BUT.. the output file will always be PNG!

    Afterwards the input-image is being processed and being turned from a
    colour picture into a monochrome picture, in which the threshold
    is being taken to account.
    */
    private static void Just_Modify(Mat GrayMe){

        Mat BufferMat = new Mat();
        String String_to_File = StringTEST_And_Prepare("TempPic");

        if(b_Normalise_Picture){
            Normalise_The_Mat(GrayMe).copyTo(GrayMe);
        }

        if(b_SW_Pic) {
            Imgproc.cvtColor(GrayMe, BufferMat, Imgproc.COLOR_BGR2GRAY);
            if(b_equal_hist){
                Equal_The_Mat(BufferMat).copyTo(BufferMat);
            }
            Imgproc.threshold(BufferMat, GrayMe, Slider_Threshold.getValue(), 255, Imgproc.THRESH_BINARY);
        }
        Imgcodecs.imwrite(String_to_File, GrayMe);
        Image_Preview_Set_Refresh(String_to_File);
    }

    /*
        This Method is for re-freshing, and setting, the imageview.
        Input is the File
     */

    private static void Image_Preview_Set_Refresh(String Data) {
        try {
            Stream_Input = new FileInputStream(Data);
            if (!b_Is_ImageView_Setup) {
                Show_Image = new ImageView();
                Show_Image.setX(20);
                Show_Image.setY(100);
                Show_Image.setFitHeight(Scene_FX_Pane.getHeight()-Show_Image.getY()-20);
                Show_Image.setFitWidth(Scene_FX_Pane.getWidth()-40);
                Show_Image.setPreserveRatio(true);
                b_Is_ImageView_Setup = true;
                DataGroup_For_FXPane.getChildren().add(Show_Image);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        DataGroup_For_FXPane.getChildren().remove(Show_Image);
        Image displayed_Image = new Image(Stream_Input);
        Show_Image.setImage(displayed_Image);
        DataGroup_For_FXPane.getChildren().add(Show_Image);
    }

    /*
        Resizes, and relocates, the elements in the window,
        once the window gets resized.
     */
    public static void Window_Is_resized(){
        NameGiver_txtField_Width = Scene_FX_Pane.getWidth()/4;

        Button_For_Exit.setLayoutX(Scene_FX_Pane.getWidth()-120);
        Button_For_Saving_The_Pic.setLayoutX(Scene_FX_Pane.getWidth()-240);
        Button_PickFile.setLayoutX((NameGiver_txtField.getLayoutX()+NameGiver_txtField_Width+20));
        Button_Refresh_Pic.setLayoutX(Button_PickFile.getLayoutX()+150);

        NameGiver_txtField.setPrefWidth(NameGiver_txtField_Width);
        NameGiver_txtField.setLayoutX(Scene_FX_Pane.getWidth()/2-NameGiver_txtField_Width/2);
        NameDesc_Label.setLayoutX(NameGiver_txtField.getLayoutX()-115);
        Resize_Pic_Place();
    }

    /*
        A short Function which just resizes the Picture
     */
    public static void Resize_Pic_Place(){
        if(HasPic) {
            Show_Image.setFitHeight(Scene_FX_Pane.getHeight() - Show_Image.getY() - 20);
            Show_Image.setFitWidth(Scene_FX_Pane.getWidth() - 40);
        }
    }


    /*
        Method for Normalise the Picture
     */
    private static Mat Normalise_The_Mat(Mat Mat_To_Norm){
        Mat BufferMat = new Mat();
        Core.normalize(Mat_To_Norm, BufferMat,Slider_Norm_Min.getValue(), Slider_Norm_Max.getValue(), Core.NORM_MINMAX);
        return BufferMat;
    }

    /*
        Method for Equalise the Histogram the Picture
     */
    private static Mat Equal_The_Mat(Mat Mat_To_Norm){
        Mat BufferMat = new Mat();
        Imgproc.equalizeHist(Mat_To_Norm,BufferMat);
        return BufferMat;
    }

    /*
        Saves the modified image.
     */
    private static void Save_Modified_Image(Mat GrayMe){
        String SaveFileName = NameGiver_txtField.getText();
        if(!SaveFileName.toUpperCase(Locale.ROOT).contains("TEMPPIC")) {
            SaveFileName = StringTEST_And_Prepare(NameGiver_txtField.getText());
        } else {
            NameGiver_txtField.setText("TempPic_as_name_Not_allowed.png");
            SaveFileName = StringTEST_And_Prepare(NameGiver_txtField.getText());
        }

        Imgcodecs.imwrite(SaveFileName, GrayMe);
        Image_Preview_Set_Refresh(SaveFileName);
    }

    /*
        A Method which modifies the savefile name.
        How does it do that?
        Well, let's say the input strung is "Test.txt", the
        ZumTest checks if a . is present, if so it takes the "Test"
        String and adds .png at the end. ( Outcome : Test.png )

        If the input is only "Test" then it automatically adds .png at the end.
     */
    private static String StringTEST_And_Prepare(String ZumTest){
        if(ZumTest.contains(".")){
            String[] Buffer = ZumTest.split("\\.");
            ZumTest = Buffer[0].concat(".png");
        } else {
            ZumTest = ZumTest.concat(".png");
        }
        return ZumTest;
    }
}
