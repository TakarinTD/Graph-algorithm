import java.awt.*;

public class Menus extends Panel {
    private CheckboxMenuItem a, b;
    protected MainGraph parent;
    protected String msg = "";
    protected Font f = new Font("Verdana", Font.BOLD,13);
    protected Menu setting;
    protected Menu file;
    protected Menu graph;
    protected Menu kind;
    protected Menu exit;
    protected  MenuItem[] item = new MenuItem[13];

    Menus(MainGraph myparent) {
        this.parent = myparent;

        //tao ra Menubar va them vao frame
        MenuBar bar = new MenuBar();
        bar.setFont(f);
        parent.setMenuBar(bar);
        file = new Menu("File");
        file.add(item[0] = new MenuItem("New"));
        file.addSeparator();
        file.add(item[1] = new MenuItem("Open"));
        file.add(item[2] = new MenuItem("Save As"));
        file.addSeparator();
        file.add(item[3] = new MenuItem("Example"));
        bar.add(file);

        graph = new Menu("Graph");
        graph.add(item[4] = new MenuItem("Reset"));
        graph.addSeparator();
        graph.add(item[5] = new MenuItem("Clear"));
        graph.addSeparator();
        graph.add(item[6] = new MenuItem("Change Weight"));
        bar.add(graph);


        kind = new Menu("Algorithms");
        kind.add(item[7] = new MenuItem("Dijkstra"));
        kind.addSeparator();
        kind.add(item[8] = new MenuItem("BFS"));
        kind.addSeparator();
        kind.add(item[9] = new MenuItem("DFS"));
        bar.add(kind);

        setting = new Menu("Languages");
        setting.add(item[11] = new MenuItem("English"));
        setting.addSeparator();
        setting.add(item[12] = new MenuItem("Vietnamese"));
        bar.add(setting);

        exit = new Menu("Exit");
        exit.add(item[10] = new MenuItem("Exit now"));
        bar.add(exit);

        MenuHandler menuHandler = new MenuHandler(this);
        for (int i = 0; i < item.length; i++) {
            item[i].addActionListener(menuHandler);
        }

    }
}

