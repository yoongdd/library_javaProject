package managerFrame;

import javax.swing.JPanel;

import manager.dao.BookDAO;
import manager.dao.ReportDAO;
import manager.dto.BookDTO;
import manager.dto.MemberDTO;
import manager.dto.ReportDTO;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;

public class BookReport extends JPanel implements ActionListener {
	private JLabel monthL;
	private JLabel totBookNumL,totLendNumL,lenPosNumL,delBookNumL
	,addBookNumL,mOverBookNumL,mLendBookNumL;
	private JButton leftBtn, rightBtn;
	SimpleDateFormat sdf;
	public static int mon;
	public static int nowMon;
	public static List<ReportDTO> reportList= new ArrayList<ReportDTO>();
	private ReportDAO reportDAO =new ReportDAO();
	
	
	
	public void paintComponent(Graphics g) {
		Dimension d = getSize();
		ImageIcon img = new ImageIcon("Images//bookreport.jpg");
		g.drawImage(img.getImage(), 0, 0, d.width, d.height, null);
	}
	
	public BookReport() {
		setLayout(null);
		setBackground(Color.WHITE);
		sdf = new SimpleDateFormat("M");
		mon = Integer.parseInt(sdf.format(new Date()));
		nowMon =Integer.parseInt(sdf.format(new Date()));
		
		
		monthL = new JLabel();
		monthL.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		monthL.setText(sdf.format(new Date()) + "월");
		monthL.setBounds(535, 206, 124, 51);
		add(monthL);

		JLabel totBookL = new JLabel("전체 도서:");
		totBookL.setBounds(488, 296, 80, 25);
		add(totBookL);

		 totBookNumL = new JLabel("0");
		totBookNumL.setBounds(590, 304, 57, 15);
		add(totBookNumL);

		JLabel totLendL = new JLabel("전체 대여:");
		totLendL.setBounds(488, 339, 80, 25);
		add(totLendL);

		 totLendNumL = new JLabel("0");
		totLendNumL.setBounds(590, 344, 57, 15);
		add(totLendNumL);

		JLabel lenPosL = new JLabel("대여 가능 도서:");
		lenPosL.setBounds(466, 374, 112, 25);
		add(lenPosL);

		 lenPosNumL = new JLabel("0");
		lenPosNumL.setBounds(590, 385, 57, 15);
		add(lenPosNumL);

		JLabel delBookL = new JLabel("삭제 도서:");
		delBookL.setBounds(488, 455, 80, 25);
		add(delBookL);

		 delBookNumL = new JLabel("0");
		delBookNumL.setBounds(590, 460, 57, 15);
		add(delBookNumL);

		JLabel addBookL = new JLabel("추가 도서:");
		addBookL.setBounds(488, 417, 80, 25);
		add(addBookL);

		 addBookNumL = new JLabel("0");
		addBookNumL.setBounds(590, 422, 57, 15);
		add(addBookNumL);

		JLabel mLendBookL = new JLabel("이달 도서 대출 수:");
		mLendBookL.setBounds(443, 490, 103, 25);
		add(mLendBookL);

		 mLendBookNumL = new JLabel("0");
		mLendBookNumL.setBounds(590, 495, 57, 15);
		add(mLendBookNumL);


		leftBtn = new JButton(new ImageIcon("Images//left.png"));
		leftBtn.setBounds(378, 195, 84, 62);
		leftBtn.setBorderPainted(false);
		leftBtn.setDisabledIcon(new ImageIcon("Images//비활성.png"));
		leftBtn.addActionListener(this);
		if (mon == 1)
			leftBtn.setEnabled(false);
		add(leftBtn);

		rightBtn = new JButton(new ImageIcon("Images//right.png"));
		rightBtn.setBounds(651, 195, 84, 62);
		rightBtn.setBorderPainted(false);
		rightBtn.setDisabledIcon(new ImageIcon("Images//비활성오.png"));
		rightBtn.addActionListener(this);
		rightBtn.setEnabled(false);
		add(rightBtn);
		
		//화면 처음 띄웠을 때 해당 월 뜨게 하는

		
		int  sleepSec = 2;
		ScheduledThreadPoolExecutor now = new ScheduledThreadPoolExecutor(1);
		now.scheduleAtFixedRate(new Runnable() {
			public void run() {
				
				nowRenew();
				
			}
		}, 0, sleepSec, TimeUnit.SECONDS);
				
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {


		if (e.getSource() == leftBtn) {
			rightBtn.setEnabled(true);
			mon -= 1;
			if (mon == 1) {
				leftBtn.setEnabled(false);
			}

			monthL.setText(mon + "월");

		} else if (e.getSource() == rightBtn) {
			leftBtn.setEnabled(true);

			mon += 1;
			if (mon == Integer.parseInt(sdf.format(new Date())) || mon == 12) {
				rightBtn.setEnabled(false);
			}
			monthL.setText(mon + "월");

		}

		for (int i = 0; i < mon + 1; i++) {
			if (mon == reportList.get(i).getMon_seq()) {
				totBookNumL.setText(reportList.get(i).getAllBook() + "");
				totLendNumL.setText(reportList.get(i).getAllLend() + "");
				lenPosNumL.setText(reportList.get(i).getLengOk() + "");
				delBookNumL.setText(reportList.get(i).getDeleteCount() + "");
				addBookNumL.setText(reportList.get(i).getAddCount() + "");
				mLendBookNumL.setText(reportList.get(i).getLendCount() + "");

			}
		} // for
		
		int  sleepSec = 2;
		ScheduledThreadPoolExecutor now = new ScheduledThreadPoolExecutor(1);
		now.scheduleAtFixedRate(new Runnable() {
			public void run() {
				
				nowRenew();
				
			}
		}, 0, sleepSec, TimeUnit.SECONDS);
		
	}// ActionListener
	
	public void nowRenew() {
		reportList = reportDAO.showReport();
		
		if (nowMon == reportList.get(mon-1).getMon_seq()) {
			totBookNumL.setText(reportList.get(mon-1).getAllBook() + "");
			totLendNumL.setText(reportList.get(mon-1).getAllLend() + "");
			lenPosNumL.setText(reportList.get(mon-1).getLengOk() + "");
			delBookNumL.setText(reportList.get(mon-1).getDeleteCount() + "");
			addBookNumL.setText(reportList.get(mon-1).getAddCount() + "");
			mLendBookNumL.setText(reportList.get(mon-1).getLendCount() + "");

		}
		
		
	}
	
	
	
	
	
}
