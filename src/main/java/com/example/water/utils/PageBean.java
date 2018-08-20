package com.example.water.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by  waiter on 18-7-6  14.
 *
 * @author waiter
 */
public class PageBean {
    /**
     * 起始页
     */
    private int currentPage = 1;
    /**
     * 每页行数
     */
    private int pageCount = 10;
    /**
     * 总行数
     */
    private int totalCount;
    /**
     * 总页数
     */
    private int totalPage;
    /**
     * 元素列表
     */
    private List pageData = new ArrayList();

    /**
     * 获取总页数
     *
     * @return
     */
    public int getTotalPage() {
        if (totalCount % pageCount == 0) {
            totalPage = totalCount / pageCount;
        } else {
            totalPage = totalCount / pageCount + 1;
        }
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List getPageData() {
        return pageData;
    }

    public void setPageData(List pageData) {
        this.pageData = pageData;
    }


}
