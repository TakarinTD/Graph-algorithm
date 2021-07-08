import javax.swing.*;
import java.awt.*;

class DocText {
    protected JScrollPane textScroll; //dung trong GraphAgorithm de tao bang
    protected MainGraph parent;
    protected JTextArea textArea; //dung trong UpdateDoc()
    Font f2 = new Font("Serif", Font.PLAIN,13);
    DocText(MainGraph thisparent){
        this.parent = thisparent;
        textArea = new JTextArea(13, 20);
        textArea.setFont(f2);
        textScroll = new JScrollPane(textArea);
        setDocText();
        textArea.setEditable(false);
    }

    public void setDocText() {
        if(parent.language == 1){
            textArea.setText(   "- Để tạo đồ thị mới: Menu -> new\n" +
                    "  Click chuột để tạo nút mới\n\n" +
                    "- Giữ Shift + kéo chuột để di chuyển nút\n\n" +
                    "- Nhấn Ctrl + click chuột vào nút để xóa\n\n" +
                    "- Giữ Ctrl + kéo chuột để di chuyển nút đầu\n\n" +
                    "- Để thay đổi trọng số:\n" +
                    " ->Menu->Graph->Change Weight\n\n");

        }
        else if(parent.language == 0){
            textArea.setText(   "- Create new graph: Menu -> new\n" +
                    "  Click mouse on the panel to create a node \n\n" +
                    "- Hold Shift + Drag mouse to move nodes\n\n" +
                    "- Hold Ctrl + click mouse to delete nodes\n\n" +
                    "- Hold Ctrl + Drag mouse to move Start node\n\n" +
                    "- To change weight:\n" +
                    " ->Menu->Graph->Change Weight\n\n");

        }

    }

    public void showDocText(String str){
        textArea.setText(str);
    }

}
