package cn.com.goldfinance.mapper.wkxf;

import java.util.List;

import cn.com.goldfinance.domain.wkxf.GrpPutMoney;


public interface GrpPutMoneyMapper {
	
	public List<GrpPutMoney> getGrpPutMoneyByAccountno(String accountno);
	
}
