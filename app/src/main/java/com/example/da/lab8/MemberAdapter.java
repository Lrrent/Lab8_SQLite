package com.example.da.lab8;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Da on 2016/11/17.
 */
public class MemberAdapter extends BaseAdapter{
    private Context context;
    private List<Member> memberList;
    public MemberAdapter(Context context,List<Member> memberList){
        this.context = context;
        this.memberList = memberList;
    }
    @Override
    public int getCount() {
        if (memberList == null)
            return 0;
        return memberList.size();
    }
    @Override
    public Object getItem(int position) {
        if (memberList == null)
            return null;
        return memberList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View View, ViewGroup parent) {
        View convertView;
        ViewHolder viewHolder;
        // 当view为空时才加载布局，并且创建一个ViewHolder,获得布局中的两个控件
        if (View == null){
            // 使用inflate的方法加载布局，context为上下文，应为使用这个adapter的activity
            convertView = LayoutInflater.from(context).inflate(R.layout.memberitem,null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.item_name); //将成员绑定到控件
            viewHolder.birth = (TextView) convertView.findViewById(R.id.item_birth);
            viewHolder.gift = (TextView) convertView.findViewById(R.id.item_gift);
            convertView.setTag(viewHolder); // 使用setTag函数将处理好的viewHolder放入view中
        }
        else {// 如果布局此时不为空，可以直接从converView中取出holder
            convertView = View;
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 在取出相应的控件之后，对它们进行赋值处理
        viewHolder.name.setText(memberList.get(position).getName());
        viewHolder.birth.setText(memberList.get(position).getBirth());
        viewHolder.gift.setText(memberList.get(position).getGift());
        return convertView; // 返回处理之后的view
    }
    private class ViewHolder{
        public TextView name;
        public TextView birth;
        public TextView gift;
    }
}
