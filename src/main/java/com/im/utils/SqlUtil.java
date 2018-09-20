package com.im.utils;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author 刘泽 liuze713@gmail.com
 * @description TODO jpa查询公共类
 * @date 2018-9-4 10:56
 */
@Component
public class SqlUtil {
    @PersistenceContext
    EntityManager entityManager;

    /**
     * @param sql     sql语句
     * @param objects 参数
     * @return List
     * @description TODO jpa联查sql 返回多条
     * @author 刘泽
     * @date 2018-9-4 11:06
     */
    public List<Map<String, Object>> execSql(String sql, Object... objects) {
        Query q = this.entityManager.createNativeQuery(sql);
        if (objects != null && objects.length > 0) {
            for (int i = 0; i < objects.length; ++i) {
                q.setParameter(i + 1, objects[i]);
            }
        }
        ((SQLQuery) q.unwrap(SQLQuery.class)).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return q.getResultList();
    }

    /**
     * @param sql     sql语句
     * @param objects 参数
     * @return map
     * @description TODO jpa联查sql 返回一条
     * @author 刘泽
     * @date 2018-9-4 11:08
     */
    public Map<String, Object> execSqlSingleResult(String sql, Object... objects) throws Exception {
        Query q = this.entityManager.createNativeQuery(sql);
        if (objects != null && objects.length > 0) {
            for (int i = 0; i < objects.length; ++i) {
                q.setParameter(i + 1, objects[i]);
            }
        }
        ((SQLQuery) q.unwrap(SQLQuery.class)).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return (Map) q.getSingleResult();
    }
}
