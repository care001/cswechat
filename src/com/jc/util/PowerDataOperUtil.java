package com.jc.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.jc.dao.ConsumeMapper;
import com.jc.entity.Consume;
import com.jc.entity.ConsumeExample;

public class PowerDataOperUtil {
	
	public static Map<String, Object> getHisByPower(Map<String, Object> map,String power){
		Map<String, Object> retmap=new HashMap<String, Object>();
		List<Consume> consumes=new ArrayList<Consume>();
		int conssum=0;
		int pagesize= (Integer) map.get("pagesize");
		int pagestart=(Integer)map.get("pagestart");
		SqlSession session = MyBatisUtil.getSession();
		ConsumeMapper mapper = session.getMapper(ConsumeMapper.class);
		ConsumeExample example=new ConsumeExample();
		if(power.equals("1")){
			example.or().andCsstatusEqualTo("1").andUseridEqualTo((Integer)map.get("userid")).andMaketimeBetween((String)map.get("seardate1"), (String)map.get("seardate2"));
			conssum=mapper.countByExample(example);//获取数据量
			example.setOrderByClause("maketime");
			example.setLimitSize(pagesize);
			example.setLimitStart(pagestart);
			consumes=mapper.selectByExample(example);
			
		}else if(power.equals("2")){
			example.or().andCsstatusEqualTo("1").andMerchantidEqualTo((Integer)map.get("Merchantid")).andMaketimeBetween((String)map.get("seardate1"), (String)map.get("seardate2"));
			conssum=mapper.countByExample(example);//获取数据量
			example.setOrderByClause("maketime");
			example.setLimitSize(pagesize);
			example.setLimitStart(pagestart);
			consumes=mapper.selectByExample(example);
			
		}else if(power.equals("3")||power.equals("4")){
			example.or().andCsstatusEqualTo("1").andMaketimeBetween((String)map.get("seardate1"), (String)map.get("seardate2"));
			conssum=mapper.countByExample(example);//获取数据量
			example.setOrderByClause("maketime");
			example.setLimitSize(pagesize);
			example.setLimitStart(pagestart);
			consumes=mapper.selectByExample(example);
			
		}else{
			
		}
		if(consumes!=null&&consumes.size()>0){
			retmap.put("consize", consumes.size());
			retmap.put("consumes", consumes);
		}else{
			retmap.put("consize", "0");
			retmap.put("consumes", consumes);
		}
		retmap.put("pagesum", conssum);
		if(conssum%pagesize==0){
			if(conssum/pagesize==0){
				retmap.put("pageall", 1);
			}else{
				retmap.put("pageall", conssum/pagesize);
			}
		}else{
			retmap.put("pageall", (conssum/pagesize)+1);
		}
		session.close();
		return retmap;
	}
	
	public static List<Consume> getAllHisByPower(Map<String, Object> map,String power){
		List<Consume> consumes=new ArrayList<Consume>();
		SqlSession session = MyBatisUtil.getSession();
		ConsumeMapper mapper = session.getMapper(ConsumeMapper.class);
		ConsumeExample example=new ConsumeExample();
		if(power.equals("1")){
			example.or().andCsstatusEqualTo("1").andUseridEqualTo((Integer)map.get("userid")).andMaketimeBetween((String)map.get("seardate1"), (String)map.get("seardate2"));
			example.setOrderByClause("userid,maketime");
			consumes=mapper.selectByExample(example);
		}else if(power.equals("2")){
			example.or().andCsstatusEqualTo("1").andMerchantidEqualTo((Integer)map.get("Merchantid")).andMaketimeBetween((String)map.get("seardate1"), (String)map.get("seardate2"));
			example.setOrderByClause("userid,maketime");
			consumes=mapper.selectByExample(example);
		}else if(power.equals("3")||power.equals("4")){
			example.or().andCsstatusEqualTo("1").andMaketimeBetween((String)map.get("seardate1"), (String)map.get("seardate2"));
			example.setOrderByClause("userid,maketime");
			consumes=mapper.selectByExample(example);
		}else{
			
		}
		session.close();
		return consumes;
	}
	
	
}
