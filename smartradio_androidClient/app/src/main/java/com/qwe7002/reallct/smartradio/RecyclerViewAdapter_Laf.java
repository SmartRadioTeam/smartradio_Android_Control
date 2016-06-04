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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class RecyclerViewAdapter_Laf extends RecyclerView.Adapter<RecyclerViewAdapter_Laf.laftabletViewHolder>
    {
        ProgressDialog mpDialog;
        private List<song> songtable;
        private Context context;

        public RecyclerViewAdapter_Laf(List<song> songtable, Context context)
            {
                this.songtable = songtable;
                this.context = context;
            }

        //自定义ViewHolder类
        static class laftabletViewHolder extends RecyclerView.ViewHolder
            {

                CardView cardView;
                TextView card_title;
                TextView card_message;
                Button checkbutton;

                public laftabletViewHolder(final View itemView)
                    {
                        super(itemView);
                        cardView = (CardView) itemView.findViewById(R.id.card_view);
                        //card_title = (TextView) itemView.findViewById(R.id.card_title);
                        card_message = (TextView) itemView.findViewById(R.id.card_message);
                        checkbutton = (Button) itemView.findViewById(R.id.Checkbotton);
                    }


            }

        @Override
        public RecyclerViewAdapter_Laf.laftabletViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
            {
                View v = LayoutInflater.from(context).inflate(R.layout.cardview_laf, viewGroup, false);
                laftabletViewHolder nvh = new laftabletViewHolder(v);
                return nvh;
            }

        @Override
        public void onBindViewHolder(RecyclerViewAdapter_Laf.laftabletViewHolder personViewHolder, int i)
            {
                final int j = i;
                personViewHolder.card_title.setText(songtable.get(i).getTitle());
                personViewHolder.card_message.setText(songtable.get(i).getmessage());
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
                                                        Snackbar.make(views, "条目已被设为" + views.getText(), Snackbar.LENGTH_SHORT)
                                                                .show();
                                                    }
                                            }).show();
                            }
                    });

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