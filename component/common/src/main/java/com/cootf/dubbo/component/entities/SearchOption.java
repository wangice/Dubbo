package com.cootf.dubbo.component.entities;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author:ice
 * @Date: 2018/5/11 14:50
 */
public class SearchOption implements Serializable {

  private static final long serialVersionUID = -3576192263518693376L;
  private Integer pageSize = 20;
  private Integer pageIndex = 0;
  private Integer rowIndex = -1;
  private String orderField;
  private String skipId;//分页起始ID

  private Date startTime;//时间范围-起始时间.
  private Date endTime;//时间范围-截止时间.
  private String searchText;//模糊查询字符串.

  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public Integer getPageIndex() {
    return pageIndex;
  }

  public void setPageIndex(Integer pageIndex) {
    this.pageIndex = pageIndex;
  }

  public Integer getRowIndex() {
    return rowIndex;
  }

  public void setRowIndex(Integer rowIndex) {
    this.rowIndex = rowIndex;
  }

  public String getOrderField() {
    return orderField;
  }

  public void setOrderField(String orderField) {
    this.orderField = orderField;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public String getSkipId() {
    return skipId;
  }

  public void setSkipId(String skipId) {
    this.skipId = skipId;
  }

  public String getSearchText() {
    return searchText;
  }

  public void setSearchText(String searchText) {
    this.searchText = searchText;
  }

  @Override
  public String toString() {
    return "SearchOption{" +
        "pageSize=" + pageSize +
        ", pageIndex=" + pageIndex +
        ", rowIndex=" + rowIndex +
        ", orderField='" + orderField + '\'' +
        ", skipId='" + skipId + '\'' +
        ", startTime=" + startTime +
        ", endTime=" + endTime +
        ", searchText='" + searchText + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }
}
