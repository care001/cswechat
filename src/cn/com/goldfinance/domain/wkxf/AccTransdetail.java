package cn.com.goldfinance.domain.wkxf;
/**
 * 
 * @author liuhui
 * 交易流水
 */

public class AccTransdetail {
	
	private String cardId;
	
	private String empId;
	//使用扇区号
	private String  secId;
	//交易日期
	private String acctransday;
	/**
	 * 交易类型，10自动充值，11手工充值，12转帐收入，20消费，21退款支出，22销户支出，23转帐支出，24补帐消费
	 */
	private String acctranstype;
	//充值金额
	private String imoneyvalue;
	//消费金额
	private String omoneyvalue;
	//卡片使用次数
	private String cardusenum;
	//卡余额
	private String cardmoneyvalue;
	//类型
	private String type;
	
	private String amount;
	
	private String devId;
	
	private String detail;
	
	private String status;
	
	private String remark;
	
	private String oprtid;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getDevId() {
		return devId;
	}

	public void setDevId(String devId) {
		this.devId = devId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getSecId() {
		return secId;
	}

	public void setSecId(String secId) {
		this.secId = secId;
	}

	public String getAcctransday() {
		return acctransday;
	}

	public void setAcctransday(String acctransday) {
		this.acctransday = acctransday;
	}

	public String getAcctranstype() {
		return acctranstype;
	}

	public void setAcctranstype(String acctranstype) {
		this.acctranstype = acctranstype;
	}

	public String getImoneyvalue() {
		return imoneyvalue;
	}

	public void setImoneyvalue(String imoneyvalue) {
		this.imoneyvalue = imoneyvalue;
	}

	public String getOmoneyvalue() {
		return omoneyvalue;
	}

	public void setOmoneyvalue(String omoneyvalue) {
		this.omoneyvalue = omoneyvalue;
	}

	public String getCardusenum() {
		return cardusenum;
	}

	public void setCardusenum(String cardusenum) {
		this.cardusenum = cardusenum;
	}

	public String getCardmoneyvalue() {
		return cardmoneyvalue;
	}

	public void setCardmoneyvalue(String cardmoneyvalue) {
		this.cardmoneyvalue = cardmoneyvalue;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOprtid() {
		return oprtid;
	}

	public void setOprtid(String oprtid) {
		this.oprtid = oprtid;
	}

	
}
