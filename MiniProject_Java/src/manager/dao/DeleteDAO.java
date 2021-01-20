package manager.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import manager.dto.BookDTO;
import manager.dto.DeleteDTO;

public class DeleteDAO {

	// XML
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@192.168.0.61:1521:xe";
	private String user = "c##member";
	private String password = "1234";
	private String sql;

	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	public DeleteDAO() {

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
	public List<DeleteDTO> getDeleteList() {

		List<DeleteDTO> list = new ArrayList<DeleteDTO>();

		getConnection();
		
		sql = "select * from delete2_table";

		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				DeleteDTO deleteDTO = new DeleteDTO();
				deleteDTO.setCode(rs.getInt("code"));
				deleteDTO.setTitle(rs.getString("title"));
				deleteDTO.setAuthor(rs.getString("author"));
				deleteDTO.setPublisher(rs.getString("publisher"));
				deleteDTO.setGenre(rs.getString("genre"));
				
				//0522 추가
				deleteDTO.setDeleteDate(rs.getDate("deleteDate"));

				list.add(deleteDTO);
				
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
	

	public void discardBook(DeleteDTO deleteDTO) {

		getConnection();
		sql = "insert into delete2_table values(seq_delete.nextval,?,?,?,?,?, sysdate)";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, deleteDTO.getCode());
			pstmt.setString(2, deleteDTO.getTitle());
			pstmt.setString(3, deleteDTO.getAuthor());
			pstmt.setString(4, deleteDTO.getPublisher());
			pstmt.setString(5, deleteDTO.getGenre());

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
