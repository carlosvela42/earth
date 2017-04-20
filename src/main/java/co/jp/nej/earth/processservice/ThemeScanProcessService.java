package co.jp.nej.earth.processservice;

import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.WorkItem;
import co.jp.nej.earth.service.EventControlService;
import co.jp.nej.earth.util.ApplicationContextUtil;

/**
 *
 * @author p-tvo-sonta
 *
 */
@Component
public class ThemeScanProcessService implements Job {

    /**
     * log
     */
    private static final Logger LOG = LoggerFactory.getLogger(ThemeScanProcessService.class);
    /**
     * service
     */
    private EventControlService service;
    /**
     * folder input
     */
    private static final String FOLDER_INPUT = "themeScan";
    /**
     * folder output
     */
    private static final String FOLDER_OUTPUT = "temp/themeScan/";

    /**
     * execute
     */
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            logic();
        } catch (EarthException | IOException e) {
            // TODO
            LOG.error(e.getMessage());
        }
    }

    /**
     * run logic
     *
     * @throws EarthException
     * @throws IOException
     */
    private void logic() throws EarthException, IOException {
        ApplicationContext context = ApplicationContextUtil.getApplicationContext();
        service = context.getBean(EventControlService.class);
        List<File> fileChildren = scanFolderImport();
        for (File child : fileChildren) {
            if (importThemeScan(child)) {
                copyFile(child, FOLDER_OUTPUT);
            }
        }
    }

    /**
     * scan folder import
     *
     * @return
     * @throws EarthException
     * @throws IOException
     */
    private List<File> scanFolderImport() throws EarthException, IOException {
        File file = new File(FOLDER_INPUT);
        List<File> result = new ArrayList<>();
        if (file.isDirectory()) {
            String[] names = file.list();
            for (String name : names) {
                File fileChild = new File(file.getAbsolutePath() + File.separator + name);
                if (fileChild.isDirectory()) {
                    result.add(fileChild);
                } else {
                    String extension = name.substring(name.lastIndexOf('.') + 1);
                    // check file is xml or not
                    if (extension.equals("xml")) {
                        try {
                            service.insertEvent(parseXml(fileChild));
                            copyFile(fileChild, FOLDER_OUTPUT);
                        } catch (JAXBException e) {
                            // write log when xml's type is wrong
                            LOG.error(e.getMessage());
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * import theme scan
     *
     * @param file
     * @return
     * @throws EarthException
     */
    private boolean importThemeScan(File file) throws EarthException {
        // check all children in a file
        String[] names = file.list();
        for (String name : names) {
            File fileChild = new File(file.getAbsolutePath() + File.separator + name);
            if (fileChild.isDirectory()) {
                importThemeScan(fileChild);
            } else {
                String extension = name.substring(name.lastIndexOf('.') + 1);
                // check file is xml or not
                if (extension.equals("xml")) {
                    try {
                        if (!service.insertEvent(parseXml(fileChild))) {
                            break;
                        }
                    } catch (JAXBException e) {
                        // write log when xml's type is wrong
                        LOG.error(e.getMessage());
                    }
                }
            }
        }
        return true;
    }

    /**
     * parse Xml file to workItem class
     *
     * @param fileChild
     * @return
     * @throws JAXBException
     */
    private WorkItem parseXml(File fileChild) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(WorkItem.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        WorkItem workItem = (WorkItem) jaxbUnmarshaller.unmarshal(fileChild);
        return workItem;
    }

    /**
     * copy file in folder
     *
     * @param source
     * @param dest
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    private boolean copyFile(File source, String dest) throws FileNotFoundException, IOException {
        String[] names = source.list();
        // get all file in folder
        for (String name : names) {
            // check file is image or not
            Image image = ImageIO.read(new File(source.getAbsolutePath() + File.separator + name));
            if (image == null) {
                break;
            }
            // check destination folder
            File destination = new File(dest + source.getName());
            if (destination.exists()) {
                copy(source, destination, name);
            } else {
                if (destination.mkdir()) {
                    copy(source, destination, name);
                }
            }
        }
        // delete folder
        deleteFolder(source);
        return true;
    }

    /**
     * copy file to file
     *
     * @param source
     * @param dest
     * @param name
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    private long copy(File source, File dest, String name) throws FileNotFoundException, IOException {
        return Files.copy(new File(source.getAbsolutePath() + File.separator + name).toPath(),
                new FileOutputStream(dest.getAbsolutePath() + File.separator + name));
    }

    /**
     * delete folder
     *
     * @param source
     * @throws IOException
     */
    private void deleteFolder(File source) throws IOException {
        FileUtils.deleteDirectory(source);
    }
}
