package com.cootf.dubbo.persistence.common;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 * @author Charles
 * @create 2017/7/17 15:47
 */
public class IgniteSqlQuery {

  private static final String COMMA = ",";
  private static final String SPACE = " ";
  private static final String SET = " set ";
  private static final String WHERE = " where ";
  private static final String UPDATE = " update ";
  private static final String DELETE = " delete ";
  private static final String FROM = " from ";
  private static final String AND = " and ";
  private StringBuffer sql;
  private List<Object> args;

  public IgniteSqlQuery() {
    this.sql = new StringBuffer();
    this.args = new ArrayList<>();
  }

  public IgniteSqlQuery(String baseSql) {
    this.sql = new StringBuffer();
    this.sql.append(baseSql);
    this.args = new ArrayList<>();
  }

  public IgniteSqlQuery(StringBuffer sql, List<Object> args) {
    this.sql = sql;
    this.args = args;
  }

  public IgniteSqlQuery update() {
    this.sql.append(UPDATE);
    this.sql.append(SPACE);
    return this;
  }

  public IgniteSqlQuery delete() {
    this.sql.append(DELETE);
    this.sql.append(SPACE);
    return this;
  }

  public IgniteSqlQuery from() {
    this.sql.append(SPACE);
    this.sql.append(FROM);
    this.sql.append(SPACE);
    return this;
  }

  public IgniteSqlQuery where() {
    this.sql.append(SPACE);
    this.sql.append(WHERE);
    this.sql.append(SPACE);
    return this;
  }

  public IgniteSqlQuery set() {
    this.sql.append(SPACE);
    this.sql.append(SET);
    this.sql.append(SPACE);
    return this;
  }

  public IgniteSqlQuery and() {
    this.sql.append(SPACE);
    this.sql.append(AND);
    this.sql.append(SPACE);
    return this;
  }

  /**
   * 空格
   */
  public IgniteSqlQuery space() {
    this.sql.append(SPACE);
    return this;
  }

  /**
   * 逗号
   */
  public IgniteSqlQuery comma() {
    this.sql.append(SPACE);
    this.sql.append(COMMA);
    this.sql.append(SPACE);
    return this;
  }

  public IgniteSqlQuery table(String tableName) {
    if (StringUtils.isEmpty(tableName)) {
      throw new NullPointerException("表名不能为空!");
    }
    this.sql.append(SPACE);
    this.sql.append(tableName);
    this.sql.append(SPACE);
    return this;
  }

  public IgniteSqlQuery appendString(String str) {
    if (StringUtils.isEmpty(str)) {
      return this;
    }
    this.sql.append(str);
    return this;
  }


  public IgniteSqlQuery appendStringLikeCondition(String fieldName, String fieldValue) {
    if (StringUtils.isNotBlank(fieldValue)) {
      this.sql.append(String.format(" and %s like ? ", fieldName));
      this.args.add(String.format("%%%s%%", fieldValue));
    }
    return this;
  }

  public IgniteSqlQuery appendBooleanEqualCondition(String fieldName, Boolean fieldValue) {
    if (fieldValue != null) {
      this.sql.append(String.format(" and %s = ? ", fieldName));
      this.args.add(fieldValue.booleanValue());
    }
    return this;
  }

  public IgniteSqlQuery appendStringEqualCondition(String fieldName, String fieldValue) {
    if (StringUtils.isNotBlank(fieldValue)) {
      this.sql.append(String.format(" and %s = ? ", fieldName));
      this.args.add(fieldValue);
    }
    return this;
  }

  /**
   * =
   */
  public IgniteSqlQuery fieldEqual(String fieldName, Object fieldValue) {
    if (fieldValue != null) {
      if (fieldValue instanceof String) {
        if (StringUtils.isEmpty(fieldValue.toString())) {
          return this;
        }
      }
      this.sql.append(fieldName);
      this.sql.append(" = ? ");
      this.args.add(fieldValue);
    }
    return this;
  }

  /**
   * left like
   */
  public IgniteSqlQuery fieldStringLeftLike(String fieldName, String fieldLike) {
    if (StringUtils.isNotBlank(fieldLike)) {
      this.sql.append(SPACE);
      this.sql.append(fieldName);
      this.sql.append(" like ? ");
      this.args.add(String.format("%s%%", fieldLike));
    }
    return this;
  }

  /**
   * right  like
   */
  public IgniteSqlQuery fieldStringRightLike(String fieldName, String fieldLike) {
    if (StringUtils.isNotBlank(fieldLike)) {
      this.sql.append(SPACE);
      this.sql.append(fieldName);
      this.sql.append(" like ? ");
      this.args.add(String.format("%%%s", fieldLike));
    }
    return this;
  }

  /**
   * like
   */
  public IgniteSqlQuery fieldStringLike(String fieldName, String fieldLike) {
    if (StringUtils.isNotBlank(fieldLike)) {
      this.sql.append(SPACE);
      this.sql.append(fieldName);
      this.sql.append(" like ? ");
      this.args.add(String.format("%%%s%%", fieldLike));
    }
    return this;
  }

  public IgniteSqlQuery appendExistsCondition(String fieldName) {
    if (StringUtils.isNotBlank(fieldName)) {
      this.sql.append(String.format(" and %s is not null ", fieldName));
    }
    return this;
  }

//  public IgniteSqlQuery appendOrderAndPagingStatement(SearchOption searchOption,
//      boolean appendPaging, boolean appendOrdering) {
//    //排序
//    if (appendOrdering && this.sql != null && StringUtils
//        .isNotBlank(searchOption.getOrderField())) {
//      this.sql.append(String.format(" order by %s %s", searchOption.getOrderField(),
//          searchOption.getOrderDirection().toString()));
//    }
//    //分页
//    if (appendPaging && this.sql != null) {
//      int startPosition = searchOption.getRowIndex();
//      if (startPosition < 0) {
//        startPosition = (searchOption.getPageIndex() - 1) * searchOption.getPageSize();
//      }
//      this.sql.append(
//          String.format(" limit %s, %s", startPosition, searchOption.getPageSize()));
//    }
//    return this;
//  }

  public IgniteSqlQuery setSimpleSearchingCondition(String searchText, String... fieldNames) {
    if (StringUtils.isEmpty(searchText) || fieldNames.length == 0) {
      return this;
    }
    int len = fieldNames.length;
    //增加对输入内容的小写转换，全部只校验小写内容是否一致_mjg_2018-02-13.
    String sqlArg = String.format("%%%s%%", searchText.toLowerCase());
    this.sql.append(" and ( ");
    for (int index = 0; index < len; index++) {
      if (index > 0) {
        this.sql.append(" or ");
      }
      // wangyaohui modify here
      this.sql.append("LOWER(" + fieldNames[index] + ") like ? ");
//      this.args.add(sqlArg);
      this.args.add(sqlArg);
    }
    this.sql.append(")");
    return this;
  }

  public IgniteSqlQuery appendFieldUpdate(String fieldName, Object fieldValue) {
    if (fieldValue != null) {
      if (fieldValue instanceof String) {
        if (StringUtils.isEmpty(fieldValue.toString())) {
          return this;
        }
      }
      if (args.size() > 0) {
        comma();
      } else {
        space();
      }
      this.sql.append(fieldName);
      this.sql.append(" = ? ");
      this.args.add(fieldValue);
    }
    return this;
  }


  public StringBuffer getSql() {
    return sql;
  }

  public List<Object> getArgs() {
    return args;
  }

  @Override
  public String toString() {
    return "IgniteSqlQuery{" +
        "sql=" + sql +
        ", args=" + args +
        '}';
  }
}
