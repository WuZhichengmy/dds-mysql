package com.xmu.modules.display_config.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xmu.model.IdEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Id;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("test")
public class Test extends IdEntity {
//    @Id
//    @Column(name = "id", nullable = false)
//    private Long id;

    @Column(name = "标准状态")
    @TableField(value = "标准状态")
    private String 标准状态;

    @Column(name = "标准号")
    private String 标准号;

    @Column(name = "发布日期")
    private LocalDate 发布日期;

    @Column(name = "实施日期")
    private LocalDate 实施日期;

    @Column(name = "发布单位")
    private String 发布单位;

    @Column(name = "中文标准名称")
    private String 中文标准名称;

    @Column(name = "英文标准名称")
    private String 英文标准名称;

    @Column(name = "代替标准")
    private String 代替标准;

    @Column(name = "被代替标准")
    private String 被代替标准;

    @Column(name = "引用标准")
    private String 引用标准;

    @Column(name = "采用关系")
    private String 采用关系;

    @Column(name = "中国标准文献分类号")
    private String 中国标准文献分类号;

    @Column(name = "国际标准分类号")
    private String 国际标准分类号;

    @Column(name = "中文主题词")
    private String 中文主题词;

    @Column(name = "英文主题词")
    private String 英文主题词;

    @Column(name = "发布年份")
    private Integer 发布年份;

    @Column(name = "标龄")
    private Integer 标龄;
}
