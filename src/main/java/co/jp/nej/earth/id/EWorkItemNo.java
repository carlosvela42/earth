package co.jp.nej.earth.id;

import co.jp.nej.earth.exception.EarthException;

public interface EWorkItemNo {

    int getNextHistoryNo(String workItemId) throws EarthException;

    int getNextFolderItemNo(String workItemId) throws EarthException;

    int getNextDocumentNo(String workItemId, int folderItemNo) throws EarthException;

    int getNextLayerNo(String workItemId, int folderItemNo, int documentNo) throws EarthException;
}
