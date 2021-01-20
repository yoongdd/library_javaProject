package manager.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import manager.dto.BookDTO;
import manager.dto.LendDTO;
import manager.dto.MemberDTO;
import manager.dto.ReportDTO;
import managerFrame.BlackList;
import managerFrame.BookManagement;
import managerFrame.BookReport;
import managerFrame.LendBookManage;

public class ReportDAO {

	List<BookDTO> bookList = new ArrayList<BookDTO>();
	List<LendDTO> lendList = new ArrayList<LendDTO>();

	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@192.168.0.61:1521:xe";
	String username = "c##member";
	String password = "1234";
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;
	private int mon;
	SimpleDateFormat sdf = new SimpleDateFormat("M");
	int checkMon = Integer.parseInt(sdf.format(new Date()));

	public ReportDAO() {

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void getConnection() {
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateReport(int mon) {
		bookList = new BookDAO().getBookList();
		lendList = new LendDAO().getLendList();

		this.mon = mon;
		if (mon == BookReport.nowMon) {

			getConnection();

			String sql = "update book_report set all_book =?,all_lend =?,lend_ok =?"
					+ ",add_count = ?,delete_count = ?,lend_count=?" + ",over_count=? where mon_seq=?";

			System.out.println("lend사이즈" + lendList.size());

			try {
				pstmt = conn.prepareStatement(sql);

				pstmt.setInt(1, bookList.size());
				pstmt.setInt(2, lendList.size());
				pstmt.setInt(3, bookList.size() - lendList.size());
				pstmt.setInt(4, BookManagement.addCount);
				pstmt.setInt(5, BookManagement.deleteCount);
				pstmt.setInt(6, LendBookManage.lendCount);

				pstmt.setInt(7, BlackList.overCount);
				pstmt.setInt(8, mon);

				pstmt.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (pstmt != null)
						pstmt.close();
					if (conn != null)
						conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} // if

	}

	public void updateReturn(int mon) {
		bookList = new BookDAO().getBookList();
		lendList = new LendDAO().getLendList();

		this.mon = mon;
		if (mon == BookReport.nowMon) {

			getConnection();

			String sql = "update book_report set all_book =?,all_lend =?,lend_ok =? where mon_seq=?";

			try {
				pstmt = conn.prepareStatement(sql);

				pstmt.setInt(1, bookList.size());
				pstmt.setInt(2, lendList.size());
				pstmt.setInt(3, bookList.size() - lendList.size());
				pstmt.setInt(4, mon);

				pstmt.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (pstmt != null)
						pstmt.close();
					if (conn != null)
						conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} // if
	}

	public void updateDelete(int mon) {
		bookList = new BookDAO().getBookList();
		lendList = new LendDAO().getLendList();

		this.mon = mon;
		if (mon == BookReport.nowMon) {

			getConnection();

			String sql = "update book_report set all_book =?,all_lend =?,lend_ok =?"
					+ ",delete_count = ? where mon_seq=?";

			System.out.println("lend사이즈" + lendList.size());

			try {
				pstmt = conn.prepareStatement(sql);

				pstmt.setInt(1, bookList.size());
				pstmt.setInt(2, lendList.size());
				pstmt.setInt(3, bookList.size() - lendList.size());
				pstmt.setInt(4, BookManagement.deleteCount);
				pstmt.setInt(5, mon);

				pstmt.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (pstmt != null)
						pstmt.close();
					if (conn != null)
						conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} // if

	}

	public void updateAdd(int mon) {
		bookList = new BookDAO().getBookList();
		lendList = new LendDAO().getLendList();

		this.mon = mon;
		if (mon == BookReport.nowMon) {

			getConnection();

			String sql = "update book_report set all_book =?,all_lend =?,lend_ok =?" + ",add_count = ? where mon_seq=?";

			System.out.println("lend사이즈" + lendList.size());

			try {
				pstmt = conn.prepareStatement(sql);

				pstmt.setInt(1, bookList.size());
				pstmt.setInt(2, lendList.size());
				pstmt.setInt(3, bookList.size() - lendList.size());
				pstmt.setInt(4, BookManagement.addCount);
				pstmt.setInt(5, mon);

				pstmt.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (pstmt != null)
						pstmt.close();
					if (conn != null)
						conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} // if

	}

	public void updateLend(int mon) {
		bookList = new BookDAO().getBookList();
		lendList = new LendDAO().getLendList();

		this.mon = mon;
		if (mon == BookReport.nowMon) {

			getConnection();

			String sql = "update book_report set all_book =?,all_lend =?,lend_ok =?"
					+ ",lend_count = ? where mon_seq=?";

			System.out.println("lend사이즈" + lendList.size());

			try {
				pstmt = conn.prepareStatement(sql);

				pstmt.setInt(1, bookList.size());
				pstmt.setInt(2, lendList.size());
				pstmt.setInt(3, bookList.size() - lendList.size());
				pstmt.setInt(4, LendBookManage.lendCount);
				pstmt.setInt(5, mon);

				pstmt.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (pstmt != null)
						pstmt.close();
					if (conn != null)
						conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} // if
	}

	public List<ReportDTO> showReport() {
		bookList = new BookDAO().getBookList();
		lendList = new LendDAO().getLendList();

		getConnection();

		List<ReportDTO> reportList = new ArrayList<ReportDTO>();
		String sql = "select*from book_report";

		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ReportDTO reportDTO = new ReportDTO();
				reportDTO.setMon_seq(rs.getInt("mon_seq"));
				reportDTO.setAllBook(rs.getInt("all_book"));
				reportDTO.setAllLend(rs.getInt("all_lend"));
				reportDTO.setLengOk(rs.getInt("lend_ok"));
				reportDTO.setAddCount(rs.getInt("add_count"));
				reportDTO.setDeleteCount(rs.getInt("delete_count"));
				reportDTO.setLendCount(rs.getInt("lend_count"));
				reportDTO.setOverCount(rs.getInt("over_count"));

				reportList.add(reportDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return reportList;
	}

	public void memberUpdate(int mon) {

		bookList = new BookDAO().getBookList();
		lendList = new LendDAO().getLendList();

		if (mon == checkMon) {

			getConnection();

			String sql = "update book_report set all_book =?,all_lend =?,lend_ok =?,lend_count=lend_count+1  where mon_seq=?";

			System.out.println("멤버 리포트 진입");

			try {
				pstmt = conn.prepareStatement(sql);

				pstmt.setInt(1, bookList.size());
				pstmt.setInt(2, lendList.size());
				pstmt.setInt(3, bookList.size() - lendList.size());
				pstmt.setInt(4, mon);

				pstmt.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (pstmt != null)
						pstmt.close();
					if (conn != null)
						conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} // if
	}

}
