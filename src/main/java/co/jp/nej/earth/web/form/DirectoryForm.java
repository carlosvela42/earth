package co.jp.nej.earth.web.form;

import co.jp.nej.earth.model.BaseModel;
import co.jp.nej.earth.model.Directory;
import org.hibernate.validator.constraints.NotEmpty;

public class DirectoryForm extends BaseModel<Directory> {

    private static final long serialVersionUID = 1L;

@NotEmpty(message = "E0001,dataDirectoryId")
  private String dataDirectoryId;

  @NotEmpty(message = "E0001,folderPath")
  private String folderPath;

  @NotEmpty(message = "E0001,newCreateFile")
  private String newCreateFile;

  @NotEmpty(message = "E0001,reservedDiskVolSize")
  private String reservedDiskVolSize;

  @NotEmpty(message = "E0001,diskVolSize")
  private String diskVolSize;

  public String getDataDirectoryId() {
    return dataDirectoryId;
  }

  public void setDataDirectoryId(String dataDirectoryId) {
    this.dataDirectoryId = dataDirectoryId;
  }

  public String getFolderPath() {
    return folderPath;
  }

  public void setFolderPath(String folderPath) {
    this.folderPath = folderPath;
  }

  public String getNewCreateFile() {
    return newCreateFile;
  }

  public void setNewCreateFile(String newCreateFile) {
    this.newCreateFile = newCreateFile;
  }

  public String getReservedDiskVolSize() {
    return reservedDiskVolSize;
  }

  public void setReservedDiskVolSize(String reservedDiskVolSize) {
    this.reservedDiskVolSize = reservedDiskVolSize;
  }

  public String getDiskVolSize() {
    return diskVolSize;
  }

  public void setDiskVolSize(String diskVolSize) {
    this.diskVolSize = diskVolSize;
  }

}
