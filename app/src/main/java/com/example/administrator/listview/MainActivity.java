package com.example.administrator.listview;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv= (ListView) findViewById(R.id.listView);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,1,0,"ArrayAdapter");
        menu.add(0,2,0,"SimpleCursorAdaper");
        menu.add(0,3,0,"SimpleAdapter");
        menu.add(0,4,0,"BaseAdapter");
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 1:
                arrayAdapter();
                break;
            case 2:
                simpleCursorAdapter();
                break;
            case 3:
                SimpleAdapte();
                break;
            case 4:
                BaseAdapter();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void arrayAdapter() {
        final String[] weeks={"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
        //this是上下文，android.R.layout.simple_list_item_1android自带的布局
        ArrayAdapter mdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,weeks);
        lv.setAdapter(mdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,weeks[position],Toast.LENGTH_LONG).show();
            }
        });
    }
    private void simpleCursorAdapter() {
        //getContentResolver()内容提供器
        final Cursor mCursor=getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
        startManagingCursor(mCursor);
        SimpleCursorAdapter mAdapter=new SimpleCursorAdapter(this,
                android.R.layout.simple_expandable_list_item_1,
                //mCursor数据源
                mCursor,
                new String[]{ContactsContract.Contacts.DISPLAY_NAME},
                new int[]{android.R.id.text1},
                0);
        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,
                        getString(mCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void SimpleAdapte() {
    final List<Map<String,Object>> list=new ArrayList<Map<String ,Object>>();
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("title","icon1");
        map.put("info","the best icon1");
        map.put("img",R.drawable.iconl);
        list.add(map);

        map=new HashMap<String,Object>();
        map.put("title","icon1");
        map.put("info","the best icon2");
        map.put("img",R.drawable.icon2);
        list.add(map);

        map=new HashMap<String,Object>();
        map.put("title","icon3");
        map.put("info","the best icon3");
        map.put("img",R.drawable.icon3);
        list.add(map);
        SimpleAdapter mAdapter=new SimpleAdapter(
                this,
                list,
                R.layout.simpleadapter_demo,
                new String[]{"title","info","img"},
                new int[]{R.id.titleView,R.id.infoView,R.id.imageView}
        );
        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,list.get(position).get("title").toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    static class ViewHolder{
        public ImageView img;
        public TextView title,info;
        public Button btn;
        public LinearLayout layout;
    }
    private void BaseAdapter() {
        class MyBaseAdapter extends BaseAdapter{
            private List<Map<String,Object>> data;
            //必须有上下文
            private Context context;
            private LayoutInflater myLayoutInflater;

         MyBaseAdapter(Context context,List<Map<String,Object>>data){
                super();
                this.data=data;
                this.context=context;
             this.myLayoutInflater=LayoutInflater.from(context);
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
            public View getView(final int position, View convertView, ViewGroup parent) {
                ViewHolder holder=null;
                if(convertView==null){
                   holder = new ViewHolder();
                    convertView=myLayoutInflater.inflate(R.layout.l1,parent,false);
                    holder.title= (TextView) convertView.findViewById(R.id.titleView);

                    convertView.setTag(holder);
                }else{
                    holder= (ViewHolder) convertView.getTag();
                }
                //为button添加点击事件
                holder.img.setImageResource(Integer.parseInt(data.get(position).toString()));
                holder.title.setText(data.get(position).get("title").toString());
                holder.info.setText(data.get(position).get("info").toString());
                if((position+1)%2==1){
                    holder.layout.setBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent));
                }else{
                    holder.layout.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
                }
                //为button添加点击事件
                holder.btn.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context,"按钮点击"+position,Toast.LENGTH_SHORT).show();
                    }
                });
                return convertView;
            }
        }
        final List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
        Map<String,Object> map=new HashMap<String,Object>();

        map.put("title","G1");
        map.put("info","google 1");
        map.put("img",R.drawable.iconl);
        list.add(map);

        map=new HashMap<String,Object>();
        map.put("title","G2");
        map.put("info","google 2");
        map.put("img",R.drawable.icon2);
        list.add(map);

        map=new HashMap<String,Object>();
        map.put("title","G3");
        map.put("info","google 3");
        map.put("img",R.drawable.icon3);
        list.add(map);

        map=new HashMap<String,Object>();
        map.put("title","G4");
        map.put("info","google 4");
        map.put("img",R.drawable.icon4);
        list.add(map);

        map=new HashMap<String,Object>();
        map.put("title","G5");
        map.put("info","google 5");
        map.put("img",R.drawable.icon5);
        list.add(map);

        MyBaseAdapter mybaseadapter=new MyBaseAdapter(this,list);
        lv.setAdapter(mybaseadapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,list.get(position).get("title").toString(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}
