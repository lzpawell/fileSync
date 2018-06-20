package xin.awell.filesync.model;

public class LoadProgress {
    String fileName;
    String localFilePath;
    String remoteFilePath;
    Integer id;
    Long fileLength;
    Long currentFinishedLength;
    Integer status;   //downloading 1    uploading 2 pause_download 3  finish_download 4  finish_upload 5  pause_upload 6
    Long createTime;


    public static final Integer STATUS_DOWNLOADING = 1;
    public static final Integer STATUS_UPLOADING = 2;
    public static final Integer STATUS_PAUSE_DOWNLOAD= 3;
    public static final Integer STATUS_FINISH_DOWNLOAD = 4;
    public static final Integer STATUS_FINISH_UPLOAD = 5;
    public static final Integer STATUS_PAUSE_UPLOAD = 6;

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    public String getRemoteFilePath() {
        return remoteFilePath;
    }

    public void setRemoteFilePath(String remoteFilePath) {
        this.remoteFilePath = remoteFilePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileLength() {
        return fileLength;
    }

    public void setFileLength(Long fileLength) {
        this.fileLength = fileLength;
    }

    public Long getCurrentFinishedLength() {
        return currentFinishedLength;
    }

    public void setCurrentFinishedLength(Long currentFinishedLength) {
        this.currentFinishedLength = currentFinishedLength;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
