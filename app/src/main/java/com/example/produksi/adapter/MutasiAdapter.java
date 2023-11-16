package com.example.produksi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.produksi.R;
import com.example.produksi.ScannerActivity;
import com.example.produksi.model.ResponseList;

import java.util.ArrayList;

public class MutasiAdapter extends RecyclerView.Adapter<MutasiAdapter.ViewHolder> {
    private static ClickListener clickListener;
    ArrayList<ResponseList> mList;
    Context context;
    private LayoutInflater mInflater;

    public MutasiAdapter(Context context, ArrayList<ResponseList> mList){
        this.mList = mList;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_mutasi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.keterangan.setText(mList.get(position).getKeterangan() + "\nKode : " + mList.get(position).getKodeSertem());
        holder.kode.setText(mList.get(position).getKodeSertem());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ScannerActivity.class);
                intent.putExtra("idSertem", mList.get(position).getIdBarangMasuk());
                context.startActivity(intent);
            }
        });
    }


    // total number of rows
    @Override
    public int getItemCount() {
        return mList.size();
    }


    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        MutasiAdapter.clickListener = clickListener;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView keterangan, kode;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            keterangan = itemView.findViewById(R.id.keterangan);
            kode = itemView.findViewById(R.id.kode);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), itemView);
        }
    }

    public void setFilter(ArrayList<ResponseList> filter){
        mList = new ArrayList<>();
        mList.addAll(filter);
        notifyDataSetChanged();
    }

}