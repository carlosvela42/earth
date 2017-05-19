package co.jp.nej.earth.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Directory;

@Transactional(rollbackFor = EarthException.class, propagation = Propagation.REQUIRED)
@Service
public class DirectoryServiceImpl implements DirectoryService {
    public List<Directory> getAllDirectories() {
        return new ArrayList<Directory>();
    }
}
