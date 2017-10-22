package net.huansi.hsgmtapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.huansi.hsgmtapp.R;
import net.huansi.hsgmtapp.bean.AssignTopFilterBaseDataBean;
import net.huansi.hsgmtapp.utils.ViewHolder;

import java.util.List;

import huansi.net.qianjingapp.adapter.HsBaseAdapter;

/**
 * Created by Administrator on 2017/7/11 0011.
 */

public class BatchNumAdapter extends HsBaseAdapter<AssignTopFilterBaseDataBean> {
    public BatchNumAdapter(List<AssignTopFilterBaseDataBean> list, Context context) {
        super(list, context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view = mInflater.inflate(R.layout.item_ordernum_list,viewGroup,false);
        }
        TextView tv  = ViewHolder.get(view,R.id.text);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(12);
        tv.setText(mList.get(i).SPRODUCTNO);
        return view;
    }
}
