package com.jc.sys.ext;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.json.JSONException;
import org.json.JSONObject;

import com.jc.dao.BankMapper;
import com.jc.dao.MerchantMapper;
import com.jc.dao.UserMapper;
import com.jc.entity.Bank;
import com.jc.entity.Merchant;
import com.jc.entity.User;
import com.jc.entity.UserExample;
import com.jc.util.Coder;
import com.jc.util.MyBatisUtil;

/**
 * 
 * @author qianjia
 * 2015.6.5
 */
@WebServlet("/sys/AddUser.ext")
public class AddUser extends HttpServlet{

	private static final long serialVersionUID = -3692170308778424641L;
	

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject result = new JSONObject();
		String desc="";
		boolean flag=false;
		String addtype = req.getParameter("addtype");
		String merchantid = req.getParameter("merchantid");
		String username = req.getParameter("username");
		String loginname = req.getParameter("loginname");
		String password = req.getParameter("password");
		String contact = req.getParameter("contact");
		String phone = req.getParameter("phone");
		String address = req.getParameter("address");
		SqlSession session = MyBatisUtil.getSession();
		MerchantMapper mapper2 = session.getMapper(MerchantMapper.class);
		UserMapper mapper = session.getMapper(UserMapper.class);
		UserExample example=new UserExample();
		example.or().andLoginnameEqualTo(loginname);
		List<User> uiUsers = mapper.selectByExample(example);
		try{
			if(uiUsers!=null&&uiUsers.size()>0){
				flag=false;
				desc="该登陆账号名已被注册";
			}else{
				if(addtype.equals("1")){
					User user=new User();
					user.setAddress(address);
					user.setContact(contact);
					user.setLoginname(loginname);
					user.setMaketime(Coder.formatTime(new Date()));
					user.setMerchantid(Integer.valueOf(merchantid));
					String mername=mapper2.selectByPrimaryKey(Integer.valueOf(merchantid)).getMername();
					user.setMername(mername);
					user.setPassword(Coder.EncoderByMd5(password));
					user.setPhone(phone);
					user.setStatus("1");
					user.setUsername(username);
					mapper.insert(user);
				}else if(addtype.equals("2")){
					User user=new User();
					user.setAddress(address);
					user.setContact(contact);
					user.setLoginname(loginname);
					user.setMaketime(Coder.formatTime(new Date()));
					user.setMerchantid(Integer.valueOf(merchantid));
					String mername=mapper2.selectByPrimaryKey(Integer.valueOf(merchantid)).getMername();
					user.setMername(mername);
					user.setPassword(Coder.EncoderByMd5(password));
					user.setPhone(phone);
					user.setStatus("2");
					user.setUsername(username);
					mapper.insert(user);
				}else if(addtype.equals("3")){
					User user=new User();
					user.setAddress(address==null?"":address);
					user.setContact(contact==null?"":contact);
					user.setPhone(phone==null?"":phone);
					user.setLoginname(loginname);
					user.setMaketime(Coder.formatTime(new Date()));
					user.setMerchantid(-1);
					user.setMername("后台一般管理员");
					user.setPassword(Coder.EncoderByMd5(password));
					user.setStatus("3");
					user.setUsername(username);
					mapper.insert(user);
				}else if(addtype.equals("4")){
					User user=new User();
					user.setAddress(address==null?"":address);
					user.setContact(contact==null?"":contact);
					user.setPhone(phone==null?"":phone);
					user.setLoginname(loginname);
					user.setMaketime(Coder.formatTime(new Date()));
					user.setMerchantid(-1);
					user.setMername("后台运营管理员");
					user.setPassword(Coder.EncoderByMd5(password));
					user.setStatus("4");
					user.setUsername(username);
					mapper.insert(user);
				}else if(addtype.equals("SH")){
					Merchant csMerchant=new Merchant();
					csMerchant.setAddress(address);
					csMerchant.setContact(contact);
					csMerchant.setMaketime(Coder.formatTime(new Date()));
					csMerchant.setMername(username);
					csMerchant.setPhone(phone);
					csMerchant.setStore(0);
					mapper2.insert(csMerchant);
				}else if(addtype.equals("BANK")){
					String bankname = req.getParameter("bankname");
					String bankuser = req.getParameter("bankuser");
					String bankno = req.getParameter("bankno");
					BankMapper mapper3 = session.getMapper(BankMapper.class);
					Bank bank=new Bank();
					bank.setBankname(bankname);
					bank.setBankno(bankno);
					bank.setBankuser(bankuser);
					bank.setMerchantid(Integer.valueOf(merchantid));
					mapper3.insert(bank);
				}
				flag=true;
				desc="添加成功";
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}finally{
			session.close();
		}
		try {
			result.put("flag", flag);
			result.put("desc", desc);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		PrintWriter out = resp.getWriter();
		out.print(result.toString());
		out.close();
	}

}