package com.yupi.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 分析
 * @TableName analysis_result
 */
@TableName(value = "analysis_result")
@Data
public class AnalysisResult implements Serializable {
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
     * 所有内容
     */
    private String all_content;

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
     * 个性化方案提供
     */
    private String individualizedDesc;
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

    /**
     * 是否删除
     */
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        AnalysisResult other = (AnalysisResult) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getAll_content() == null ? other.getAll_content() == null : this.getAll_content().equals(other.getAll_content()))
                && (this.getFacial_analysis() == null ? other.getFacial_analysis() == null : this.getFacial_analysis().equals(other.getFacial_analysis()))
                && (this.getStatus_judgment() == null ? other.getStatus_judgment() == null : this.getStatus_judgment().equals(other.getStatus_judgment()))
                && (this.getIndividualized() == null ? other.getIndividualized() == null : this.getIndividualized().equals(other.getIndividualized()))
                && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
                && (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getAll_content() == null) ? 0 : getAll_content().hashCode());
        result = prime * result + ((getFacial_analysis() == null) ? 0 : getFacial_analysis().hashCode());
        result = prime * result + ((getStatus_judgment() == null) ? 0 : getStatus_judgment().hashCode());
        result = prime * result + ((getIndividualized() == null) ? 0 : getIndividualized().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", status=").append(status);
        sb.append(", all_content=").append(all_content);
        sb.append(", facial_analysis=").append(facial_analysis);
        sb.append(", status_judgment=").append(status_judgment);
        sb.append(", individualized=").append(individualized);
        sb.append(", userId=").append(userId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}