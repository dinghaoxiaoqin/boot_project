package com.rrk.common.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 分页的参数
 */
@Data
@Setter
@Getter
public class PageDto implements Serializable {

    /**
     * 第几页(默认第一页)
     */
    private Integer page = 1;
    /**
     * 每页的数据（默认10条）
     */
    private Integer pageSize = 10;
    /**
     * 总共多少条数据(默认0条)
     */
    private Long totalData = 0L;
    /**
     * 总共多少页（默认0页）
     */
    private Long totalPage = 0L;

    public PageDto(){}

    public PageDto(Integer page, Integer pageSize){
        if (page <= 0) {
            page = 1;
        }
        if (pageSize < 0) {
            pageSize = 10;
        }
        page = (page - 1) * pageSize;
        this.setPage(page);
        this.setPageSize(pageSize);
    }
    public PageDto(Long total, Integer pageSize){
        this.setTotalData(total);
        if (total % pageSize != 0) {
             totalPage = total / pageSize + 1;
           this.setTotalPage(totalPage);
        } else {
            totalPage =  total / pageSize;
            this.setTotalPage(totalPage);
        }
    }
    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalData() {
        return totalData;
    }

    public void setTotalData(Long totalData) {
        this.totalData = totalData;
    }

    public Long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }

    @Override
    public String toString() {
        return "PageDto{" +
                "page=" + page +
                ", pageSize=" + pageSize +
                ", totalData=" + totalData +
                ", totalPage=" + totalPage +
                '}';
    }
}
