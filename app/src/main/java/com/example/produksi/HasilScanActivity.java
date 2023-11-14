package com.example.produksi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.produksi.adapter.ScanAdapter;
import com.example.produksi.model.ResponseFG;
import com.example.produksi.model.data_scan;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HasilScanActivity extends AppCompatActivity {

    ScanAdapter adapter;
    private ArrayList<data_scan> data_scanArrayList = new ArrayList<>();
    RecyclerView recyclerView;
    String barcode;
    Button simpan;
    ProgressDialog progressDialog;
    ArrayList<String> list;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ApiInterface apiInterface;
    AlertDialog.Builder builder;
    String idSertem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_scan);
        Bundle extras = getIntent().getExtras();
        idSertem = extras.getString("kirim");

        recyclerView = findViewById(R.id.list_scan);
        simpan = findViewById(R.id.btn_simpan);
        apiInterface = Api.getClient().create(ApiInterface.class);
        builder = new AlertDialog.Builder(this);
        list = new ArrayList<>();

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.pink_2)));
        getSupportActionBar().setTitle("Serah Terima Barang");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        loadData();
        buildRecyclerView();

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = ProgressDialog.show(HasilScanActivity.this, null, "Harap Tunggu...", true, false);
                //Toast.makeText(hasil_scan.this, new Gson().toJson(data_scan_ArrayList), Toast.LENGTH_SHORT).show();
                for (int i = 0; i < data_scanArrayList.size(); i++) {
                    barcode = replaceString(data_scanArrayList.get(i).getDatascan());
                    //Toast.makeText(HasilScanActivity.this, barcode, Toast.LENGTH_LONG).show();
                    if (barcode.contains("/") || barcode.contains(",")) {
                        barcode = data_scanArrayList.get(i).getDatascan().replace("/", "replace").replace(",", "koma");
                        list.add(barcode);
                        sendPost(idSertem, barcode, adapter);
                        simpan.setEnabled(false);
                    } else {
                        list.add(barcode);
                        sendPost(idSertem, barcode, adapter);
                        simpan.setEnabled(false);
                    }

                }
            }
        });

    }

    public void sendPost(String id, String barcode, ScanAdapter adapter) {
        apiInterface.saveFG(id, barcode).enqueue(new Callback<ResponseFG>() {
            @Override
            public void onResponse(Call<ResponseFG> call, Response<ResponseFG> response) {
                progressDialog.dismiss();
                ResponseFG data = response.body();
                if (data.getStatus().equals("1")){
                    sharedPreferences = getSharedPreferences("shared preferences", 0);
                    sharedPreferences.edit().remove("datanya").commit();
                    adapter.clear();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(HasilScanActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HasilScanActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseFG> call, Throwable t) {
                Toast.makeText(HasilScanActivity.this, "Error Mssg : " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                //Log.d("Error mssg : ", t.getLocalizedMessage());
                progressDialog.dismiss();
            }
        });
    }

    private void buildRecyclerView() {
        adapter = new ScanAdapter(data_scanArrayList,this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        adapter.setOnItemListenerListener(new ScanAdapter.OnItemListener() {
            @Override
            public void OnItemLongClickListener(View view, int position) {
                builder.setMessage("Yakin ingin menghapus data?")
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                data_scanArrayList.remove(position);
                                recyclerView.setAdapter(adapter);

                                sharedPreferences = getSharedPreferences("shared preferences", 0);
                                sharedPreferences.edit().remove("datanya").commit();

                                sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
                                editor = sharedPreferences.edit();
                                Gson gson = new Gson();
                                String json = gson.toJson(data_scanArrayList);
                                editor.putString("datanya", json);
                                editor.commit();
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.setTitle("Hapus");
                alertDialog.show();
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private void loadData() {

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("datanya", null);
        Type type = new TypeToken<ArrayList<data_scan>>() {}.getType();
        data_scanArrayList = gson.fromJson(json, type);

        if (data_scanArrayList == null) {
            data_scanArrayList = new ArrayList<>();
            simpan.setEnabled(false);
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    String replaceString(String string) {
        return string.replaceAll("[^a-zA-Z0-9-/]","");
    }
}
