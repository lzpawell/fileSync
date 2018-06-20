package xin.awell.filesync.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import xin.awell.filesync.R;
import xin.awell.filesync.model.LoadProgress;
import xin.awell.filesync.model.LoadProgressVO;

public class TransferListAdapter extends BaseAdapter {


    private Activity activity;
    private List<LoadProgressVO> loadProgressVOList;

    public TransferListAdapter(Activity activity, List<LoadProgressVO> dataList){
        this.activity = activity;
        this.loadProgressVOList = dataList;
    }

    @Override
    public int getCount() {
        return loadProgressVOList.size();
    }

    @Override
    public Object getItem(int i) {
        return loadProgressVOList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder{
        TextView tvCurrentSize;
        TextView tvTotalSize;
        TextView tvCurrentSpeed;
        ProgressBar progressBar;
        ImageView ivOperation;
        TextView tvFileName;

        public ViewHolder(View rootView){
            tvCurrentSize = rootView.findViewById(R.id.tv_current_length);
            tvCurrentSpeed = rootView.findViewById(R.id.tv_current_speed);
            tvTotalSize = rootView.findViewById(R.id.tv_total_length);
            progressBar = rootView.findViewById(R.id.progress_trans);
            ivOperation = rootView.findViewById(R.id.img_transfer);
            tvFileName = rootView.findViewById(R.id.tv_file_time);
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view == null){
            view = activity.getLayoutInflater().inflate(R.layout.list_item_trans_control, null);
        }
        holder = new ViewHolder(view);

        LoadProgressVO loadProgressVO = this.loadProgressVOList.get(i);
        holder.tvFileName.setText(loadProgressVO.getLoadProgress().getFileName());
        holder.tvCurrentSize.setText(loadProgressVO.getLoadProgress().getCurrentFinishedLength() + "");
        holder.tvTotalSize.setText(loadProgressVO.getLoadProgress().getFileLength() + "");
        holder.ivOperation.setOnClickListener(view1 -> {

        });
        return null;
    }
}
