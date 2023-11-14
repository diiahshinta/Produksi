package com.example.produksi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.produksi.adapter.MutasiAdapter;
import com.example.produksi.model.ResponseList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MutasiActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MutasiAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressDialog loading;
    ArrayList<ResponseList> responseLists;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mutasi);

        recyclerView = findViewById(R.id.list_mutasi);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);

        apiInterface = Api.getClient().create(ApiInterface.class);
        loading = ProgressDialog.show(MutasiActivity.this, null, "Loading...", true, false);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue_1)));
        getSupportActionBar().setTitle("Serah Terima Barang");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        getList();
        responseLists = new ArrayList<>();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                getList();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    void getList(){
        apiInterface.getListMutasi().enqueue(new Callback<ArrayList<ResponseList>>() {
            @Override
            public void onResponse(Call<ArrayList<ResponseList>> call, Response<ArrayList<ResponseList>> response) {
                if (response.isSuccessful()){
                    responseLists = response.body();
                    adapter = new MutasiAdapter(MutasiActivity.this, responseLists);
                    recyclerView.setAdapter(adapter);
                    loading.dismiss();
                } else {
                    loading.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ResponseList>> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(MutasiActivity.this, "Server Maintenance : " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                ArrayList<ResponseList> itemFilter = new ArrayList<>();
                for (ResponseList model : responseLists){
                    String no = model.getKodeSertem().toLowerCase();
                    if (no.contains(newText)){
                        itemFilter.add(model);
                    }
                }
                adapter.setFilter(itemFilter);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}