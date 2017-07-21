package co.jp.nej.earth.id;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Directory;
import co.jp.nej.earth.service.DirectoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class EDatadirectoryIdImpl implements EDatadirectoryId {

    @Autowired
    private DirectoryService directoryService;

    @Override
    public String getAutoId() throws EarthException {
        List<Directory> directories = directoryService.getAllDirectories();
        List<Integer> intTemplateIds = new ArrayList<>();
        if (directories.size() > 0) {
            for (Directory directorie : directories) {
                intTemplateIds.add(directorie.getDataDirectoryId());
            }
            return String.valueOf(Collections.max(intTemplateIds) + 1);
        } else {
            return String.valueOf(1);
        }
    }

}
