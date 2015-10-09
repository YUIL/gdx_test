package com.mygdx.game.database.dao.imp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mygdx.game.database.JdbcUtils;
import com.mygdx.game.database.dao.UserDao;
import com.mygdx.game.entity.User;

public class UserDaoImp implements UserDao{

	@Override
	public void addUser(User user) {
		// TODO Auto-generated method stub
		Connection conn=null;
		PreparedStatement st=null;
		ResultSet rs=null;
		try {
			String sql="insert into user (user_id,open_id) values (?,?)";
			conn=JdbcUtils.getConnection();
			st=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			st.setInt(1, user.getUserId());
			st.setString(2, user.getOpenId());
			st.executeUpdate();
			rs=st.getGeneratedKeys();
			if(rs.next()){
				user.setUserId(rs.getInt(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			JdbcUtils.free(rs, st, conn);
		}
	}

	@Override
	public User getUser(int userId) {
		// TODO Auto-generated method stub
		User user=null;
		Connection conn=null;
		PreparedStatement st=null;
		ResultSet rs=null;
		
		try {
			String sql="select * from user where user_id=?";
			conn=JdbcUtils.getConnection();
			st=conn.prepareStatement(sql);
			st.setInt(1, userId);
			rs=st.executeQuery();
			
			if (rs.next()){
				user=getUser(rs);
			}	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			JdbcUtils.free(rs, st, conn);
		}
		return user;	
	}

	@Override
	public User getUser(String openId) {
		// TODO Auto-generated method stub
		User user=null;
		Connection conn=null;
		PreparedStatement st=null;
		ResultSet rs=null;
		try {
			String sql="select * from user where open_id=?";
			conn=JdbcUtils.getConnection();
			st=conn.prepareStatement(sql);
			st.setString(1, openId);
			
			rs=st.executeQuery();
			
			if (rs.next()){
				user=getUser(rs);
			}
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			JdbcUtils.free(rs, st, conn);

		}
		return user;
	}

	@Override
	public User findUser(String loginName, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(User user) {
		// TODO Auto-generated method stub
		
	}

	public User getUser(ResultSet rs){
		User user=new User();
		try {
			user.setUserId(rs.getInt("user_id"));
			user.setOpenId(rs.getString("open_id"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return user;
	}
}
