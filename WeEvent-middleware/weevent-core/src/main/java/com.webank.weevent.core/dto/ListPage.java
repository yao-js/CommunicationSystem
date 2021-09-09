package com.webank.weevent.core.dto;


import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * list page result.
 *
 * @author matthewliu
 * @since 2019/02/11
 */
@Getter
@Setter
public class ListPage<T> {
    private Integer total;
    private Integer pageIndex;
    private Integer pageSize;
    private List<T> pageData = new ArrayList<>();

    public List<T> getPageData() {
        return pageData;
    }

    public void setPageData(List<T> pageData) {
        this.pageData = pageData;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
