import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static String source_image_path = "";
    public static String destination_image_path = "";
    public static String secret_message = "";

    public static String read_image_path = "SecretsImage\\images\\result.png";

    public static String result_text_file = "SecretsImage\\images\\result.txt";

    public static ImageTrace current_pixel = new ImageTrace(0,0);
    public static List<Pixel> pixels = new ArrayList<Pixel>();

    public static String final_message = "";

    public static void SetSourceData(String path, String message){
        path = path.substring(15);
        String file_name = GetFileName(path);
        source_image_path = GetParentPath(path) + file_name;
        destination_image_path = GetParentPath(path) + "result.png";

        System.out.println("source path : " + source_image_path);
        System.out.println("dest path : " + destination_image_path);

        secret_message = message;

        Preview();
    }

    public static void SetReadData(String path){
        path = path.substring(15);
        String file_name = GetFileName(path);
        read_image_path = GetParentPath(path) + file_name;
        System.out.println("destinatio : " + read_image_path);
        ReadImage();
    }

    private static String GetFileName(String path){
        String[] parts = path.split("\\\\");
        return  parts[parts.length - 1];
    }

    private static String GetParentPath(String path){
        String div = "\\\\";
        String[] parts = path.split(div);
        String parent = "";
        for(int i = 0; i < parts.length - 1; i++){
            parent += parts[i] + "\\\\";
        }
        return parent;
    }

    public static void main(String[] args){
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        Scanner sc = new Scanner(System.in);
        String decision = "";
        do {
            System.out.println("Do you want to create an image, read it , preview, or exit? (c/r/p/e)");
            decision = sc.nextLine();
            switch (decision){
                case "c":
                    HideMessage();
                    break;
                case "r":
                    ReadImage();
                    break;
                case "p":
                    Preview();
                    break;
                case "e":
                    break;
                default:
                    System.out.println("Please retype");
                    break;
            }
        }while (!decision.equals("e"));
    }

    /*
        Pixel: [Alpha][Red][Green][Blue]
        8 bit for each section
     */

    private static void Preview(){
        BufferedImage source_image = GetImage(source_image_path);
        int tot_pixels = source_image.getWidth() * source_image.getHeight();
        int tot_bits = tot_pixels * 2;
        int tot_chars = tot_bits / 8;
        float perc = secret_message.length() * 100 / tot_chars;
        GUI.ShowPreview(true,Float.toString(perc));
        System.out.println("You can hide max : " + tot_chars + " in this image. \nYou are using : " + secret_message.length());
    }

    private static void SeePixels(BufferedImage img){
        for(int i = 0; i < 4; i++){
            String value = Integer.toBinaryString(img.getRGB(0, i));
            System.out.println("(0;" + i + ") " + value);
        }
    }

    private static String CheckSyntax(String path){
        if(path.charAt(path.length() - 1) == '\\')return path;
        path += "\\";
        return path;
    }

    public static void SaveToFile(String result_path, String result_name){
        String result = CheckSyntax(result_path) + result_name + ".txt";
        System.out.println("name : " + result_name);
        CreateFile(result);
        WriteToFile(result, final_message);
        GUI.ShowReadSaveResult("Hidden message has been saved to : " + result);
    }

    public static void ChangeDestinationPath(String path){
        read_image_path = path;
    }

    private static void ReadImage(){
        BufferedImage result_image = GetImage(read_image_path);
        int tot_pixels = result_image.getWidth() * result_image.getHeight();
        int pos = 0;
        final_message = "";
        String char_collection = "";
        SeePixels(result_image);
        System.out.println("READING MESSAGE...");
        for(int x = 0; x < result_image.getWidth(); x++){
            for(int y = 0; y < result_image.getHeight(); y++){
                String value = Integer.toBinaryString(result_image.getRGB(current_pixel.x, current_pixel.y));
                System.out.println("Value : " + value);

                Pixel p = new Pixel(current_pixel.x, current_pixel.y, value);
                String blue = p.GetBlue();

                if(pos % 4 == 0 && pos != 0){
                    char ascii_char = (char)Integer.parseInt(char_collection, 2);
                    final_message += ascii_char;
                    char_collection = "";
                }

                char_collection += blue.substring(6,8);
                pos++;
                NextPixel(result_image);
            }
        }
        System.out.println("Compelted");
        GUI.ShowResult(final_message);

    }

    public static String GetSourceFilePath(){
        return read_image_path;
    }

    private static void CreateFile(String path){
        try {
            File myObj = new File(path);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static void WriteToFile(String path, String message){
        try {
            FileWriter myWriter = new FileWriter(path);
            myWriter.write(message);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void HideMessage(){
        BufferedImage source_image = GetImage(source_image_path);
        int tot_pixels = source_image.getWidth() * source_image.getHeight();
        int updated_pixels = 0;

        BufferedImage destination_image = source_image;
        SeePixels(source_image);
        System.out.println("CLONING MESSAGE PIXELS...");

        byte[] b = secret_message.getBytes(StandardCharsets.US_ASCII);
        for (byte l : b) {
            String value = NormalizeASCII(Integer.toBinaryString(l), 8);

            for(int i = 0; i < value.length() / 2; i++){
                if(updated_pixels > tot_pixels){
                    System.err.println("Message to long for this image");
                    return;
                }
                String img_blue = GetPixel(source_image);
                String letter_part = value.substring(i * 2,i * 2 + 2);
                String img_keep_part = img_blue.substring(0, 6);
                String result = img_keep_part + letter_part;
                pixels.get(pixels.size() - 1).UpdatePixel(result);

                NextPixel(source_image);
                updated_pixels++;
            }
        }

        System.out.println("CLONING SAME PIXELS...");

        for(int i = updated_pixels; i < tot_pixels; i++){
            String pixel = Integer.toBinaryString(source_image.getRGB(current_pixel.x, current_pixel.y));
            Pixel p = new Pixel(current_pixel.x, current_pixel.y, pixel);
            pixels.add(p);
            NextPixel(source_image);
        }

        System.out.println("CREATING DESTINATION IMAGE");
        int pos = 0;
        for(int x = 0; x < source_image.getWidth(); x++){
            for(int y = 0; y < source_image.getHeight(); y++){
                Pixel p = pixels.get(pos);
                Color color = new Color(p.GetIntRed(), p.GetIntGreen(), p.GetIntBlue(), p.GetIntAlpha());
                destination_image.setRGB(x, y, color.getRGB());
                pos++;
            }
        }

        File destination = new File(destination_image_path);System.out.println("creating file : " + destination_image_path);
        try {
            if(!destination.exists())
            destination.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ImageIO.write(destination_image, "png", destination);
            System.out.println("Completed");
            GUI.ShowResult(true, "File successfully created");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String GetPixel(BufferedImage img){
        String pixel = Integer.toBinaryString(img.getRGB(current_pixel.x, current_pixel.y));
        Pixel p = new Pixel(current_pixel.x, current_pixel.y, pixel);
        pixels.add(p);
        return p.GetBlue();
    }

    private static void NextPixel(BufferedImage img){
        if(current_pixel.y < img.getHeight() - 1){
            current_pixel.y++;
        }else{
            current_pixel.y = 0;
            current_pixel.x++;
        }
    }

    private static String NormalizeASCII(String value, int total_length){
        if(value.length() == total_length)return value;
        if(value.length() > total_length){
            System.err.println("Max lenth is " + total_length + " but value is : " + value);
            return null;
        }else{
            int amount = total_length - value.length();
            String normalized = "";
            for(int i = 0; i < amount; i++){
                normalized += "0";
            }
            return  normalized + value;
        }
    }

    private static BufferedImage GetImage(String path){
        File file = new File(path);

        try {
            BufferedImage img = ImageIO.read(new File(path));
            return img;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
