package com.haojie.dao.genericDao;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class GenericDao<T> {
    QueryRunner queryRunner = null;
    Class cls = null;

    public GenericDao() {
        queryRunner = new QueryRunner();
        //获取该类(DAO)的父类型
        Type type = this.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            //把其转换成带有泛型参数的类型的Type
            ParameterizedType parameterizedType = (ParameterizedType) type;
            //获取实际的泛型参数
            Type[] typeArgs = parameterizedType.getActualTypeArguments();
            if (typeArgs[0] instanceof Class) {
                cls = (Class) typeArgs[0];
            }
        }


    }


    public int update(Connection conn, String sql, Object... objs) throws SQLException {

        return queryRunner.update(conn, sql, objs);
    }


    public T queryForOne(Connection conn, String sql, Object... objects) throws SQLException {
        T t = null;
        t = (T) queryRunner.query(conn, sql, new BeanHandler<>(cls), objects);
        return t;
    }


    public List<T> queryForList(Connection conn, String sql, Object... objects) throws SQLException {
        List<T> list = null;
        list = (List<T>) queryRunner.query(conn, sql, new BeanListHandler<>(cls), objects);
        return list;
    }

    public <A> List<A> queryForColumnList(Connection conn, String sql, Object... objects) throws SQLException {
        List<A> list = null;
        list = (List<A>) queryRunner.query(conn, sql, new ColumnListHandler(), objects);
        return list;
    }


    public <A> A queryForColumn(Connection conn, String sql, Object... objects) throws SQLException {
        A a = null;
        a = (A) queryRunner.query(conn, sql, new ScalarHandler(), objects);
        return a;
    }
}
