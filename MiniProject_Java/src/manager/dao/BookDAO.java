package manager.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import manager.dto.BookDTO;

public class BookDAO {

	// XML
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@192.168.0.61:1521:xe";
	private String user = "c##member";
	private String password = "1234";
	private String sql;

	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	public BookDAO() {

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void getConnection() {
		try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 도서정보 list -> 테이블에 추가
	public List<BookDTO> getBookList() {

		List<BookDTO> list = new ArrayList<BookDTO>();

		getConnection();
		sql = "select * from book_table";

		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				BookDTO bookDTO = new BookDTO();
				bookDTO.setCode(rs.getInt("code"));
				bookDTO.setTitle(rs.getString("title"));
				bookDTO.setAuthor(rs.getString("author"));
				bookDTO.setPublisher(rs.getString("publisher"));
				bookDTO.setGenre(rs.getString("genre"));
				bookDTO.setLendCheck(rs.getInt("lendCheck"));

				list.add(bookDTO);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			list = null; // 예외가 발생하면 list에 잘못된 data가 저장되므로 list를 null로 초기화 시켜준다
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
		return list;
	}

	// DB에서 시퀀스 가져오기
	public int getSeqBook() {
		int seq = 0;

		getConnection();
		sql = "select code.nextval from dual";

		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			rs.next();
			seq = rs.getInt(1);

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
		return seq;
	}

	// 도서정보 등록
	public void bookInsert(BookDTO bookDTO) {

		getConnection();
		sql = "insert into book_table values(?,?,?,?,?,?)";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bookDTO.getCode());// ---------------------------------0515
			pstmt.setString(2, bookDTO.getTitle());
			pstmt.setString(3, bookDTO.getAuthor());
			pstmt.setString(4, bookDTO.getPublisher());
			pstmt.setString(5, bookDTO.getGenre());
			pstmt.setInt(6, bookDTO.getLendCheck());

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
	}

	// 도서정보 수정
	public void bookUpdate(BookDTO bookDTO) {

		getConnection();
		sql = "update book_table set title=?, author=?, publisher=?, genre=?, lendCheck=? where code = ?";
		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(6, bookDTO.getCode()); // --------------------0515
			pstmt.setString(1, bookDTO.getTitle());
			pstmt.setString(2, bookDTO.getAuthor());
			pstmt.setString(3, bookDTO.getPublisher());
			pstmt.setString(4, bookDTO.getGenre());
			pstmt.setInt(5, bookDTO.getLendCheck());

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
	}

	// 도서정보 삭제
	public void bookDelete(BookDTO bookDTO) {

		getConnection();
		sql = "delete book_table where code = ?"; // ---------------0515

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bookDTO.getCode()); // ---------------0515

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
	}

}
