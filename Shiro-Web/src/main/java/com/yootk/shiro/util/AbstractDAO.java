package com.yootk.shiro.util;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class AbstractDAO {
    protected Connection conn ;
    protected PreparedStatement pstmt ;
    public AbstractDAO() {
        this.conn = DatabaseConnection.getConnection() ;
    }
    public <T> T handleResultToVO(ResultSet rs, Class<T> clazz) throws SQLException {
        try {
            Object lineObject = null ;
            if (rs.next()) {
                lineObject = clazz.getDeclaredConstructor().newInstance(); // 实例化对象
                for (int x = 1; x <= pstmt.getMetaData().getColumnCount(); x++) { // 获取每一列的内容
                    Field field = clazz.getDeclaredField(this.pstmt.getMetaData().getColumnName(x));
                    Method method = clazz.getDeclaredMethod("set" + StringUtil.initcap(this.pstmt.getMetaData().getColumnName(x)), field.getType());
                    method.invoke(lineObject, this.getResultSetValue(rs, field));
                }
            }
            return (T) lineObject ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null ;
    }
    public <T> List<T> handleResultToList(ResultSet rs , Class<T> clazz) throws SQLException {
        List<T> all = new ArrayList<>(ModuleInfo.LINESIZE_CAPACITY) ;
        while (rs.next()) {
            try {
                Object lineObject = clazz.getDeclaredConstructor().newInstance(); // 实例化对象
                for (int x = 1 ; x <= pstmt.getMetaData().getColumnCount() ; x ++) { // 获取每一列的内容
                    Field field = clazz.getDeclaredField(this.pstmt.getMetaData().getColumnName(x)) ;
                    Method method = clazz.getDeclaredMethod("set" + StringUtil.initcap(this.pstmt.getMetaData().getColumnName(x)),field.getType()) ;
                    method.invoke(lineObject,this.getResultSetValue(rs,field)) ;
                }
                all.add((T)lineObject) ;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return all ;
    }
    public Long handleCount(String tableName) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + tableName ;
        this.pstmt = this.conn.prepareStatement(sql) ;
        ResultSet rs = this.pstmt.executeQuery() ;
        if (rs.next()) {
            return rs.getLong(1) ;
        }
        return 0L ;
    }
    public Long handleCount(String tableName,String column,String keyWord) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE " + column + " LIKE ?" ;
        this.pstmt = this.conn.prepareStatement(sql) ;
        this.pstmt.setString(1,"%"+keyWord+"%");
        ResultSet rs = this.pstmt.executeQuery() ;
        if (rs.next()) {
            return rs.getLong(1) ;
        }
        return 0L ;
    }

    public boolean handleRemove(String tableName, String columnName, Set<Long> ids) throws SQLException {
        StringBuffer sql = new StringBuffer("DELETE FROM ").append(tableName).append(" WHERE ") ;
        sql.append(columnName).append(" IN (") ;
        for (Long id : ids) {
            sql.append("?,") ;
        }
        sql.delete(sql.length() - 1,sql.length()).append(")") ;
        this.pstmt = this.conn.prepareStatement(sql.toString()) ;
        int foot = 1 ;
        for (Long id : ids) {
            this.pstmt.setLong(foot++,id);
        }
        return this.pstmt.executeUpdate() == ids.size() ;
    }
    public Long handleAutoIncrementKey() throws SQLException {
        String sql = "SELECT LAST_INSERT_ID()" ;
        this.pstmt = this.conn.prepareStatement(sql) ;
        ResultSet rs = this.pstmt.executeQuery() ;
        if (rs.next()) {
            return rs.getLong(1) ;
        }
        return 0L ;
    }
    public boolean isBatchSuccess(int result[]) {
        int count = 0 ; // 个数统计
        for (int x = 0; x < result.length; x++) {
            if (result[x] == Statement.SUCCESS_NO_INFO || result[x] > 0) {
                count ++ ; // 进行统计操作
            }
        }
        return count == result.length ;
    }
    private Object getResultSetValue(ResultSet rs , Field field) throws SQLException {
        if ("java.lang.String".equals(field.getType().getName())) {
            return rs.getString(field.getName()) ;
        } else if ("java.lang.Integer".equals(field.getType().getName()) || "int".equals(field.getType().getName())) {
            return rs.getInt(field.getName()) ;
        } else if ("java.lang.Long".equals(field.getType().getName()) || "long".equals(field.getType().getName())) {
            return rs.getLong(field.getName()) ;
        } else if ("java.lang.Double".equals(field.getType().getName()) || "double".equals(field.getType().getName())) {
            return rs.getDouble(field.getName()) ;
        } else if ("java.util.Date".equals(field.getType().getName())) {
            return rs.getDate(field.getName()) ;
        }
        return null ;
    }

    public Long getLastId() throws SQLException {
        String sql = "SELECT LAST_INSERT_ID()" ;
        this.pstmt = this.conn.prepareStatement(sql) ;
        ResultSet rs = this.pstmt.executeQuery() ;
        if (rs.next()) {
            return rs.getLong(1) ;
        }
        return null ;
    }
}
