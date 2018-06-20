package xin.awell.filesync.model;

import java.util.List;

public class WorkGroup {
    private String groupId;

    private String password;

    private String folderLocation;


    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getFolderLocation() {
        return folderLocation;
    }

    public void setFolderLocation(String folderLocation){
        this.folderLocation = folderLocation == null ? null : folderLocation.trim();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof WorkGroup == false)
            return false;
        else
            return ((WorkGroup)obj).getGroupId().equals(this.groupId);
    }
}