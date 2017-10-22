package net.huansi.hsgmtapp.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;


import net.huansi.hsgmtapp.R;
import net.huansi.hsgmtapp.adapter.JsSubmitBrowseAdapter;
import net.huansi.hsgmtapp.event.BackgroundEvent;
import net.huansi.hsgmtapp.event.SerialPortEvent;
import net.huansi.hsgmtapp.model.JsSubmitBrowse;
import net.huansi.hsgmtapp.model.JsSubmitBrowseParent;
import net.huansi.hsgmtapp.model.JsWebToApp;
import net.huansi.hsgmtapp.utils.OthersUtil;
import net.huansi.hsgmtapp.utils.WebServices;
import net.huansi.hsgmtapp.utils.WsUtil;
import net.huansi.hsgmtapp.view.LoadProgressDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import huansi.net.qianjingapp.entity.WsData;
import huansi.net.qianjingapp.entity.WsEntity;
import huansi.net.qianjingapp.utils.JSONEntity;
import huansi.net.qianjingapp.utils.WsUtilInLibrary;


/**
 * Created by Administrator on 2016/4/22 0022.
 */
public class JsSubmitBrowseActivity extends Activity {

    private ExpandableListView lvJsSubmitBrowse;
    private TextView tvTitle;
    private ImageView imvBack;
    private TextView tvAdd;//添加附件

    private List<JsSubmitBrowseParent> mParentList;
    private List<List<JsSubmitBrowse>> mChildList;
//    private List<List<Boolean>> isRecordingList;
    private JsSubmitBrowseAdapter mAdapter=null;

//    private PtrFrameLayout ptrJsSubmitBrowse;//下拉刷新

    private WebServices webServices;
    private JsWebToApp jsWebToApp;
    private WsData data;
//    private JSONEntity jsonEntity;
//    private MediaPlayer player;
//    private InteMediaPlayer interPlayer;//播放网络语音
    private LoadProgressDialog dialog;


    private StringBuffer sbError;
//    private String downHead="";

    private final int LIST_TASK=1;//获得数据的列表
    private final int DELETE_FILE_TASK=7;//删除附件

    private final int ADD_QUEST_CODE=8;//添加图片的请求码
//    public static final int RECORD_PLAY=2;//播放录音
//    public static final int RECORD_DOWNLOAD=3;//下载录音

//    public static final int OPEN_FILE=10; //打开本地文件
//    public static final int DOWNLOAD_FILE=11; //网络下载文件
//    private String billNo; //单号
//    private String attachmentType; //附件类型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.js_submit_browse_activity);
        EventBus.getDefault().register(this);
        findView();
        init();
//        fresh();
        lvJsSubmitBrowse.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Integer groupPos= (Integer) view.getTag(R.layout.js_submit_browse_parent_item);
                Integer childPos= (Integer) view.getTag(R.layout.js_submit_browse_child_item);
                if(childPos==null||groupPos==null){
                    return false;
                }
                setOnLongClick(groupPos,childPos);
                return true;
            }
        });
        imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText("文件浏览");
        OthersUtil.showLoadDialog(dialog);
        WsUtil.toAsync(LIST_TASK,JsSubmitBrowseActivity.class);
        //添加附件
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(JsSubmitBrowseActivity.this,UploadPictureActivity.class);
                intent.putExtra("jsWebToApp",jsWebToApp);
                startActivityForResult(intent,ADD_QUEST_CODE);
            }
        });

//        //下拉刷新
//        ptrJsSubmitBrowse.setPtrHandler(new PtrDefaultHandler() {
//            @Override
//            public void onRefreshBegin(PtrFrameLayout frame) {
//                fresh(false);
//            }
//        });
    }

//    /**
//     * 刷新或者第一次获取数据
//     */
//    private void fresh(){
//        if(isStart) showDialog();
//
//    }

    private void findView(){
//        ptrJsSubmitBrowse= (PtrFrameLayout) findViewById(R.id.ptrJsSubmitBrowse);
        lvJsSubmitBrowse= (ExpandableListView) findViewById(R.id.lvJsSubmitBrowse);
        View title=findViewById(R.id.jsSubmitBrowseTitle);
        tvTitle= (TextView) title.findViewById(R.id.tvTitleText);
        imvBack= (ImageView) title.findViewById(R.id.imvTiteBack);
        tvAdd= (TextView) findViewById(R.id.tvTitleRight);
    }

    private void init(){
//        try {
//            downHead=OthersUtil.getWebserviceUrl(getApplicationContext())+"/";
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        sbError=new StringBuffer();
        mParentList=new ArrayList<>();
        mChildList=new ArrayList<>();
//        OthersUtil.initRefresh(ptrJsSubmitBrowse,getApplicationContext());
        dialog=new LoadProgressDialog(JsSubmitBrowseActivity.this);
        webServices=new WebServices(getApplicationContext());
        jsWebToApp= (JsWebToApp) getIntent().getSerializableExtra("jsWebToApp");
        tvAdd.setText("添加");
        tvAdd.setVisibility(View.VISIBLE);
//        String addTypeCode=getIntent().getStringExtra(FinalString.APPROVAL_ADD_ENCLOSURE_PARAM_NAME);//添加的typecode
//        billNo=getIntent().getStringExtra("billNo"); //单号
//        attachmentType=getIntent().getStringExtra("attachmentType");
//        if (attachmentType!=null&&!attachmentType.isEmpty()) {
//            if (attachmentType.equals("3")) {
//                downHead = Data.getCustomerProjectServerIP(getApplicationContext());
//            }
//        }
//        //0：不显示附件按钮；1：显示附件按钮，但不能上传；2显示附件，也可上传；
//        if(addTypeCode==null||addTypeCode.isEmpty()||addTypeCode.equalsIgnoreCase("2")){
//            imvAdd.setVisibility(View.VISIBLE);
//        }else {
//            imvAdd.setVisibility(View.GONE);
//        }
//        jsonEntity=new JSONEntity();

//        isRecordingList=new ArrayList<>();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void async(BackgroundEvent event){
        if(event.aClass!=JsSubmitBrowseActivity.class) return;
        SerialPortEvent serialPortEvent =new SerialPortEvent();
        serialPortEvent.aClass=event.aClass;
        serialPortEvent.index=event.index;
        switch (event.index){
            case LIST_TASK:
                fileBrowseAsync();
                break;

            case DELETE_FILE_TASK:
                deleteFileAsync(event.str1);
                break;

        }

        EventBus.getDefault().post(serialPortEvent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void main(SerialPortEvent event){
        switch (event.index){
            case LIST_TASK:
                fileBrowseMain();
                mAdapter=new JsSubmitBrowseAdapter(mParentList,mChildList,JsSubmitBrowseActivity.this);
                lvJsSubmitBrowse.setAdapter(mAdapter);
//                int groupCount=lvJsSubmitBrowse.getCount();
//                for (int i=0;i<groupCount;i++){
//                    lvJsSubmitBrowse.expandGroup(i);
//                }
                break;
//            case RECORD_DOWNLOAD:
//                playRecordByInternet((String) event.object);
//                break;
            case DELETE_FILE_TASK:
                deleteFileMain();
                break;
        }

        if(!sbError.toString().isEmpty()){
            OthersUtil.ToastMsg(getApplicationContext(),sbError.toString());
            OthersUtil.clearSb(sbError);
        }
        OthersUtil.dismissLoadDialog(dialog);
//        if(dialog!=null&&dialog.isShowing()){
//            dialog.dismiss();
//        }
//        if(ptrJsSubmitBrowse.isRefreshing()){
//            ptrJsSubmitBrowse.refreshComplete();
//        }
    }

//    /**
//     * 显示dialog
//     */
//    private void showDialog(){
//        if(dialog!=null&&!dialog.isShowing()){
//            dialog.showLoadDialog(getResources().getString(R.string.dialog_loading));
//        }
//    }

//    /**
//     * adapter到activity的传值
//     * @param event
//     */
//    @Subscribe (threadMode = ThreadMode.MAIN)
//    public void jsAdapterToThisMain(JsSBroAdapterToJsBroActiEvent event){
//        switch (event.index){
//            //播放录音
//            case RECORD_PLAY:
//                playRecord(event);
//               break;
//            //播放网络录音
//            case RECORD_DOWNLOAD:
//                playRecordByInternetWarning(event);
//                break;
//            //打开文件
//            case OPEN_FILE:
//                Intent intent= FileUtils.getFileIntent(event.object.toString());
//                startActivity(intent);
//                break;
//            case DOWNLOAD_FILE:
//                final String fileNameUrl=event.fileNameUrl;
//                final String filePath=event.object.toString();
//                final String fileName=event.fileName;
//                AlertDialog.Builder builder=new AlertDialog.Builder(JsSubmitBrowseActivity.this);
//                builder.setMessage(getResources().getString(R.string.js_submit_browse_play_internet_reocrd_dialog))
//                        .setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        })
//                        .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                DownloadUtils.downLoadFile(fileNameUrl,filePath,fileName,JsSubmitBrowseActivity.this);
//                            }
//                        })
//                        .setCancelable(false)
//                        .show();
//                break;
//        }
//    }


    /**
     * 长按item
     * @param groupPosition
     * @param childPosition
     */
    private void setOnLongClick(final int groupPosition, final int childPosition){
        new AlertDialog.Builder(JsSubmitBrowseActivity.this)
                .setItems(new String []{"删除"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int which) {
                        if(mChildList.get(groupPosition).get(childPosition).BDTLALLOWDELETE.equals("0")){
                            OthersUtil.ToastMsg(getApplicationContext(),"您没有删除的权限");
                        }else {
                            OthersUtil.showLoadDialog(dialog);
                            WsUtil.toAsync(DELETE_FILE_TASK,JsSubmitBrowseActivity.class,
                                    mChildList.get(groupPosition).get(childPosition).IDTLIDEN);
//                            BackgroundEvent event = new JsSubmitNativeAsyncEvent();
//                            event.index = ;
//                            event.object = mChildList.get(groupPosition).get(childPosition).IDTLIDEN;
//                            EventBus.getDefault().post(event);
                        }
                        d.dismiss();
                    }
                })
                .show();

    }
//    /**
//     * 播放录音
//     */
//    private void playRecord(JsSBroAdapterToJsBroActiEvent event){
//        List<Boolean> isRocrdings=isRecordingList.get(event.groupPosition);
//        boolean isRcording=isRocrdings.get(event.childPosition);
//        //按下的item是正在播放的
//        if(isRcording){
//            isRocrdings.set(event.childPosition,false);
//            isRecordingList.set(event.groupPosition,isRocrdings);
//            stopPlayRecord();
//        }else {
//            stopPlayRecord();
//            initRecordState();
//            isRocrdings.set(event.childPosition,true);
//            isRecordingList.set(event.groupPosition,isRocrdings);
//            startPlayRecord((String) event.object,event.childPosition,event.groupPosition);
//        }
//        reFreshAdapter(event.groupPosition);
//
//    }

//    /**
//     * 更改播放录音的状态为未播放
//     */
//    private void initRecordState(){
//        for(int i=0;i<isRecordingList.size();i++){
//            List<Boolean> isReocrds=isRecordingList.get(i);
//            for(int j=0;j<isReocrds.size();j++){
//                isReocrds.set(j,false);
//            }
//            isRecordingList.set(i,isReocrds);
//        }
//    }

//    /**
//     * 刷新adapter
//     */
//    private void reFreshAdapter(int groupPosition){
//        lvJsSubmitBrowse.collapseGroup(groupPosition);
//        lvJsSubmitBrowse.expandGroup(groupPosition);
//        mAdapter.reFresh();
//    }

//    /**
//     * 播放网络的录音的提示
//     */
//    private void playRecordByInternetWarning(final JsSBroAdapterToJsBroActiEvent event){
//        AlertDialog.Builder builder=new AlertDialog.Builder(JsSubmitBrowseActivity.this);
//        builder.setMessage(getResources().getString(R.string.js_submit_browse_play_internet_reocrd_dialog))
//                .setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        stopPlayRecord();
//                        initRecordState();
//                        String uri= downHead+event.object.toString();
//                        dialog.dismiss();
//                        JsSubmitNativeMainEvent asyncEvent=new JsSubmitNativeMainEvent();
//                        asyncEvent.object=uri;
//                        asyncEvent.index=RECORD_DOWNLOAD;
//                        EventBus.getDefault().post(asyncEvent);
//                    }
//                })
//                .setCancelable(false)
//                .show();
//
//    }

//    /**
//     * 播放网络语音
//     */
//    private void playRecordByInternet(String url){
//        View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.seekbar,null);
//        SeekBar seekBar= (SeekBar) view.findViewById(R.id.seekbar);
//        interPlayer=new InteMediaPlayer(seekBar);
//        playUrl(url);
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            private int progress;
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                this.progress = progress * interPlayer.mediaPlayer.getDuration()
//                        / seekBar.getMax();
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                interPlayer.mediaPlayer.seekTo(progress);
//            }
//        });
//        new AlertDialog.Builder(JsSubmitBrowseActivity.this)
//                .setView(view)
//                .setPositiveButton(getResources().
//                                getString(R.string.js_submit_browse_play_internet_reocrd_cancel),
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
////                                OthersUtil.getInstance().dialogNotDismissClickOut(dialog);
//                                dialog.dismiss();
//                                interPlayer.stop();
//                            }
//                        })
//                .setCancelable(false)
//                .show();
//    }

    /**
     * 从服务器获得列表的数据
     */
    private void fileBrowseAsync(){
        mChildList.clear();
        mParentList.clear();
        data=null;
        String values="";
//        if (attachmentType!=null&&!attachmentType.isEmpty()) {
//            if (attachmentType.equals("3")) {
//
//            }else {
//                Map<String, String> map = new HashMap<>();
//                map.put("iProjectId", Data.getCustomerProejctNo(getApplicationContext()));
//                map.put("sAppUserNo", Data.getUserNo(getApplicationContext()));
//                map.put("sDataKey", jsWebToApp.sDataKey);
//                values = webServices.GetData("AttachBrowse", map);
//            }
//        } else {
            Map<String, String> map = new HashMap<>();
            map.put("iWorkTeamId", jsWebToApp.iWorkTeamId);
            map.put("sUserNo",jsWebToApp.sUserNo);
            map.put("sDataKey", jsWebToApp.sDataKey);
            values = webServices.GetData("AttachBrowse", map);
//        }

//        values= WsUtil.getJsonDataAsync(getApplicationContext(),
//                "spappBillAttachment",
//                "iProjectId="+Data.getCustomerProejctNo(getApplicationContext())+
//                        ",sAppUserNo="+Data.getUserNo(getApplicationContext())+
//                        ",sBillNo="+billNo+
//                        ",sQueryType=" + "List");
        sbError.append(WsUtil.getErrorFromWs(getApplicationContext(),values));
        if(sbError.toString().isEmpty()){
            data = JSONEntity.GetWsData(values,JsSubmitBrowse.class.getName());
        }
    }

    /**
     * 获得列表的数据
     */
    private void fileBrowseMain(){
//        if(data==null){
//            error=getResources().getString(R.string.connect_server_error);
//            return;
//        }
//        if(data.SSTATUS==null){
//            error=getResources().getString(R.string.connect_server_error);
//            return;
//        }
//        if(!data.SSTATUS.equals("0")){
//            error=data.SMESSAGE;
//            return;
//        }
        if(!sbError.toString().isEmpty()) return;
        sbError.append(WsUtil.getErrorFromWs(getApplicationContext(),data,true));
        if(!sbError.toString().isEmpty()) return;
        List<WsEntity> entityList=data.LISTWSDATA;

//        isRecordingList.clear();
        Map<String,List<JsSubmitBrowse>> map=new HashMap<>();
        for(int i=0;i<entityList.size();i++){
            JsSubmitBrowse browse= (JsSubmitBrowse) entityList.get(i);
            List<JsSubmitBrowse> browses=null;
            //map中含有IHDRIDEN的list
            if(!map.containsKey(browse.IHDRIDEN)){
                browses=new ArrayList<>();
            }else {
                browses=map.get(browse.IHDRIDEN);
            }
            browses.add(browse);
            map.put(browse.IHDRIDEN,browses);
        }
        Set<Map.Entry<String, List<JsSubmitBrowse>>> set= map.entrySet();
        Iterator it=set.iterator();
        while (it.hasNext()){
            Map.Entry<String,List<JsSubmitBrowse>> entry= (Map.Entry<String, List<JsSubmitBrowse>>) it.next();
            List<JsSubmitBrowse> browses=entry.getValue();
            if(browses==null&&browses.size()==0) return;
            JsSubmitBrowseParent  browseParent=new JsSubmitBrowseParent();
            JsSubmitBrowse browse=browses.get(0);
            browseParent.DHDRDATE=browse.DHDRDATE;
            browseParent.DHDRTIME=browse.DHDRTIME;
            browseParent.IHDRIDEN=browse.IHDRIDEN;
            browseParent.SHDRCONTENTS=browse.SHDRCONTENTS;
            browseParent.SHDRTITLE=browse.SHDRTITLE;
            mParentList.add(browseParent);
            List<JsSubmitBrowse> browseList=map.get(browse.IHDRIDEN);
            Collections.sort(browseList, new Comparator<JsSubmitBrowse>() {
                @Override
                public int compare(JsSubmitBrowse lhs, JsSubmitBrowse rhs) {
                    int l=Integer.valueOf(lhs.IDTLIDEN);
                    int r=Integer.valueOf(rhs.IDTLIDEN);
                    if(l>r){
                        return 1;
                    }else if(r==l){
                        return 0;
                    }else {
                        return -1;
                    }
                }
            });
            mChildList.add(browseList);
        }
        Collections.sort(mChildList, new Comparator<List<JsSubmitBrowse>>() {
            @Override
            public int compare(List<JsSubmitBrowse> lhs, List<JsSubmitBrowse> rhs) {
                JsSubmitBrowse lhsJs=lhs.get(0);
                JsSubmitBrowse rhsJs=rhs.get(0);
                int l=Integer.valueOf(lhsJs.IDTLIDEN);
                int r=Integer.valueOf(rhsJs.IDTLIDEN);
                if(l>r){
                    return 1;
                }else if(r==l){
                    return 0;
                }else {
                    return -1;
                }
            }
        });
        Collections.sort(mParentList, new Comparator<JsSubmitBrowseParent>() {
            @Override
            public int compare(JsSubmitBrowseParent lhs,JsSubmitBrowseParent rhs) {
                int l=Integer.valueOf(lhs.IHDRIDEN);
                int r=Integer.valueOf(rhs.IHDRIDEN);
                if(l>r){
                    return 1;
                }else if(r==l){
                    return 0;
                }else {
                    return -1;
                }
            }
        });
//        for(int i=0;i<mChildList.size();i++){
////            List<Boolean> isRecordings=new ArrayList<>();
//            for(int j=0;j<mChildList.get(i).size();j++){
//                isRecordings.add(false);
//            }
//            isRecordingList.add(isRecordings);
//        }
    }

    /**
     * 删除附件的异步操作
     * @param delIden
     */
    private void deleteFileAsync(String delIden){
        data=null;
        Map<String,String> map=new HashMap<>();
        map.put("iWorkTeamId", jsWebToApp.iWorkTeamId);
        map.put("sUserNo",jsWebToApp.sUserNo);
        map.put("iDtlIden",delIden);
        String values=webServices.GetData("AttachDelete",map);
        sbError.append(WsUtil.getErrorFromWs(getApplicationContext(),values));
        if(sbError.toString().isEmpty()){
            data = JSONEntity.GetWsData(values,WsData.class.getName());
        }
//        if(values.isEmpty()){
//            error=getResources().getString(R.string.connect_server_error);
//            return;
//        }else {
//            data = JSONEntity.GetWsData(values, WsData.class.getName());
//        }
    }



    /**
     * 显示删除附件
     */
    private void deleteFileMain(){
//        if(data==null){
//            error=getResources().getString(R.string.connect_server_error);
//            return;
//        }
//        if(data.SSTATUS==null){
//            error=getResources().getString(R.string.connect_server_error);
//            return;
//        }
        if(!sbError.toString().isEmpty()) return;
        sbError.append(WsUtil.getErrorFromWs(getApplicationContext(),data,false));
        if(!sbError.toString().isEmpty()) return;
//        if(!data.SSTATUS.equals("0")){
//            error=data.SMESSAGE;
//            return;
//        }else {
//
//        }
        OthersUtil.ToastMsg(getApplicationContext(),"成功删除！！！");
        WsUtil.toAsync(LIST_TASK,JsSubmitBrowseActivity.class);
    }


//    /**
//     * 停止录音
//     */
//    private void stopPlayRecord(){
//        if(player!=null) {
//            player.stop();
//            player.release();
//            player = null;
//        }
//    }

//    /**
//     * 播放录音
//     * @param path
//     */
//    private void startPlayRecord(String path, final int childPoition, final int groupPosition){
//        player=new MediaPlayer();
//        try {
//            FileInputStream fis = new FileInputStream(path);
//            player.setDataSource(fis.getFD());
//            player.prepare();
//            player.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if(player!=null){
//            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    List<Boolean> isRecordings=isRecordingList.get(groupPosition);
//                    isRecordings.set(childPoition,false);
//                    isRecordingList.set(groupPosition,isRecordings);
//                    reFreshAdapter(groupPosition);
//                }
//            });
//        }
//    }

//    /**
//     * 播放网络的语音
//     * @param videoUrl
//     */
//    private void playUrl(final String videoUrl) {
////        try {
////            player.reset();
////            player.setDataSource(videoUrl);
////            player.prepare();//prepare之后自动播放
//////            player.start();
////        } catch (IllegalArgumentException e) {
////            e.printStackTrace();
////        } catch (IllegalStateException e) {
////            e.printStackTrace();
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                interPlayer.playUrl(videoUrl);
//            }
//        }).start();
//
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_OK) return;
        switch (requestCode){
            case ADD_QUEST_CODE:
                WsUtil.toAsync(LIST_TASK,JsSubmitBrowseActivity.class);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        stopPlayRecord();
//        EventBus.clearCaches();
        EventBus.getDefault().unregister(this);
    }
}
