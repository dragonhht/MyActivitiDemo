package hht.dragon.entity;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Description.
 *
 * @author: huang
 * Date: 17-11-5
 */
public interface LeaveBillRepository extends JpaRepository<LeaveBill, String> {
    @Override
    LeaveBill getOne(String s);
}
