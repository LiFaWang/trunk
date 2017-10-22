package net.huansi.hsgmtapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import net.huansi.hsgmtapp.R;
import net.huansi.hsgmtapp.activity.LargerImageSHowActivity;
import net.huansi.hsgmtapp.model.JsSubmitBrowse;
import net.huansi.hsgmtapp.model.JsSubmitBrowseParent;
import net.huansi.hsgmtapp.utils.OthersUtil;

import java.io.File;
import java.util.List;


/**
 * Created by Administrator on 2016/4/22 0022.
 */
public class JsSubmitBrowseAdapter extends BaseExpandableListAdapter {
    private List<JsSubmitBrowseParent> mParentList;
    private List<List<JsSubmitBrowse>> mChildList;
//    private List<List<Boolean>> mIsRecordingList;
    private Context mContext;
    private static Handler handler;
    private String urlHead="";
//    private LoadProgressDialog dialog;

    public JsSubmitBrowseAdapter(List<JsSubmitBrowseParent> parentList,
                                 List<List<JsSubmitBrowse>> childList, Context context){
//        this.urlHead=urlHead;
        mParentList=parentList;
        mChildList=childList;
        mContext=context;
        try {
            urlHead=OthersUtil.getWebserviceUrl(context)+"/";
        } catch (Exception e) {
            e.printStackTrace();
        }
//        mIsRecordingList=isRecordingList;
//        handler = new Handler(){
//
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                notifyDataSetChanged();
//            }
//        };
//        dialog=new LoadProgressDialog((Activity) mContext);
    }



//    public void reFresh(){
//        handler.sendEmptyMessage(0);
//    }
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return mParentList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mParentList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChildList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        BrowseParentWrapper wrapper;
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.js_submit_browse_parent_item,parent,false);
            wrapper=new BrowseParentWrapper();
            convertView.setTag(wrapper);
            findViewByParent(convertView,wrapper);
        }else {
            wrapper= (BrowseParentWrapper) convertView.getTag();
        }
        if(isExpanded){
            wrapper.imvJsSubmitBrowseParentItemIndicator.setImageDrawable(mContext.getResources()
                    .getDrawable(R.drawable.icon_expanlistview_arraw_down));
        }else {
            wrapper.imvJsSubmitBrowseParentItemIndicator.setImageDrawable(mContext.getResources()
                    .getDrawable(R.drawable.icon_expanlistview_arraw_right));
        }
        initParent(wrapper,mParentList.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        BrowseChildWrapper wrapper = null;
        convertView = LayoutInflater.from(mContext).inflate(R.layout.js_submit_browse_child_item, parent, false);
        convertView.setTag(R.layout.js_submit_browse_parent_item,groupPosition);
        convertView.setTag(R.layout.js_submit_browse_child_item,childPosition);

        wrapper = new BrowseChildWrapper();
        findViewByChild(convertView, wrapper);
//        final JsSBroAdapterToJsBroActiEvent event = new JsSBroAdapterToJsBroActiEvent();
        final JsSubmitBrowse browse = mChildList.get(groupPosition).get(childPosition);
//        boolean isRecoding = mIsRecordingList.get(groupPosition).get(childPosition);
        final File file=new File(browse.SDTLLOCALFILEPATH);
        initChild(wrapper, browse, file,convertView);
        wrapper.imvJsSubmitBrowseChildItemPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageViewClick(file,browse);
            }
        });
//        wrapper.jsSubmitBrowseChildItemLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                TextView tvFileType= (TextView) view.findViewById(R.id.tvJsSubmitBrowseChildItemFileName);
//
//            }
//        });
//        wrapper.jsSubmitBrowseChildItemRecordLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                File file = new File(browse.SDTLLOCALFILEPATH);
//                if (file.exists()) {
//                    //播放录音
//                    event.index = JsSubmitBrowseActivity.RECORD_PLAY;
//                    event.object = browse.SDTLLOCALFILEPATH;
//                    event.childPosition = childPosition;
//                    event.groupPosition = groupPosition;
//                } else {
//                    //播放网络录音
//                    event.index = JsSubmitBrowseActivity.RECORD_DOWNLOAD;
//                    event.object=browse.SDTLFILEPATH;
//                }
//                EventBus.getDefault().post(event);
//            }
//        });
//        wrapper.imvJsSubmitBrowseChildItemPicture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//                String pictureUrl="";
//                if(file.exists()){
//                    pictureUrl=Uri.fromFile(file).toString();
//                }else {
//                    pictureUrl=urlHead + browse.SDTLFILEPATH;
//                }
//                ImageLoader.getInstance().loadImage(pictureUrl, ImageLoadeUtil.getOptions(),
//                        new SimpleImageLoadingListener() {
//                            @Override
//                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                                OthersUtil.dismissLoadDialog(dialog);
//                                ChangeImage.showPop(mBiggerImagePopup,ChangeImage.enlargePicture(loadedImage, (Activity) mContext),v,
//                                        (Activity) mContext);
//                            }
//
//                            @Override
//                            public void onLoadingStarted(String imageUri, View view) {
//                                super.onLoadingStarted(imageUri, view);
//                                OthersUtil.showLoadDialog(dialog,mContext);
//                            }
//                        });
//            }
//        });
        // 打开本地文件
//        wrapper.tvJsSubmitBrowseChildItemFileName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String fileNameUrl="";
//                String filePath="sdcard/"+
//                        browse.SDTLFILEPATH.substring(browse.SDTLFILEPATH.lastIndexOf("/")+1,browse.SDTLFILEPATH.length());
//                File file=new File(filePath);
//                if (file.exists()){
//                    event.index=JsSubmitBrowseActivity.OPEN_FILE;
//                    event.object=file;
//                }else {
//                    filePath="sdcard/";
//                    fileNameUrl=urlHead+browse.SDTLFILEPATH;
//                    event.index=JsSubmitBrowseActivity.DOWNLOAD_FILE;
//                    event.object=filePath;
//                    event.fileNameUrl=fileNameUrl;
//                    event.fileName=browse.SDTLFILEPATH.substring(browse.SDTLFILEPATH.lastIndexOf("/")+1,browse.SDTLFILEPATH.length());
//                }
//                EventBus.getDefault().post(event);
//            }
//        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }


    class BrowseParentWrapper{
        ImageView imvJsSubmitBrowseParentItemIndicator;
        TextView tvJsSubmitBrowseParentItemContent,//内容
                tvJsSubmitBrowseParentItemTime,//时间
                tvJsSubmitBrowseParentItemDate,//日期
                tvJsSubmitBrowseParentItemTitle,//主题
                tvJsSubmitBrowseParentItemChildCount;//子控件的个数
    }

    class BrowseChildWrapper{
//        RelativeLayout jsSubmitBrowseChildItemLayout; //总的Layout
//        LinearLayout jsSubmitBrowseChildItemRecordLayout;//录音
        ImageView imvJsSubmitBrowseChildItemPicture,//图片
                imvJsSubmitBrowseChildItemRecordPlay;//播放的按钮
        TextView tvJsSubmitBrowseChildItemSeq,//附件的顺序号
                tvJsSubmitBrowseChildItemRecordTimes;//时长
//        TextView tvJsSubmitBrowseChildItemFileName; //文件名字(当文件无法打开时，如.doc/.xls/.pdf 等直接显示名字)
//        TextView getTvJsSubmitBrowseChildItemFileHint; //文件提示信息
    }

    private void findViewByParent(View view,BrowseParentWrapper wrapper){
        wrapper.imvJsSubmitBrowseParentItemIndicator= (ImageView) view.
                findViewById(R.id.imvJsSubmitBrowseParentItemIndicator);
        wrapper.tvJsSubmitBrowseParentItemContent= (TextView) view.
                findViewById(R.id.tvJsSubmitBrowseParentItemContent);
        wrapper.tvJsSubmitBrowseParentItemTime= (TextView) view.
                findViewById(R.id.tvJsSubmitBrowseParentItemTime);
        wrapper.tvJsSubmitBrowseParentItemDate= (TextView) view.
                findViewById(R.id.tvJsSubmitBrowseParentItemDate);
        wrapper.tvJsSubmitBrowseParentItemTitle= (TextView) view.
                findViewById(R.id.tvJsSubmitBrowseParentItemTitle);
    }

    private void findViewByChild(View view,BrowseChildWrapper wrapper){
//        wrapper.jsSubmitBrowseChildItemRecordLayout= (LinearLayout) view.
//                findViewById(R.id.jsSubmitBrowseChildItemRecordLayout);
        wrapper.imvJsSubmitBrowseChildItemPicture= (ImageView) view.
                findViewById(R.id.imvJsSubmitBrowseChildItemPicture);
//        wrapper.imvJsSubmitBrowseChildItemRecordPlay= (ImageView) view.
//                findViewById(R.id.imvJsSubmitBrowseChildItemRecordPlay);
//        wrapper.tvJsSubmitBrowseChildItemRecordTimes= (TextView) view.
//                findViewById(R.id.tvJsSubmitBrowseChildItemRecordTimes);
        wrapper.tvJsSubmitBrowseChildItemSeq= (TextView) view.
                findViewById(R.id.tvJsSubmitBrowseChildItemSeq);
//        wrapper.tvJsSubmitBrowseChildItemFileName= (TextView) view.
//                findViewById(R.id.tvJsSubmitBrowseChildItemFileName);
//        wrapper.getTvJsSubmitBrowseChildItemFileHint= (TextView) view.
//                findViewById(R.id.tvJsSubmitBrowseChildItemFileHint);
//        wrapper.jsSubmitBrowseChildItemLayout= (RelativeLayout) view.
//                findViewById(R.id.jsSubmitBrowseChildItemLayout);
    }



    private void initChild(final BrowseChildWrapper wrapper, JsSubmitBrowse jsSubmitBrowse, File file,View convertView){
        wrapper.tvJsSubmitBrowseChildItemSeq.setText(jsSubmitBrowse.IDTLSEQ);
//        wrapper.getTvJsSubmitBrowseChildItemFileHint.setText(jsSubmitBrowse.SFILEDESC);

        if(jsSubmitBrowse.SDTLFILETYPE.toUpperCase().equals("IMAGE")) {
            if (!file.exists()) {
                Glide.with(mContext)
                        .load(urlHead + jsSubmitBrowse.SDTLTHUMBFILEPATH)
                        .asBitmap()
                        .centerCrop()
                        .into(wrapper.imvJsSubmitBrowseChildItemPicture);
//                ImageLoader.getInstance().displayImage(urlHead + jsSubmitBrowse.SDTLTHUMBFILEPATH,
//                        wrapper.imvJsSubmitBrowseChildItemPicture, ImageLoadeUtil.getOptions());
            } else {
                Glide.with(mContext)
                        .load(file)
                        .asBitmap()
                        .centerCrop()
                        .into(wrapper.imvJsSubmitBrowseChildItemPicture);
//                ImageLoader.getInstance().displayImage(Uri.fromFile(file).toString(),
//                        wrapper.imvJsSubmitBrowseChildItemPicture,ImageLoadeUtil.getOptions());
            }

        }
//        wrapper.tvJsSubmitBrowseChildItemFileName.setVisibility(View.VISIBLE);
        wrapper.imvJsSubmitBrowseChildItemPicture.setVisibility(View.VISIBLE);
//        wrapper.jsSubmitBrowseChildItemRecordLayout.setVisibility(View.GONE);
//        wrapper.tvJsSubmitBrowseChildItemFileName.setText(mContext.getResources().getString(R.string.feedback_adapter_picture_count_dafault));

    }

    private void initParent(BrowseParentWrapper wrapper,JsSubmitBrowseParent  browseParent){
        wrapper.tvJsSubmitBrowseParentItemTitle.setText(browseParent.SHDRTITLE);
        wrapper.tvJsSubmitBrowseParentItemContent.setText(browseParent.SHDRCONTENTS);
        wrapper.tvJsSubmitBrowseParentItemDate.setText(browseParent.DHDRDATE);
        wrapper.tvJsSubmitBrowseParentItemTime.setText(browseParent.DHDRTIME);
    }


//    /**
//     * 获得录音的时长
//     * @param string
//     * @return
//     */
//    private String getTime(String string){
//// 使用此方法可以直接在后台获取音频文件的播放时间，而不会真的播放音频
//        MediaPlayer player = new MediaPlayer();  //首先你先定义一个mediaplayer
//        try {
//            player.setDataSource(string);  //String是指音频文件的路径
//            player.prepare();        //这个是mediaplayer的播放准备 缓冲
//
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        } catch (SecurityException e) {
//            e.printStackTrace();
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        int size =player.getDuration();//得到音频的时间
//       String longTime= TimeUtil.getDurByString((int) Math.ceil((size / 1000)));//转换为秒
//        player.stop();//暂停播放
//        player.release();//释放资源
//        return  longTime;  //返回音频时间
//    }

    /**
     * 图片文件的点击
     * @param file
     * @param browse
     */
    private void onImageViewClick(File file,JsSubmitBrowse browse){
        String pictureUrl="";
        if(file.exists()){
            pictureUrl=Uri.fromFile(file).toString();
        }else {
            pictureUrl=urlHead + browse.SDTLFILEPATH;
        }
        Intent intent=new Intent(mContext, LargerImageSHowActivity.class);
        intent.putExtra("picturePath",pictureUrl);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);

//        Glide.with(mContext)
//                .load(pictureUrl)
//                .asBitmap()
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                        OthersUtil.dismissLoadDialog(dialog);
////                        ChangeImage.showPop(mBiggerImagePopup,ChangeImage.enlargePicture(resource, (Activity) mContext),null,
////                                (Activity) mContext);
//                    }
//
//                    @Override
//                    public void onLoadStarted(Drawable placeholder) {
//                        super.onLoadStarted(placeholder);
//                        OthersUtil.showLoadDialog(dialog,mContext);
//                    }
//                });
//        ImageLoader.getInstance().loadImage(pictureUrl, ImageLoadeUtil.getOptions(),
//                new SimpleImageLoadingListener() {
//                    @Override
//                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                        OthersUtil.dismissLoadDialog(dialog);
//                        ChangeImage.showPop(mBiggerImagePopup,ChangeImage.enlargePicture(loadedImage, (Activity) mContext),view,
//                                (Activity) mContext);
//                    }
//
//                    @Override
//                    public void onLoadingStarted(String imageUri, View view) {
//                        super.onLoadingStarted(imageUri, view);
//                        OthersUtil.showLoadDialog(dialog,mContext);
//                    }
//                });
    }

//    /**
//     * 语音文件的点击
//     * @param browse
//     * @param event
//     * @param childPosition
//     * @param groupPosition
//     */
//    private void onAutoViewClick(JsSubmitBrowse browse,JsSBroAdapterToJsBroActiEvent event,int childPosition,int groupPosition){
//        File file = new File(browse.SDTLLOCALFILEPATH);
//        if (file.exists()) {
//            //播放录音
//            event.index = JsSubmitBrowseActivity.RECORD_PLAY;
//            event.object = browse.SDTLLOCALFILEPATH;
//            event.childPosition = childPosition;
//            event.groupPosition = groupPosition;
//        } else {
//            //播放网络录音
//            event.index = JsSubmitBrowseActivity.RECORD_DOWNLOAD;
//            event.object=browse.SDTLFILEPATH;
//        }
//        EventBus.getDefault().post(event);
//    }


//    /**
//     * 其他文件的点击
//     * @param browse
//     * @param event
//     */
//    private void onOtherViewClick(JsSubmitBrowse browse,JsSBroAdapterToJsBroActiEvent event){
//        String fileNameUrl="";
//        String filePath="sdcard/"+
//                browse.SDTLFILEPATH.substring(browse.SDTLFILEPATH.lastIndexOf("/")+1,browse.SDTLFILEPATH.length());
//        File file=new File(filePath);
//        if (file.exists()){
//            event.index=JsSubmitBrowseActivity.OPEN_FILE;
//            event.object=file;
//        }else {
//            filePath="sdcard/";
//            fileNameUrl=urlHead+browse.SDTLFILEPATH;
//            event.index=JsSubmitBrowseActivity.DOWNLOAD_FILE;
//            event.object=filePath;
//            event.fileNameUrl=fileNameUrl;
//            event.fileName=browse.SDTLFILEPATH.substring(browse.SDTLFILEPATH.lastIndexOf("/")+1,browse.SDTLFILEPATH.length());
//        }
//        EventBus.getDefault().post(event);
//    }
}
