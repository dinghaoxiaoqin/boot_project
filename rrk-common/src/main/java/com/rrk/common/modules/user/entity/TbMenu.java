package com.rrk.common.modules.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 系统菜单表
 * </p>
 *
 * @author dinghao
 * @since 2020-02-08
 */
@ApiModel(value = "菜单")
@TableName("tb_menu")
public class TbMenu extends Model<TbMenu> implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 父菜单ID
     */
    private Integer menuPid;

    /**
     * 当前菜单所有父菜单
     */
    private String menuPids;

    /**
     * 0:不是叶子节点，1:是叶子节点
     */
    @TableField("is_leaf")
    private Integer isLeaf;

    /**
     * 菜单名称
     */
    @TableField("menu_name")
    private String menuName;

    /**
     * 跳转URL
     */
    private String url;

    private String icon;
    @TableField("icon_color")
    private String iconColor;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 菜单层级
     */
    private Integer level;

    /**
     * 0:启用,1:禁用
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 菜单唯一标识
     */
    @TableField("menu_sign")
    private String menuSign;

    public Integer getId() {
        return id;
    }

    public TbMenu setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getMenuPid() {
        return menuPid;
    }

    public TbMenu setMenuPid(Integer menuPid) {
        this.menuPid = menuPid;
        return this;
    }

    public String getMenuPids() {
        return menuPids;
    }

    public TbMenu setMenuPids(String menuPids) {
        this.menuPids = menuPids;
        return this;
    }

    public Integer getIsLeaf() {
        return isLeaf;
    }

    public TbMenu setIsLeaf(Integer isLeaf) {
        this.isLeaf = isLeaf;
        return this;
    }

    public String getMenuName() {
        return menuName;
    }

    public TbMenu setMenuName(String menuName) {
        this.menuName = menuName;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public TbMenu setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getIcon() {
        return icon;
    }

    public TbMenu setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public String getIconColor() {
        return iconColor;
    }

    public TbMenu setIconColor(String iconColor) {
        this.iconColor = iconColor;
        return this;
    }

    public Integer getSort() {
        return sort;
    }

    public TbMenu setSort(Integer sort) {
        this.sort = sort;
        return this;
    }

    public Integer getLevel() {
        return level;
    }

    public TbMenu setLevel(Integer level) {
        this.level = level;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public TbMenu setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public TbMenu setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public TbMenu setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getMenuSign() {
        return menuSign;
    }

    public TbMenu setMenuSign(String menuSign) {
        this.menuSign = menuSign;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "TbMenu{" +
                "id=" + id +
                ", menuPid=" + menuPid +
                ", menuPids=" + menuPids +
                ", isLeaf=" + isLeaf +
                ", menuName=" + menuName +
                ", url=" + url +
                ", icon=" + icon +
                ", iconColor=" + iconColor +
                ", sort=" + sort +
                ", level=" + level +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", menuSign=" + menuSign +
                "}";
    }
}
