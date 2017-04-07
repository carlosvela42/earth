package co.jp.nej.earth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.jp.nej.earth.dao.WorkItemDao;
import co.jp.nej.earth.model.WorkItem;

@Transactional
@Service
public class WorkItemServiceImpl implements WorkItemService {
    
    @Autowired
    private WorkItemDao workItemDao;
    
    public WorkItem getWorkItemDataStructure(long workItemId) {
        return workItemDao.getWorkItemDataStructure(workItemId);
    }
}
