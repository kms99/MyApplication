package com.example.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class cafe_kara_pcActivity extends AppCompatActivity {

    private ArrayList<HashMap<String,String>> Data = new ArrayList<HashMap<String, String>>();
    private HashMap<String,String> InputData1 = new HashMap<>();
    private HashMap<String,String> InputData2 = new HashMap<>();
    private ListView listView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu1, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //고기
        listView=(ListView)findViewById(R.id.listView);
        InputData1.put("cafe","리프");
        Data.add(InputData1);

        InputData2.put("cafe","팬도로시시");        Data.add(InputData2);

        //simpleAdapter 생성
        SimpleAdapter simpleAdapter1 = new SimpleAdapter(this,Data,android.R.layout.simple_list_item_2,new String[]{"cafe"},new int[]{android.R.id.text1});
        listView.setAdapter(simpleAdapter1);
//
//        ListView listView1 = (ListView) findViewById(R.id.listView);

    }
}
