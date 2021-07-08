import javafx.scene.shape.Path;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Paths;
import java.sql.SQLOutput;

public class MainGraph extends JFrame{

    protected GraphCanvas graphcanvas = new GraphCanvas(this);

    public int language = 1;
    protected Table table = new Table(this);
    protected DocText docText = new DocText(this);
    protected Options options = new Options(this);
    protected JLabel labelTextArea1 = new JLabel();
    protected JLabel labelTextArea2 = new JLabel();
    protected Menus menus;

    protected JPanel graphPanel = new JPanel();
    protected JLabel welcomeLabel;

    protected Font f1 = new Font("Serif", Font.PLAIN,15);
    protected Font f2 = new Font("Verdana", Font.BOLD,15);
    protected Color color1 = new Color(0,	100,	0);
    protected Color color2 = new Color(54,	117,	23);
    protected Color color3 = new Color(0,	127,	84);

    public MainGraph(String title) throws URISyntaxException {
        super(title);
        this.setSize(1010, 700);
        this.setLocation(140,20);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(MainGraph.class.getResource("/images/graphicon.png")));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        //tao menu bar
        menus = new Menus(this);


        //tao container cua Jfame
        Container frame = this.getContentPane();
        frame.setLayout(new BorderLayout(4,4));

        //panel trung tam
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(new LineBorder(color1, 1));
        mainPanel.setLayout(new FlowLayout(0));
        mainPanel.setPreferredSize(new Dimension(1004, 670));

        //panel ben trai
        JPanel leftPanel = new JPanel();
        //leftPanel.setBorder(new LineBorder(color1, 1));
        leftPanel.setLayout(new BorderLayout(3,1));
        leftPanel.setBorder(new LineBorder(color1, 1));
        leftPanel.setPreferredSize(new Dimension(246, 640));

        //panel cua text
        JPanel textPanel = new JPanel();
        textPanel.setBorder(new LineBorder(color1, 1));
        textPanel.setLayout(new BorderLayout(3,0));

        //panel cua bang
        JPanel tablePanel = new JPanel();
        tablePanel.setBorder(new LineBorder(color1, 1));
        tablePanel.setLayout(new BorderLayout(3,0));

        //tao doc text
        textPanel.add(docText.textScroll, BorderLayout.SOUTH);

        //tao bang
        tablePanel.add(table.jc, BorderLayout.SOUTH);

        //tao option
        JPanel optionPanel = new JPanel();
        optionPanel.setBorder(new LineBorder(color1, 1));
        optionPanel.setLayout(new FlowLayout(20));
        optionPanel.setPreferredSize(new Dimension(740, 100));
        optionPanel.add(options);




        //tao graph
        graphPanel.setLayout(new BorderLayout(0,12));
        graphPanel.setBorder(new LineBorder(color1, 1));
        graphPanel.setPreferredSize(new Dimension(740, 638));
        graphPanel.setDoubleBuffered(true);

        welcomeLabel = new JLabel("<html>Chào Mừng các bạn đã đến <br/>với sản phẩm của chúng tôi!<br/><p>Xem hướng dẫn để tạo đồ thị mới.<p></html>");

//        welcomeLabel.setIcon(new ImageIcon("out/production/Tuan14/images/Graph3.png"));
////
        welcomeLabel.setIcon(new ImageIcon(MainGraph.class.getClassLoader().getResource("images/Graph3.png")));

        welcomeLabel.setForeground(color3);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Verdana", Font.BOLD, 30));

        welcomeLabel.setPreferredSize(new Dimension(740, 535));
        graphPanel.add(welcomeLabel, BorderLayout.NORTH);
        graphPanel.add(optionPanel, BorderLayout.SOUTH);

        //tao lable text area
        labelTextArea1.setText("Hướng dẫn");
        labelTextArea1.setHorizontalAlignment(SwingConstants.CENTER);
        Setting.setLabel(labelTextArea1,color2,0,Color.red, color1, f2);
        textPanel.add(labelTextArea1,BorderLayout.NORTH);

        labelTextArea2.setText("Bảng thông số");
        labelTextArea2.setHorizontalAlignment(SwingConstants.CENTER);
        Setting.setLabel(labelTextArea2,color2,0,Color.red, color1, f2);
        labelTextArea2.setPreferredSize(new Dimension(240, 25));
        tablePanel.add(labelTextArea2, BorderLayout.NORTH);

        //ban đầu các nút sẽ khoong được nhấn
        options.s1.setEnabled(false);
        for (int i = 0; i < options.b.length ; i++)
            options.b[i].setEnabled(false);

        leftPanel.add(textPanel, BorderLayout.SOUTH);
        leftPanel.add(tablePanel, BorderLayout.NORTH);
        mainPanel.add(leftPanel);
        mainPanel.add(graphPanel);
        frame.add(mainPanel, BorderLayout.WEST);

    }
    public void changePanel(){
        graphPanel.remove(welcomeLabel);
        options.s1.setEnabled(true);
        for (int i = 0; i < options.b.length ; i++)
            options.b[i].setEnabled(true);
        graphcanvas.setSize(738,534);
        graphcanvas.setLocation(1,1);
        graphPanel.add(graphcanvas, BorderLayout.NORTH);
    }
    public void setLabel(){
        graphPanel.remove(graphcanvas);
        options.s1.setEnabled(false);
        for (int i = 0; i < options.b.length ; i++)
            options.b[i].setEnabled(false);
        graphPanel.add(welcomeLabel, BorderLayout.CENTER);
    }

    public static void main(String args[]) throws URISyntaxException {
        MainGraph g = new MainGraph("PROJECT I - TRỰC QUAN HÓA CÁC THUẬT TOÁN ĐỒ THỊ");
        g.setVisible(true);
    }

}