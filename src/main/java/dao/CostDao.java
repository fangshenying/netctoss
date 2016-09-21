package dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entity.Cost;
import util.DBUtil;

public class CostDao implements Serializable {
	
	//查找资费表中的所有资费信息的方法
	public List<Cost> findAll() {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select * from cost_light order by cost_id";
			Statement smt = conn.createStatement();
			ResultSet rs = smt.executeQuery(sql);
			List<Cost> list = new ArrayList<Cost>();
			while(rs.next()) {
				Cost c = createCost(rs);
				list.add(c);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("查询资费失败",e);
		} finally {
			DBUtil.close(conn);
		}
	}
	
	//创建Cost实体类对象的方法
	private Cost createCost(ResultSet rs) throws SQLException {
		Cost c = new Cost();
		c.setCostId(rs.getInt("cost_id"));
		c.setName(rs.getString("name"));
		c.setBaseDuration(rs.getInt("base_duration"));
		c.setBaseCost(rs.getDouble("base_cost"));
		c.setUnitCost(rs.getDouble("unit_cost"));
		c.setStatus(rs.getString("status"));
		c.setDescr(rs.getString("descr"));
		c.setCreatime(rs.getTimestamp("creatime"));
		c.setStartime(rs.getTimestamp("startime"));
		c.setCostType(rs.getString("cost_type"));
		return c;
	}

	//新增加一条资费的方法
	public void save(Cost c) {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "insert into cost_light values(cost_seq_light.nextval,?,?,?,?,'1',?,sysdate,null,?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			passParam(c, ps);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("新增资费失败",e);
		} finally {
			DBUtil.close(conn);
		}
	}

	//通过传入一个Cost对象给PS赋值
	private void passParam(Cost c, PreparedStatement ps) throws SQLException {
		ps.setString(1, c.getName());
		//JDBC里setInt()和setDouble()不允许传入null,但是实际业务中这样的数据时可以为null的，
		//并且数据库也允许存null，此时可以按照Object对其进行处理。
		//不同类型的套餐要输入的数据的个数不一样。
		ps.setObject(2, c.getBaseDuration());
		ps.setObject(3, c.getBaseCost());
		ps.setObject(4, c.getUnitCost());
		ps.setString(5, c.getDescr());
		ps.setString(6, c.getCostType());
	}
	
	//通过id查找的方法
	public Cost findById(int id) {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select * from cost_light where cost_id=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			//查到数据时的操作
			while(rs.next()) {
				return createCost(rs);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("查询资费失败", e);
		} finally {
			DBUtil.close(conn);
		}
		//没有查到数据时的操作
		return null;
	}
	
	//更新方法
	public void update(Cost cost) {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "update cost_light set name=?,base_duration=?,base_cost=?,unit_cost=?,"
						 + "descr=?,cost_type=? where cost_id=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			passParam(cost, ps);
			ps.setObject(7, cost.getCostId());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("修改资费信息失败",e);
		} finally {
			DBUtil.close(conn);
		}
	}
	
	//通过Id删除资费信息
	public void deleteById(int id) {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "delete from cost_light where cost_id=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setObject(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("修改资费信息失败",e);
		} finally {
			DBUtil.close(conn);
		}
	}
	
	//通过Id修改资费信息
	public void modifyById(int id) {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "update cost_light set status=0,startime=sysdate where cost_id=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setObject(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("启用资费信息失败",e);
		} finally {
			DBUtil.close(conn);
		}
	}
	
//	//测试deleteById()方法
//	public static void main(String[] args) {
//		CostDao dao = new CostDao();
//		dao.deleteById(102);
//	}
		        
//	//测试update()
//	public static void main(String[] args) {
//		CostDao dao = new CostDao();
//		Cost c = new Cost();
//		c.setCostId(1);
//		c.setName("1.9元套餐");
//		c.setBaseDuration(20);
//		c.setBaseCost(40.0);
//		c.setUnitCost(2.5);
//		c.setDescr("1.9元套餐很便宜");
//		c.setCostType("2");
//		dao.update(c);
//	}

	
//	//测试findAll()
//	public static void main(String[] args) {
//		CostDao dao = new CostDao();
//		List<Cost> list = dao.findAll();
//		if(list != null) {
//			for(Cost c : list) {
//				System.out.println(c.getCostId() + "," + c.getName() + "," + c.getDescr());
//			}
//		}
//	}
	
//	//测试save()方法
//	public static void main(String[] args) {
//		CostDao dao = new CostDao();
//		Cost c = new Cost();
//		c.setName("包月");
//		//c.setBaseDuration(880);
//		c.setBaseCost(500.0);
//		//c.setUnitCost(0.8);
//		c.setDescr("包月真爽");
//		c.setCostType("1");
//		dao.save(c);
//	}

//	//测试findById()方法
//	public static void main(String[] args) {
//		CostDao dao = new CostDao();
//		Cost c = dao.findById(1);
//		if(c != null) {
//			System.out.println(c.getName() + "," + c.getDescr());
//		}
//	}
	
}


































