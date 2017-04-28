package co.jp.nej.earth.dao;

import org.springframework.stereotype.Repository;

import co.jp.nej.earth.model.Document;

@Repository
public class DocumentDaoImpl extends BaseDaoImpl<Document> implements DocumentDao {

    public DocumentDaoImpl() throws Exception {
        super();
    }

}
