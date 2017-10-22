//package net.huansi.hsgmtapp.adapter;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//
//import net.huansi.hsgmtapp.R;
//
//import java.util.List;
//
//import huansi.net.qianjingapp.adapter.HsBaseAdapter;
//import huansi.net.qianjingapp.utils.ViewHolder;
//
///**
// * Created by Tony on 2017/8/29.
// * 22:06
// */
//
//public class GroupNumAdapter extends HsBaseAdapter<String> {
//    public GroupNumAdapter(List<String> list, Context context) {
//        super(list, context);
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        if (convertView==null){
//            convertView=mInflater.inflate(R.layout.item_group_num_list,parent,false);
//        }
//        TextView tv  = ViewHolder.get(convertView,R.id.text);
//        tv.setTextColor(Color.BLACK);
//        tv.setTextSize(20);
//        tv.setText(mList.get(position));
//        return convertView;
//    }
//}
