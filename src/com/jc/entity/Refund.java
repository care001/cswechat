package com.jc.entity;

public class Refund {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cs_refund.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cs_refund.orderid
     *
     * @mbggenerated
     */
    private Integer orderid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cs_refund.cssum
     *
     * @mbggenerated
     */
    private Double cssum;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cs_refund.clientid
     *
     * @mbggenerated
     */
    private String clientid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cs_refund.userid
     *
     * @mbggenerated
     */
    private Integer userid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cs_refund.remark
     *
     * @mbggenerated
     */
    private String remark;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cs_refund.maketime
     *
     * @mbggenerated
     */
    private String maketime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cs_refund.makeuser
     *
     * @mbggenerated
     */
    private String makeuser;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cs_refund.id
     *
     * @return the value of cs_refund.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cs_refund.id
     *
     * @param id the value for cs_refund.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cs_refund.orderid
     *
     * @return the value of cs_refund.orderid
     *
     * @mbggenerated
     */
    public Integer getOrderid() {
        return orderid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cs_refund.orderid
     *
     * @param orderid the value for cs_refund.orderid
     *
     * @mbggenerated
     */
    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cs_refund.cssum
     *
     * @return the value of cs_refund.cssum
     *
     * @mbggenerated
     */
    public Double getCssum() {
        return cssum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cs_refund.cssum
     *
     * @param cssum the value for cs_refund.cssum
     *
     * @mbggenerated
     */
    public void setCssum(Double cssum) {
        this.cssum = cssum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cs_refund.clientid
     *
     * @return the value of cs_refund.clientid
     *
     * @mbggenerated
     */
    public String getClientid() {
        return clientid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cs_refund.clientid
     *
     * @param clientid the value for cs_refund.clientid
     *
     * @mbggenerated
     */
    public void setClientid(String clientid) {
        this.clientid = clientid == null ? null : clientid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cs_refund.userid
     *
     * @return the value of cs_refund.userid
     *
     * @mbggenerated
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cs_refund.userid
     *
     * @param userid the value for cs_refund.userid
     *
     * @mbggenerated
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cs_refund.remark
     *
     * @return the value of cs_refund.remark
     *
     * @mbggenerated
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cs_refund.remark
     *
     * @param remark the value for cs_refund.remark
     *
     * @mbggenerated
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cs_refund.maketime
     *
     * @return the value of cs_refund.maketime
     *
     * @mbggenerated
     */
    public String getMaketime() {
        return maketime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cs_refund.maketime
     *
     * @param maketime the value for cs_refund.maketime
     *
     * @mbggenerated
     */
    public void setMaketime(String maketime) {
        this.maketime = maketime == null ? null : maketime.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cs_refund.makeuser
     *
     * @return the value of cs_refund.makeuser
     *
     * @mbggenerated
     */
    public String getMakeuser() {
        return makeuser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cs_refund.makeuser
     *
     * @param makeuser the value for cs_refund.makeuser
     *
     * @mbggenerated
     */
    public void setMakeuser(String makeuser) {
        this.makeuser = makeuser == null ? null : makeuser.trim();
    }
}