package com.example.communicationwarningsystem.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.communicationwarningsystem.R;
import com.example.communicationwarningsystem.activity.CallActivity;
import com.example.communicationwarningsystem.activity.MessageActivity;
import com.example.communicationwarningsystem.entity.Message;
import com.example.communicationwarningsystem.entity.Record;
import com.example.communicationwarningsystem.util.Util;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
    private static final String TAG = "ComWa-RecAda";
    private List<Message> messageList;
    private List<Record> recordList;
    private int index;
    private OnItemClickListener clickListener;


    public RecordAdapter(int index){
        this.index = index;
        Log.d(TAG, "RecordAdapter: " + index);
    }



    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public void setRecordList(List<Record> recordList) {
        this.recordList = recordList;
        Log.d(TAG, "setRecordList: " + recordList);
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView contentText;
        TextView dateText;
        TextView durationText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contentText = itemView.findViewById(R.id.text_number);
            dateText = itemView.findViewById(R.id.text_date);
            durationText = itemView.findViewById(R.id.text_duration);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String content = "";
        String date = "";
        String duration = "";

        if(index == MessageActivity.CODE && messageList.size() > 0){          // 短信
            Message message = messageList.get(position);
            content = message.getContent();
            date = message.getDate();
        }
        else{                                       // 通话记录
            if(recordList != null && recordList.size() > 0) {
                Record record = recordList.get(position);
                content = Util.getStateString(record.getState());
                date = record.getDate();
                duration = record.getDuration().toString() + "秒";
            }
        }

        holder.contentText.setText(content);            // 内容
        holder.contentText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(position);
            }
        });

        holder.dateText.setText(date);                  // 日期
        holder.dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(position);
            }
        });

        holder.durationText.setText(duration);          // 时长
        holder.durationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        int count = 0;

        if(index == MessageActivity.CODE && messageList != null && messageList.size() > 0){
            count = messageList.size();
        }
        else if(index == CallActivity.CODE && recordList != null && recordList.size() > 0){
            count = recordList.size();
        }

        return count;
    }

    // Item点击接口
    public interface OnItemClickListener{
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener){
        this.clickListener = clickListener;
    }

    @Override
    public String toString() {
        return "RecordAdapter{" +
                "messageList=" + messageList +
                ", recordList=" + recordList +
                ", index=" + index +
                '}';
    }
}
