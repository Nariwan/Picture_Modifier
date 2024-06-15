package PicModi;

import javafx.scene.Group;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;

public class Pic_Modi_Artist {

    private static Pane scene_pane;
    private static Group child_group;

    private static boolean b_Normalise_Picture = false;
    private static boolean b_SW_Pic = false;
    private static boolean b_equal_hist = false;
    private static boolean b_Is_ImageView_Setup = false;
    private static ImageView imview_Show_Image;

    private static Slider sld_thres, sld_norm_min, sld_norm_max;
    private static TextField txt_info;

    private static int i_sub_from_imgview = 0;
    private static Window win_Artplace;
    private String str_Last_Folder_Path;

    public Pic_Modi_Artist(Window in_stage){
        win_Artplace = in_stage;
        str_Last_Folder_Path = System.getProperty("user.home");
    }

    /*
                The Yay Function!
                ( I'll keep calling them Functions, even if they're
                 called Methods, I know.. )
                The Function reads the File into the TestMat and checks
                if the TestMat is empty ( Just to make sure there's no
                NULL-Mat )
                Then it starts the Modify_And_Save method.

                Why 'Yay'? Well, for some reason it made me go "Yay" after
                I made it work.
             */
    public int Yay(File Data){
        Mat TestMat;
        TestMat = Imgcodecs.imread(Data.getAbsolutePath());
        if(!TestMat.empty()){
            Just_Modify(TestMat);
            Save_Modified_Image(TestMat);
            return 0;
        }
        return 1;
    }
    /*
        Copy of Yay, just for Threshold refresh
    */
    public void Image_Refresh(File Data, boolean Preview){
        if(Preview) {
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
    public void Just_Modify(Mat GrayMe){

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
            Imgproc.threshold(BufferMat, GrayMe, sld_thres.getValue(), 255, Imgproc.THRESH_BINARY);
        }
        Imgcodecs.imwrite(String_to_File, GrayMe);
        Image_Preview_Set_Refresh(String_to_File);
    }

    /*
        This Function is for re-freshing, and setting, the imageview.
        Input is the File
     */

    public void Image_Preview_Set_Refresh(String Data) {
        try {
            InputStream fs_instream = new FileInputStream(Data);

            if (!b_Is_ImageView_Setup) {
                imview_Show_Image = new ImageView();
                imview_Show_Image.setX(20);
                imview_Show_Image.setY(100);
                imview_Show_Image.setFitHeight(scene_pane.getHeight()- imview_Show_Image.getY()-20);
                imview_Show_Image.setFitWidth(scene_pane.getWidth()-40-i_sub_from_imgview);
                imview_Show_Image.setPreserveRatio(true);
                b_Is_ImageView_Setup = true;
                child_group.getChildren().add(imview_Show_Image);
            }

            child_group.getChildren().remove(imview_Show_Image);
            Image displayed_Image = new Image(fs_instream);
            imview_Show_Image.setImage(displayed_Image);
            child_group.getChildren().add(imview_Show_Image);
            fs_instream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
        Function for Normalise the Picture
     */
    public Mat Normalise_The_Mat(Mat Mat_To_Norm){
        Mat BufferMat = new Mat();
        Core.normalize(Mat_To_Norm, BufferMat,sld_norm_min.getValue(), sld_norm_max.getValue(), Core.NORM_MINMAX);
        return BufferMat;
    }

    /*
        Function for Equalise the Histogram the Picture
     */
    public Mat Equal_The_Mat(Mat Mat_To_Norm){
        Mat BufferMat = new Mat();
        Imgproc.equalizeHist(Mat_To_Norm,BufferMat);
        return BufferMat;
    }

    /*
        Saves the modified image.
     */
    public void Save_Modified_Image(Mat GrayMe){
        String SaveFileName = txt_info.getText();
        DirectoryChooser dirch = new DirectoryChooser();
        dirch.setTitle("Save Folder");
        dirch.setInitialDirectory(new File(str_Last_Folder_Path));

        str_Last_Folder_Path = dirch.showDialog(win_Artplace).toString() + "\\";

        if (SaveFileName.toUpperCase(Locale.ROOT).contains("TEMPPIC")) {
            txt_info.setText("TempPic_as_name_Not_allowed.png");
        }
        SaveFileName = StringTEST_And_Prepare(txt_info.getText());

        if(Files.isDirectory(Paths.get(str_Last_Folder_Path))){
            Imgcodecs.imwrite(str_Last_Folder_Path + SaveFileName, GrayMe);
            Image_Preview_Set_Refresh(str_Last_Folder_Path + SaveFileName);
        }
    }

    /*
        A Function which modifies the savefile name.
        How does it do that?
        Well, let's say the input strung is "Test.txt", the
        ZumTest checks if a . is present, if so it takes the "Test"
        String and adds .png at the end. ( Outcome : Test.png )

        If the input is only "Test" then it automatically adds .png at the end.
     */
    public String StringTEST_And_Prepare(String ZumTest){
        if(ZumTest.contains(".")){
            String[] Buffer = ZumTest.split("\\.");
            ZumTest = Buffer[0].concat(".png");
        } else {
            ZumTest = ZumTest.concat(".png");
        }
        return ZumTest;
    }

    // Setters
    public void setI_sub_from_imgview(int in_number){
        i_sub_from_imgview = in_number;
    }
    public void setScene_pane(Pane in_scene_pane) {
        scene_pane = in_scene_pane;
    }

    public void setChild_group(Group in_child_group) {
        child_group = in_child_group;
    }

    public boolean isB_equal_hist() {
        return b_equal_hist;
    }

    public void setB_equal_hist(boolean in_b_equal_hist) {
        b_equal_hist = in_b_equal_hist;
    }

    public void setB_SW_Pic(boolean in_b_SW_Pic) {
        b_SW_Pic = in_b_SW_Pic;
    }

    public void setB_Is_ImageView_Setup(boolean in_b_Is_ImageView_Setup) {
        b_Is_ImageView_Setup = in_b_Is_ImageView_Setup;
    }

    public void setB_Normalise_Picture(boolean in_b_Normalise_Picture) {
        b_Normalise_Picture = in_b_Normalise_Picture;
    }

    public void setSld_norm_max(Slider in_sld_norm_max) {
        sld_norm_max = in_sld_norm_max;
    }

    public void setSld_norm_min(Slider in_sld_norm_min) {
        sld_norm_min = in_sld_norm_min;
    }

    public void setSld_thres(Slider in_sld_thres) {
        sld_thres = in_sld_thres;
    }

    public void setTxt_info(TextField in_txt_info) {
        txt_info = in_txt_info;
    }

    // Switches
    public void switch_B_Normalise_Picture() {
        b_Normalise_Picture = !b_Normalise_Picture;
    }
    public void switch_B_SW_Pic(){
        b_SW_Pic = !b_SW_Pic;
    }
    public void switch_B_equal_hist(){
        b_equal_hist = !b_equal_hist;
    }
    // Getters
    public ImageView getShow_Image() {
        return imview_Show_Image;
    }

    // Boolean IS Functions
    public boolean isB_Is_ImageView_Setup() {
        return b_Is_ImageView_Setup;
    }

    public boolean isB_Normalise_Picture() {
        return b_Normalise_Picture;
    }

    public boolean isB_SW_Pic() {
        return b_SW_Pic;
    }
}
