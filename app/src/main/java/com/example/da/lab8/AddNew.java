package com.example.da.lab8;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNew extends AppCompatActivity {
    private EditText inputName;
    private EditText inputBirth;
    private EditText inputGift;
    private Button addButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
        findAllView();
        addButton.setOnClickListener(new addNew_listener());
    }
    private void findAllView(){
        inputName = (EditText) findViewById(R.id.input_name);
        inputBirth = (EditText) findViewById(R.id.input_birth);
        inputGift = (EditText) findViewById(R.id.input_gift);
        addButton = (Button) findViewById(R.id.add_bt);
    }
    //获得所有的输入数据,包括名字,生日,礼物,然后返回一个数组
    private String[] getInput(){
        String[] input = new String[3];
        input[0] = inputName.getText().toString();
        input[1] = inputBirth.getText().toString();
        input[2] = inputGift.getText().toString();
        return input;
    }
    //add的监听事件
    private class addNew_listener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            String [] input = getInput();  //获得输入数据
            MyDBHelper myDBHelper = new MyDBHelper(AddNew.this);
            boolean repeat = myDBHelper.queryRepeat(new Member(input[0],input[1],input[2]));  //判断名字是否重复
            //Log.i(t+"是否重复", "onActivityResult: ");
            if (repeat){
                Toast.makeText(getApplicationContext(),"名字重复啦,请核查",Toast.LENGTH_LONG).show();
            }
            else if(input[0].equals("")){ //名字已经存在的情况处理,条件还没有,需要和SQLite数据库进行连接
                Toast.makeText(getApplicationContext(),"名字为空,请完善",Toast.LENGTH_LONG).show();
            }
            else{ //允许插入新的记录,这里使用由回传数据的跳转,将数据传回MainActivity后再进行插入和更新
                Intent intent = new Intent();
                intent.putExtra("name",input[0]);
                intent.putExtra("birth",input[1]);
                intent.putExtra("gift",input[2]);
                AddNew.this.setResult(1,intent);
                AddNew.this.finish();
            }
        }
    }
}
