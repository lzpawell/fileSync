package xin.awell.filesync.ui.popup;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import xin.awell.filesync.R;

public class PopupMenuNormalFileMore {
    public static void showMenu(Context context, View anchorView){
        PopupMenu popupMenu = new PopupMenu(context, anchorView);
        popupMenu.inflate(R.menu.file_normal_more);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_delete:
                        Log.i("popup", "item delete");
                        break;
                    case R.id.item_download_default_pos:
                        Log.i("popup", "item download default");
                        break;
                    case R.id.item_move:
                        Log.i("popup", "item move");
                        break;
                    case R.id.item_rename:
                        Log.i("popup", "item rename");
                        break;

                    default:break;
                }
                return false;
            }
        });

        popupMenu.show();
    }
}
