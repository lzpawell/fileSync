package xin.awell.filesync.model;

public class MyFile {
    private String name;
    private boolean directory;
    private long size;
    private int subFileCount;
    private long modifiedTime;
    private String path;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getSubFileCount() {
        return subFileCount;
    }

    public void setSubFileCount(int subFileCount) {
        this.subFileCount = subFileCount;
    }

    public long getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(long modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
