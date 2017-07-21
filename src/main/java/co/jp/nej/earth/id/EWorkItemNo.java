package co.jp.nej.earth.id;

import co.jp.nej.earth.exception.EarthException;

public interface EWorkItemNo {

    int getNextHistoryNo(String workItemId) throws EarthException;

    String getNextFolderItemNo() throws EarthException;

    String getNextDocumentNo() throws EarthException;

    String getNextLayerNo() throws EarthException;
}
