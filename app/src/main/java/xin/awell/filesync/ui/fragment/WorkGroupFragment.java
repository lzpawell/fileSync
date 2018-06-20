package xin.awell.filesync.ui.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xin.awell.filesync.R;
import xin.awell.filesync.databinding.FragmentWorkGroupBinding;
import xin.awell.filesync.model.MyFile;
import xin.awell.filesync.model.User;
import xin.awell.filesync.model.WorkGroup;
import xin.awell.filesync.service.FileLoadService;
import xin.awell.filesync.service.FileLoadServiceImpl;
import xin.awell.filesync.service.INetService;
import xin.awell.filesync.service.NetService;
import xin.awell.filesync.ui.adapter.NormalFileListAdapter;
import xin.awell.filesync.ui.popup.PopupWinMkDir;

/**
 */
public class WorkGroupFragment extends Fragment {

    FragmentWorkGroupBinding binding;

    INetService netService;

    public WorkGroupFragment() {
        // Required empty public constructor
    }



    public static WorkGroupFragment newInstance() {
        WorkGroupFragment fragment = new WorkGroupFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        netService = NetService.getInstance();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_work_group, container,false);
        initView();

        return binding.getRoot();
    }

    private void initView() {
        binding.toolbar.setTitle("工作组");
    }


    @Override
    public void onResume() {
        super.onResume();

        //loadData;
        User user = User.getCurrentUser();
        if (user != null){
            Call<List<WorkGroup>> call = netService.listGroup(user.getUserId());
            call.enqueue(new Callback<List<WorkGroup>>() {
                @Override
                public void onResponse(Call<List<WorkGroup>> call, Response<List<WorkGroup>> response) {
                    for(WorkGroup workGroup : response.body()){
                        Log.i("workGroup", workGroup.getGroupId() + "  " + workGroup.getFolderLocation());
                    }

                    getActivity().runOnUiThread(()->{
                        initRootList(response.body());
                    });
                }

                @Override
                public void onFailure(Call<List<WorkGroup>> call, Throwable throwable) {
                    Log.i("retrofit", "加载workGroup 失败");
                }
            });
        }
    }


    private HashMap<String, NormalFileListAdapter> adapterHashMap = new HashMap<>();

    public void initRootList(List<WorkGroup> workGroupList){
        List<String> stringList = new ArrayList<>();
        for (WorkGroup workGroup : workGroupList)
            stringList.add(workGroup.getGroupId());


        ArrayAdapter arrayAdapter = new ArrayAdapter(this.getActivity(), android.R.layout.simple_list_item_1, stringList);

        binding.lvFile.setAdapter(arrayAdapter);
        binding.lvFile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                binding.lvFile.setVisibility(View.GONE);
                binding.lvSingleGroupFile.setVisibility(View.VISIBLE);
                //发起群组浏览
                groupId = workGroupList.get(i).getGroupId();
                currentPath = "/";
                viewGroupFileList();
            }
        });
    }


    private String groupId;
    private String currentPath;
    private PopupWinMkDir popupWinMkDir;
    private NormalFileListAdapter groupFileListAdapter;
    private FileLoadService fileLoadService;

    private void viewGroupFileList() {
        fileLoadService = new FileLoadServiceImpl(System.out::println);
        initGroupFileView();
        new Thread(()->{
            try {
                List<MyFile> data = new ArrayList<>();
                data = fileLoadService.loadFile(groupId, currentPath);
                handleGroupFileData(data);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    private void initGroupFileView() {

        /*
         * init popup
         * */
        popupWinMkDir = new PopupWinMkDir(getActivity(), binding.layoutRoot, new PopupWinMkDir.OnGetDirNameCallback() {
            @Override
            public void getDirName(String dirName) {
                String folderPath = currentPath +dirName;
                Call<JSONObject> call = netService.createDir(groupId, folderPath);
                Log.i("retrofit", "create dir : "  + folderPath);
                call.enqueue(new Callback<JSONObject>() {
                    @Override
                    public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                        //Log.i("retrofit", response.body() + "");
                        //创建成功后， refresh一下页面
                        Log.i("retrofit", "created");
                        new Thread(()->{
                            try {
                                if(groupId != null)
                                    handleGroupFileData(fileLoadService.refresh());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }).start();
                        Log.i("retrofit", "refresh failed");
                    }

                    @Override
                    public void onFailure(Call<JSONObject> call, Throwable throwable) {
                        Log.i("retrofit", throwable.getMessage());
                        throwable.printStackTrace();
                    }
                });
            }
        });

        /*
         * init toolbar
         * */
        binding.toolbar.inflateMenu(R.menu.file_toolbar_menu);
        binding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_refresh:
                        if(groupId == null)
                            return true;
                        new Thread(()->{
                            List<MyFile> data = new ArrayList<>();
                            try {
                                data = fileLoadService.refresh();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            handleGroupFileData(data);
                        }).start();
                        break;
                    case R.id.item_mk_dir:
                        Log.i("fileFragment", item.getItemId() + " mkdir");
                        popupWinMkDir.show();
                        break;

                    default:break;
                }
                return false;
            }
        });



        groupFileListAdapter = new NormalFileListAdapter(this.getActivity(), fileLoadService, binding.layoutRoot);

        binding.lvSingleGroupFile.setAdapter(groupFileListAdapter);
    }

    private void handleGroupFileData(List<MyFile> data) {
        if(groupId != null){
            groupFileListAdapter.setDataAndRefreshList(data);
        }
    }

    /*
     * 处理这个fragment处于首页时， 一些返回键按压的效果
     * 成功return true
     * */
    public boolean onKeyBackPressed(){
        List<MyFile> dataList = fileLoadService.rockback();
        if(dataList == null){
            if(groupId == null){
                return false;
            }else{
                groupId = null;
                currentPath = "/";
                binding.lvSingleGroupFile.setVisibility(View.GONE);
                binding.lvFile.setVisibility(View.VISIBLE);
                return true;
            }

        }else{
            groupFileListAdapter.setDataAndRefreshList(dataList);
            return true;
        }
    }
}
