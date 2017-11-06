package hht.dragon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;

/**
 * Description.
 *
 * @author: huang
 * Date: 17-11-2
 */
@SpringBootApplication
public class MyActivitiDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyActivitiDemoApplication.class, args);
    }

    @Bean
    public HibernateJpaSessionFactoryBean sessionFactory() {
        return new HibernateJpaSessionFactoryBean();
    }

}
