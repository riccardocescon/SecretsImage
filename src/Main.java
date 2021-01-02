import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

class ImageTrace{
    int x;
    int y;

    ImageTrace(int _x, int _y){
        x = _x;
        y = _y;
    }
}

public class Main {
    public static String source_image_path = "images\\source.png";
    public static String secret_message = "ciao";

    public static ImageTrace current_pixel = new ImageTrace(0,0);
    public static List<Pixel> pixels = new ArrayList<Pixel>();

    public static void main(String[] args){
        BufferedImage source_image = GetImage(source_image_path);
        int tot_pixels = source_image.getWidth() * source_image.getHeight();
        int updated_pixels = 0;

        BufferedImage destination_image = source_image;

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

        File destination = new File("images\\result.png");

        try {
            ImageIO.write(destination_image, "png", destination);
            System.out.println("Completed");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
        Pixel: [Alpha][Red][Green][Blue]
        8 bit for each section
     */

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
            BufferedImage img = ImageIO.read(file);
            return img;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
