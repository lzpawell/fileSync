package xin.awell.filesync.model;

public class LoadProgressVO {
    private LoadProgress loadProgress;
    private String currentSpeed;

    public LoadProgressVO(){

    }

    public LoadProgressVO(LoadProgress loadProgress){
        this.loadProgress = loadProgress;

    }

    public LoadProgress getLoadProgress() {
        return loadProgress;
    }

    public void setLoadProgress(LoadProgress loadProgress) {
        this.loadProgress = loadProgress;
    }

    public String getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(String currentSpeed) {
        this.currentSpeed = currentSpeed;
    }
}
