public class Pixel {
    private int x;
    private int y;
    private String pixel;
    private String blue;
    private String red;
    private String green;
    private String alpha;

    Pixel(int _x, int _y, String _pixel){
        x = _x;
        y = _y;
        pixel = _pixel;
        alpha = Calculate_Alpha(_pixel);
        red = Calculate_Red(_pixel);
        green = Calculate_Green(_pixel);
        blue = Calculate_Blue(_pixel);
    }

    private String Calculate_Blue(String rgba){
        String b = "";
        for(int i = rgba.length() * 3 / 4; i < rgba.length(); i++){
            b += rgba.charAt(i);
        }
        return b;
    }

    private String Calculate_Red(String rgba){
        String r = "";
        for(int i = rgba.length() * 1 / 4; i < rgba.length() * 1 / 2; i++){
            r += rgba.charAt(i);
        }
        return r;
    }

    private String Calculate_Green(String rgba){
        String g = "";
        for(int i = rgba.length() * 1 / 2; i < rgba.length() * 3 / 4; i++){
            g += rgba.charAt(i);
        }
        return g;
    }

    private String Calculate_Alpha(String rgba){
        String a = "";
        for(int i = 0; i < rgba.length() * 1 / 4; i++){
            a += rgba.charAt(i);
        }
        return a;
    }

    public String GetAlpha(){
        return alpha;
    }

    public String GetRed(){
        return red;
    }

    public String GetGreen(){
        return green;
    }

    public String GetBlue(){
        return blue;
    }

    public int GetIntAlpha(){
        return Integer.parseInt(alpha, 2);
    }

    public int GetIntRed(){
        return Integer.parseInt(red, 2);
    }

    public int GetIntGreen(){
        return Integer.parseInt(green, 2);
    }

    public int GetIntBlue(){
        return Integer.parseInt(blue, 2);
    }

    public String GetPixel(){
        return pixel;
    }

    public void UpdatePixel(String new_pixel){
        pixel = new_pixel;
    }
}
