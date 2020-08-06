package com.example.communicationwarningsystem.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.communicationwarningsystem.R;
import com.example.communicationwarningsystem.entity.Phone;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private static final String TAG = "ComWa-MainAda";
    private List<Phone> list;
    private OnItemClickListener clickListener;


    public MainAdapter(List<Phone> list){
        this.list = list;
    }

    public void setList(List<Phone> list) {
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameText;
        TextView numberText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.text_name);
            numberText = itemView.findViewById(R.id.text_number);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Phone phone = list.get(position);

        holder.nameText.setText(phone.getName());
        holder.nameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(clickListener != null){
                clickListener.onClick(position);
            }
            }
        });

        holder.numberText.setText(phone.getNumber());
        holder.numberText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(clickListener != null){
                clickListener.onClick(position);
            }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // Item点击接口
    public interface OnItemClickListener{
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener){
        this.clickListener = clickListener;
    }
}
