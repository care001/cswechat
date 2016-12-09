package com.jc.dao;

import com.jc.entity.Config;
import com.jc.entity.ConfigExample;
import com.jc.entity.ConfigKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ConfigMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cs_config
     *
     * @mbggenerated
     */
    int countByExample(ConfigExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cs_config
     *
     * @mbggenerated
     */
    int deleteByExample(ConfigExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cs_config
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(ConfigKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cs_config
     *
     * @mbggenerated
     */
    int insert(Config record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cs_config
     *
     * @mbggenerated
     */
    int insertSelective(Config record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cs_config
     *
     * @mbggenerated
     */
    List<Config> selectByExample(ConfigExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cs_config
     *
     * @mbggenerated
     */
    Config selectByPrimaryKey(ConfigKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cs_config
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") Config record, @Param("example") ConfigExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cs_config
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") Config record, @Param("example") ConfigExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cs_config
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Config record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cs_config
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Config record);
}