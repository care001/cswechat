package cn.com.goldfinance.mapper.wkxf;

import java.util.List;

import cn.com.goldfinance.domain.wkxf.AccHead;
import cn.com.goldfinance.domain.wkxf.AccHeadPojo;

public interface AccHeadMapper {
	
	public List<AccHead> getAccHeadByAccHeadPojo(AccHeadPojo accHeadPojo);
	public List<AccHead> getAccHeadByAccHeadPojo2(AccHeadPojo accHeadPojo);
	
}
