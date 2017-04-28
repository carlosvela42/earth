package co.jp.nej.earth.dao;

import org.springframework.stereotype.Repository;

import co.jp.nej.earth.model.Layer;

@Repository
public class LayerDaoImpl extends BaseDaoImpl<Layer> implements LayerDao {

    public LayerDaoImpl() throws Exception {
        super();
    }

}
