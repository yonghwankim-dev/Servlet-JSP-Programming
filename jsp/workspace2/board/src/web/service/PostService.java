package web.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import web.entity.Post;

public class PostService {
	
	public List<Post> getAllPostList(int page)
	{
		List<Post> list = new ArrayList<Post>();
		try {
			String url = "jdbc:oracle:thin:@localhost:1521:orcl";
			String sql = "select * "
						+ "from post_view "
						+ "where num between ? and ?";
			int start = 1+(page-1)*10;
			int end = page*10;
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection(url,"board_admin","board_admin"); // 드라이버 매니저를 통해서 연결 객체 생성
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, start);
			pst.setInt(2, end);
			ResultSet rs = pst.executeQuery();
			
			while(rs.next())
			{
				Post post = new Post(
										rs.getInt("id"),
										rs.getString("title"),
										rs.getString("writer_id"),
										rs.getString("content"),
										rs.getDate("regdate"),
										rs.getInt("hit"),
										rs.getString("files"),
										rs.getBoolean("pub")
									);
				list.add(post);
			}
			
			rs.close();
			pst.close();
			con.close();	
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}
	
	public List<Post> getPostList(int page)
	{
		List<Post> list = new ArrayList<Post>();
		try {
			String url = "jdbc:oracle:thin:@localhost:1521:orcl";
			String sql = "select * "
						+ "from post_view "
						+ "where num between ? and ? and pub=1";
			int start = 1+(page-1)*10;
			int end = page*10;
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection(url,"board_admin","board_admin"); // 드라이버 매니저를 통해서 연결 객체 생성
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, start);
			pst.setInt(2, end);
			ResultSet rs = pst.executeQuery();
			
			while(rs.next())
			{
				Post post = new Post(
										rs.getInt("id"),
										rs.getString("title"),
										rs.getString("writer_id"),
										rs.getString("content"),
										rs.getDate("regdate"),
										rs.getInt("hit"),
										rs.getString("files"),
										rs.getBoolean("pub")
									);
				list.add(post);
			}
			
			rs.close();
			pst.close();
			con.close();	
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}
	
	public boolean insertPost(String title, String writer_id, String content, String files, int pub)
	{
		try {
			String url = "jdbc:oracle:thin:@localhost:1521:orcl";
			String sql = "INSERT INTO post (title, writer_id, content, files, pub) values(?,?,?,?,?)";
					
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection(url,"board_admin","board_admin"); // 드라이버 매니저를 통해서 연결 객체 생성
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setString(1, title);
			pst.setString(2, writer_id);
			pst.setString(3, content);
			pst.setString(4, files);
			pst.setInt(5, pub);
			pst.executeUpdate();
			
			pst.close();
			con.close();	
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean updatePost()
	{
		return false;
	}
	
	public boolean deletePost()
	{
		return false;
	}
	
	public int getPostCount()
	{
		int count = 0;
		try {
			String url = "jdbc:oracle:thin:@localhost:1521:orcl";
			String sql = "select count(*) as count from post";
					
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection(url,"board_admin","board_admin"); // 드라이버 매니저를 통해서 연결 객체 생성
			Statement pst = con.createStatement();
			ResultSet rs = pst.executeQuery(sql);
			
			
			if(rs.next())
			{
				count = rs.getInt("count");
			}
			
			rs.close();
			pst.close();
			con.close();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return count;
	}

	public int pubNoticeAll(List<String> oids, List<String> cids) {
		// TODO Auto-generated method stub
		String oidsCSV = String.join(",", oids);
		String cidsCSV = String.join(",", cids);
		return pubNoticeAll(oidsCSV, cidsCSV);
	}

	private int pubNoticeAll(String oidsCSV, String cidsCSV) {
		// TODO Auto-generated method stub
		String sqlOpen = String.format("update post set pub=1 where id in (%s)",oidsCSV);
		String sqlClose = String.format("update post set pub=0 where id in (%s)",cidsCSV);
		
		int result = 0;
		String url = "jdbc:oracle:thin:@localhost:1521:orcl";
		
		try 
		{	
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			Connection con = DriverManager.getConnection(url,"board_admin","board_admin"); // 드라이버 매니저를 통해서 연결 객체 생성
			Statement stOpen = con.createStatement(); // 실행 도구 생성
			result += stOpen.executeUpdate(sqlOpen);
			Statement stClose = con.createStatement(); // 실행 도구 생성
			result += stClose.executeUpdate(sqlClose);
			
			
			stOpen.close();
			stClose.close();
			con.close();
		}catch (ClassNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
