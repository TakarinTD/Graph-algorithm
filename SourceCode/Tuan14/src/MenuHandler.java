import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

class MenuHandler implements ActionListener, ItemListener{
        private Menus menus;
        public MenuHandler(Menus menus){
            this.menus = menus;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String msg = "You chose ";
            String arg = e.getActionCommand();
            if(arg.equals(menus.item[1].getLabel())){
                msg += "Open";
                try {
                    menus.parent.options.initLock();
                    menus.parent.graphcanvas.openFile();
                    menus.parent.changePanel();
                } catch (IOException ex){
                    System.out.println("Can't open file");
                }
            }
            else if(arg.equals(menus.item[2].getLabel())){
                msg += "Save As";
                try {
                    menus.parent.graphcanvas.saveFile();
                    if(menus.parent.graphcanvas.testSave)
                        JOptionPane.showMessageDialog(menus.parent, "Saved file");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(menus.parent, "Can't save file");
                }
            }

            else if(arg.equals(menus.item[4].getLabel())) {
                msg += "Reset";
                menus.parent.graphcanvas.reset();
                menus.parent.options.initLock();
            }
            else if(arg.equals(menus.item[5].getLabel())) {
                msg += "Clear";
                if(menus.parent.language ==  1){
                    ImageIcon icon = new ImageIcon(MainGraph.class.getClassLoader().getResource("images/Trash-Delete-icon.png"));
                    int xoa = JOptionPane.showConfirmDialog(menus.parent, "Bạn có muốn xóa đồ thị không?","Bạn đã lưu file chưa?",JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
                    if(xoa == JOptionPane.YES_OPTION){
                        menus.parent.graphcanvas.clear();
                        menus.parent.options.initLock();
                        menus.parent.setLabel();
                    }
                }
                else if(menus.parent.language == 0){
                    ImageIcon icon = new ImageIcon(MainGraph.class.getClassLoader().getResource("images/Trash-Delete-icon.png"));
                    int xoa = JOptionPane.showConfirmDialog(menus.parent, "Do you want to clear graph?","Have you saved file?",JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
                    if(xoa == JOptionPane.YES_OPTION){
                        menus.parent.graphcanvas.clear();
                        menus.parent.options.initLock();
                        menus.parent.setLabel();
                    }
                }
            }

            else if(arg.equals(menus.item[3].getLabel())) {
                msg += "Example";
                menus.parent.changePanel();
                menus.parent.options.initLock();
                menus.parent.graphcanvas.showexample();
            }
            else if(arg.equals(menus.item[10].getLabel())){
                msg += "Exit";
                int x = JOptionPane.showConfirmDialog(menus.parent, " Bạn muốn thoát ứng dụng ? \n Bạn chắc chứ ?");
                if(x == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
            else if(arg.equals(menus.item[10].getLabel())){
                msg += "Exit";
                int x = JOptionPane.showConfirmDialog(menus.parent, " Do you want to exit? \n Are you sure ?");
                if(x == JOptionPane.YES_OPTION){
                    System.exit(0);
                }
            }

            else if(arg.equals(menus.item[7].getLabel())){
                msg += "You choose Dijstra Algorithm";
                menus.parent.graphcanvas.setAlgorithm(1);
                if(menus.parent.language == 1)
                    JOptionPane.showMessageDialog(menus.parent, "Bạn đã chọn thuật toán Dijkstra");
                else if(menus.parent.language == 0)
                    JOptionPane.showMessageDialog(menus.parent, "You have chosen Dijkstra algorithm");
                menus.parent.table.changeTypeTable("k/c", "k/c Cuối","dist","final dist");

                if(menus.parent.language == 1)
                    menus.parent.labelTextArea2.setText("Thuật toán Dijkstra");
                else if(menus.parent.language == 0)
                    menus.parent.labelTextArea2.setText("Dijkstra Algorithm");
                menus.parent.labelTextArea2.setForeground(new Color(255,0,0));

            }
            else if(arg.equals(menus.item[8].getLabel())){
                msg += "You choose Breath First Search(BFS) Algorithm";
                menus.parent.graphcanvas.setAlgorithm(2);
                if(menus.parent.language == 1)
                    JOptionPane.showMessageDialog(menus.parent, "Bạn đã chọn thuật toán BFS");
                else if(menus.parent.language == 0)
                    JOptionPane.showMessageDialog(menus.parent, "You have chosen BFS algorithm");
                menus.parent.table.changeTypeTable("t.t xét", "Đã xét","Order set","Seted");
                if(menus.parent.language == 1)
                    menus.parent.labelTextArea2.setText("Thuật toán BFS");
                else if(menus.parent.language == 0)
                    menus.parent.labelTextArea2.setText("BFS Algorithm");
                menus.parent.labelTextArea2.setForeground(new Color(66,103,178));

            }
            else if(arg.equals(menus.item[9].getLabel())){
                msg += "You choose Depth First Search(DFS) Algorithm";
                menus.parent.graphcanvas.setAlgorithm(3);
                if(menus.parent.language == 1)
                    JOptionPane.showMessageDialog(menus.parent, "Bạn đã chọn thuật toán DFS");
                else if(menus.parent.language == 0)
                    JOptionPane.showMessageDialog(menus.parent, "You have chosen DFS algorithm");
                menus.parent.table.changeTypeTable("t.t xét", "Đã xét","Order set","Seted");
                if(menus.parent.language == 1)
                    menus.parent.labelTextArea2.setText("Thuật toán DFS");
                else if(menus.parent.language == 0)
                    menus.parent.labelTextArea2.setText("DFS Algorithm");
                menus.parent.labelTextArea2.setForeground(new Color(255,150,57));

            }
            else if(arg.equals(menus.item[6].getLabel())){
                msg += "You choose Change Weight";
                menus.parent.graphcanvas.changeWeight();
            }
            else if(arg.equals(menus.item[0].getLabel())){
                msg += "You choose New";
                //if(menus.parent.graphcanvas.sonut != 0)
                menus.parent.graphcanvas.clear();
                menus.parent.options.initLock();
                menus.parent.changePanel();

            }
            else if(arg.equals(menus.item[11].getLabel())){
                if(menus.parent.language == 0)
                {
                    ImageIcon icon = new ImageIcon(MainGraph.class.getClassLoader().getResource("images/warning.png"));
                    JOptionPane.showMessageDialog(menus.parent, "You've already chosen this language","Warning !!!",1,icon);
                }
                else{
                ImageIcon icon = new ImageIcon(MainGraph.class.getClassLoader().getResource("images/en.gif"));
                int language = JOptionPane.showConfirmDialog(menus.parent, "Do you want to change the language to English\n Are you sure:","Change Language", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,icon);
                if(language == JOptionPane.YES_OPTION) {

                        msg += "You choose English";
                        menus.parent.language = 0;
                        menus.parent.options.l[0].setText("STEP BY STEP");
                        menus.parent.options.l[2].setText("RUN AUTOMATICALLY");
                        menus.parent.options.l[1].setText("speed(miniseconds)");
                        menus.parent.welcomeLabel.setText("<html>Welcome to our product!<br/>See the documentaion for<br/>creating new graphs<br/></html>");
                        menus.parent.labelTextArea1.setText("Documentation");
                        if(menus.parent.graphcanvas.Algorithm == 1)
                            menus.parent.labelTextArea2.setText("Dijkstra Algorithm");
                        else if(menus.parent.graphcanvas.Algorithm == 2)
                            menus.parent.labelTextArea2.setText("BFS Algorithm");
                        else if(menus.parent.graphcanvas.Algorithm == 3)
                            menus.parent.labelTextArea2.setText("DFS Algorithm");
                        else
                            menus.parent.labelTextArea2.setText("Parameter Table");
                        menus.parent.docText.setDocText();
                        menus.parent.table.changeLanguageEN();
                    }
                }
            }
            else if(arg.equals(menus.item[12].getLabel())){
                if(menus.parent.language == 1){
                    ImageIcon icon = new ImageIcon(MainGraph.class.getClassLoader().getResource("images/warning.png"));
                    JOptionPane.showMessageDialog(menus.parent, "Bạn đã chọn ngôn ngữ này rồi!","Cảnh báo !!!",1,icon);
                }
                else{
                ImageIcon icon = new ImageIcon(MainGraph.class.getClassLoader().getResource("images/vi.gif"));
                int language = JOptionPane.showConfirmDialog(menus.parent, "Bạn có muốn chuyển ngôn ngữ sang Tiếng Việt\n Bạn chắc chắn chứ:","Đổi ngôn ngữ", JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE, icon);
                if(language == JOptionPane.YES_OPTION) {

                        msg += "You choose Tiếng việt";
                        menus.parent.language = 1;
                        menus.parent.options.l[0].setText("CHẠY TỪNG BƯỚC");
                        menus.parent.options.l[2].setText("CHẠY TỰ ĐỘNG");
                        menus.parent.options.l[1].setText("Tốc độ(mini giây):");
                        menus.parent.welcomeLabel.setText("<html>Chào Mừng các bạn đã đến   <br/>với sản phẩm của chúng tôi!<br/><p>Xem hướng dẫn để tạo đồ thị mới.<p></html>");
                        menus.parent.labelTextArea1.setText("Hướng dẫn");
                        if(menus.parent.graphcanvas.Algorithm == 1)
                            menus.parent.labelTextArea2.setText("Thuật toán Dijkstra");
                        else if(menus.parent.graphcanvas.Algorithm == 2)
                            menus.parent.labelTextArea2.setText("Thuật toán BFS");
                        else if(menus.parent.graphcanvas.Algorithm == 3)
                            menus.parent.labelTextArea2.setText("Thuật toán DFS");
                        else
                            menus.parent.labelTextArea2.setText("Bảng thông số");;
                        menus.parent.docText.setDocText();
                        menus.parent.table.changeLanguageVN();
                    }
                }
            }
            System.out.println(msg);
        }

        public void itemStateChanged(ItemEvent e){
            menus.repaint();
        }

    }

