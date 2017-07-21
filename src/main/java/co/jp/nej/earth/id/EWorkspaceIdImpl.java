package co.jp.nej.earth.id;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.MgrWorkspace;
import co.jp.nej.earth.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class EWorkspaceIdImpl implements EWorkspaceId {

    @Autowired
    private WorkspaceService workspaceService;

    @Override
    public String getAutoId() throws EarthException {
        List<String> stringWorkspaceIds = new ArrayList<>();
        List<Integer> intWorkspaceIds = new ArrayList<>();
        List<MgrWorkspace> mgrWorkspaces = workspaceService.getAll();

        if (mgrWorkspaces.size() > 0) {
            for (MgrWorkspace temp : mgrWorkspaces) {
                stringWorkspaceIds.add(temp.getWorkspaceId());
            }
            for (String stringWorkspaceId : stringWorkspaceIds) {
                intWorkspaceIds.add(Integer.valueOf(stringWorkspaceId));
            }
            return String.valueOf(Collections.max(intWorkspaceIds) + 1);

        } else {
            return String.valueOf(1);
        }
    }

}
