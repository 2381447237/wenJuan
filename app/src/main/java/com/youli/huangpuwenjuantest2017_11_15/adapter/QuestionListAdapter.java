package com.youli.huangpuwenjuantest2017_11_15.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.youli.huangpuwenjuantest2017_11_15.R;
import com.youli.huangpuwenjuantest2017_11_15.bean.NaireListInfo;

import java.util.List;

/**
 * Created by liutao on 2017/7/20.
 */

public class QuestionListAdapter extends BaseAdapter{

    private List<NaireListInfo> data;
    private Context context;

    public QuestionListAdapter(Context context, List<NaireListInfo> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh;

        if(convertView==null){

            vh=new ViewHolder();

            convertView=LayoutInflater.from(context).inflate(R.layout.item_question,parent,false);

            vh.id= (TextView) convertView.findViewById(R.id.item_id);
            vh.title= (TextView) convertView.findViewById(R.id.item_title);
            vh.num= (TextView) convertView.findViewById(R.id.item_question_num);
            vh.time= (TextView) convertView.findViewById(R.id.item_time);

            convertView.setTag(vh);

        }else{

            vh= (ViewHolder) convertView.getTag();

        }

        NaireListInfo qb=data.get(position);

        vh.id.setText((position+1)+"");
        vh.title.setText(qb.getTITLE());
        vh.num.setText(qb.getNO());
        vh.time.setText(qb.getCREATE_TIME());

        return convertView;
    }

    class ViewHolder{

        TextView id,title,num,time;

    }

}
