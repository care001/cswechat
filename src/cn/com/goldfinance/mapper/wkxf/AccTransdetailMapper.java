package cn.com.goldfinance.mapper.wkxf;

import java.util.List;

import cn.com.goldfinance.domain.wkxf.AccTransdetail;
import cn.com.goldfinance.domain.wkxf.CoinPojo;

public interface AccTransdetailMapper {
	
	public List<AccTransdetail> getAccTransdetailByEmpId(String EmpId);
	public List<AccTransdetail> getAccTransdetailByCoinPojo(CoinPojo coinPojo);

}
