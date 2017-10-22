package net.huansi.hsgmtapp.reciver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;


import net.huansi.hsgmtapp.model.Constans;
import net.huansi.hsgmtapp.utils.OthersUtil;

import static android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE;

/**
 * Created by qiuliang on 2017/5/11.
 */

public class OtherReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()){
            case ACTION_DOWNLOAD_COMPLETE:
                downComplete(context,intent);
                break;
        }
    }


    /**
     * 下载完成
     * @param context
     * @param intent
     */
    private void downComplete(Context context,Intent intent){
        //TODO 判断这个id与之前的id是否相等，如果相等说明是之前的那个要下载的文件
        long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
        String idSave= OthersUtil.getSpData(Constans.UPDATE_DOWN_APK_ID,context);//下载的时候保存的id
        if(idSave.isEmpty()||!idSave.equalsIgnoreCase(id+"")){
            return;
        }
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(id);
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Cursor cursor = downloadManager.query(query);
        String path = null;
        //TODO 这里把所有的列都打印一下，有什么需求，就怎么处理,文件的本地路径就是path
        while(cursor.moveToNext()) {
            path=cursor.getString(cursor.getColumnIndex("local_filename"));
        }
        if(path==null) return;
        OthersUtil.installApk(context,path);
    }
}
