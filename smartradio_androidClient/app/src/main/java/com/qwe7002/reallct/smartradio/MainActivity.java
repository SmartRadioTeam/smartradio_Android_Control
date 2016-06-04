package com.qwe7002.reallct.smartradio;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    refulshReceiver receiver = new refulshReceiver();
    private RecyclerView recyclerView;
    private List<song> songList;
    private RecyclerViewAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshWidget;
    Toolbar toolbar;
    class refulshReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            adapter = new RecyclerViewAdapter(songList, MainActivity.this, toolbar);
            recyclerView.setAdapter(adapter);
        }
    }
    @Override
    protected void onStop()
    {
        NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(1);
        super.onStop();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction("refulsh.activity");
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
        NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(1);
    }
    @Override
    protected void onPause(){
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //设定navhead的账户系统信息
        View header = navigationView.inflateHeaderView(R.layout.nav_header_main);
        TextView tv = (TextView) header.findViewById(R.id.navusername);
        tv.setText(public_value.username);
        //设定正文
        mSwipeRefreshWidget = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_widget);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mSwipeRefreshWidget.setColorSchemeResources(R.color.colorPrimary);
        new getlist().execute();
        mSwipeRefreshWidget.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {

                new getlist().execute();
            }
        });
    }

    public void setSongList(JsonArray array, boolean today)
    {
        songList = new ArrayList<song>();
        for (JsonElement jsonElement : array)
        {
            JsonObject item = jsonElement.getAsJsonObject();
            JsonObject songinfo = public_value.songinfo.get(item.get("songid").getAsString()).getAsJsonObject();
            String time = item.get("time").getAsString().replace("-", "月") + "日 " + item.get("option").getAsString();
            if (today)
            {
                SimpleDateFormat dateformat1 = new SimpleDateFormat("MM-dd");
                String a1 = dateformat1.format(new Date());
                if (a1 == item.get("time").getAsString())
                {

                    songList.add(new song(songinfo.get("songtitle").getAsString(), item.get("message").getAsString(), item.get("user").getAsString(), item.get("to").getAsString(), time, item.get("songid").getAsString(), Integer.parseInt(item.get("info").getAsString())));
                }
            } else
            {
                songList.add(new song(songinfo.get("songtitle").getAsString(), item.get("message").getAsString(), item.get("user").getAsString(), item.get("to").getAsString(), time, item.get("songid").getAsString(), Integer.parseInt(item.get("info").getAsString())));
            }


        }
        adapter = new RecyclerViewAdapter(songList, MainActivity.this, toolbar);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.action_settings)
        {
            Intent intent = new Intent(this, settingActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        switch (item.getItemId())
        {
            case R.id.Today:
                public_value.navistate=0;
                setSongList(public_value.songtable, true);
                break;
            case R.id.song:
                public_value.navistate=1;
                setSongList(public_value.songtable, false);
                break;
            case R.id.nav_laf:
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class getlist extends AsyncTask<Void, Integer, Boolean>
    {
        ProgressDialog mpDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute()
        {
            if (public_value.remotecontext == null)
            {

                mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mpDialog.setTitle("正在连接服务器...");
                mpDialog.setMessage("登录中，请稍后...");
                mpDialog.setIndeterminate(false);
                mpDialog.setCancelable(false);
                mpDialog.show();
            } else
            {
                mSwipeRefreshWidget.setRefreshing(true);
            }
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            try
            {
                public_value.remotecontext = APIs.getlistjson();
                return true;
            } catch (Exception e)
            {
                return false;
            }
        }


        @Override
        protected void onPostExecute(Boolean result)
        {
            if (result)
            {
                mSwipeRefreshWidget.setRefreshing(false);
                mpDialog.cancel();
                JsonParser parser = new JsonParser();
                JsonObject object = (JsonObject) parser.parse(public_value.remotecontext);
                public_value.songtable = object.getAsJsonArray("songtable");
                public_value.songinfo = object.getAsJsonObject("songinfo");
                public_value.laftable = object.getAsJsonArray("lostandfound");
                switch(public_value.navistate){
                    case 0:
                        setSongList(public_value.songtable, true);
                        break;
                    case 1:
                        setSongList(public_value.songtable, false);
                        break;

                }

            }

        }
    }
}
