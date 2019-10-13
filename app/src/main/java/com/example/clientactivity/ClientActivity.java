package com.example.clientactivity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.view.GravityCompat;
import android.widget.Toast;
import android.support.annotation.NonNull;
import android.content.Intent;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientActivity extends AppCompatActivity {

    private final static String TAG = "ClientActivity";
    private DrawerLayout drawerLayout;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.arg1 == 1){
                String tmp = txv.getText().toString() + msg.obj.toString() + "\n" ;
                txv.setText(tmp);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        spinner_table();
    }

    TextView txv;
    EditText edt_ip;
    String tmp_1,tmp_2;
    final String[] lunch = {"雞腿飯", "魯肉飯", "排骨飯", "水餃", "陽春麵"};

    public void initView() {
        txv = (TextView) findViewById(R.id.client_activity_txv);
        edt_ip = (EditText) findViewById(R.id.client_activity_edt_ip);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> lunchList = ArrayAdapter.createFromResource(ClientActivity.this,
                R.array.food,
                android.R.layout.select_dialog_item);
        spinner.setAdapter(lunchList);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tmp_1 = lunch[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        NavigationView navigation_View = findViewById(R.id.nav_view);
        navigation_View.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.menu ) {

                Intent intent = new Intent();
                intent.setClass(ClientActivity.this , Main2Activity.class);
                startActivity(intent);
                Toast.makeText(ClientActivity.this, "菜單", Toast.LENGTH_SHORT).show();
                return true;
            }
                else if(id == R.id.title ){
                return true;
            }

                drawerLayout.closeDrawer(GravityCompat.START);

                return false;
            }

        });


    }
        public void spinner_table(){

            final String[] Table = {"1", "2", "3"};
            Spinner spinner_2= (Spinner)findViewById(R.id.spinner2);
            ArrayAdapter<CharSequence> tableList = ArrayAdapter.createFromResource(ClientActivity.this,
                    R.array.table,
                    android.R.layout.select_dialog_item);
            spinner_2.setAdapter(tableList);

            spinner_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    tmp_2 = Table[position];
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        }


    public void click_send(View v) {
        if (os != null) {
                String tmp = "第" + tmp_2 +"桌 : " +  tmp_1 ;
                if (!tmp.equals("")) {
                    send(tmp);
            }
        }
    }

    public void connect(View v){
        String ip = edt_ip.getText().toString();
        new ClientThread(ip).start();
    }

    InputStream is;
    OutputStream os;
    public class ClientThread extends Thread{

        final int socketServerPORT = 8080;
        Socket socket;
        String ip;
        public ClientThread(String ip){
            this.ip = ip;
        }
        @Override
        public void run() {
            super.run();
            try {
                socket = new Socket(ip,socketServerPORT);
                is = socket.getInputStream();
                os = socket.getOutputStream();

                new ReadThread(socket).start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class ReadThread extends Thread{

        public Socket socket;
        public ReadThread(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            while (socket.isConnected()) {
                byte[] buffer = new byte[128];
                int count = 0;
                if (socket != null) {
                    try {
                        String tmp;
                        count = is.read(buffer);
                        tmp = new String(buffer, 0, count, "utf-8");
                        Message message = new Message();

                        message.arg1 = 1;
                        message.obj = tmp;

                        handler.sendMessage(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d(TAG, "ReadThread IOE");
                        break;
                    }
                }
            }
        }
    }
    public void send(final String tmp){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (os!=null) {
                    try {
                        os.write(tmp.getBytes("utf-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d(TAG,"send IOE");
                    }
                }
            }
        }).start();
    }




}