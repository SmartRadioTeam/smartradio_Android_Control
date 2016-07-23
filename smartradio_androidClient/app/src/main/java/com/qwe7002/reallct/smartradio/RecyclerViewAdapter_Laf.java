package com.qwe7002.reallct.smartradio;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;


public class RecyclerViewAdapter_Laf extends RecyclerView.Adapter<RecyclerViewAdapter_Laf.laftabletViewHolder> {
    ProgressDialog mpDialog;
    private List<laf> laftable;
    private Context context;

    public RecyclerViewAdapter_Laf(List<laf> laftable, Context context, Toolbar toolbar) {
        this.laftable = laftable;
        this.context = context;
    }

    //自定义ViewHolder类
    static class laftabletViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView card_user;
        TextView card_tel;
        TextView card_message;
        Button checkbutton;

        public laftabletViewHolder(final View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            card_user = (TextView) itemView.findViewById(R.id.card_laf_user);
            card_tel = (TextView) itemView.findViewById(R.id.card_laf_tel);
            card_message = (TextView) itemView.findViewById(R.id.card_laf_message);
            checkbutton = (Button) itemView.findViewById(R.id.card_laf_Checkbotton);
        }


    }

    @Override
    public RecyclerViewAdapter_Laf.laftabletViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.cardview_laf, viewGroup, false);
        laftabletViewHolder nvh = new laftabletViewHolder(v);
        return nvh;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter_Laf.laftabletViewHolder personViewHolder, int i) {
        final int j = i;
        personViewHolder.card_user.setText("丢失人：" + laftable.get(i).getTitle());
        personViewHolder.card_message.setText("「" + laftable.get(i).getmessage() + "」");
        personViewHolder.card_tel.setText("电话：" + laftable.get(i).gettel());
        personViewHolder.checkbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final View views = v;
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog
                        .setTitle("您是否要执行此操作？")
                        .setMessage("选中项将根据您的设定被删除。")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String mode = "lostandfound";
                                        Intent intent = new Intent();
                                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                                        new sendcontrol().execute("{'mode':'" + mode + "','id':" + laftable.get(j).getid() + ",'submitmode':'single'}");
                                        Snackbar.make(views, "条目已被删除", Snackbar.LENGTH_SHORT).show();
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).setCancelable(false).create().show();
            }
        });

    }

    private void showProgress(boolean switchs) {
        if (switchs) {
            mpDialog = new ProgressDialog(context);
            mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mpDialog.setTitle("正在连接服务器...");
            mpDialog.setMessage("提交请求中，请稍候...");
            mpDialog.setIndeterminate(false);
            mpDialog.setCancelable(false);
            mpDialog.show();
        } else {
            mpDialog.hide();
        }
    }

    @Override
    public int getItemCount() {
        return laftable.size();
    }

    public class sendcontrol extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            showProgress(true);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                JsonParser parser = new JsonParser();
                JsonObject object = (JsonObject) parser.parse(params[0]);
                return APIs.ItemsControl(object.get("mode").getAsString(), object.get("id").getAsString(), object.get("submitmode").getAsString().equals("muilt"));
            } catch (Exception e) {
                return null;
            }
        }


        @Override
        protected void onPostExecute(String result) {
            showProgress(false);
            if (result != null) {
                JsonParser parser = new JsonParser();
                JsonObject object = (JsonObject) parser.parse(result);
                if (object.get("mode").getAsString().equals("success")) {

                    return;
                }
            } else {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog
                        .setTitle("无法连接到网络！")
                        .setMessage("请检查网络连接！")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).setCancelable(false).create().show();
            }
        }
    }

}