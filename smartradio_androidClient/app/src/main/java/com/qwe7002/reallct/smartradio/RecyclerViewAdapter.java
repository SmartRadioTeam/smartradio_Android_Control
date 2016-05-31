package com.qwe7002.reallct.smartradio;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.songtabletViewHolder>
    {
        private ActionMode actionMode;
        ProgressDialog mpDialog;
        private List<song> songtable;
        private Context context;
        private List Selectlist;
        private List<View> Selectview;
        private Toolbar toolbar;
        public RecyclerViewAdapter(List<song> songtable, Context context,Toolbar toolbar)
            {
                this.songtable = songtable;
                this.context = context;
                this.toolbar = toolbar;
            }
        //自定义ViewHolder类
        static class songtabletViewHolder extends RecyclerView.ViewHolder
            {

                CardView cardView;
                TextView card_title;
                TextView card_message;
                Button checkbutton;

                public songtabletViewHolder(final View itemView)
                    {
                        super(itemView);
                        cardView = (CardView) itemView.findViewById(R.id.card_view);
                        card_title = (TextView) itemView.findViewById(R.id.card_title);
                        card_message = (TextView) itemView.findViewById(R.id.card_message);
                        checkbutton = (Button) itemView.findViewById(R.id.Checkbotton);
                    }


            }
        private ActionMode.Callback mCallback = new ActionMode.Callback() {

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                int count = 0;
                for (View i:Selectview)
                {

                    setbuttonstate((Button)i,songtable.get((int)Selectlist.get(count)).gettaskstate());
                    count++;
                }
                Selectview=null;
                Selectlist=null;
                actionMode=null;

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.muiltselect, menu);
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                final MenuItem itemfinal = item;
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog
                        .setTitle("是否打开网易云音乐？")
                        .setMessage("")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        switch(itemfinal.getItemId())
                                        {
                                            case R.id.muilt_normal:
                                                break;
                                            case R.id.muilt_played:
                                                break;
                                            case R.id.muilt_unplay:
                                                break;
                                        }
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
                mode.finish();
                return true;
            }
        };

        @Override
        public RecyclerViewAdapter.songtabletViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
            {
                View v = LayoutInflater.from(context).inflate(R.layout.cardview_songtable, viewGroup, false);
                songtabletViewHolder nvh = new songtabletViewHolder(v);
                return nvh;
            }

        @Override
        public void onBindViewHolder(RecyclerViewAdapter.songtabletViewHolder personViewHolder, int i)
            {
                final int j = i;
                personViewHolder.card_title.setText(songtable.get(i).getTitle());
                personViewHolder.card_message.setText(songtable.get(i).getmessage());
                setbuttonstate(personViewHolder.checkbutton, songtable.get(i).gettaskstate());
                personViewHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v)
                    {
                        if(actionMode!=null){
                            return false;
                        }
                        toolbar.startActionMode(mCallback);
                        setselect(v,j);
                        return true;
                    }
                });
                //notifyItemInserted(i);
                personViewHolder.cardView.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                            {
                                if(actionMode!=null){
                                    setselect(v,j);
                                    return;
                                }
                                Uri uri;
                                if (isPkgInstalled("com.netease.cloudmusic"))
                                    {
                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                                        alertDialog
                                                .setTitle("是否打开网易云音乐？")
                                                .setMessage("播放歌曲：" + songtable.get(j).getTitle())
                                                .setPositiveButton("确定",
                                                        new DialogInterface.OnClickListener()
                                                            {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which)
                                                                    {
                                                                        Uri uri;
                                                                        pushnotify(context, "Smartradio管理系统正在运行", "点击此处返回");
                                                                        uri = Uri.parse("orpheus://song/" + songtable.get(j).getsongid());
                                                                        Intent it = new Intent(Intent.ACTION_VIEW, uri);
                                                                        context.startActivity(it);
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
                                    } else
                                    {
                                        Toast.makeText(context, "未找到网易云音乐，请先下载安装！", Toast.LENGTH_SHORT).show();
                                        uri = Uri.parse("market://details?id=com.netease.cloudmusic");
                                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                                        try
                                            {
                                                context.startActivity(goToMarket);
                                            } catch (ActivityNotFoundException e)
                                            {
                                                uri = Uri.parse("http://music.163.com/m/");
                                                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                                                context.startActivity(it);
                                            }

                                    }

                            }
                    });
                personViewHolder.checkbutton.setOnClickListener(new View.OnClickListener()
                    {
                        final CharSequence[] charSequences = {"已播放", "无法播放", "未播放"};
                        @Override
                        public void onClick(View v)
                            {
                                final Button views = (Button) v;
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("更改状态为：")
                                        .setItems(charSequences, new DialogInterface.OnClickListener()
                                            {

                                                @Override
                                                public void onClick(DialogInterface dialog, int which)
                                                    {
                                                        //showProgress(true);
                                                        switch (which)
                                                            {
                                                                case 0:
                                                                    break;
                                                                case 1:
                                                                    break;
                                                                case 2:
                                                                    break;
                                                            }
                                                        setbuttonstate(views, which);
                                                        Snackbar.make(views, "条目已被设为" + views.getText(), Snackbar.LENGTH_SHORT)
                                                                .show();
                                                    }
                                            }).show();
                            }
                    });

            }

        private void setselect(View v,int i) {
            if(!Selectlist.contains(i)){
                Selectview.add(v);
                Selectlist.add(i);
                setbuttonstate((Button)v, 3);
            }else{
                setbuttonstate((Button)v, songtable.get(i).gettaskstate());
                Selectview.remove(v);
                Selectlist.remove((Object)i);
            }

        }

        void setbuttonstate(Button v, int state)
            {
                boolean systemflag=false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                   systemflag=true;
                }
                switch (state)
                    {
                        case 0:
                            v.setText("已播放");
                            if(systemflag)
                            {
                                v.setBackgroundResource(R.drawable.button_played);
                            }else{
                                v.setBackgroundResource(R.color.played_button);
                            }
                            break;
                        case 1:
                            v.setText("无法播放");
                            if(systemflag)
                            {
                                v.setBackgroundResource(R.drawable.button_unplay);
                            }else{
                                v.setBackgroundResource(R.color.no_button);
                            }
                            break;
                        case 2:
                            v.setText("未播放");
                            if(systemflag)
                            {
                                v.setBackgroundResource(R.drawable.button_normal);
                            }else{

                                v.setBackgroundResource(R.color.normal_button);
                            }
                            break;
                        case 3:
                            v.setText("已选中");
                            if(systemflag)
                            {
                                v.setBackgroundResource(R.drawable.button_select);
                            }else{

                                v.setBackgroundResource(R.color.select_button);
                            }
                            break;
                    }

            }

        private void showProgress(boolean switchs)
            {
                if (switchs)
                    {
                        mpDialog = new ProgressDialog(context);
                        mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        mpDialog.setTitle("正在连接服务器...");
                        mpDialog.setMessage("提交请求中，请稍候...");
                        mpDialog.setIndeterminate(false);
                        mpDialog.setCancelable(false);
                        mpDialog.show();
                    } else
                    {
                        mpDialog.hide();
                    }
            }

        void pushnotify(Context context, String title, String text)
            {
                Notification.Builder builder = new Notification.Builder(context);
                builder.setSmallIcon(R.mipmap.ic_launcher);
                builder.setContentTitle(title);
                builder.setContentText(text);
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setClass(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                PendingIntent contextIntent = PendingIntent.getActivity(context, 0, intent, 0);
                builder.setPriority(Notification.PRIORITY_HIGH);
                builder.setContentIntent(contextIntent);
                Notification notification = builder.build();
                notification.flags = Notification.FLAG_NO_CLEAR;
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(1, notification);
            }

        private boolean isPkgInstalled(String pkgName)
            {
                PackageInfo packageInfo = null;
                try
                    {
                        packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
                    } catch (PackageManager.NameNotFoundException e)
                    {
                        packageInfo = null;
                        e.printStackTrace();
                    }
                if (packageInfo == null)
                    {
                        return false;
                    } else
                    {
                        return true;
                    }
            }

        @Override
        public int getItemCount()
            {
                return songtable.size();
            }
    }