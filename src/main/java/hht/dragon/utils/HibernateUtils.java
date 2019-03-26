package hht.dragon.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Description.
 *
 * @author: huang
 * Date: 17-11-5
 */
@Component
public class HibernateUtils{

    /*@Autowired
    private SessionFactory sessionFactory;*/

    /**
     * 获取表数据.
     * @param tableName 表的实体名
     * @param idName 表中的主键属性名称
     * @param idValue 主键值
     * @return 表数据
     */
    public Object getOne(String tableName, String idName, String idValue) {
        /*Session session = sessionFactory.openSession();
        String hql = "select o from " + tableName + " o where o." + idName + " = " + idValue;
        List<Object> objects =  session.createQuery(hql).list();
        session.close();
        if (objects != null && objects.size() > 0) {
            return objects.get(0);
        }*/
        return null;
    }

}
