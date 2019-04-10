package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class RestaurantActivity extends AppCompatActivity {
    ListView list1;
    ListView list2;
    ListView list3;
    Button btnAdd,btnSearch;
    //Spinner spnCategory;
    EditText editPlaceName;
    String[] arrPlace;

    ArrayList<PlaceDTO> items;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            PlaceAdapter adapter = new PlaceAdapter(
                    RestaurantActivity.this,
                    R.layout.place_row,
                    items);
            list1.setAdapter(adapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        list1 = (ListView)findViewById(R.id.listView1);
        //spnCategory=(Spinner)findViewById(R.id.spnCategory);
        //editPlaceName=(EditText)findViewById(R.id.editPlaceName);
/*
        btnSearch=(Button)findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category=arrPlace[spnCategory.getSelectedItemPosition()];
                String placeName=editPlaceName.getText().toString();
                search(category, placeName);
            }
        });

        btnAdd=(Button)findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, PlaceAdd.class);
                startActivity(intent);
            }
        });

        arrPlace=(String[])getResources().getStringArray(R.array.category);
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                arrPlace);
        adapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        spnCategory.setAdapter(adapter);

        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        */
    }

    @Override
    protected void onResume() {
        super.onResume();

        list();
    }

    void list(){
        //네트워크 관련 작업은 백그라운드 스레드에서 처리
        final StringBuilder sb=new StringBuilder(); // final은 지역변수를 상수화 시켜준다. 즉, 한번 실행한 뒤 없어지는 것이 아니라 계속해서 유지 가능하게 해준다.
        Thread th = new Thread(new Runnable() {
            public void run() {
                try {
                    items = new ArrayList<PlaceDTO>();
                    String page = Common.SERVER_URL+"/place_list.php";

                    URL url = new URL(page);
                    // 커넥션 객체 생성
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    // 연결되었으면.
                    if (conn != null) {
                        //타임아웃 시간 설정
                        conn.setConnectTimeout(10000);
                        //캐쉬 사용 여부
                        conn.setUseCaches(false);
                        //url에 접속 성공하면
                        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                            //스트림 생성
                            BufferedReader br=
                                    new BufferedReader(
                                            new InputStreamReader(
                                                    conn.getInputStream(),"utf-8"));
                            while(true){
                                String line=br.readLine(); //한 라인을 읽음
                                if(line == null) break;//더이상 내용이 없으면 종료
                                sb.append(line+"\n");
                            }
                            br.close(); //버퍼 닫기
                        }
                        conn.disconnect();
                    }
// 스트링을 json 객체로 변환
                    JSONObject jsonObj = new JSONObject(sb.toString());

// json.get("변수명")
                    JSONArray jArray = (JSONArray) jsonObj.get("sendData"); // 이 부분 이해 안됨
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject row = jArray.getJSONObject(i);
                        PlaceDTO dto = new PlaceDTO();
                        dto.setIdx(row.getInt("idx"));
                        dto.setAddress(row.getString("address"));
                        dto.setCategory(row.getString("category"));
                        dto.setEnd_time(row.getString("end_time"));
                        dto.setStart_time(row.getString("start_time"));
                        dto.setTel(row.getString("tel"));
                        dto.setPlace_name(row.getString("place_name"));
                        items.add(dto);
                    }
                    //핸들러에게 화면 갱신 요청
                    handler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        th.start();
    }

    void search(final String category, final String placeName){
        //네트워크 관련 작업은 백그라운드 스레드에서 처리
        final StringBuilder sb=new StringBuilder();
        Thread th = new Thread(new Runnable() {
            public void run() {
                try {
                    items = new ArrayList<PlaceDTO>();
                    String page = Common.SERVER_URL+"/place_search.php?category="+category+"&placeName="+placeName;

                    URL url = new URL(page);
                    // 커넥션 객체 생성
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    // 연결되었으면.
                    if (conn != null) {
                        //타임아웃 시간 설정
                        conn.setConnectTimeout(10000);
                        //캐쉬 사용 여부
                        conn.setUseCaches(false);
                        //url에 접속 성공하면
                        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                            //스트림 생성
                            BufferedReader br=
                                    new BufferedReader(
                                            new InputStreamReader(
                                                    conn.getInputStream(),"utf-8"));
                            while(true){
                                String line=br.readLine(); //한 라인을 읽음
                                if(line == null) break;//더이상 내용이 없으면 종료
                                sb.append(line+"\n");
                            }
                            br.close(); //버퍼 닫기
                        }
                        conn.disconnect();
                    }
// 스트링을 json 객체로 변환
                    JSONObject jsonObj = new JSONObject(sb.toString());

// json.get("변수명")
                    JSONArray jArray = (JSONArray) jsonObj.get("sendData");
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject row = jArray.getJSONObject(i);
                        PlaceDTO dto = new PlaceDTO();
                        dto.setIdx(row.getInt("idx"));
                        dto.setAddress(row.getString("address"));
                        dto.setCategory(row.getString("category"));
                        dto.setEnd_time(row.getString("end_time"));
                        dto.setStart_time(row.getString("start_time"));
                        dto.setTel(row.getString("tel"));
                        dto.setPlace_name(row.getString("place_name"));
                        items.add(dto);
                    }
                    //핸들러에게 화면 갱신 요청
                    handler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        th.start();
    }
    class PlaceAdapter extends ArrayAdapter<PlaceDTO> {                 // 여기 class 이해 안됨
        //ArrayList<BookDTO> item;
        public PlaceAdapter(Context context, int textViewResourceId,
                            ArrayList<PlaceDTO> objects) {
            super(context, textViewResourceId, objects);
//this.item= objects;
        }

        @Override
        public View getView(int position, View convertView,                 // getView에 대한 이해 부족
                            ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater li = (LayoutInflater)
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = li.inflate(R.layout.place_row, null);
            }
            final PlaceDTO dto = items.get(position);
            if (dto != null) {
                TextView idx = (TextView) v.findViewById(R.id.idx);
                TextView place_name =(TextView) v.findViewById(R.id.place_name);
                TextView start_time =(TextView) v.findViewById(R.id.start_time);
                TextView end_time =(TextView) v.findViewById(R.id.end_time);
                TextView category =(TextView) v.findViewById(R.id.category);
                TextView address =(TextView) v.findViewById(R.id.address);
                TextView tel =(TextView) v.findViewById(R.id.tel);

                idx.setText(dto.getIdx()+"");
                place_name.setText(dto.getPlace_name());
                start_time.setText(dto.getStart_time());
                end_time.setText(dto.getEnd_time());
                category.setText(dto.getCategory());
                address.setText(dto.getAddress());
                tel.setText(dto.getTel());
            }
            //클릭하면 코드를 넘겨서 받아옴
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(RestaurantActivity.this, DetailActivity.class);
                    intent.putExtra("idx", dto.getIdx()); //putExtra 는 값을 전달하는 역할을 한다. 받는곳은 getExtra 가 된다.
                    startActivity(intent);
                }
            });
            return v;
        }
    }
        /*
        //고기
        listView1=(ListView)findViewById(R.id.listView1);
        l1_InputData1.put("고기","수업이 끝난 오후");
        Data1.add(l1_InputData1);

        l1_InputData2.put("고기","이슬목장");
        Data1.add(l1_InputData2);

        //simpleAdapter 생성
        SimpleAdapter simpleAdapter1 = new SimpleAdapter(this,Data1,android.R.layout.simple_list_item_2,new String[]{"고기"},new int[]{android.R.id.text1});
        listView1.setAdapter(simpleAdapter1);

        //중식
        listView2=(ListView)findViewById(R.id.listView2);
        l2_InputData1.put("중식","흥부반점");
        Data2.add(l2_InputData1);

        l2_InputData2.put("중식","하이린");
        Data2.add(l2_InputData2);

        //simpleAdapter 생성
        SimpleAdapter simpleAdapter2 = new SimpleAdapter(this,Data2,android.R.layout.simple_list_item_2,new String[]{"중식"},new int[]{android.R.id.text1});
        listView2.setAdapter(simpleAdapter2);

        //한식
        listView3=(ListView)findViewById(R.id.listView3);
        l3_InputData1.put("한식","이모네로와");
        Data3.add(l3_InputData1);

        l3_InputData2.put("한식","옹달샘");
        Data3.add(l3_InputData2);

        //simpleAdapter 생성
        SimpleAdapter simpleAdapter3 = new SimpleAdapter(this,Data3,android.R.layout.simple_list_item_2,new String[]{"한식"},new int[]{android.R.id.text1});
        listView3.setAdapter(simpleAdapter3);
        */

        //TabLayout tabLayout=(TabLayout)findViewById(R.id.tabs);
       /* tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos=tab.getPosition();
                changeView(pos);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
    }*/
    public void changeView(int index){
        list1 = (ListView)findViewById(R.id.listView1);
        list2 = (ListView) findViewById(R.id.listView2);
        list3 = (ListView) findViewById(R.id.listView3);

        switch (index){

            case 0 :
                list1.setVisibility(View.VISIBLE);
                list2.setVisibility(View.INVISIBLE);
                list3.setVisibility(View.INVISIBLE);
                break;
            case 1 :
                list2.setVisibility(View.VISIBLE);
                list1.setVisibility(View.INVISIBLE);
                list3.setVisibility(View.INVISIBLE);
                break;
            case 2 :
                list3.setVisibility(View.VISIBLE);
                list2.setVisibility(View.INVISIBLE);
                list1.setVisibility(View.INVISIBLE);
                break;

        }
    }
}