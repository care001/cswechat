package com.jc.entity;

public class Agency {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cs_agency.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cs_agency.agency
     *
     * @mbggenerated
     */
    private String agency;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cs_agency.agencyno
     *
     * @mbggenerated
     */
    private String agencyno;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cs_agency.remark
     *
     * @mbggenerated
     */
    private String remark;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cs_agency.id
     *
     * @return the value of cs_agency.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cs_agency.id
     *
     * @param id the value for cs_agency.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cs_agency.agency
     *
     * @return the value of cs_agency.agency
     *
     * @mbggenerated
     */
    public String getAgency() {
        return agency;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cs_agency.agency
     *
     * @param agency the value for cs_agency.agency
     *
     * @mbggenerated
     */
    public void setAgency(String agency) {
        this.agency = agency == null ? null : agency.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cs_agency.agencyno
     *
     * @return the value of cs_agency.agencyno
     *
     * @mbggenerated
     */
    public String getAgencyno() {
        return agencyno;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cs_agency.agencyno
     *
     * @param agencyno the value for cs_agency.agencyno
     *
     * @mbggenerated
     */
    public void setAgencyno(String agencyno) {
        this.agencyno = agencyno == null ? null : agencyno.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cs_agency.remark
     *
     * @return the value of cs_agency.remark
     *
     * @mbggenerated
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cs_agency.remark
     *
     * @param remark the value for cs_agency.remark
     *
     * @mbggenerated
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}