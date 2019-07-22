package com.fengyuxing.tuyu.zego;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fengyuxing.tuyu.MyApplication;
import com.fengyuxing.tuyu.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LogsActivity extends AppCompatActivity {


    @BindView(R.id.logs_container)
    ListView logsContainer;
    @BindView(R.id.empty_data_tip)
    TextView emptyDataTip;
    private BaseAdapter logsAdapter;

    private MyApplication.ILogUpdateObserver logUpdateObserver = new MyApplication.ILogUpdateObserver() {
        @Override
        public void onLogAdd(String logMessage) {
            if (logsAdapter != null && !isFinishing()) {
                ((LogsAdapter) logsAdapter).insertItem(logMessage);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        ButterKnife.bind(this);

        logsAdapter = new LogsAdapter();
        logsContainer.setAdapter(logsAdapter);
        logsContainer.setEmptyView(emptyDataTip);

        setTitle(getIntent().getStringExtra("title"));

        ((MyApplication) getApplication()).registerLogUpdateObserver(logUpdateObserver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MyApplication) getApplication()).unregisterLogUpdateObserver(logUpdateObserver);
    }




//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_upload_log:
//                ZegoLiveRoom.uploadLog();
//                logsContainer.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (!isDestroyed()) {
//                            Toast.makeText(AudioApplication.sApplication, getString(R.string.zg_menu_logs_upload_log), Toast.LENGTH_LONG).show();
//                        }
//                    }
//                }, 3000);
//                return true;
//
//            case R.id.action_clean_events:
//                ((AudioApplication) getApplication()).getLogSet().clear();
//                ((LogsAdapter) logsAdapter).reloadData();
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    private class LogsAdapter extends BaseAdapter {

        private ArrayList<String> logs;
        private LayoutInflater inflater;

        public LogsAdapter() {
            logs = new ArrayList<>();
            logs.addAll(((MyApplication) getApplication()).getLogSet());
            inflater = getLayoutInflater();
        }

        @Override
        public int getCount() {
            return logs != null ? logs.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return logs != null ? logs.get(position) : "";
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.widget_list_item, null, false);
            }

            ((TextView) convertView).setText((String) getItem(position));
            return convertView;
        }

        public void insertItem(String logMessage) {
            logs.add(0, logMessage);

            notifyDataSetChanged();
        }

        public void reloadData() {
            logs.clear();
            logs.addAll(((MyApplication) getApplication()).getLogSet());
            notifyDataSetChanged();
        }
    }
}
