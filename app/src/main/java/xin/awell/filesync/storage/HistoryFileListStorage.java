package xin.awell.filesync.storage;


import java.util.List;
import java.util.Stack;

/*
* 临时存储一个递进式列表展示的数据
* */
public class HistoryFileListStorage <T>{

    private String currentPath;
    private Stack<List<T>> historyStack;

    public HistoryFileListStorage(){
        historyStack = new Stack<>();
    }

    public void push(List<T> data){
        historyStack.push(data);
    }

    public List<T> pop(){
        return historyStack.pop();
    }
}
