package hht.dragon.activiti.assigneehandler;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 财务任务候选.
 *
 * @author: huang
 * Date: 17-11-8
 */
public class FinanceAssigneeHandler implements TaskListener {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void notify(DelegateTask delegateTask) {


    }
}
