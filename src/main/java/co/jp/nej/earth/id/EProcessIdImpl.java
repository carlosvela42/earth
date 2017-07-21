package co.jp.nej.earth.id;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.MgrProcess;
import co.jp.nej.earth.service.ProcessService;

@Repository
public class EProcessIdImpl implements EProcessId {

    @Autowired
    private ProcessService processService;

    @Override
    public int getAutoId(String workspaceId) throws EarthException {
        List<Integer> processIds = new ArrayList<>();
        List<MgrProcess> mgrProcessList = processService.getAllByWorkspace(workspaceId);
        if (mgrProcessList.size() > 0) {
            for (MgrProcess temp : mgrProcessList) {
                processIds.add(temp.getProcessId());
            }
            return Collections.max(processIds) + 1;
        } else {
            return 1;
        }
    }

}
