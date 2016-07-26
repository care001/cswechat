package com.jc.dao;

import com.jc.entity.Merchant;
import com.jc.entity.MerchantExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MerchantMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cs_merchant
     *
     * @mbggenerated
     */
    int countByExample(MerchantExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cs_merchant
     *
     * @mbggenerated
     */
    int deleteByExample(MerchantExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cs_merchant
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer merchantid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cs_merchant
     *
     * @mbggenerated
     */
    int insert(Merchant record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cs_merchant
     *
     * @mbggenerated
     */
    int insertSelective(Merchant record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cs_merchant
     *
     * @mbggenerated
     */
    List<Merchant> selectByExample(MerchantExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cs_merchant
     *
     * @mbggenerated
     */
    Merchant selectByPrimaryKey(Integer merchantid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cs_merchant
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") Merchant record, @Param("example") MerchantExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cs_merchant
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") Merchant record, @Param("example") MerchantExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cs_merchant
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Merchant record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cs_merchant
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Merchant record);
}