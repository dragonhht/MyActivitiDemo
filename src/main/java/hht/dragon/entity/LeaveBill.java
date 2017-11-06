package hht.dragon.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Description.
 *
 * @author: huang
 * Date: 17-11-5
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class LeaveBill implements Serializable {
    @Id
    private String id;
    private String name;
}
