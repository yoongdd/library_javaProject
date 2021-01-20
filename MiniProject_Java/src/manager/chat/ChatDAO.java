package manager.chat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChatDAO {
	String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@192.168.0.61:1521:xe";
	private String user = "c##member";
	private String passwordDB = "1234";

	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	public ChatDAO() {
		// TODO Auto-generated constructor stub
		try {

			Class.forName(driver);
			System.out.println("드라이버 로딩 성공");

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}

	public void getConnection() {
		try {
			conn = DriverManager.getConnection(url, user, passwordDB);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public List<ChatDTO> getChatList() {

		List<ChatDTO> list = new ArrayList<ChatDTO>();
		getConnection();
		String sql = "select * from ask_table";

		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ChatDTO chatDTO = new ChatDTO();
				chatDTO.setId(rs.getString("id"));
				chatDTO.setCheck(rs.getInt("check_id"));
				chatDTO.setComplited(rs.getInt("complited"));

				list.add(chatDTO);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			list = null;
		} finally {

			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return list;
	}

	public void chatUpdate(ChatDTO chatDTO) {
		String sql;
		getConnection();
		sql = "update ask_table set check_id=? where id = ?";
		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, chatDTO.getCheck());
			pstmt.setString(2, chatDTO.getId());

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

	public void chatDelete(String id) {
		String sql;
		getConnection();
		sql = "delete from ask_table where id =?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);

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
