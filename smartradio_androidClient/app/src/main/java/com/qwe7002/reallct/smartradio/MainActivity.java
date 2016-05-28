package com.qwe7002.reallct.smartradio;

import android.app.ProgressDialog;
import android.os.Bundle;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    ProgressDialog mpDialog;
    private RecyclerView recyclerView;
    private List<song> songList;
    private RecyclerViewAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshWidget;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Gson gson = new Gson();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        mSwipeRefreshWidget = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_widget);
        recyclerView= (RecyclerView) findViewById(R.id.my_recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mSwipeRefreshWidget.setRefreshing(true);
                    // 此处在现实项目中，请换成网络请求数据代码，sendRequest .....
                    //handler.sendEmptyMessageDelayed(0, 3000);
                    mSwipeRefreshWidget.setRefreshing(false);
                }
            }
        });
    }
    private void showProgress(boolean switchs)
    {
        if(switchs)
        {
            mpDialog = new ProgressDialog(this);
            mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mpDialog.setTitle("正在连接服务器...");
            mpDialog.setMessage("提交请求中，請稍候...");
            mpDialog.setIndeterminate(false);
            mpDialog.setCancelable(false);
            mpDialog.show();
        }else
        {
         mpDialog.hide();
        }
    }
    private void initSongData() {
        songList =new ArrayList<>();
        songList.add(new song("追梦赤子心 - GALA","「真的超开心苏运营被提名金曲奖最佳女歌手，太棒了！不过竞争激励精彩。希望拿奖吧。希望明天回校听得到（实习狗）点歌于火车上」","27808044"));
        songList.add(new song("追梦赤子心 - GALA","「真的超开心苏运营被提名金曲奖最佳女歌手，太棒了！不过竞争激励精彩。希望拿奖吧。希望明天回校听得到（实习狗）点歌于火车上」","664962"));
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
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        switch(item.getItemId()){
            case R.id.Today:
                recyclerView= (RecyclerView) findViewById(R.id.my_recycler_view);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                initSongData();
                adapter=new RecyclerViewAdapter(songList,MainActivity.this);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
