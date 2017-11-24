package com.youli.huangpuwenjuantest2017_11_15;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youli.huangpuwenjuantest2017_11_15.adapter.QuestionListAdapter;
import com.youli.huangpuwenjuantest2017_11_15.bean.NaireListInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends Activity {

    private ListView lv;
    private QuestionListAdapter adapter;
    private List<NaireListInfo> data=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv= (ListView) findViewById(R.id.question_lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MainActivity.this,NaireDetailActivity.class);
                intent.putExtra("wenjuan",data.get(position));
                startActivity(intent);
            }
        });
        getLocalData();
    }

    private void getLocalData(){

        try {
            InputStream is=getAssets().open("myjson2.txt");
            final String text=readTextFromSDcard(is);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Gson gson=new Gson();

                    data=gson.fromJson(text,new TypeToken<LinkedList<NaireListInfo>>(){}.getType());
                    adapter=new QuestionListAdapter(MainActivity.this,data);
                    lv.setAdapter(adapter);

                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private  String  readTextFromSDcard(InputStream is) throws IOException {

        InputStreamReader reader=new InputStreamReader(is,"GBK");
        BufferedReader bufferedReader=new BufferedReader(reader);
        StringBuffer buffer=new StringBuffer("");
        String str;
        while ((str=bufferedReader.readLine())!=null){
              buffer.append(str);
              buffer.append("\n");
        }
          return buffer.toString();
    }


}
