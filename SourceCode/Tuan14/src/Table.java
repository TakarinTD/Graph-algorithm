import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

class Table {
    protected MainGraph parent;
    protected JTable tb; //dung trong updateTable()
    protected JScrollPane jc; //dung trong GraphAgorithm
    private Object[] tenCot;
    protected DefaultTableModel model; //dung trong updateTable()

    Table(MainGraph myparent){
        this.parent = myparent;
        tenCot = new Object[]{"Đỉnh", "k/c", "k/c Cuối", "Path"};

        tb = new JTable();
        model = new DefaultTableModel();
        model.setColumnIdentifiers(tenCot);

        tb.setModel(model);

        Setting.setting(tb,parent.color2,1, parent.color2, parent.f1);
        tb.setBounds(0,0,100,100);
        tb.setEnabled(false);

        tb.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tb.getColumnModel().getColumn(0).setPreferredWidth(40);
        tb.getColumnModel().getColumn(1).setPreferredWidth(40);
        tb.getColumnModel().getColumn(2).setPreferredWidth(50);
        tb.getColumnModel().getColumn(3).setPreferredWidth(109);

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        tb.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        tb.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        tb.getColumnModel().getColumn(2).setCellRenderer(centerRender);


        jc = new JScrollPane(tb);
        jc.setPreferredSize(new Dimension(243, 350));

    }
    public void clearTable(){
        DefaultTableModel model1 = (DefaultTableModel) tb.getModel();
        model1.setRowCount(0);
    }
    public void changeTypeTable(String str1, String str2, String str3, String str4){
            JTableHeader th = tb.getTableHeader();
            TableColumnModel tcm = th.getColumnModel();
            if(parent.language == 1){
                TableColumn tc = tcm.getColumn(1);
                tc.setHeaderValue(str1);
                TableColumn td = tcm.getColumn(2);
                td.setHeaderValue(str2);
            }
            else if(parent.language == 0){
                TableColumn tc = tcm.getColumn(1);
                tc.setHeaderValue(str3);
                TableColumn td = tcm.getColumn(2);
                td.setHeaderValue(str4);
            }
            th.repaint();

    }
    public void changeLanguageEN(){
        JTableHeader th = tb.getTableHeader();
        TableColumnModel tcm = th.getColumnModel();

        TableColumn t0 = tcm.getColumn(0);
        t0.setHeaderValue("vertex");
        TableColumn t1 = tcm.getColumn(1);
        t1.setHeaderValue("dist");
        TableColumn t2 = tcm.getColumn(2);
        t2.setHeaderValue("final dist");
        TableColumn t3 = tcm.getColumn(3);
        t3.setHeaderValue("min path");
        th.repaint();
    }

    public void changeLanguageVN(){
        JTableHeader th = tb.getTableHeader();
        TableColumnModel tcm = th.getColumnModel();

        TableColumn t0 = tcm.getColumn(0);
        t0.setHeaderValue("đỉnh");
        TableColumn t1 = tcm.getColumn(1);
        t1.setHeaderValue("k/c");
        TableColumn t2 = tcm.getColumn(2);
        t2.setHeaderValue("k/c cuối");
        TableColumn t3 = tcm.getColumn(3);
        t3.setHeaderValue("đường đi");
        th.repaint();
    }
}