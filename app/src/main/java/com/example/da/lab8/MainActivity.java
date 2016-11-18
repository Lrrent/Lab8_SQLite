package com.example.da.lab8;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button addEntry;
    private ListView listView;
    private MyDBHelper myDBHelper = new MyDBHelper(MainActivity.this);
    private List<Member> memberList = new ArrayList<Member>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findAllView();
        addEntry.setOnClickListener(new add_listener());
        listView.setOnItemClickListener(new listView_listen());
        listView.setOnItemLongClickListener(new listView_longListener());
        updateListView();
    }
    private void findAllView(){
        addEntry = (Button) findViewById(R.id.add);
        listView = (ListView) findViewById(R.id.message);
    }
    private void setData(List<Member> mData){  //从数据库中获得数据,并保存在一个member List中
        Cursor cursor = myDBHelper.query();  // 调用helper的查询方法获得数据库的游标对象
        while (cursor.moveToNext()){
            Member member = new Member();
            member.setName(cursor.getString(cursor.getColumnIndex("name")));
            member.setBirth(cursor.getString(cursor.getColumnIndex("birth")));
            member.setGift(cursor.getString(cursor.getColumnIndex("gift")));
            member.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            //Log.i(member.getId()+"", "setData: ");
            mData.add(member);
        }
    }
    //加载数据库中的数据显示在ListView中,使用自定义的Adapter
    public void updateListView(){
        memberList.clear(); //在更新列表之前,一定要先把列表中的数据清除掉,否则之前留下的还会继续显示
        setData(memberList);   // 取得数据
        final MemberAdapter adapter = new MemberAdapter(getApplicationContext(),memberList);
        listView.setAdapter(adapter);
    }
    //adapter 短按监听
    private class listView_listen implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            myDialog(memberList.get(position)).show();
        }
    }
    // 列表向长按监听:显示删除对话框
    private class listView_longListener implements AdapterView.OnItemLongClickListener{
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            // member = memberList.get(position);
            //myDBHelper.delete(member);
            //updateListView();
            deleteDialog(memberList.get(position)).show();
            Log.i("delete success", "onItemLongClick: ");
            return true;
        }
    }
    //返回自定义对话框,用于当点击列表项时
    private AlertDialog myDialog(final Member member){
        final AlertDialog alertDialog;
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View view = layoutInflater.inflate(R.layout.alertdialog,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(view);
        alertDialog = builder.create();
        //注意这里findViewById的时候要使用view.,否则默认MainActivity
        final Button quit = (Button) view.findViewById(R.id.quitAlert);
        final TextView dialogName = (TextView) view.findViewById(R.id.dialog_name);
        final TextView dialogBirth = (TextView) view.findViewById(R.id.dialog_birth);
        final TextView dialogGift = (TextView) view.findViewById(R.id.dialog_gift);
        final TextView dialogPhone = (TextView) view.findViewById(R.id.dialog_phone);
        final Button save = (Button) view.findViewById(R.id.saveAert);
        dialogName.setText(member.getName());
        dialogBirth.setText(member.getBirth());
        dialogGift.setText(member.getGift());
        //放弃修改按钮的监听,返回到主界面,
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        //保存修改按钮的监听
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                member.setBirth(dialogBirth.getText().toString());
                member.setGift(dialogGift.getText().toString());
                myDBHelper.update(member);  //将更新的数据存到数据库中
                updateListView();    //更新列表
                alertDialog.cancel();
                Log.i("save", "onClick: ");
            }
        });
        //拿到电话号码
        dialogPhone.setText(getPhone(member));
        return alertDialog;
    }
    //获取所选人的电话号码
    private String getPhone(Member member){
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
        String num = "";
        while (cursor.moveToNext()){
            int nameIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            String name = cursor.getString(nameIndex);  //获得联系人名字
            int isHas = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
            if (name.equals(member.getName()) && isHas != 0){ //当联系人为选中的列表项并且通讯录中有号码时
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor cursor_num = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = "+contactId,null,null);
                while (cursor_num.moveToNext()){
                    num += cursor_num.getString(cursor_num.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))+" ";  //获取该联系人的多个号码
                    Log.i(num, "获取的号码: ");
                }
                cursor_num.close();
            }
        }
        cursor.close();
        return num;
    }
    //删除对话框,自定义,用于长按列表项时使用
    private AlertDialog deleteDialog(final Member member){
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View view = layoutInflater.inflate(R.layout.deletedialog,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(view);
        final AlertDialog deleteDialog = builder.create();
        final Button noDelete = (Button) view.findViewById(R.id.no_delete);
        final Button delete = (Button) view.findViewById(R.id.delete);
        noDelete.setOnClickListener(new View.OnClickListener() { //点击对话框的否,取消对话框
            @Override
            public void onClick(View v) {
                deleteDialog.cancel();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() { //点击是,删除记录并且更新列表
            @Override
            public void onClick(View v) {
                myDBHelper.delete(member);
                updateListView();
                deleteDialog.cancel();
            }
        });
        return deleteDialog;
    }
    //addEntry按钮的监控,跳转到增加界面
    private class add_listener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this,AddNew.class);
            int requestCode = 1;
            startActivityForResult(intent,requestCode);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // 插入新数据时获得数据并且进行插入和更新
        Log.i(requestCode+"Request", "onActivityResult: ");
        Log.i(resultCode+"result", "onActivityResult: ");
        if (requestCode == 1 && resultCode == 1){
            Log.i("ok", "onActivityResult: ");
            String name = data.getStringExtra("name");
            String birth = data.getStringExtra("birth");
            String gift = data.getStringExtra("gift");
            myDBHelper.insert(new Member(name,birth,gift));
        }
        updateListView();  //更新列表
    }
}
