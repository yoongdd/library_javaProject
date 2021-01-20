package managerFrame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;


public class BasicFrameManager extends JFrame {
    public BookManagement p1;
    public LendBookManage p2;
    public BookReport p3;
    public BlackList p4;
    public ManagerManagement p5;
    public MemberManagement p6;
    public DeleteBookList p7;
    

    public BasicFrameManager() {
      	
        super("Library");

        JTabbedPane t = new JTabbedPane(); // JTabbedPane생성

        p1 = new BookManagement();
        p2 = new LendBookManage();
        p3 = new BookReport();
        p4 = new BlackList();
        p5 = new ManagerManagement();
        p6 = new MemberManagement();
        p7 = new DeleteBookList();

        JTabbedPane tab = new JTabbedPane();
        tab.add(p1,"도서관리");
        tab.add(p7, "삭제도서관리");
        tab.add(p2,"대여관리");
        tab.add(p3,"도서현황");
        tab.add(p4,"연체자");
        tab.add(p5,"관리자관리");
        tab.add(p6,"회원관리");
        
        add(tab);
        
        setBounds(700, 100, 1100, 800);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                int result=  JOptionPane.showConfirmDialog(null, "종료하시겠습니까?");
                if(result == JOptionPane.YES_OPTION)
                    System.exit(0);
            }
        });
            
    }    

    public static void main(String[] args) {
        new BasicFrameManager();
    }

}

