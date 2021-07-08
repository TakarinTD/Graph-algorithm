import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

class Setting{

    public static void setting(Button button, Color mauvien, int day,Color maunen, Color mauchu, Font font){
        //button.setBorder(new LineBorder(mauvien, day));
        button.setBackground(maunen);
        button.setForeground(mauchu);
        button.setFont(font);
    }
    public static void setPanel(JPanel panel, Color mauvien, int day,Color maunen){
        panel.setBorder(new LineBorder(mauvien, day));
        panel.setBackground(maunen);
    }

    public static void setLabel(JLabel lable, Color mauchu, Font font){
        lable.setForeground(mauchu);
        lable.setFont(font);
    }
    public static void setLabel(JLabel lable, Color mauchu, Color maunen, Font font){
        lable.setBackground(maunen);
        lable.setForeground(mauchu);
        lable.setFont(font);
    }
    public static void setLabel(JLabel lable, Color mauvien,int day ,Color maunen, Color mauchu, Font font){
        lable.setBorder(new LineBorder(mauvien, day));
        lable.setBackground(maunen);
        lable.setForeground(mauchu);
        lable.setFont(font);
    }


    public static void setSlider(JSlider jSlider, Color mauvien, int day,Color maunen, Color mauchu, Font font){
        jSlider.setBorder(new LineBorder(mauvien, day));
        jSlider.setBackground(maunen);
        jSlider.setForeground(mauchu);
        jSlider.setFont(font);
    }

    public static void setting(JTable table, Color mauvien, int day, Color mauchu, Font font){
        table.setBorder(new LineBorder(mauvien, day));
        table.setForeground(mauchu);
        table.setFont(font);
    }

}
