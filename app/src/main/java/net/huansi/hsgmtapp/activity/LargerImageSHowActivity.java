package net.huansi.hsgmtapp.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.polites.android.GestureImageView;

import net.huansi.hsgmtapp.R;
import net.huansi.hsgmtapp.utils.OthersUtil;
import net.huansi.hsgmtapp.view.LoadProgressDialog;

//import net.huansi.hsapp.widget.imageview.DragImageView;

/**
 * Created by Administrator on 2016/4/26 0026.
 */
public class LargerImageSHowActivity extends Activity {
    private GestureImageView givPopEnlarge;
    private ImageView imvBack;
    private TextView tvTitle;
    private LoadProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_enlarge);
        String path=getIntent().getStringExtra("picturePath");
        dialog=new LoadProgressDialog(this);
        if(path==null||path.isEmpty()){
            OthersUtil.ToastMsg(getApplicationContext(),
                    getResources().getString(R.string.connect_server_error));
            finish();
        }
        givPopEnlarge= (GestureImageView) findViewById(R.id.givPopEnlarge);
        View view=findViewById(R.id.largerImageTitle);
        view.setBackgroundColor(getResources().getColor(R.color.black));
        imvBack= (ImageView) view.findViewById(R.id.imvTiteBack);
        tvTitle= (TextView) view.findViewById(R.id.tvTitleText);
        imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText("查看图片");
        tvTitle.setTextColor(getResources().getColor(android.R.color.white));
        Glide.with(this)
                .load(path)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        OthersUtil.dismissLoadDialog(dialog);
                        givPopEnlarge.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        OthersUtil.showLoadDialog(dialog);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        OthersUtil.dismissLoadDialog(dialog);
                        OthersUtil.ToastMsg(getApplicationContext(),"加载失败...");
                    }
                });
    }
}
