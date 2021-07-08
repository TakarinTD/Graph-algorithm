import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

class GraphCanvas extends Canvas implements Runnable {

    // drawing area for the graph
    protected final int MAXNUT = 20;
    private final int MAX = MAXNUT + 1;
    protected final int NUTSIZE = 30;
    private final int NUTRADIX = 16;
    private ArrayList<Integer> VisitedList;
    private ArrayList<Integer> listPoint;

    private int[] thutuxet = new int[MAX];
    protected boolean testSave = false;

    private int buoc; //dem buoc chạy thuật toán
    private String s = new String(""); //chuỗi thông tin hiển thị trên doc text
    private int nutKe = 0; //số nút kề
    private boolean[] dathaydoi = new boolean[MAX]; //chỉ ra thay đổi khoảng cách trong thuật toán
    private int sothaydoi = 0; //số nút thay đổi trong thuật toán

    protected int Algorithm;
    private Stack<Integer> stack = new Stack<Integer>();
    private boolean[] Visited;
    private LinkedList<Integer> open;// use for BFS
    private LinkedList<Integer> B[];

    protected Point nut[] = new Point[MAX];          // nut
    protected int weight[][] = new int[MAX][MAX];     // weight of muiten
    protected Point startp[][] = new Point[MAX][MAX]; // start and
    protected Point endp[][] = new Point[MAX][MAX];   // endpoint of muiten
    protected Point tbp[][] = new Point[MAX][MAX];
    protected Point mtp[][] = new Point[MAX][MAX];

    // thông tin đồ thị khi chạy thuật toán
    protected boolean canh[][] = new boolean[MAX][MAX];
    protected int kc[] = new int[MAX];
    protected int kcCuoi[] = new int[MAX];
    protected Color colornut[] = new Color[MAX];

    // dùng cho thuật toán step by step
    protected int minkc, minnut, minstart, minend; //k/c nho nhat( tim dinh thu nhat)

    protected float[][] dir_x = new float[MAX][MAX];
    protected float[][] dir_y = new float[MAX][MAX];//huong x,y

    //dùng cho tính năng xóa, di chuyển
    protected Point thispoint = new Point(0, 0); //Vị trí hiện tại của con trỏ chuột
    protected Point oldpoint = new Point(0, 0); // Vị trí cũ của nút được di chuyển

    protected int nut1, nut2;
    protected int nutdanhan = 0;
    protected int sonut = 0;      // number of nuts
    protected int sonuttrong = 0;    // empty spots in array nut[] (due to nut deletion)
    protected int nutdau = 0;    // start of graph
    protected boolean performalg = false;

    //dùng cho hành động hiện tại
    protected boolean muitenmoi = false;
    protected boolean dichuyennut = false;
    protected boolean dichuyennutdau = false;
    protected boolean xoanut = false;

    // dùng cho double buffering
    protected Image mhAnh;
    protected Graphics mhDoHoa;

    // dùng cho tính năng chạy
    private Thread algrthm;
    protected int timeAlg = 2000;
    private volatile boolean running = true;
    private volatile boolean paused = false;
    private final Object pauselook = new Object();
    protected boolean isFinished = false;
    private boolean isReachable = false;

    MainGraph parent;
    //contructor
    GraphCanvas(MainGraph myparent) {
        parent = myparent;
        setBackground(Color.white);
    }

    public void setAlgorithm(int x) {
        this.Algorithm = x;
    }

    public void stop() {
        if (algrthm != null)
            algrthm.suspend();
        running = false;
        resume();
    }

    //khoi tao cau hinh ban dau cua do thi( mau xam, canh = false, mau root = xanh)
    public void init() { //dung cho runAlg, StepAlg, Example, Open, Reset, Clear
        for (int i = 0; i < MAXNUT; i++) {
            colornut[i] = Color.green;
            thutuxet[i] = 0;
            for (int j = 0; j < MAXNUT; j++)
                canh[i][j] = false;
        }
        colornut[nutdau] = Color.red;
        performalg = false;
        buoc = 1;
    }

    public void clear() {
        nutdau = 0;
        sonut = 0;
        sonuttrong = 0;
        for (int i = 0; i < MAXNUT; i++) {
            nut[i] = new Point(0, 0);
            for (int j = 0; j < MAXNUT; j++)
                weight[i][j] = 0;
        }
        init();
        parent.docText.setDocText();
        parent.table.clearTable();
        repaint();
    }

    public void reset() {
        parent.docText.setDocText();
        parent.table.clearTable();
        init();
        repaint();
    }

    public void runalg() {
        if(Algorithm == 1 || Algorithm == 2 || Algorithm == 3){
            System.out.println(Algorithm);
            initalg();
            minend = nutdau; //đầu tiên nút minend (nút có kc nhỏ nhất ) được gán bằng nút đầu
            performalg = true;
            algrthm = new Thread(this);
            algrthm.start();
        }
        else {
            showMes("Hãy chọn 1 thuật toán trước khi bắt đầu" + "\nVào Menu -> Đồ thị -> Thuật toán",
                    "Choose one algorithm before start" + "\n Menu -> Graph -> Algorithms");
        }
    }

    public void initalg() {
        Visited = new boolean[sonut];
        VisitedList = new ArrayList<Integer>();
        open = new LinkedList<Integer>();
        B = new LinkedList[sonut];

        init();
        for (int i = 0; i < sonut; i++) {
            Visited[i] = false;
            B[i] = new LinkedList();
            kc[i] = -1;
            kcCuoi[i] = -1;
        }
        open.addLast(nutdau);
        kc[nutdau] = 0; //cau hinh ban dau thuat toan
        kcCuoi[nutdau] = 0;
    }

    public void stepAlg() {
        if(Algorithm == 1 || Algorithm == 2 || Algorithm == 3){
            initalg();
            performalg = true;
            minend = nutdau;//đầu tiên nút minend (nút có kc nhỏ nhất ) được gán bằng nút đầu
            if(Algorithm == 2)
                Visited[nutdau] = true;
            nextstep();
        }
        else {
            showMes("Hãy chọn 1 thuật toán trước khi bắt đầu" + "\nVào Menu -> Đồ thị -> Thuật toán",
                    "Choose one algorithm before start" + "\n Menu -> Graph -> Algorithms");
        }
    }

    public void nextstep() {
        //tinh toan tim duong di ngan nhat toi nut tiep theo
        if (Algorithm == 1) {
            kcCuoi[minend] = minkc;
            canh[minstart][minend] = true;
            colornut[minend] = Color.yellow;
            updateTable(parent.table);
        }
        else if (Algorithm == 3) {

            kcCuoi[minend] = 1;
            canh[minstart][minend] = true;
            colornut[minend] = Color.yellow;
            thutuxet[minend] = buoc;
            updateTable(parent.table);
        }
        else if (Algorithm == 2) {
            kcCuoi[minend] = 2;
            canh[minstart][minend] = true;
            colornut[minend] = Color.yellow;
            thutuxet[minend] = buoc;
            updateTable(parent.table);
        }
        buoc++;
        repaint();
    }

    public void run() {
        for (int i = 0; i < (sonut - sonuttrong); i++) {
                nextstep();
                try {
                    algrthm.sleep(timeAlg);
                } catch (InterruptedException e) {
                    break;
                }
                if(paused){
                    try{
                        synchronized (pauselook){
                            pauselook.wait();
                        }

                    }catch (InterruptedException ex){
                        break;
                    }
                    if(!running){
                        break;
                    }
                }
            }
            algrthm = null;//ket thuc run thì luông = null

    }
    public void pause(){
        paused = true;
    }
    public void resume(){
        synchronized (pauselook){
            paused = false;
            pauselook.notifyAll(); //unlock thread
        }
    }

    public void showexample(){
        int w, h;
        clear();
        init();
        sonut = 11;
        sonuttrong = 0;
        for (int i = 0; i < MAXNUT; i++) {
            nut[i] = new Point(0, 0);
            for (int j = 0; j < MAXNUT; j++)
                weight[i][j] = 0;
        }
        w = this.size().width / 10;
        h = this.size().height / 8;
        nut[0] = new Point(1 * w, 4 * h);
        nut[1] = new Point(3 * w, 1 * h);
        nut[2] = new Point(3 * w, 4 * h);
        nut[3] = new Point(3 * w, 7 * h);
        nut[4] = new Point(5 * w, 1 * h);
        nut[5] = new Point(5 * w, 4 * h);
        nut[6] = new Point(5 * w, 7 * h);
        nut[7] = new Point(7 * w, 1 * h);
        nut[8] = new Point(7 * w, 4 * h);
        nut[9] = new Point(7 * w, 7 * h);
        nut[10] = new Point(9 * w, 4 * h);

        weight[0][1] = 10;
        weight[0][2] = 16;
        weight[0][3] = 32;
        weight[1][2] = 5;
        weight[1][4] = 24;
        weight[2][3] = 17;
        weight[2][5] = 37;
        weight[3][6] = 52;
        weight[4][5] = 15;
        weight[4][7] = 63;
        weight[5][6] = 16;
        weight[5][8] = 11;
        weight[6][9] = 51;
        weight[7][8] = 22;
        weight[7][10] = 43;
        weight[8][9] = 23;
        weight[8][10] = 25;
        weight[9][10] = 12;

        for (int i = 0; i < sonut; i++)
            for (int j = 0; j < sonut; j++)
                if (weight[i][j] > 0)
                    capNhatMuiTen(i, j, weight[i][j]);

        repaint();
    }

    public void saveFile() throws IOException {

        if(sonut > 0) {
            JTextField filename = new JTextField();
            JFileChooser luu = new JFileChooser("E:\\Documents\\Tuan14_jar\\SourceCode\\Tuan14");
            if(parent.language == 0)
                luu.setDialogTitle("Save File");
            else if(parent.language == 1)
                luu.setDialogTitle("Lưu Tệp Tin");
            int fVal = luu.showSaveDialog(parent);
            if (fVal == JFileChooser.APPROVE_OPTION) {
                filename.setText(luu.getSelectedFile().getName());
            }
            FileWriter save3 = new FileWriter(luu.getCurrentDirectory().toString() +
                    "\\" + filename.getText(), false);

            for (int i = 0; i < sonut + 1; i++) {
                if (i == 0) {
                    save3.write(Integer.toString(sonut));
                    save3.write("\n");
                    save3.write(Integer.toString(nutdau));
                    save3.write("\n");

                } else {

                        save3.write(Integer.toString(nut[i - 1].x));
                        save3.write(" ");
                        save3.write(Integer.toString(nut[i - 1].y));
                        save3.write("\n");

                }
            }
            for (int i = 0; i < sonut; i++) {
                for (int j = 0; j < sonut; j++) {
                    if(weight[i][j] >= 0) {
                        save3.write(Integer.toString(weight[i][j]));
                        save3.write(" ");
                    }
                }
                save3.write("\n");
            }
            save3.close();
            testSave = true;
        }else {
            showMes("Bạn chưa tạo đồ thị!", "You have not a graph!");
        }
    }

    public void openFile() throws IOException {
        JTextField filename = new JTextField();
        JFileChooser chon = new JFileChooser("E:\\Documents\\Tuan14_jar\\SourceCode\\Tuan14");
        if(parent.language == 0)
            chon.setDialogTitle("Open File");
        else if(parent.language == 1)
            chon.setDialogTitle("Mở Tệp Tin");
        int rval = chon.showOpenDialog(parent);
        if (rval == JFileChooser.APPROVE_OPTION) {
            filename.setText(chon.getSelectedFile().getName());
        }
        FileReader open = new FileReader(chon.getCurrentDirectory().toString() + "\\" + filename.getText());
        BufferedReader Buffread = new BufferedReader(open);
        String s = Buffread.readLine();

        clear();
         //lấy nội dung dòng thứ nhất
        sonut = Integer.parseInt(s);
        s = Buffread.readLine();
        nutdau = Integer.parseInt(s); //gán nội dung donagf thứ 2
        init();

        String luuXY[] = new String[2];
        s = Buffread.readLine(); // lấy nội dung các dòng còn lại
        for (int i = 0; i < sonut; i++) {
            luuXY = s.split(" ");
            nut[i] = new Point(Integer.parseInt(luuXY[0]), Integer.parseInt(luuXY[1]));
            if(Integer.parseInt(luuXY[0]) < 0)
                sonuttrong++;
            luuXY[0] = luuXY[1] = "";
            if (i < sonut - 1)
                s = Buffread.readLine();
        }
        s = Buffread.readLine();
        String[] luuH = new String[sonut];
        for (int j = 0; j < sonut; j++) {
            luuH = s.split(" ");
            for (int k = 0; k < sonut; k++) {
                weight[j][k] = Integer.parseInt(luuH[k]);
            }
            s = Buffread.readLine();
        }
        for (int j = 0; j < sonut; j++)
            for (int k = 0; k < sonut; k++)
                if (weight[j][k] > 0)
                    capNhatMuiTen(j, k, weight[j][k]);

        repaint();
        Buffread.close();
    }

    public void capNhatMuiTen(int p1, int p2, int w) {

        int dx = nut[p2].x - nut[p1].x;
        int dy = nut[p2].y - nut[p1].y;
        float l = (float) (sqrt((float) (dx * dx + dy * dy)));
        float lt = l - 10;
        dir_x[p1][p2] = dx / l;
        dir_y[p1][p2] = dy / l;

        int diff_x = (int) (Math.abs(20 * dir_x[p1][p2]));
        int diff_y = (int) (Math.abs(20 * dir_y[p1][p2]));

        if (weight[p2][p1] > 0) {
            startp[p1][p2] = new Point((int) (nut[p1].x - 5 * dir_y[p1][p2]), (int) (nut[p1].y + 5 * dir_x[p1][p2]));
            endp[p1][p2] = new Point((int) (nut[p2].x - 5 * dir_y[p1][p2]), (int) (nut[p2].y + 5 * dir_x[p1][p2]));
        } else {
            startp[p1][p2] = new Point(nut[p1].x, nut[p1].y);
            endp[p1][p2] = new Point(nut[p2].x, nut[p2].y);
        }
        tbp[p1][p2] = new Point((nut[p1].x + nut[p2].x) / 2, (nut[p1].y + nut[p2].y) / 2);

        // tinh vi tri x cua dau mui ten
        if (startp[p1][p2].x > endp[p1][p2].x) {
            mtp[p1][p2] = new Point(endp[p1][p2].x + diff_x, 0);
        } else {
            mtp[p1][p2] = new Point(startp[p1][p2].x + diff_x + (Math.abs(endp[p1][p2].x - startp[p1][p2].x) - 2 * diff_x), 0);
        }

        // tinh vi tri y cua dau mui ten
        if (startp[p1][p2].y > endp[p1][p2].y) {
            mtp[p1][p2].y = endp[p1][p2].y + diff_y;
        } else {
            mtp[p1][p2].y = startp[p1][p2].y + diff_y + (Math.abs(endp[p1][p2].y - startp[p1][p2].y) - 2 * diff_y);
        }
    }

    public boolean mouseDown(Event e, int x, int y) {
        //di chuyển nút
        if(!performalg) {
            if (e.shiftDown()) {
                if (nhannut(x, y, NUTSIZE)) {
                    oldpoint = nut[nutdanhan];
                    nut1 = nutdanhan;
                    dichuyennut = true;
                }
            }
            //kiem tra nut la nut dau thi chuyen mau nut dau
            //khong thi xoa nut
            else if (e.controlDown()) {
                if (nhannut(x, y, NUTSIZE)) {
                    nut1 = nutdanhan;
                    if (nutdau == nut1) {
                        dichuyennutdau = true;
                        thispoint = new Point(x, y);
                        colornut[nutdau] = Color.green;
                    } else
                        xoanut = true;
                }
            }
            //vẽ mũi tên mới
            else if (nhannut(x, y, NUTSIZE)) {

                if (!muitenmoi) {
                    muitenmoi = true;
                    thispoint = new Point(x, y);
                    nut1 = nutdanhan;
                }
            }
            //vẽ nút mới
            else if (!nhannut(x, y, NUTSIZE)) {
                if (sonuttrong == 0) {
                    if (sonut < MAXNUT)
                        nut[sonut++] = new Point(x, y);

                } else {
                    int i;
                    for (i = 0; i < sonut; i++)
                        if (nut[i].x == -100) break;
                    nut[i] = new Point(x, y);
                    sonuttrong--;
                }
            }
            repaint();

        }else{
            showMes("Thuật toán đang chạy!", "Algorithm is running!");
        }
        return true;
    }

    public boolean nhannut(int x, int y, int kc) {
        for (int i = 0; i < sonut; i++) {
            if (pow(x - nut[i].x, 2) + pow(y - nut[i].y, 2) < kc * kc) {
                nutdanhan = i;
                return true;
            }
        }
        return false;
    }

    public boolean mouseDrag(Event e, int x, int y) {
        if(dichuyennut){
            nut[nut1] = new Point(x, y);
            // di chuyển nút và điều khiển mũi tên
            for (int i = 0; i < sonut ; i++) {
                if(weight[i][nut1] > 0){
                    capNhatMuiTen(i, nut1, weight[i][nut1]);
                }
                if(weight[nut1][i] > 0){
                    capNhatMuiTen(nut1, i, weight[nut1][i]);
                }
            }
            repaint(); //thay đổi vẽ liên tục mũi tên theo di chuyển chuột

        }
        else if(dichuyennutdau || muitenmoi){
            thispoint = new Point(x, y);
            repaint(); //mỗi khi di chuyển nút mà drag thì hiển thị nút đó liên tục
        }
        return true;
    }

    public boolean mouseUp(Event e, int x, int y) {
        //trường hợp di chuyển mũi tên đi ra ngoài hoặc quá gần nút khác
        if(dichuyennut){
            nut[nut1] = new Point(0, 0);
            if(nhannut(x, y, NUTSIZE ) || (x < 0) || (x > this.size().width) ||
                    (y < 0) || (y > this.size().height)){
                nut[nut1] = oldpoint;
                JOptionPane.showMessageDialog(parent,"Vị trí quá gần hoặc ngoài phạm vi");
            }
            else nut[nut1] = new Point(x, y);
            //cập nhập lại các nút
            for (int i=0; i < sonut; i++) {
                if (weight[i][nut1]>0)
                    capNhatMuiTen(i, nut1, weight[i][nut1]);
                if (weight[nut1][i]>0)
                    capNhatMuiTen(nut1, i, weight[nut1][i]);
            }
            dichuyennut = false;
        }
        else if(xoanut){
            xoaNut();
            xoanut = false;
        }
        else if (muitenmoi) {
            muitenmoi = false;
            if (nhannut(x, y, NUTSIZE)) {
                nut2 = nutdanhan;
                if (nut1 != nut2) {
                    int trongso = nhapTrongSo(nut1, nut2);
                    weight[nut1][nut2] = trongso;
                        if (weight[nut1][nut2] > 0){
                            capNhatMuiTen(nut1, nut2, trongso);
                        }
                        if (weight[nut2][nut1] > 0) {//mut ten tu nut2 den nut1
                            capNhatMuiTen(nut2, nut1, trongso);
                        }
                }
            }
        }
        else if(dichuyennutdau){
            if(nhannut(x, y, NUTSIZE))
                nutdau = nutdanhan;
            //initalg();
            colornut[nutdau] = Color.red;
            dichuyennutdau = false;
        }
        repaint();
        return true;
    }

    public void xoaNut(){
        //xóa nút và tất cả các mũi tên của nút đó
        nut[nut1] = new Point(-100, -100);
        for (int i = 0; i < sonut ; i++) {
            weight[nut1][i] = 0;
            weight[i][nut1] = 0;
        }

        sonuttrong++;
    }
    public void showMes(String vn, String en){
        if(parent.language == 1)
            JOptionPane.showMessageDialog(parent, vn);
        else if(parent.language == 0)
            JOptionPane.showMessageDialog(parent, en);
    }

    public void changeWeight(){
        ImageIcon icon = new ImageIcon(MainGraph.class.getClassLoader().getResource("images/change.png"));
        if(sonut >= 2) {
            JTextField field1 = new JTextField();
            JTextField field2 = new JTextField();
            JTextField field3 = new JTextField();

            Object[] fields = {
                    "Nút 1:", field1,
                    "Nút 2:", field2,
                    "Trọng số mới:", field3
            };
            Object[] fieldEn = {
                    "Node 1:", field1,
                    "Node 2:", field2,
                    "New Weight:", field3
            };
            if(parent.menus.item[6].getLabel() == "Đổi trọng số")
                JOptionPane.showMessageDialog(parent, fields, "Đổi trọng số từ x tới y", 1,icon);
            else if(parent.menus.item[6].getLabel() == "Change Weight")
                JOptionPane.showMessageDialog(parent, fieldEn, "Change Weight from x to y", 1,icon);

            if(!field1.getText().equals("") && !field2.getText().equals("") && !field3.getText().equals("")){

                String s1 = field1.getText();
                char c = s1.charAt(0);
                int x = CharToInt(c);

                String s2 = field2.getText();
                char c2 = s2.charAt(0);
                int y = CharToInt(c2);

                String s3 = field3.getText();
                int z = Integer.parseInt(s3);
                if(s1.length() == 1 && s2.length() == 1 && ((int)c >= 65 && (int)c <= 90 || (int)c >= 97 && (int)c <= 122)) {
                    if (weight[x][y] > 0)
                        weight[x][y] = z;
                    else{
                        showMes("Bạn đã chọn sai đỉnh!\n Vui lòng thử lại!","You 've been chosen wrong vertex!\nPlease Try again!");
                    }
                }
                else
                {
                    showMes("Bạn đã nhập sai định dạng\nCác đỉnh chỉ có 1 chữ cái a-z hoặc A-Z","You have written wrong fomat\nVertex have only one character: a-z or A-Z");
                }
            }else {
                showMes("Bạn vẫn chưa thay đổi!","Nothing changed!");
            }
            repaint();

        }
        else if(sonut == 1){
            if(parent.menus.item[6].getLabel() == "Đổi trọng số")
                JOptionPane.showMessageDialog(parent, "Đồ thị phải có ít nhất 2 nút\nmới thay đổi được trọng số!");
            else if(parent.menus.item[6].getLabel() == "Change Weight")
                JOptionPane.showMessageDialog(parent, "The graph must have at least 2 nodes\njust changed the weight!");


        }
        else{
            if(parent.menus.item[6].getLabel() == "Đổi trọng số")
                JOptionPane.showMessageDialog(parent, "Đồ thị chưa có nút nào cả\nHãy tạo đồ thị trước!");
            else if(parent.menus.item[6].getLabel() == "Change Weight")
                JOptionPane.showMessageDialog(parent, "The graph has no nodes yet\nCreate the graph first!");

        }
    }

    public void updateDocText(Graphics graph){

        for (int i = 0; i < sonut ; i++) {
            if(nut[i].x > 0 && kc[i] != -1){ // nút chưa xét tới
                graph.setColor(Color.black);
                String str = new String ("" + kc[i]);
                graph.drawString(str, nut[i].x - 10, nut[i].y + 5);
                if(kcCuoi[i] == -1){
                    nutKe++;
                    if(nutKe != 1)
                        s += ", ";
                    s += intToString(i) + " = " + kc[i];
                }
            }
            s = s + ". ";
        }
        if( buoc > 1 && sothaydoi > 0){
            s += "\n- Các khoảng cách tới ";

            for (int i = 0; i < sonut ; i++) {
                if (dathaydoi[i])
                    s += intToString(i) + ", ";
            }
            s += "đã thay dổi.";
        }
        if(nutKe > 1){
            //nếu có nhiều hơn 1 nút kề và tại sao nút này lại được chấp nhận
            s += "\n- Nút " + intToString(minend) + " đã có khoảng cách nhỏ nhất\n";

            //kiểm tra xem có đường nào khác tới nút minend
            int duongmoi = 0;
            for (int i = 0; i < sonut ; i++)
                if(nut[i].x > 0 && weight[i][minend] > 0 && kcCuoi[i] == -1) // nút chưa có kcCuoi mà đi được tới minend
                     duongmoi++;
            if(duongmoi  > 0)
                s += "- Bất cứ đường nào tới " + intToString(minend) + " từ một \n nút kề nào khác sẽ lớn hơn "+ kc[minend]+ "\n";
            else
                s += "- Không có đường nào khác đi tới được "+ intToString(minend) + "\n";
        }
        else{
            boolean coNhieuHon1Nut = false;
            boolean bacCau = false;
            for (int i = 0; i < sonut; i++) {
                if (nut[i].x > 0 && kcCuoi[i] == -1 && weight[i][minend] > 0)
                    coNhieuHon1Nut = true;
                if(nut[i].x > 0 && kcCuoi[i] == -1 && weight[minend][i] > 0)
                    bacCau = true;
            }
            if(coNhieuHon1Nut && bacCau)
                s += "- Vì nút này làm cầu nối tới các nút còn lại\n"+ " nên không có đường đi nào khác dài hơn\n";
            else if( coNhieuHon1Nut && !bacCau)
                s += "- Không thể đến được các nút XÁM còn lại\n";
            else
                s += "- Không còn đường đi nào tới được "+ intToString(minend);
        }
        s += "- Nút "+ intToString(minend) + " sẽ được tô màu VÀNG\n  và " + minkc + " sẽ là k/c nhỏ nhất tới "+ intToString(minend) + "\n" ;
        parent.docText.showDocText(s);
    }

    public void ketThucBuoc(Graphics g){
        if(Algorithm  == 1 ||Algorithm == 2 || Algorithm == 3){ //nếu 1 trong các thuật toán
            updateDocText(g);
        }
        if(performalg && minkc == 0){ //khi thuật toán vẫn đang chạy và
            if(algrthm != null)       // minkc ban đầu = 0 vẫn không thay đổi
                algrthm.stop();       // thi mới vào trong
            int cotheToiDuoc = 0;
            for (int i = 0; i < sonut; i++) { //kiem tra xem con nút nào có kc cuois nữa không để tới
                if (kcCuoi[i] > 0)
                    cotheToiDuoc++;
            }
            if(cotheToiDuoc == 0)
                parent.docText.showDocText("Thuật toán không thực hiện được.");
            else if(cotheToiDuoc < (sonut - sonuttrong - 1))
                parent.docText.showDocText("Thuật toán vẫn chưa đi được hết các nút.");
            else
                parent.docText.showDocText("Thuật toán hoàn thành.");
            isFinished = true;
            performalg = false;
        }else
            isFinished = false;
    }
    public void ketThucBuocBD(Graphics g){
        String s2 =     " - Những Nút MÀU VÀNG là\n" +
                        "   những nút đã xét."+
                        "\n- Những nút MÀU XANH là\n" +
                        "  những nút đang xét."+
                        "\n- Những nút MÀU ĐỎ là\n" +
                        "  những nút chờ được xét.\n"+
                        "\n- Những mũi tên MÀU XANH chỉ\n" +
                        "  những nút đang xét.\n"+
                        "\n- Những mũi tên MÀU ĐỎ chỉ\n" +
                        "  những nút đã xét.";
        parent.docText.showDocText(s2);
    }

    public void update(Graphics g){
        // Chuẩn bị cho một màn hình mới
        if (mhAnh == null) {
            mhAnh = createImage(this.getSize().width, this.getSize().height); //tạo ảnh mới có kich thước bằng kích thước màn hình
            mhDoHoa = mhAnh.getGraphics();
        }
        //vẽ
        mhDoHoa.setColor(getBackground());
        mhDoHoa.fillRect(0, 0, this.getSize().width, this.getSize().height);
        paint(mhDoHoa);
        //vẽ hình chữ nhật ra khỏi vector
        g.drawImage(mhAnh, 0, 0, this);

    }

    public void veMuiTen(Graphics g, int i, int j){
        Graphics2D g2 = (Graphics2D) g;
        int x1, x2, x3, y1, y2, y3;

        // tính tọa độ tam giác đầu mũi tên
        x1 = (int) (mtp[i][j].x - 2 * dir_x[i][j] + 6 * dir_y[i][j]);
        x2 = (int) (mtp[i][j].x - 2 * dir_x[i][j] - 6 * dir_y[i][j]);
        x3 = (int) (mtp[i][j].x + 6 * dir_x[i][j]);

        y1 = (int) (mtp[i][j].y - 2 * dir_y[i][j] - 6 * dir_x[i][j]);
        y2 = (int) (mtp[i][j].y - 2 * dir_y[i][j] + 6 * dir_x[i][j]);
        y3 = (int) (mtp[i][j].y + 6 * dir_y[i][j]);

        int mtphead_x[] = {x1, x2, x3, x1};
        int mtphead_y[] = {y1, y2, y3, y1};
        //vẽ mũi tên
        g2.fillPolygon(mtphead_x, mtphead_y, 4);

        //tô màu cạnh
        if (canh[i][j]){ // nếu cạnh đã xét thì tô màu đỏ và đậm
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.red);
            g2.fillPolygon(mtphead_x, mtphead_y, 4);
        }else if(isReachable){
            g2.setStroke(new BasicStroke(2));
        }else
            g2.setStroke(new BasicStroke(1));


        //vẽ thân mũi tên
        g2.drawLine(startp[i][j].x, startp[i][j].y, endp[i][j].x, endp[i][j].y);

        g2.setColor(Color.black);


        //vẽ trọng số của mũi tên ở giữa thân của mũi tên
        int dx = (int)(Math.abs(7*dir_y[i][j]));
        int dy = (int)(Math.abs(7*dir_x[i][j]));
        String str = new String("" + weight[i][j]);
        g2.setColor(Color.black);
        if ((startp[i][j].x > endp[i][j].x) && (startp[i][j].y >= endp[i][j].y))
            g2.drawString( str, tbp[i][j].x + dx, tbp[i][j].y - dy);

        if ((startp[i][j].x >= endp[i][j].x) && (startp[i][j].y < endp[i][j].y))
            g2.drawString( str, tbp[i][j].x - 23 - dx ,tbp[i][j].y - dy);

        if ((startp[i][j].x < endp[i][j].x) && (startp[i][j].y <= endp[i][j].y))
            g2.drawString( str, tbp[i][j].x - 23 ,tbp[i][j].y + 20);

        if ((startp[i][j].x <= endp[i][j].x) && (startp[i][j].y > endp[i][j].y)) //x dau <= x cuoi va y dau > y cuoi
            g2.drawString( str, tbp[i][j].x + dx,tbp[i][j].y + 10 );

    }

    public int nhapTrongSo(int nut1, int nut2) {
        int a = 0;
        if(parent.language == 1) {
            String w = JOptionPane.showInputDialog(parent,"Nhập vào trọng số từ nút " + intToString(nut1) + " tới nút  " + intToString(nut2), 5);
            a = Integer.parseInt(w);
        }else if(parent.language == 0){
            String w = JOptionPane.showInputDialog(parent,"Input Weight from node " + intToString(nut1) + " to node  " + intToString(nut2), 5);
            a = Integer.parseInt(w);
        }
        return a;
    }



    public void detailsDijkstra(Graphics g, int i, int j) {
        // kiem tra neu nut j co kc nho nhat toi nut bat dau
        if ((kcCuoi[i] != -1) && (kcCuoi[j] == -1)) {
            isReachable = true;
            g.setColor(Color.green); // tô màu xanh cho canh i ---> j
            if ((kc[j] == -1) || (kc[j] >= (kc[i] + weight[i][j]))) {
                if ( (kc[i] + weight[i][j]) < kc[j] ) {
                    dathaydoi[j] = true;
                    sothaydoi++;
                }
                kc[j] = kc[i] + weight[i][j]; // cập nhật khoảng cách nút lân cận
                colornut[j] = new Color(223,	53,	57); // tô màu đỏ cho nút được cập nhật khoảng cách (hàng xóm)
                if ((minkc == 0) || (kc[j] < minkc)) {
                    minkc = kc[j];
                    minstart = i;
                    minend = j;
                    colornut[minend] = new Color(32,	90,	167);
                }
            }
        }
        else{
            isReachable = false;
            g.setColor(Color.gray);
        }
    }


    public void detailDFS(Graphics g, int i, int j) {
        if (kcCuoi[i] != -1 && kcCuoi[j] == -1 ) {
            stack.push(i);
            while (!stack.empty()){
                i = stack.pop();
                if (!Visited[i])
                    Visited[i] = true;

                if (weight[i][j] > 0) {
                    if (!Visited[j]) {
                        g.setColor(Color.green); // tô màu xanh cho cạnh i--->j
                        colornut[j] = Color.red; // tô màu đỏ cho nút được xét
                        stack.push(j);
                        minstart = i;
                        minend = j;
                    }
                }
                while (!stack.empty())
                    stack.pop();
            }
        }
    }

    public void detailBFS(Graphics g, int i, int j) {
        if (kcCuoi[i] != -1 && kcCuoi[j] == -1) {
            open.addLast(i);
            if(Visited[i] == false)
                Visited[i] = true;
            while(open.size() != 0) {
                i = open.pollFirst();
                 if(weight[i][j] > 0){
                    if (Visited[j] == false){
                        g.setColor(Color.green);
                        Visited[j] = true;
                        open.addLast(j);
                        minstart = i;
                        minend = j;
                    }
               }else
                     break;
            }
        }
    }


    public void updateTable(Table table) {
        boolean test = false;
        int i = table.tb.getRowCount();
        Object[] row = new Object[4];
        if (i <= 0){
            for (int j = 0; j < sonut; j++) {
                row[0] = intToString(j);
                if(Algorithm == 1) {
                    row[1] = Integer.toString(kc[j]);
                    row[2] = Integer.toString(kcCuoi[j]);
                }
                if(Algorithm == 2 || Algorithm == 3) {
                    row[1] = Integer.toString(thutuxet[j]);
                    row[2] = Boolean.toString(Visited[j]);
                }
                row[3] = "";

                table.model.addRow(row);
                test = true;
            }
        }
        else {
            StringBuffer s5 = new StringBuffer();
            for (int j = 0; j < sonut; j++) {
                table.model.setValueAt(intToString(j), j, 0);
                if(Algorithm == 1) {
                    table.model.setValueAt(kc[j], j, 1);
                    table.model.setValueAt(kcCuoi[j], j, 2);
                }
                if(Algorithm == 2 || Algorithm == 3) {
                    if(colornut[j] == Color.yellow)
                        Visited[j] = true;
                    table.model.setValueAt(thutuxet[j], j, 1);
                    table.model.setValueAt(Visited[j], j, 2);
                }
                test = true;
            }

            s5.append(intToString(nutdau) + ",");
            s5.append(intToString(minend));
            table.model.setValueAt(s5, minend, 3);

            for (int j = 1; j < sonut + 1; j++) {
                if (minstart == j - 1 && minstart != nutdau) {
                    s5.insert(0, table.model.getValueAt(j - 1, 3));
                    s5.delete(s5.length() - 3, s5.length() - 2);
                }
            }
        }
        if (test == false)
            JOptionPane.showMessageDialog(parent, "Update error");
    }

    public String intToString(int i) {
        String s = "";
        char c = 'a';
        c += i;
        return s + c;
    }

    public int CharToInt(char x){
        int a = 0;
        char c = 'a';
        a = x - c;
        return a;
    }

    public void paint(Graphics g) {
        minkc = 0;
        minnut = MAXNUT;
        minstart = sonut;
        minend = sonut;

        for (int i = 0; i < sonut ; i++) { //khởi tạo mảng ban đầu chưa thay đổi
            dathaydoi[i] = false;
        }
        sothaydoi = 0;
        nutKe = 0;

        Graphics2D g2 = (Graphics2D) g;
        Font plainFont = new Font("Verdana", Font.PLAIN, 18);

        g2.setFont(plainFont);
        g2.setColor(Color.black);

        if(buoc == 1)
            s =       "  BẮT ĐẦU: " +
                    "\n- Các NÚT ĐỎ là những nút có thể" +
                    "\n  đi tới từ nút bắt đầu." +
                    "\n- Các NÚT XANH DƯƠNG ĐẬM đã từng" +
                    "\n  là ứng viên"+
                    "\n- NÚT XANH DƯƠNG NHẠT là ứng viên" +
                    "\n  cuối cùng.\n";
        else
            s =        " Bước: "+ buoc + ":" +
                    "\n- Những MŨI TÊN XANH trỏ tới những " +
                    "\n  nút có thể tới được từ những nút  " +
                    "\n  đã có k/c Cuối(đã xét)."+
                    "\n- Những MŨI TÊN ĐỎ là đường đi ngắn"+
                    "\n  nhất tới các nút đã xét.\n";

        //ve 1 mui ten moi cho vi tri hien tai
        if (muitenmoi)
            g2.drawLine(nut[nut1].x, nut[nut1].y, thispoint.x, thispoint.y);

        //Vẽ tất cả mũi tên
          int countAlg = 0;
          for (int i = 0; i < sonut; i++) {
              for (int j = 0; j < sonut; j++) {
                  if (weight[i][j] > 0) {
                      if (performalg) {// Nếu thuật toán đang chạy, thực hiện thuật toán theo để vẽ mũi tên
                          if (Algorithm == 1) {
                              detailsDijkstra(g, i, j);
                          }
                          else if(Algorithm == 2 && countAlg == 0 && Visited[j] == false){
                              detailBFS(g, i, j);
                              countAlg++;
                          }
                           else if (Algorithm == 3) {
                              detailDFS(g, i, j);
                          }
                      }
                      veMuiTen(g, i, j);
                  }
              }
         }
        if(performalg == true)
            colornut[minend] = new Color(0,	178,	191);

         //Vẽ các nút
        for (int i = 0; i < sonut; i++)
            if (nut[i].x > 0) {
                g2.setColor(colornut[i]);
                g2.fillOval(nut[i].x - NUTRADIX, nut[i].y - NUTRADIX, NUTSIZE, NUTSIZE);
            }

        //hiệu ứng khi nút đầu được di chuyển
        if(dichuyennutdau)
            g2.fillOval(thispoint.x - NUTRADIX, thispoint.y -NUTRADIX, NUTSIZE, NUTSIZE);

        // hoàn thành 1 bước và in ra text documentation
        if(performalg)
            if(Algorithm == 1)
                ketThucBuoc(g);
            if(Algorithm == 2 || Algorithm == 3)
                ketThucBuocBD(g);
        //tao ra stt dinh ben canh nut.
        for (int i=0; i < sonut; i++)
            if (nut[i].x > 0){

                if(i == nutdau){
                    g2.setColor(Color.red);
                    g2.setStroke(new BasicStroke(2));
                }else{
                    g2.setColor(Color.black);
                    g2.setStroke(new BasicStroke(1));
                }
                g2.drawOval(nut[i].x-NUTRADIX, nut[i].y-NUTRADIX, NUTSIZE, NUTSIZE);
                g2.setColor(Color.blue);
                g2.drawString(intToString(i), nut[i].x-14, nut[i].y-20);
            }
    }
}