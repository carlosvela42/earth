package co.jp.nej.earth.processservice;

import co.jp.nej.earth.config.JdbcConfig;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Document;
import co.jp.nej.earth.model.FolderItem;
import co.jp.nej.earth.model.TemplateData;
import co.jp.nej.earth.model.WorkItem;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.entity.CtlEvent;
import co.jp.nej.earth.service.EventControlService;
import co.jp.nej.earth.util.ApplicationContextUtil;
//import co.jp.nej.earth.util.DateUtil;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
//import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
//import javax.xml.bind.Marshaller;
//import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import java.io.File;
import java.io.IOException;
//import java.io.StringWriter;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author p-tvo-sonta
 */

@Configuration
@PropertySource("classpath:batch_config.properties")
@Import(JdbcConfig.class)
@ComponentScan(basePackages = { "co.jp.nej.earth" }, excludeFilters = {
        @Filter(type = FilterType.ANNOTATION, value = Configuration.class),
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "co.jp.nej.earth.web.controller.*") })
@Component
public class ThemeScanProcessService implements Job {

    /* Logging */
    private static final Logger LOG = LoggerFactory.getLogger(ThemeScanProcessService.class);

    private static final String MAX_MS = "999";
    /* Directory scan */
    private static final String SEPARATOR = File.separator;
    private static final String OUTPUT_FOLDER = "temp";
    private static final int DEPTH_SCAN = 2;

    /* XML Parser */
    //private static final String ROOT_ELEMENT = "batch";
    //private static final String NAMESPACE = "http://tempuri.org/Batch.xsd";

    /* Control Event */
    private static final String DEFAULT_EVENT_STATUS = "EDIT";
    private static final String DEFAULT_EVENT_WIID = "-1";
    private static final String DEFAULT_ADD_ID = "-1";

    @Autowired
    private EventControlService service;

    //@Autowired
    //@Value("#{batch.scanPath}")
    //private String pathScan;

    private String workspaceId;
    private String userId;
    //private String processId = "";
    //private String hostName = "";
    //private String threadId = "";

    private StringBuilder inputPath;
    private StringBuilder outputPath;

    public ThemeScanProcessService() {
        //inputPath = new StringBuilder(Paths.get(".").toAbsolutePath().normalize().toString());
        inputPath = new StringBuilder("C:\\Earth\\Scan");
        //inputPath = new StringBuilder(pathScan);
        outputPath = new StringBuilder(inputPath).append(SEPARATOR).append(OUTPUT_FOLDER);

        //System.out.println(inputPath);
        //System.out.println(outputPath);
    }

    /**
     * execute
     */
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            System.out.println("ThemeScanRun");

            workspaceId=context.getJobDetail().getJobDataMap().getString(Constant.AgentBatch.P_WORKSPACE_ID);
            userId=context.getJobDetail().getJobDataMap().getString(Constant.AgentBatch.P_USER_ID);

            logic();
        } catch (EarthException | IOException e) {

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
        Path pathToImport = Paths.get(inputPath.toString());

        Files.walkFileTree(pathToImport, Collections.<FileVisitOption>emptySet(), DEPTH_SCAN,
            new SimpleFileVisitor<Path>() {
                @Override
            public FileVisitResult postVisitDirectory(Path batchPath, IOException e) throws IOException {
                    if (e == null) {

                        String stringPath = batchPath.toAbsolutePath().toString();
                        boolean isRootFolder = stringPath.equals(inputPath.toString());
                        boolean isOutputFolder = stringPath.equals(outputPath.toString());

                        if(!(isRootFolder || isOutputFolder)) {
                            try {

                                ArrayList<String> fileList = new ArrayList<String>();

                                if (importThemeScan(batchPath, fileList)) {
                                    moveFiles(batchPath, fileList);
                                    System.out.println("End");
                                }
                            } catch (EarthException e1) {
                                LOG.error(e1.getMessage());
                            }
                        }
                            return FileVisitResult.CONTINUE;
                    } else {
                        System.out.println("Exception while iterating directory.");
                        throw e;
                    }
                }
            });
    }

    /**
     * import theme scan
     *
     * @param file
     * @return
     * @throws EarthException
     */
    private boolean importThemeScan(Path path, ArrayList<String> fileList) throws EarthException, IOException {

        boolean resultImport = false;

        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.xml");

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path entry : stream) {
                if (Files.isRegularFile(entry)) {

                    if (matcher.matches(entry)) {
                        try {
                            System.out.println("Processing file: " + entry.toAbsolutePath());
                            FoldersXML folders = parseXml(entry.toFile());

                            String destinationBuilder =
                                new StringBuilder(outputPath).append(SEPARATOR)
                                .append(path.getFileName().normalize().toString())
                                .append(SEPARATOR).toString();

                            folders.setPathBach(destinationBuilder);
                            makeFileList(folders,fileList);
                            List<CtlEvent> events = new LinkedList<>();
                            for(FolderXML f:folders.folderList) {
                                CtlEvent event = new CtlEvent();
                                event.setUserId(userId);
                                event.setWorkitemId(DEFAULT_EVENT_WIID);
                                event.setStatus(DEFAULT_EVENT_STATUS);
                                event.setTaskId(f.getTaskId());
                                //event.setTransactionToken(makeTransactionToken());
                                event.setWorkitemData(f.makeWorkItemJson(workspaceId, folders.getPathBach()));
                                events.add(event);
                            }

                            if (!service.insertEvents(workspaceId, events)) {
                                break;
                            }
                            resultImport = true;
                        } catch (JsonProcessingException | JAXBException e) {
                            LOG.error(e.getMessage());
                            throw new EarthException(e);
                        }
                    }
                }
            }
        }

        return resultImport;
    }

    /**
     * parse Xml file to folderXML class
     *
     * @param fileChild
     * @return
     * @throws JAXBException
     */
    private FoldersXML parseXml(File fileChild) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(FoldersXML.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        FoldersXML folders = (FoldersXML) jaxbUnmarshaller.unmarshal(fileChild);
        return folders;
    }

    private void makeFileList(FoldersXML folders, ArrayList<String> fileList) throws IOException{
        for(FolderXML folder : folders.folderList) {
            for(ImageDocumentXML document : folder.documentList) {
                for(ImagePageXML page : document.pageList) {
                    String filename = page.getFilePath();
                    fileList.add(filename);
                }
            }
        }
    }

    private void moveFiles(Path batchPath,ArrayList<String> fileList) throws IOException{

        String batchId = batchPath.getFileName().normalize().toString();
        String sourceBuilder =
            new StringBuilder(inputPath).append(SEPARATOR).append(batchId).append(SEPARATOR).toString();
        String destinationBuilder =
            new StringBuilder(outputPath).append(SEPARATOR).append(batchId).append(SEPARATOR).toString();

        /* make sure output is avaiable */
        createDirectory(destinationBuilder.toString());

        //StringBuilder sourceFile = new StringBuilder(sourceBuilder);
        //StringBuilder destinationFile = new StringBuilder(destinationBuilder);
        /* start copy all file */
        for(String filename: fileList) {
            StringBuilder sourceFile = new StringBuilder(sourceBuilder);
            StringBuilder destinationFile = new StringBuilder(destinationBuilder);
            copyFile(sourceFile.append(filename).toString(), destinationFile.append(filename).toString());
        }

        /* remove the folder */
        deleteFolder(batchPath.toFile());
    }

    private void createDirectory(String path) throws IOException {
        Files.createDirectories(Paths.get(path));
    }

    private void copyFile(String source, String destination) throws IOException{
        CopyOption[] options = new CopyOption[]{
            StandardCopyOption.REPLACE_EXISTING,
            StandardCopyOption.COPY_ATTRIBUTES};
        Files.copy(Paths.get(source), Paths.get(destination),options);
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


    /*private String makeTransactionToken(){
        StringBuilder transactionBuilder = new StringBuilder(processId).append(hostName).append(threadId);
        transactionBuilder.append(DateUtil.getCurrentDate(Constant.DatePattern.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
        transactionBuilder.append(MAX_MS);
        return transactionBuilder.toString();
    }*/


    public static void main(String[] args) throws EarthException, ClassNotFoundException {
        ApplicationContext context = new AnnotationConfigApplicationContext(ThemeScanProcessService.class);

        ApplicationContextUtil appUtil = new ApplicationContextUtil();
        appUtil.setApplicationContext(context);
        //System.out.println("Working Directory = " + System.getProperty("user.dir"));
        //System.out.println("Working Directory = " + Paths.get(".").toAbsolutePath().normalize().toString());

        ThemeScanProcessService process = new ThemeScanProcessService();
        String wiId = null;
        if (args.length > 0) {
            wiId = args[0];
        } else {
            wiId = Constant.EARTH_WORKSPACE_ID;
        }
        appUtil.setWorkspaceId(wiId);
        //process.test();

        try {

            process.workspaceId = ApplicationContextUtil.getWorkspaceId();
            process.userId = "admin";
            process.logic();

        } catch (EarthException | IOException e) {
            LOG.error(e.getMessage());
        }
    }


    /* private classes for parser XML */

    @XmlRootElement(name="Batch")
    private static class FoldersXML {

        private String taskId = "";
        private String pathBach = "";

        private ArrayList<FolderXML> folderList;


        @XmlElement(name = "Status")
        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }
        public String getTaskId() {
            return taskId;
        }

        public void setPathBach(String pathBach) {
            this.pathBach = pathBach;
        }
        public String getPathBach() {
            return pathBach;
        }

        @XmlElementWrapper(name = "Folders")
        @XmlElement(name = "Folder")
        public void setFolderList(ArrayList<FolderXML> folderList) {
            this.folderList = folderList;
        }

        public ArrayList<FolderXML> getFolderList() {
            return folderList;
        }

    }

    @XmlType(propOrder={"taskId", "templateId", "attributeList", "documentList"})
    @XmlRootElement(name="Folder")
    private static class FolderXML {

        private String taskId = "";

        private String templateId = "";

        private ArrayList<AttributeXML> attributeList;

        private ArrayList<ImageDocumentXML> documentList;

        @XmlElement(name = "Status")
        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }
        public String getTaskId() {
            return taskId;
        }

        @XmlElement(name = "TemplateId")
        public void setTemplateId(String templateId) {
            this.taskId = templateId;
        }

        public String getTemplateId() {
            return templateId;
        }

        @XmlElementWrapper(name = "Attributes")
        @XmlElement(name = "Attribute")
        public void setAttributeList(ArrayList<AttributeXML> attributeList) {
            this.attributeList = attributeList;
        }

        public ArrayList<AttributeXML> getAttributeList() {
            return attributeList;
        }

        @XmlElement(name = "ImageDocument")
        public void setDocumentList(ArrayList<ImageDocumentXML> documentList) {
            this.documentList = documentList;
        }

        public ArrayList<ImageDocumentXML> getDocumentList() {
            return documentList;
        }

        public String makeWorkItemJson(String workspaceId, String fullPath) {

            WorkItem workItem = new WorkItem();

            workItem.setWorkspaceId(workspaceId);
            //workItem.setWorkitemId(DEFAULT_ADD_ID);
            workItem.setTaskId(this.getTaskId());
            workItem.setTemplateId(this.getTemplateId());

            TemplateData workItemData = new TemplateData();
            workItemData.setDataMap(this.getMapOfAttributes());
            workItem.setWorkItemData(new TemplateData());

            List<FolderItem> folderItemList = new LinkedList<>();
            for(ImageDocumentXML documentXML : this.documentList) {
                FolderItem folderItem = new FolderItem();

                folderItem.setTemplateId(documentXML.getTemplateId());
                //folderItem.setFolderItemNo(DEFAULT_ADD_ID);

                TemplateData folderItemData = new TemplateData();
                folderItemData.setDataMap(documentXML.getMapOfAttributes());
                folderItem.setFolderItemData(new TemplateData());

                List<Document> documents = new LinkedList<>();
                for(ImagePageXML page : documentXML.pageList) {
                    Document document = new Document();
                    //document.setDocumentNo(DEFAULT_ADD_ID);
                    document.setDocumentPath(new StringBuilder(fullPath).append(page.getFilePath()).toString());
                    documents.add(document);
                }

                folderItem.setDocuments(documents);
                folderItemList.add(folderItem);
            }

            workItem.setFolderItems(folderItemList);

            String workItemDataJson = "";
            try {
                workItemDataJson = new ObjectMapper().writeValueAsString(workItem);
            } catch (JsonProcessingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("Json data: " + workItemDataJson);

            return workItemDataJson;
        }

        private Map<String,Object> getMapOfAttributes() {
            Map<String,Object> map = new HashMap<String,Object>();
            for (AttributeXML attribute : this.attributeList) {
                map.put(attribute.getName(),attribute.getValue());
            }
            return map;
        }
    }

    @XmlType(propOrder={"templateId", "attributeList", "pageList"})
    @XmlRootElement(name="ImageDocument")
    private static class ImageDocumentXML {

        private String templateId = "";

        private ArrayList<AttributeXML> attributeList;

        private ArrayList<ImagePageXML> pageList;

        @XmlElement(name = "TemplateId")
        public void setTemplateId(String templateId) {
            this.templateId = templateId;
        }

        public String getTemplateId() {
            return templateId;
        }

        @XmlElementWrapper(name = "Attributes")
        @XmlElement(name = "Attribute")
        public void setAttributeList(ArrayList<AttributeXML> attributeList) {
            this.attributeList = attributeList;
        }

        public ArrayList<AttributeXML> getAttributeList() {
            return attributeList;
        }

        public void setPageList(ArrayList<ImagePageXML> pageList) {
            this.pageList = pageList;
        }

        @XmlElement(name = "ImagePage")
        public ArrayList<ImagePageXML> getPageList() {
            return pageList;
        }


        private Map<String,Object> getMapOfAttributes() {
            Map<String,Object> map = new HashMap<String,Object>();
            for (AttributeXML attribute : this.attributeList) {
                map.put(attribute.getName(),attribute.getValue());
            }
            return map;
        }
    }

    @XmlRootElement(name = "ImagePage")
    private static class ImagePageXML {

        private String filePath = "";

        @XmlValue
        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }
    }

    @XmlRootElement(name="Attribute")
    private static class AttributeXML {

        private String name = "";
        private String value = "";

        @XmlAttribute(name = "name")
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @XmlValue
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
/*
    private void test() {
        final int limit = 5;
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(FoldersXML.class);

            Marshaller marshallerObj = jaxbContext.createMarshaller();
            //marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            FoldersXML f=new FoldersXML();
            f.folderList=new ArrayList<>();
            f.taskId="Init Scan in folders";
            //f.folderList.add(new FolderXML());

            FolderXML folder1 = new FolderXML();
            folder1.taskId = "InitialScan";
            folder1.attributeList = new ArrayList<AttributeXML>();

            folder1.templateId = "Temp001";

            for(int i = 1; i < limit; i++) {
                AttributeXML attr1 = new AttributeXML();
                attr1.name = "attr" + i;
                attr1.value = "value of attr" + i;
                folder1.attributeList.add(attr1);
            }

            ImageDocumentXML doc1 = new ImageDocumentXML();
            doc1.attributeList = new ArrayList<AttributeXML>();
            doc1.templateId = "TempDoc001";

            for(int i = 1; i < limit; i++) {
                AttributeXML attr = new AttributeXML();
                attr.name = "doc_attr" + i;
                attr.value = "doc_value of attr" + i;
                doc1.attributeList.add(attr);
            }

            ImagePageXML page1 = new ImagePageXML();
            page1.filePath = "6111.IMG";

            doc1.pageList = new ArrayList<>();
            doc1.pageList.add(page1);

            ImagePageXML page2 = new ImagePageXML();
            page2.filePath = "6112.IMG";
            doc1.pageList.add(page2);

            ImageDocumentXML doc2 = new ImageDocumentXML();
            doc2.attributeList = new ArrayList<AttributeXML>();
            doc2.pageList = new ArrayList<>();
            doc2.pageList.add(page1);

            folder1.documentList = new ArrayList<>();
            folder1.documentList.add(doc1);
            folder1.documentList.add(doc2);

            f.folderList.add(folder1);

            StringWriter sw = new StringWriter();
            marshallerObj.marshal(f, sw);
            System.out.println(sw.toString());
        } catch (JAXBException e2) {
            e2.printStackTrace();
        }
    }*/
}
