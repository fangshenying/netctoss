package web;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.net.httpserver.spi.HttpServerProvider;

import dao.AdminDao;
import dao.CostDao;
import entity.Admin;
import entity.Cost;
import util.ImageUtil;

public class MainServlet extends HttpServlet {

	@Override
	protected void service(
			HttpServletRequest req, 
			HttpServletResponse res) throws ServletException, IOException {
			//获取请求路径	
			String path = req.getServletPath();
			//根据规范做出判断及处理
			if("/findCost.do".equals(path)) {
				findCost(req,res);
			} else if("/toAddCost.do".equals(path)){
				toAddCost(req,res);
			} else if ("/addCost.do".equals(path)) {
				addCost(req, res);
			} else if("/toUpdateCost.do".equals(path)) {
				toUpdateCost(req,res);
			} else if("/updateCost.do".equals(path)) {
				updateCost(req, res);
			} else if("/toLogin.do".equals(path)) {
				toLogin(req,res);
			} else if("/toIndex.do".equals(path)) {
				toIndex(req,res);
			} else if("/login.do".equals(path)) {
				login(req,res);
			} else if("/toDeleteCost.do".equals(path)){
				DeleteCost(req,res);
			} else if("/createImg.do".equals(path)) {
				createImg(req,res);
			} else if("/modifyStatus.do".equals(path)) {
				modifyStatus(req,res);
			} else if("/logOut.do".equals(path)) {
				logOut(req,res);
			} else {
				throw new RuntimeException("查无此页");
			}
	}
	
	//查询所有的资费
	protected void findCost(
			HttpServletRequest req, 
			HttpServletResponse res) throws ServletException, IOException {
			//查询所有的资费
			CostDao dao = new CostDao();
			List<Cost> list = dao.findAll();
			//转发到查询的jsp
			req.setAttribute("costs", list);
			//当前：/netctoss/findCost.do
			//目标：/netctoss/WEB-INF/cost/find.jsp
			req.getRequestDispatcher("WEB-INF/cost/find.jsp").forward(req, res);
	}
	
	//打开增加资费的页面
	protected void toAddCost(
			HttpServletRequest req, 
			HttpServletResponse res) throws ServletException, IOException {
			//当前：/netctoss/toAddCost.do
			//目标：/netctoss/WEB-INF/cost/add.jsp
			req.getRequestDispatcher("WEB-INF/cost/add.jsp").forward(req, res);
	}
	
	//增加资费
	protected void addCost(
			HttpServletRequest req, 
			HttpServletResponse res) throws ServletException, IOException {
			//避免中文乱码
			req.setCharacterEncoding("utf-8");
			Cost c = packagingData(req);
			CostDao dao = new CostDao();
			dao.save(c);
			//重定向到资费查询
			//当前：//netctoss/addCost.do
			//目标://netctoss/findCost.do
			res.sendRedirect("findCost.do");
	}

	//接收表单发来的数据，保存在一个Cost对象中返回
	private Cost packagingData(HttpServletRequest req) throws UnsupportedEncodingException {
		//接收表单提交的数据
		String name = req.getParameter("name");
		String costType = req.getParameter("costType");
		String baseDuration = req.getParameter("baseDuration");
		String baseCost = req.getParameter("baseCost");
		String unitCost = req.getParameter("unitCost");
		String descr = req.getParameter("descr");
		//封装并保存这些数据
		Cost c = new Cost();
		c.setName(name);
		c.setCostType(costType);
		c.setDescr(descr);
		//判断为空时要判的尽量全一点
		if(baseDuration != null && !baseDuration.equals("")){
			c.setBaseDuration(new Integer(baseDuration));
		}
		if(baseCost != null && !baseCost.equals("")){
			c.setBaseCost(new Double(baseCost));
		}
		if(unitCost != null && !unitCost.equals("")){
			c.setUnitCost(new Double(unitCost));
		}
		return c;
	}
	
	//打开修改资费界面
	protected void toUpdateCost(
			HttpServletRequest req, 
			HttpServletResponse res) throws ServletException, IOException {
			//接受参数
			String id = req.getParameter("id");
			//查询要修改的资费
			CostDao dao = new CostDao();
			Cost cost = dao.findById(new Integer(id));
			//将其转发到修改页面
			req.setAttribute("cost", cost);
			req.getRequestDispatcher("WEB-INF/cost/update.jsp").forward(req, res);
	}

	
	//保存资费信息的修改
	protected void updateCost(
			HttpServletRequest req, 
			HttpServletResponse res) throws ServletException, IOException {
			//避免中文乱码
			req.setCharacterEncoding("utf-8");
			Cost c = packagingData(req);
			String costId = req.getParameter("costId");
			if(costId != null && !costId.equals("")){
				c.setCostId(new Integer(costId));
			}
			CostDao dao = new CostDao();
			dao.update(c);;
			//重定向到资费查询
			res.sendRedirect("findCost.do");
			
			
	}
	
	
	//打开登录页面
	protected void toLogin(
			HttpServletRequest req, 
			HttpServletResponse res) throws ServletException, IOException {
			req.getRequestDispatcher("WEB-INF/main/login.jsp").forward(req, res);
	}
	
	//打开主页
	protected void toIndex(
			HttpServletRequest req, 
			HttpServletResponse res) throws ServletException, IOException {
			req.getRequestDispatcher("WEB-INF/main/index.jsp").forward(req, res);
	}
	
	//登录验证
	protected void login(
			HttpServletRequest req, 
			HttpServletResponse res) throws ServletException, IOException {
			//获取传入的表单数据
			String adminCode = req.getParameter("adminCode");
			String password = req.getParameter("password");
			String code = req.getParameter("code");
			//检查验证码
			HttpSession session = req.getSession();
			String imgcode = (String)session.getAttribute("imgcode");
			if(code == null || code.equals("") || !code.equalsIgnoreCase(imgcode)) {
				req.setAttribute("error", "验证码错误");
				req.getRequestDispatcher("WEB-INF/main/login.jsp").forward(req, res);
				return;
			}
			//验证帐号和密码
			AdminDao dao = new AdminDao();
			Admin a = dao.findByCode(adminCode);
			if(a==null) {
				//帐号错误,转发到登录页面
				req.setAttribute("error", "帐号错误");
				req.getRequestDispatcher("WEB-INF/main/login.jsp").forward(req, res);
			} else if(!a.getPassword().equals(password)) {
				//密码错误，转发到登录页面
				req.setAttribute("error", "密码错误");
				req.getRequestDispatcher("WEB-INF/main/login.jsp").forward(req, res);
			} else {
				//方法一、设置cookie
				//将帐号存入cookie，后续资费查询、增加、修改等页面上要显示它。
				Cookie c = new Cookie("adminCode",adminCode);
				//此cookie的有效路径为/netctoss
				res.addCookie(c);
				//方法二、将帐号存入session，后续资费查询、增加、修改等页面上要显示它。
				//HttpSession session = req.getSession();
				session.setAttribute("adminCode", adminCode);
				//验证通过,重定向到主页
				res.sendRedirect("toIndex.do");
			}
			
	}
	
	//删除资费
	protected void DeleteCost(
			HttpServletRequest req, 
			HttpServletResponse res) throws ServletException, IOException {
			//获取页面传过来的参数
			String id = req.getParameter("id");
			//删除选中的资费
			CostDao dao = new CostDao();
			dao.deleteById(new Integer(id));
			//将其重定向到查询页面
			res.sendRedirect("findCost.do");
			
	}
	
	//修改资费状态
	protected void modifyStatus(
			HttpServletRequest req, 
			HttpServletResponse res) throws ServletException, IOException {
			//获取页面传过来的参数
			String id = req.getParameter("id");
			//修改选中的资费的状态
			CostDao dao = new CostDao();
			dao.modifyById(new Integer(id));
			//将其重定向到查询页面
			res.sendRedirect("findCost.do");
			
	}
	
	//生成验证码
	protected void createImg(
			HttpServletRequest req, 
			HttpServletResponse res) throws ServletException, IOException {
			//生成验证码及图片
			Object[] objs = ImageUtil.createImage();
			//将验证码存入session
			String imgcode = (String)objs[0];
			HttpSession session = req.getSession();
			session.setAttribute("imgcode", imgcode);
			//将图片输出给浏览器
			res.setContentType("image/jpeg");
			BufferedImage img = (BufferedImage)objs[1];
			//该输出流由tomcat创建，目标是浏览器
			OutputStream os = res.getOutputStream();
			ImageIO.write(img, "jpeg", os);
			os.close();
	}
	
	//退出系统，删除session对象
	protected void logOut(
			HttpServletRequest req, 
			HttpServletResponse res) throws ServletException, IOException {
			//获取session对象
			HttpSession session = req.getSession();
			//立即删除session对象
			session.invalidate();
			//重定向到登陆页面
			res.sendRedirect("toLogin.do");
		
	}
	
}










