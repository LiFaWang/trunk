package net.huansi.hsgmtapp.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by Administrator on 2016/2/27.
 */
public class OnCamera {
    private OnCamera(){}


//    public Bitmap getPictureFromCamera(String path) {
//        Bitmap bitmap=null;
//        bitmap= BitmapCache.getInstance().getBitmap(path);
//        return bitmap;
//    }
    public static String feedbackMediaPackageName="";



    public static String getPathFromCamera(Activity activity,int requestCode){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File path1 = null;
            //创建文件夹
            if (feedbackMediaPackageName.equals("")) {
                feedbackMediaPackageName = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera";
                path1 = new File(feedbackMediaPackageName);
                if (!path1.exists()) {
                    path1.mkdirs();
                }
            } else {
                path1 = new File(feedbackMediaPackageName);
            }
            File file = new File(path1, "IMG_" + System.currentTimeMillis() + ".jpg");
            Uri mOutPutFileUri = Uri.fromFile(file);
            Bundle bundle = new Bundle();
            bundle.putParcelable(MediaStore.EXTRA_OUTPUT, mOutPutFileUri);
            intent.putExtras(bundle);
            activity.startActivityForResult(intent, requestCode);
            return mOutPutFileUri.getPath();
        }else {
            OthersUtil.ToastMsg(activity,"请插入SD卡");
            return null;
        }
    }


    public static String  getPictureFromAlbum(Activity activity,Uri uri){
        String path=null;

                //显得到bitmap图片这里开始的第二部分，获取图片的路径：

                String[] proj = {MediaStore.Images.Media.DATA};

        ContentResolver cr = activity.getContentResolver();
                //好像是android多媒体数据库的封装接口，具体的看Android文档
//        Cursor cursor = activity.managedQuery(uri, proj, null, null, null);
        Cursor cursor=cr.query(uri, proj,null, null, null);
        if(cursor!=null) {
            //按我个人理解 这个是获得用户选择的图片的索引值
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            //将光标移至开头 ，这个很重要，不小心很容易引起越界
            cursor.moveToFirst();
            //最后根据索引值获取图片路径
            path = cursor.getString(column_index);
        }else {
            path=uri.getPath();
        }
        if(cursor!=null)  cursor.close();
        return path;
    }


}
