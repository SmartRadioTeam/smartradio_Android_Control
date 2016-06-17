package com.qwe7002.reallct.smartradio;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private refulshReceiver receiver = new refulshReceiver();
    private RecyclerView recyclerView;
    private List<song> songList;
    private List<laf> lafList;
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private Toolbar toolbar;
    private Boolean firstlaunch = true;

    class refulshReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            new getlist().execute();
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
    protected void onPause()
    {
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
        setTitle(public_value.settings.get("projectname").getAsString()+"管理中心");
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
                mSwipeRefreshWidget.setRefreshing(true);
                new getlist().execute();
            }
        });
    }

    public void setSongList(boolean today)
    {
        try
        {
            songList = new ArrayList<song>();
            int row = 0;
            for (JsonElement jsonElement : public_value.songtable)
            {
                JsonObject item = jsonElement.getAsJsonObject();
                JsonObject songinfo = public_value.songinfo.get(item.get("songid").getAsString()).getAsJsonObject();
                String time = item.get("time").getAsString().replace("-", "月") + "日 " + item.get("option").getAsString();
                if (today)
                {
                    SimpleDateFormat dateformat1 = new SimpleDateFormat("MM-dd");
                    String a1 = dateformat1.format(new Date());
                    if (a1.equals(item.get("time").getAsString()))
                    {
                        songList.add(new song(row, item.get("id").getAsInt(), songinfo.get("songtitle").getAsString(), item.get("message").getAsString(), item.get("user").getAsString(), item.get("to").getAsString(), time, item.get("songid").getAsString(), Integer.parseInt(item.get("info").getAsString())));
                        row++;
                    }
                } else
                {
                    songList.add(new song(row, item.get("id").getAsInt(), songinfo.get("songtitle").getAsString(), item.get("message").getAsString(), item.get("user").getAsString(), item.get("to").getAsString(), time, item.get("songid").getAsString(), Integer.parseInt(item.get("info").getAsString())));
                    row++;
                }

            }
            if (row == 0)
            {
                if (public_value.settings.get("permission").getAsString().equals("0"))
                {
                    Snackbar.make(mSwipeRefreshWidget, "当前策略禁止点歌！", Snackbar.LENGTH_SHORT).show();
                } else
                {
                    Snackbar.make(mSwipeRefreshWidget, "当前选项暂无项目！", Snackbar.LENGTH_SHORT).show();
                }

            }
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(songList, MainActivity.this, toolbar);
            recyclerView.setAdapter(adapter);
        } catch (Exception e)
        {
            Log.i("exception", e.toString());
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        switch (item.getItemId())
        {
            case R.id.Today:
                public_value.navistate = 0;
                setSongList(true);
                break;
            case R.id.song:
                public_value.navistate = 1;
                setSongList(false);
                break;
            case R.id.nav_laf:
                public_value.navistate = 2;
                setlostandfound();
                break;
            case R.id.action_settings:
                Intent intent = new Intent(this, settingActivity.class);
                startActivity(intent);
                break;
            case R.id.logout:
                SharedPreferences.Editor editors = getSharedPreferences("user", Context.MODE_PRIVATE).edit();
                editors.clear();
                editors.apply();
                Intent intents = new Intent(this, LoginActivity.class);
                startActivity(intents);
                finish();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setlostandfound()
    {
        if (public_value.laftable.size() == 0)
        {
            Snackbar.make(mSwipeRefreshWidget, "当前选项暂无项目！", Snackbar.LENGTH_SHORT).show();
        }
        lafList = new ArrayList<laf>();
        for (JsonElement JE : public_value.laftable)
        {
            JsonObject item = JE.getAsJsonObject();
            lafList.add(new laf(item.get("id").getAsInt(), item.get("user").getAsString(), item.get("message").getAsString(), item.get("tel").getAsString()));
        }
        RecyclerViewAdapter_Laf adapter = new RecyclerViewAdapter_Laf(lafList, MainActivity.this, toolbar);
        recyclerView.setAdapter(adapter);
    }

    public class getlist extends AsyncTask<Void, Integer, String>
    {
        ProgressDialog mpDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute()
        {
            if (firstlaunch)
            {
                firstlaunch = false;
                mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mpDialog.setTitle("正在连接服务器...");
                mpDialog.setMessage("正在获取数据，请稍后...");
                mpDialog.setIndeterminate(false);
                mpDialog.setCancelable(false);
                mpDialog.show();
            }
        }

        @Override
        protected String doInBackground(Void... params)
        {
            try
            {
                return APIs.getlistjson();

            } catch (Exception e)
            {
                return null;
            }
        }


        @Override
        protected void onPostExecute(String result)
        {
            mSwipeRefreshWidget.setRefreshing(false);
            mpDialog.cancel();
            if (result != null)
            {
                Log.i("result", result);
                try
                {
                    JsonParser parser = new JsonParser();
                    JsonObject object = (JsonObject) parser.parse(result);
                    public_value.songtable = object.getAsJsonArray("songtable");
                    public_value.songinfo = object.getAsJsonObject("songinfo");
                    public_value.laftable = object.getAsJsonArray("lostandfound");
                } catch (Exception e)
                {
                    Log.i("error", e.toString());
                }
                switch (public_value.navistate)
                {
                    case 0:
                        setSongList(true);
                        break;
                    case 1:
                        setSongList(false);
                        break;
                    case 2:
                        setlostandfound();
                        break;
                }

            } else
            {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog
                        .setTitle("无法连接到网络！")
                        .setMessage("是否进行重试？")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                    }
                                }).setCancelable(false).create().show();
            }

        }
    }
}
