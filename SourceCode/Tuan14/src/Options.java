import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

class Options extends Panel {
    protected JLabel[] l = new JLabel[5];
    protected Button[] b = new Button[6];
    protected JSlider s1 = new JSlider(SwingConstants.HORIZONTAL, 1000, 2500, 2000);
    private Dimension dimForStep = new Dimension(160,78);
    private Dimension dimForAuto = new Dimension(350,78);
    private Dimension dimForSpeed = new Dimension(180,78);

    protected boolean isNext = false;
    protected boolean isAutoRun = false;
    protected boolean isStepRun = false;
    protected boolean isRunning = false;

    protected Font f = new Font( "Verdana", Font.BOLD,15);
    protected Font f2 = new Font( "Serif", Font.BOLD,15);
    protected Color color3 = new Color(91,	189,	43);
    protected Color color4 = new Color(249,244,	0);
    protected Color color5 = new Color(0,	100,	0);
    protected Color color6 = new Color(40, 40, 40);

    protected MainGraph parent;

    Options(MainGraph myparent) {
        parent = myparent;
        b[0] = new Button("NEXT");
        b[1] = new Button("START");
        b[2] = new Button("RUN");
        b[3] = new Button("STOP");
        b[4] = new Button("PAUSE");
        b[5] = new Button("RESUME");

        for (int i = 0; i < b.length ; i++){
            Setting.setting(b[i], color6,1, color4, color5, f);
        }


        l[0] = new JLabel("CHẠY TỪNG BƯỚC");
        l[1] = new JLabel("Tốc độ(mini giây)");
        l[2] = new JLabel("CHẠY TỰ ĐỘNG");
        l[3] = new JLabel("--------------------");
        l[4] = new JLabel("----------------------------------");


        for (int i = 0; i < l.length; i++)
            l[i].setHorizontalAlignment(SwingConstants.CENTER);

        Setting.setLabel(l[0], color6, f);
        Setting.setLabel(l[1], color6, color3, f);
        Setting.setLabel(l[2], color6, f);



        s1.setMajorTickSpacing(500);
        s1.setMinorTickSpacing(100);
        s1.setPaintTicks(true);
        s1.setPaintLabels(true);

        Setting.setSlider(s1,color3,2,color3, color6, f2);
        s1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                parent.graphcanvas.timeAlg = s1.getValue();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,2, 5,3));
        buttonPanel.setBackground(color3);
        buttonPanel.setPreferredSize(new Dimension(120, 30 ));
        buttonPanel.add(b[1]);
        buttonPanel.add(b[0]);

        JPanel runAutoPanel = new JPanel();
        runAutoPanel.setLayout(new GridLayout(1,4, 5, 3));
        runAutoPanel.setBackground(color3);
        runAutoPanel.add(b[2]);
        runAutoPanel.add(b[3]);
        runAutoPanel.add(b[4]);
        runAutoPanel.add(b[5]);


        Dimension d = new Dimension(200,140);
        JPanel nextPanel = new JPanel();
        nextPanel.setLayout(new BorderLayout(4,4));
        Setting.setPanel(nextPanel, color5,2, color3);
        nextPanel.setPreferredSize(dimForStep);
        nextPanel.add(l[0], BorderLayout.NORTH);
        nextPanel.add( buttonPanel,BorderLayout.CENTER);
        nextPanel.add(l[3], BorderLayout.SOUTH);

        JPanel runPanel = new JPanel();
        runPanel.setLayout(new BorderLayout(4,4));
        Setting.setPanel(runPanel, color5,2, color3);
        runPanel.setPreferredSize(dimForAuto);
        runPanel.add(l[2], BorderLayout.NORTH);
        runPanel.add(runAutoPanel, BorderLayout.CENTER);
        runPanel.add(l[4], BorderLayout.SOUTH);

        JPanel speedPanel = new JPanel();
        speedPanel.setLayout(new BorderLayout(4,4));
        Setting.setPanel(speedPanel, color5,2, color3);
        speedPanel.setPreferredSize(dimForSpeed);
        speedPanel.add(l[1], BorderLayout.NORTH);
        speedPanel.add(s1,BorderLayout.SOUTH);

        setLayout(new FlowLayout( 3,10, 5));
        setPreferredSize(new Dimension(730, 90));
        add(runPanel);
        add(nextPanel);
        add(speedPanel);

    }
    public void showMesWhenRun(){
        if (parent.language == 1)
            JOptionPane.showMessageDialog(parent, "Thuật toán đang chạy\nNếu bạn muốn bắt đầu lại\nVui lòng thiết lập lại đồ thị\nMenu -> Đồ thị -> thiết lập lại");
        else if (parent.language == 0)
            JOptionPane.showMessageDialog(parent, "Algorithm is running\nIf you want to restart\nPlease reset graph\nMenu -> Graph -> Reset ");
    }


    public void initLock(){
        isRunning = false;
        isStepRun = false;
        isAutoRun = false;
        isNext = false;
    }


    public boolean action(Event evt, Object arg) {

        if (evt.target instanceof Button) {
            if(isAutoRun == false){
                if (((String) arg).equals(b[0].getLabel())) {
                    isStepRun = true;
                    parent.graphcanvas.nextstep();
                    isNext = true;
                }
                if (((String) arg).equals(b[1].getLabel())) {
                    isStepRun = true;
                    System.out.println("isNext = " + isNext);
                    if(isNext == false && parent.graphcanvas.performalg == false) {
                        parent.graphcanvas.stepAlg();
                    }
                    else showMesWhenRun();

                }
            }
            if(isStepRun == false) {
                if (((String) arg).equals(b[2].getLabel())) {
                    isAutoRun = true;
                    if (isRunning == false) {
                        parent.graphcanvas.runalg();
                        if(parent.graphcanvas.performalg == true)
                            isRunning = true;
                    } else showMesWhenRun();

                }
                if (((String) arg).equals(b[3].getLabel())) {
                    parent.graphcanvas.stop();
                }
                if (((String) arg).equals(b[4].getLabel())) {
                    parent.graphcanvas.pause();
                }
                if (((String) arg).equals(b[5].getLabel())) {
                    parent.graphcanvas.resume();
                }
            }
        }
        return true;
    }
}
