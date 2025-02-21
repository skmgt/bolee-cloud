package com.itheima.bolee.insurance.service;

import com.itheima.bolee.framework.commons.dto.dict.DataDictVO;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.insurance.dto.QuestionTemplateVO;
import com.itheima.bolee.insurance.dto.SeekAdviceVO;
import com.itheima.bolee.insurance.pojo.Sick;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.SickVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.bolee.insurance.rule.advice.AdviceHealthDTO;

import java.util.List;

/**
 * @Description：疾病表服务类
 */
public interface ISickService extends IService<Sick>{

    /**
     * @Description 多条件查询疾病表分页
     * @param sickVO 疾病表查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<SickVO>
     */
    Page<SickVO> findPage(SickVO sickVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询疾病表
    * @param sickId 合同信息ID
    * @return SickVO
    */
    SickVO findById(String sickId);

    /**
     * @Description 疾病表新增
     * @param sickVO 疾病表查询条件
     * @return SickVO
     */
    SickVO save(SickVO sickVO);

    /**
     * @Description 疾病表修改
     * @param sickVO 疾病表对象
     * @return SickVO
     */
    Boolean update(SickVO sickVO);

    /**
     * @Description 疾病表删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询疾病表列表
     * @param sickVO 查询条件
     * @return: List<SickVO>
     */
    List<SickVO> findList(SickVO sickVO);

    /**
     * 获取所有的疾病类型
     * @return
     */
    List<DataDictVO> findSickType();

    /**
     * 搜索疾病
     * @param sickName
     * @return
     */
    List<SickVO> searchSick(String sickName, UserVO userVO);

    /**
     * 健康咨询的方法
     * @param seekAdviceVO
     */
    QuestionTemplateVO seekAdviceHealth(SeekAdviceVO seekAdviceVO);

    /**
     * 用户提交答案
     * @param seekAdviceVO
     * @return
     */
    AdviceHealthDTO submitQuestionChoose(AdviceHealthDTO seekAdviceVO);

    /**
     * 常见疾病
     * @return
     */
    List<SickVO> commonDiseases();

}
