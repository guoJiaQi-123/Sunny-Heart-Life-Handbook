package com.yupi.springbootinit.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yupi.springbootinit.model.entity.AnalysisResult;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 分析
 * @TableName analysis_result
 */
@TableName(value = "analysis_result")
@Data
public class AnalysisResultVO implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * false-生成中，true-已生成
     */
    private Integer status;


    /**
     * 面部要素分析
     */
    private String facial_analysis;

    /**
     * 状态判断
     */
    private String status_judgment;

    /**
     * 个性化方案提供
     */
    private String individualized;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    /**
     * 包装类转对象
     *
     * @return
     */
    public static AnalysisResult voToObj(AnalysisResultVO analysisResultVO) {
        if (analysisResultVO == null) {
            return null;
        }
        AnalysisResult analysisResult = new AnalysisResult();
        BeanUtils.copyProperties(analysisResultVO, analysisResult);
        return analysisResult;
    }

    /**
     * 对象转包装类
     *
     * @return
     */
    public static AnalysisResultVO objToVo(AnalysisResult analysisResult) {
        if (analysisResult == null) {
            return null;
        }
        AnalysisResultVO analysisResultVO = new AnalysisResultVO();
        BeanUtils.copyProperties(analysisResult, analysisResultVO);
        return analysisResultVO;
    }
}