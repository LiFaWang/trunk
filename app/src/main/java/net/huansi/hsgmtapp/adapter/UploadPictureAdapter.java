package net.huansi.hsgmtapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import net.huansi.hsgmtapp.R;
import net.huansi.hsgmtapp.activity.LargerImageSHowActivity;
import net.huansi.hsgmtapp.utils.BitmapCache;
import net.huansi.hsgmtapp.utils.DeviceUtil;
import net.huansi.hsgmtapp.utils.OthersUtil;
import net.huansi.hsgmtapp.utils.ViewHolder;

import java.io.File;
import java.util.List;

/**
 * Created by 单中年 on 2016/12/12.
 */

public class UploadPictureAdapter extends BaseAdapter {
    private List<String> mPath;
    private Context mContext;
    private LayoutInflater mInflater;

    public UploadPictureAdapter(List<String> path, Context context) {
        this.mPath = path;
        this.mContext = context;
        mInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mPath.size();
    }

    @Override
    public Object getItem(int position) {
        return mPath.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Wrapper wrapper;
        if(convertView==null){
            convertView=mInflater.inflate(R.layout.upload_picture_item,parent,false);
            wrapper=new Wrapper();
            wrapper.imvUploadPictureItem= (ImageView) convertView.findViewById(R.id.imvUploadPictureItem);
            int width= (int)(parent.getWidth()/5.0);
            convertView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT,width));
            wrapper.imvUploadPictureItem.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,width));
            convertView.setTag(wrapper);
        }else {
            wrapper= (Wrapper) convertView.getTag();
        }
//        ImageView imvUploadPictureItem= ViewHolder.get(convertView,R.id.imvUploadPictureItem);
//        parent.measure(0,0);

//        LayoutParams lp=imvUploadPictureItem.getLayoutParams();
//        lp.width=width;
//        lp.height=width;
//        imvUploadPictureItem.setLayoutParams(lp);
        File file=new File(mPath.get(position));
        Glide.with(mContext)
                .load(file)
                .asBitmap()
                .centerCrop()
//                .placeholder(R.drawable.myicon_desktop)
                .into(wrapper.imvUploadPictureItem);

//        imvUploadPictureItem.setImageBitmap(OthersUtil.changePictureSize
//                (BitmapCache.getInstance().getBitmap(mPath.get(position)),width));
        return convertView;
    }

    class Wrapper{
        ImageView imvUploadPictureItem;
    }


}
