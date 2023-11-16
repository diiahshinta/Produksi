package com.example.produksi.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.produksi.R;
import com.example.produksi.model.data_scan;

import java.util.ArrayList;

public class ScanAdapter extends RecyclerView.Adapter<ScanAdapter.ViewHolder> {

    private ArrayList<data_scan> data_scanArrayList;
    private Context context;
    String json, data_kirim;
    SharedPreferences sharedPreferences;
    private OnItemListener onItemListener;

    public ScanAdapter(ArrayList<data_scan> data_scanArrayList, Context context){
        this.data_scanArrayList = data_scanArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ScanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scan, parent, false);
        view.setOnLongClickListener(new RV_ItemListener());
        return new ViewHolder(view);
    }

    public void clear() {
        int size = data_scanArrayList.size();
        data_scanArrayList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public interface OnItemListener{
        void OnItemLongClickListener(View view, int position);
    }

    class RV_ItemListener implements View.OnLongClickListener{

        @Override
        public boolean onLongClick(View v) {
            if (onItemListener != null){
                onItemListener.OnItemLongClickListener(v, v.getId());
            }
            return true;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ScanAdapter.ViewHolder holder, int position) {
        Bundle extras = ((Activity) context).getIntent().getExtras();
        data_kirim= extras.getString("kirim");
        sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        json = sharedPreferences.getString("datanya", null);

        data_scan modal = data_scanArrayList.get(position);
        holder.barcode.setText(modal.getDatascan());
        holder.itemView.setId(position);
    }

    public void setOnItemListenerListener(OnItemListener listener){
        this.onItemListener = listener;
    }

    @Override
    public int getItemCount() {
        return data_scanArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView barcode;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            barcode = itemView.findViewById(R.id.scan_qrcode);
        }
    }
}
