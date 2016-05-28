package com.qwe7002.reallct.smartradio;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.songtabletViewHolder>
{

    private List<song> songtable;
    private Context context;

    public RecyclerViewAdapter(List<song> songtable, Context context)
    {
        this.songtable = songtable;
        this.context = context;
    }


    //自定义ViewHolder类
    static class songtabletViewHolder extends RecyclerView.ViewHolder
    {

        CardView cardView;
        TextView card_title;
        TextView card_message;

        public songtabletViewHolder(final View itemView)
        {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            card_title = (TextView) itemView.findViewById(R.id.card_title);
            card_message = (TextView) itemView.findViewById(R.id.card_message);
        }


    }

    @Override
    public RecyclerViewAdapter.songtabletViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(context).inflate(R.layout.cardview, viewGroup, false);
        songtabletViewHolder nvh = new songtabletViewHolder(v);
        return nvh;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.songtabletViewHolder personViewHolder, int i)
    {
        final int j = i;

        personViewHolder.card_title.setText(songtable.get(i).getTitle());
        personViewHolder.card_message.setText(songtable.get(i).getmessage());
        personViewHolder.cardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
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
                                        if (isPkgInstalled("com.netease.cloudmusic"))
                                        {
                                            pushnotify(context,"Smartradio管理系统正在运行","点击此处返回");
                                            uri = Uri.parse("orpheus://song/" + songtable.get(j).getsongid());
                                        }
                                        else
                                        {
                                            Toast.makeText(context, "未找到网易云音乐，请先下载安装！", Toast.LENGTH_SHORT).show();
                                            pushnotify(context,"Smartradio管理系统正在运行","点击此处返回");
                                            uri = Uri.parse("http://music.163.com/m/");
                                        }
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

            }
        });


    }
    void pushnotify(Context context,String title,String text){
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
        notification.flags = Notification.FLAG_AUTO_CANCEL;
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
        public int getItemCount () {
        return songtable.size();
    }
    }