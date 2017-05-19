//package co.jp.nej.earth.service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import co.jp.nej.earth.BaseTest;
//import co.jp.nej.earth.exception.EarthException;
//import co.jp.nej.earth.model.Directory;
//import co.jp.nej.earth.model.Document;
//import co.jp.nej.earth.model.DocumentSavingInfo;
//import co.jp.nej.earth.model.enums.DocumentSavingType;
//
//public class DocumentServiceTest extends BaseTest {
//
//    @Autowired
//    private DocumentService documentService;
//
//    private Document document;
//
//    private DocumentSavingInfo documentSavingInfo;
//
//    private static final int FOLDERITEMNO = 1121;
//
//    @Before
//    public void excutedBefore() {
//        document = new Document();
//        document.setFolderItemNo(FOLDERITEMNO);
//        document.setDocumentNo(-1);
//        document.setDocumentPath("C:\\test\\test.png");
//        document.setWorkitemId("1002");
//
//        documentSavingInfo = new DocumentSavingInfo();
//
//        documentSavingInfo.setSavingType(DocumentSavingType.FILE_UNTIL_FULL);
//        Directory directory = new Directory();
//        directory.setDataDirectoryId(1);
//        directory.setDiskVolSize("1000000000000000");
//        directory.setReservedDiskVolSize("111111111111111");
//        directory.setFolderPath("C:\\Users\\p-tvo-sonta\\Desktop\\directory");
//        List<Directory> list = new ArrayList<>();
//        list.add(directory);
//        documentSavingInfo.setDataDef(list);
//    }
//
//    @Test
//    public void getDocumentTest() {
//        System.out.println("-----------------------------------------------");
//        try {
//            System.out.println(documentService.getDocument(document, documentSavingInfo));
//        } catch (EarthException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void saveDocumentTest() {
//        try {
//            Assert.assertEquals(documentService.saveDocument(document, documentSavingInfo), true);
//        } catch (EarthException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void getDocumentSavingInfoTest() throws EarthException {
//        DocumentSavingInfo documentSavingInfo = documentService.getDocumentSavingInfo(1, 1);
//        Assert.assertEquals(documentSavingInfo.getSavingType(), DocumentSavingType.FILE_UNTIL_FULL);
//    }
//}
