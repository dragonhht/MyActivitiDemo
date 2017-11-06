package hht.dragon.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 * Description.
 *
 * @author: huang
 * Date: 17-11-5
 */
public class GetOneUtils {

    public void getOne(String name, String id) {

        SessionFactory sessionFactory = HibernateUtils.get();
        System.out.println(sessionFactory);

        /*WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        String[] names = wac.getBeanDefinitionNames();
        for(int i=0; i<names.length; i++){
            System.out.println("---"+names[i]);
        }*/
    }

}
