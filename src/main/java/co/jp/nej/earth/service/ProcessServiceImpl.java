package co.jp.nej.earth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.jp.nej.earth.model.EventControl;

@Transactional
@Service
public class ProcessServiceImpl implements ProcessService {

    public String openProcess(String token, String workspaceId, String processId, String workItemId) {
        // 1. Create new Event.
        EventControl event = new EventControl();
        event.setWorkItemId(workItemId);
        return null;
    }

    public String closeProcess(String token, String workspaceId, String processId) {
        return null;
    }

    public Process getProcess(String token, String workspaceId, String processId) {
        return null;
    }

    public String updateProcess(String token, String workspaceId, Process process) {
        return null;
    }

}
