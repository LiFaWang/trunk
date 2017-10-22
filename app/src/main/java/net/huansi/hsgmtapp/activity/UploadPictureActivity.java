package net.huansi.hsgmtapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import net.huansi.hsgmtapp.R;
import net.huansi.hsgmtapp.adapter.UploadPictureAdapter;
import net.huansi.hsgmtapp.event.BackgroundEvent;
import net.huansi.hsgmtapp.event.SerialPortEvent;
import net.huansi.hsgmtapp.model.JsUploadSubmitText;
import net.huansi.hsgmtapp.model.JsWebToApp;
import net.huansi.hsgmtapp.utils.OthersUtil;
import net.huansi.hsgmtapp.utils.PictureAddDialogUtils;
import net.huansi.hsgmtapp.utils.WsUtil;
import net.huansi.hsgmtapp.view.LoadProgressDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 单中年 on 2016/12/12.
 */

public class UploadPictureActivity extends FragmentActivity implements OnClickListener{
    private TextView tvTitle;
    private ImageView imvBack;
    private TextView tvOk;
    private GridView gvUploadPicture;
    private Button btnUploadPictureAdd;//添加图片
    private EditText etUploadPictureTheme,//主题
            etUploadPictureContent;//内容

//    private String dataKey="";
    public JsWebToApp jsWebToApp;
    private LoadProgressDialog dialog;

    private StringBuffer sbError;
    private List<String> mPathList;

    private UploadPictureAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_picture_activity);
        OthersUtil.hideInputFirst(this);
        findView();
        jsWebToApp= (JsWebToApp) getIntent().getSerializableExtra("jsWebToApp");
        dialog=new LoadProgressDialog(this);
        mPathList=new ArrayList<>();
        sbError=new StringBuffer();
        tvTitle.setText("图片上传");
        tvOk.setText("上传");
        tvOk.setVisibility(View.VISIBLE);
        tvOk.setTextColor(Color.parseColor("#FF0000"));
        OthersUtil.registerEvent(this);
        mAdapter=new UploadPictureAdapter(mPathList,this);
        gvUploadPicture.setAdapter(mAdapter);
        btnUploadPictureAdd.setOnClickListener(this);
        imvBack.setOnClickListener(this);
        tvOk.setOnClickListener(this);
        gvUploadPicture.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mPathList.remove(position);
                mAdapter.notifyDataSetChanged();
                return true;
            }
        });
        gvUploadPicture.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(UploadPictureActivity.this, LargerImageSHowActivity.class);
                intent.putExtra("picturePath",mPathList.get(position));
                startActivity(intent);
            }
        });
    }

    private void findView(){
        View title=findViewById(R.id.upLoadPictureTitle);
        tvTitle= (TextView) title.findViewById(R.id.tvTitleText);
        imvBack= (ImageView) title.findViewById(R.id.imvTiteBack);
        tvOk= (TextView) title.findViewById(R.id.tvTitleRight);
        gvUploadPicture= (GridView) findViewById(R.id.gvUploadPicture);
        btnUploadPictureAdd= (Button) findViewById(R.id.btnUploadPictureAdd);
        etUploadPictureTheme= (EditText) findViewById(R.id.etUploadPictureTheme);
        etUploadPictureContent= (EditText) findViewById(R.id.etUploadPictureContent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imvTiteBack:
                finish();
                break;
            //上传
            case R.id.tvTitleRight:
                OthersUtil.showLoadDialog(dialog);
                WsUtil.toAsync(0,UploadPictureActivity.class);
                break;
            case R.id.btnUploadPictureAdd:
                PictureAddDialogUtils.getInstance().showPictureAddDialog(UploadPictureActivity.this);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void background(BackgroundEvent event){
        if(event.aClass!=UploadPictureActivity.class) return;
        SerialPortEvent serialPortEvent =new SerialPortEvent();
        serialPortEvent.aClass=event.aClass;
        serialPortEvent.index=event.index;
        switch (event.index){
            //上传头部
            case 0:
                serialPortEvent.str1=WsUtil.sendTextAsync(jsWebToApp.iWorkTeamId,jsWebToApp.sDataKey,
                        etUploadPictureTheme.getText().toString(),
                        etUploadPictureContent.getText().toString(),jsWebToApp.sUserNo,getApplicationContext());
                break;
            //上传明细
            case 1:
                try {
                    OthersUtil.clearSb(sbError);
                    sbError.append(WsUtil.sendExAsync(mPathList,event.str1,"IMAGE",getApplicationContext()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        EventBus.getDefault().post(serialPortEvent);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void main(SerialPortEvent event){
        if(event.aClass!=UploadPictureActivity.class) return;
        switch (event.index){
            //上传头部
            case 0:
                Serializable serializable=WsUtil.sendTextMain(event.str1,getApplicationContext());
                try{
                    JsUploadSubmitText jsUploadSubmitText= (JsUploadSubmitText) serializable;
                    WsUtil.toAsync(1,UploadPictureActivity.class,jsUploadSubmitText.IIDEN);
                }catch (ClassCastException e) {
                    OthersUtil.dismissLoadDialog(dialog);
                    OthersUtil.clearSb(sbError);
                    sbError.append((String) serializable);
                    OthersUtil.ToastMsg(getApplicationContext(),sbError.toString());
                    OthersUtil.clearSb(sbError);
                }
                break;
            //上传明细
            case 1:
                WsUtil.sendExMain(dialog,getApplicationContext(),sbError,null,
                        null,"保存成功！！！");
                if(sbError.toString().isEmpty()) {
                    setResult(RESULT_OK);
                    finish();
                }
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_OK) return;
        switch (requestCode){
            //相册
            case PictureAddDialogUtils.ALBUM_PICTURE_REQUEST_CODE:
                PictureAddDialogUtils.getInstance().initPictureAfterChooseByAlbum(data,this,mPathList);
                mAdapter.notifyDataSetChanged();
                break;
            //相机
            case PictureAddDialogUtils.CAMERA_PICTURE_REQUEST_CODE:
                PictureAddDialogUtils.getInstance().initPictureAfterChooseByCamera(data,
                        PictureAddDialogUtils.getInstance().getPicturePath(),mPathList);
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
