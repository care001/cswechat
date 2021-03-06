package com.jc.filters;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jc.dao.PowerMapper;
import com.jc.entity.Power;
import com.jc.entity.User;
import com.jc.util.MyBatisUtil;

/**
 * Set string filter UTF-8
 * @author tom
 * 2015.12.30
 */
public class CharsetFilter implements Filter{
	
	private static Logger logger = Logger.getLogger(CharsetFilter.class);
	
	private FilterConfig config = null;
	private final static int TODOIT=0;
	private final static int TOLOGIN=1;
	private final static int NOPOWER=2;
	
	@Override
    public void destroy() {
        logger.info("CharsetFilter already destroy");
    }
	
    @Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
	                 FilterChain chain) throws IOException, ServletException {
	     HttpServletRequest request = (HttpServletRequest)arg0;
	     HttpServletResponse response = (HttpServletResponse)arg1;
	     request.setCharacterEncoding(config.getInitParameter("charset"));
	     response.setContentType(config.getInitParameter("contentType"));
	     response.setCharacterEncoding(config.getInitParameter("charset"));
	     response.setHeader("Access-Control-Allow-Origin", "*");
	     response.setHeader("Access-Control-Request-Method", "GET, POST");
	     String url = request.getRequestURL().toString();
	     if(offVerify(url)){
				int towhere=toWhere(url, request);
				if(towhere==TOLOGIN){
					goLogin(response,request.getContextPath() + "/admin/login.html");
				}else if(towhere==NOPOWER){
					goLogin(response,request.getContextPath() + "/admin/NoPower.html");
				}else{
					chain.doFilter(request, response);
				}
				
			}else {
				chain.doFilter(request, response);
			}	
	 }
	
    @Override
	public void init(FilterConfig arg0) throws ServletException {
	     this.config = arg0;
	     logger.info("CharsetFilter initialize");
	}
    private void goLogin(HttpServletResponse resp,String url) throws IOException{
    	  PrintWriter out=resp.getWriter();
    	  out.println("<html>");
    	  out.println("<script>");
    	  out.println("window.open('"+url+"','_top')");
    	  out.println("</script>");
    	  out.println("</html>");
    	 }
    //过滤
    private boolean offVerify(String url){
    	boolean verify=!url.endsWith(".css")&&!url.endsWith(".js")&&!url.endsWith(".png")
				&&!url.endsWith(".jpg")&&!url.endsWith("login.html")&&!url.endsWith("admin/top.jsp")&&!url.endsWith("AdminLogin.ext")
				&&!url.endsWith("/admin/NoPower.html")&&!url.endsWith("/admin/Loginout.ext");
  	 return verify;
  	 }
    //权限判断
    private int toWhere(String url,HttpServletRequest request){
    	SqlSession session=MyBatisUtil.getSession();
		PowerMapper mapper = session.getMapper(PowerMapper.class);
		List<Power> powers=mapper.getAllPower();
		session.close();
    	for (int i = 0; i < powers.size(); i++) {
			if(url.contains(powers.get(i).getUrl())){
				HttpSession httpSession=request.getSession();
				User user=(User)httpSession.getAttribute("admin");
				if(user==null||user.getUserid()<=0){
					return TOLOGIN;//访问需要权限的url未登陆
				}else{
					boolean haspower=false;
					String[] haspowers=powers.get(i).getPower().split(",");
					for (int j = 0; j < haspowers.length; j++) {
						if (user.getStatus().equals(haspowers[j])) {
							haspower=true;
						}
						
					}
					if(haspower){
						return TODOIT;//访问需要权限的url 已授权
					}else{
						return NOPOWER;//访问需要权限的url 无权限
					}
				}
				
			}
			
		}
  	 return TODOIT;//无需权限
  	 }
    
    
}
