import java.awt.event.*;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GUI extends JFrame {
    static JFrame frame;

    static JButton button, generate_button;

    static JLabel preview_label;
    static JLabel preview_text;

    static JLabel result_text, result_value;

    static Main secretsImage = new Main();

    static JLabel read_result_value;
    static  JPanel read_panel;
    static int read_numPairs;

    static Color background_color = new Color(5, 0, 2, 255);
    static Color color_ui = new Color(115, 115, 115, 255);
    static Color color_text = new Color(55, 255, 41, 255);

    public static void main(String[] args){
        frame = new JFrame("Secrets Image");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        HomePanel();
    }

    private static void SetFrame(){
        frame.setSize(800, 500);
        frame.setVisible(true);
    }

    private static void ClearFrame(JPanel panel){
        frame.remove(panel);
    }

    public static void HomePanel(){
        JPanel listPane = new JPanel(new SpringLayout());
        int numPairs = 0;


        JLabel m = new JLabel("", JLabel.TRAILING);
        listPane.add(m);
        JLabel message = new JLabel("What do you want to do?");
        message.setForeground(color_text);
        message.setHorizontalAlignment(JLabel.CENTER);
        m.setLabelFor(message);
        listPane.add(message);
        numPairs++;

        JLabel gb = new JLabel("", JLabel.TRAILING);
        listPane.add(gb);
        JButton generate_button = new JButton("Generate");
        generate_button.setForeground(color_text);
        generate_button.setBackground(color_ui);
        generate_button.setHorizontalAlignment(JButton.CENTER);
        gb.setLabelFor(generate_button);
        listPane.add(generate_button);
        numPairs++;

        generate_button.addActionListener(e ->{
            ClearFrame(listPane);
            GenerateImagePanel();
        });

        JLabel rb = new JLabel("", JLabel.TRAILING);
        listPane.add(rb);
        JButton read_button = new JButton("Read");
        read_button.setForeground(color_text);
        read_button.setBackground(color_ui);
        read_button.setHorizontalAlignment(JButton.CENTER);
        rb.setLabelFor(read_button);
        listPane.add(read_button);
        numPairs++;

        read_button.addActionListener(e ->{
            ClearFrame(listPane);
            ReadFile();
        });

        //TEMP
        /*String something = "<html>here is osme text ajsdkls hfuisdfhjdfhdsuifjsd fsdhfhdsifdsfhdsuifjdsfhsd iufdifjdu fhsdufdjfdhfu ishfuid sfhuidsfhsdu fjsdfhsdfus dfjsdufhs difusdj f sdhfusdf</html>";

        JLabel temp = new JLabel("Prova: ", JLabel.TRAILING);
        temp.setForeground(color_text);
        listPane.add(temp);
        JLabel text = new JLabel(something);
        text.setSize(10, 500);
        text.setForeground(color_text);
        temp.setLabelFor(text);
        listPane.add(text);
        numPairs++;
        *///

        //Lay out the panel.
        SpringUtilities.makeCompactGrid(listPane,
                numPairs, 2, //rows, cols
                6, 6,        //initX, initY
                6, 10);       //xPad, yPad

        listPane.setBackground(background_color);
        frame.add(listPane);
        SetFrame();
    }

    public static void ReadFile(){
        read_panel = new JPanel(new SpringLayout());
        read_numPairs = 0;

        JLabel image_lable = new JLabel("Image: ", JLabel.TRAILING);
        image_lable.setForeground(color_text);
        read_panel.add(image_lable);
        JButton pick_button = new JButton("Pick");
        pick_button.setBackground(color_ui);
        pick_button.setForeground(color_text);
        image_lable.setLabelFor(pick_button);
        read_panel.add(pick_button);
        read_numPairs++;

        JLabel image_picked = new JLabel("current: ", JLabel.TRAILING);
        image_picked.setForeground(color_text);
        read_panel.add(image_picked);
        JLabel image_picked_label = new JLabel("[no_path_assigned]");
        image_picked_label.setHorizontalAlignment(JLabel.CENTER);
        image_picked.setLabelFor(image_picked_label);
        image_picked_label.setForeground(color_text);
        read_panel.add(image_picked_label);
        read_numPairs++;

        pick_button.addActionListener(e-> {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter imageFilter = new FileNameExtensionFilter("png", ImageIO.getReaderFileSuffixes());
            fileChooser.addChoosableFileFilter(imageFilter);
            fileChooser.setAcceptAllFileFilterUsed(false);

            int option = fileChooser.showOpenDialog(frame);
            if(option == JFileChooser.APPROVE_OPTION){
                File file = fileChooser.getSelectedFile();
                image_picked_label.setText("File Selected: " + file.getPath());
            }else{
                image_picked_label.setText("Open command canceled");
            }
        });

        JLabel l = new JLabel("", JLabel.TRAILING);
        read_panel.add(l);
        button = new JButton("Read");
        button.setBackground(color_ui);
        button.setForeground(color_text);
        l.setLabelFor(button);
        read_panel.add(button);
        read_numPairs++;

        button.addActionListener(e -> {
            secretsImage.SetReadData(image_picked_label.getText());
        });



        //Lay out the panel.
        SpringUtilities.makeCompactGrid(read_panel,
                read_numPairs, 2, //rows, cols
                6, 6,        //initX, initY
                6, 10);       //xPad, yPad

        read_panel.setBackground(background_color);

        frame.add(read_panel);
        frame.pack();
        SetFrame();

    }

    public static void ShowResult(String result){
        JLabel t = new JLabel("Result : ");
        t.setForeground(color_text);
        read_panel.add(t);
        read_result_value = new JLabel("Result value", JLabel.CENTER);
        read_result_value.setForeground(color_text);
        read_result_value.setBackground(background_color);
        JScrollPane scroller = new JScrollPane(read_result_value, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroller.setBackground(background_color);
        scroller.setForeground(color_ui);
        scroller.setOpaque(true);
        scroller.getViewport().setBackground(background_color);
        t.setLabelFor(scroller);
        read_panel.add(scroller);
        read_numPairs++;

        //Lay out the panel.
        SpringUtilities.makeCompactGrid(read_panel,
                read_numPairs, 2, //rows, cols
                6, 6,        //initX, initY
                6, 10);       //xPad, yPad


        read_result_value.setText("<html>" + result + "</html>");
        read_result_value.resize(read_result_value.preferredSize());
        frame.add(read_panel);
        frame.pack();
        SetFrame();
    }

    public static void GenerateImagePanel(){
        String[] labels = {"Image: ", "Text: "};
        int numPairs = 0;

        //Create and populate the panel.
        JPanel p = new JPanel(new SpringLayout());

        JLabel t = new JLabel("");
        p.add(t);
        JLabel title = new JLabel("Secrets Image", JLabel.CENTER);
        title.setForeground(color_text);
        t.setLabelFor(title);
        p.add(title);
        numPairs++;

        JLabel image_lable = new JLabel("Image: ", JLabel.TRAILING);
        image_lable.setForeground(color_text);
        p.add(image_lable);
        JButton pick_button = new JButton("Pick");
        pick_button.setBackground(color_ui);
        pick_button.setForeground(color_text);
        image_lable.setLabelFor(pick_button);
        p.add(pick_button);
        numPairs++;

        JLabel image_picked = new JLabel("current: ", JLabel.TRAILING);
        image_picked.setForeground(color_text);
        p.add(image_picked);
        JLabel image_picked_label = new JLabel("[no_path_assigned]");
        image_picked_label.setHorizontalAlignment(JLabel.CENTER);
        image_picked.setLabelFor(image_picked_label);
        image_picked_label.setForeground(color_text);
        p.add(image_picked_label);
        numPairs++;

        JLabel message_label = new JLabel("Message: ", JLabel.TRAILING);
        message_label.setForeground(color_text);
        p.add(message_label);
        JTextField message_text = new JTextField(10);
        message_text.setForeground(color_text);
        message_text.setBackground(color_ui);
        message_label.setLabelFor(message_text);
        p.add(message_text);
        numPairs++;

        pick_button.addActionListener(e-> {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter imageFilter = new FileNameExtensionFilter("png", ImageIO.getReaderFileSuffixes());
            fileChooser.addChoosableFileFilter(imageFilter);
            fileChooser.setAcceptAllFileFilterUsed(false);

            int option = fileChooser.showOpenDialog(frame);
            if(option == JFileChooser.APPROVE_OPTION){
                File file = fileChooser.getSelectedFile();
                image_picked_label.setText("File Selected: " + file.getPath());
            }else{
                image_picked_label.setText("Open command canceled");
            }
        });

        numPairs++;
        JLabel l = new JLabel("", JLabel.TRAILING);
        p.add(l);
        button = new JButton("Preview");
        button.setBackground(color_ui);
        button.setForeground(color_text);
        l.setLabelFor(button);
        p.add(button);

        button.addActionListener(e -> {
            secretsImage.SetSourceData(image_picked_label.getText(), message_text.getText());
        });

        //after preview button
        preview_label = new JLabel("You are currently using : ", JLabel.TRAILING);
        preview_label.setForeground(color_text);
        p.add(preview_label);
        preview_text = new JLabel("[num] % of the image. Wanna proceed?");
        preview_text.setForeground(color_text);
        preview_label.setLabelFor(preview_text);
        p.add(preview_text);
        numPairs++;

        //generate button
        JLabel generate_label = new JLabel("", JLabel.TRAILING);
        p.add(generate_label);
        generate_button = new JButton("Generate");
        generate_button.setForeground(color_text);
        generate_button.setBackground(color_ui);
        generate_label.setLabelFor(generate_button);
        p.add(generate_button);
        numPairs++;

        generate_button.addActionListener(e -> {
            secretsImage.HideMessage();
        });

        result_text = new JLabel("Result : ", JLabel.TRAILING);
        result_text.setForeground(color_text);
        p.add(result_text);
        result_value = new JLabel("Result status");
        result_value.setForeground(color_text);
        result_text.setLabelFor(result_value);
        p.add(result_value);
        numPairs++;

        ShowPreview(false, "0");


        //Lay out the panel.
        SpringUtilities.makeCompactGrid(p,
                numPairs, 2, //rows, cols
                6, 6,        //initX, initY
                6, 10);       //xPad, yPad

        p.setBackground(background_color);

        frame.add(p);
        frame.pack();
        SetFrame();
    }

    public static void ShowPreview(boolean show, String amount){
        preview_label.setVisible(show);
        preview_text.setVisible(show);
        if(!show) ShowGenerateButton(false);
        if(!show) ShowResult(false, "");
        preview_text.setText(amount + "% of the image. wanna proceed?");
        if(show){
            if(Float.parseFloat(amount) <= 100f){
                ShowGenerateButton(true);
            }else{

            }
        }

    }

    private static void ShowGenerateButton(boolean show){
        generate_button.setVisible(show);
    }

    public static void ShowResult(boolean show, String result){
        result_text.setVisible(show);
        result_value.setVisible(show);
        if(show){
            result_value.setText(result);
        }
    }

}
