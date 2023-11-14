package com.example.produksi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.produksi.model.Item;
import com.example.produksi.model.ResponseCheck;
import com.example.produksi.model.data_scan;
import com.google.gson.Gson;
import com.google.zxing.Result;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView scannerView;
    FrameLayout camera;
    Item dtlnya;
    private ArrayList<data_scan> data_scan_ArrayList = new ArrayList<>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Button back;
    String id, token;
    ApiInterface apiInterface;
    ProgressDialog progressDialog;
    String idSertem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        camera = findViewById(R.id.frame_layout_camera);
        apiInterface = Api.getClient2().create(ApiInterface.class);
        back = findViewById(R.id.btn_back);

        token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9." +
                "eyJhdWQiOiIxIiwianRpIjoiZGFiODVjMTk3YTJiNWNmMzI3NDUxODc1NmVkZTlhNmRiY2ZlNTJjODYxZjc" +
                "4NjczNjg3ZGQ0NTc2MzkzYmI3ZTMyMDE4ODUyYzI3OTczZDIiLCJpYXQiOjE2OTk4NTk0NzguMzgzNzg4LCJ" +
                "uYmYiOjE2OTk4NTk0NzguMzgzNzkyLCJleHAiOjE3MzE0ODE4NzguMzc2MjA0LCJzdWIiOiI3OCIsInNjb3BlcyI" +
                "6WyJkYXRhcHJpbnQuc2NhbiIsImRhdGFwcmludC5rYXJhbnRpbmEiLCJkYXRhcHJpbnQucmVqZWN0IiwiZGF0YXByaW50Lmx1bHVzIl19." +
                "jqdy7cugofSc3hoD-kG78dz22avfSlUXfTQyliFQtPJGxYiIsilx3hGtiuOd5vrfkM4ngQwtTwS44Q9Ko-5StUMkX45ottLCqrqNphjPAz5wp_" +
                "6kWXzeObZxGOaWpr4rn6TaIfgQfiuBNhbP2kbhXgqKjVqwhe0vy2pERTh6fWYq4v18LzufNsA1JS-R_YkYxcXQAHJp8eQGxJv3yZL6Dy0QaDG-" +
                "AZY2ru3oFtYoWXuckTERpu53_9y8lWLOefsDUghAP6im2eq1FkBdPQ4TCG0CdmWvpDB5IMoJ36hErC80RLvbWGQhW63_bHdenxD3zMLdSDcO9AYqQVaabRW474_" +
                "Gas_F8648ysl5gOCcorWrGe6X6wvuXmLDqOBihTDFuY7_P-BcHFgTBvQjihW2-GxtCOPIY0JmduacEhHvZxhmjcJg3927CQR7EFw8yHmxlJ4SuBiFC-" +
                "kmIpRDoDvvMoogc4hM7E8kFMRYPKj63dUmCj0BSJdLR0yW68EftmgAyMU1cGghg6FqBUqUHIuQW7pYzyHM0Wxv6ZdG6kiIjE5c-opuzGzkPSZK-" +
                "fPWdq6qpoMpcfpbRBzpAbQKj8UDMHTX60dhnYf1hlGM-k6pBv6xfPFNejCLpX0tRHeFCT4grVs9IxSRc625aSAYYZrVQvbu-X5eHBXI-6UQp5WlQw0";

        progressDialog = ProgressDialog.show(ScannerActivity.this, null, "loading...", true, false);


        Bundle bundle = getIntent().getExtras();
        idSertem = bundle.getString("idSertem");

        //Toast.makeText(this, idSertem, Toast.LENGTH_LONG).show();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        initScannerView();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initScannerView(){
        progressDialog.dismiss();
        scannerView = new ZXingScannerView(ScannerActivity.this);
        scannerView.setAutoFocus(true);
        scannerView.setResultHandler(this::handleResult);
        camera.addView(scannerView);
        scannerView.startCamera();
    }

    @Override
    protected void onStart() {
        progressDialog.dismiss();
        scannerView.startCamera();
        doRequestPermission();
        super.onStart();
    }

    private void doRequestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initScannerView();
            } else {

            }
        }
    }

    @Override
    protected void onPause() {
        scannerView.stopCamera();
        super.onPause();
    }

    @Override
    public void handleResult(Result rawResult) {
        String result = rawResult.getText();
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                getData(replaceString(result), token);
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    private void initView(){
        progressDialog.dismiss();
        scannerView.resumeCameraPreview(this::handleResult);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.lihat_data:
                Intent in = new Intent(ScannerActivity.this, HasilScanActivity.class);
                in.putExtra("kirim", idSertem);
                startActivity(in);
                break;

        }
        return true;
    }

    void getData(String barcode, String token){
        Call<ResponseCheck> response = apiInterface.getStatus("Bearer " + token, barcode, "4");
        response.enqueue(new Callback<ResponseCheck>() {
            @Override
            public void onResponse(Call<ResponseCheck> call, retrofit2.Response<ResponseCheck> response) {
                progressDialog.dismiss();
                ResponseCheck data = response.body();
                if (data == null){
                    Toast.makeText(ScannerActivity.this, "No data available!", Toast.LENGTH_SHORT).show();
                } else {
                    if (data.getData2().getProductData().getStatus().equals("lulus")){
                        data_scan_ArrayList.add(new data_scan(barcode));
                        for (int i = 0; i < data_scan_ArrayList.size(); i++) {
                            for (int j = i + 1 ; j < data_scan_ArrayList.size(); j++) {
                                if (data_scan_ArrayList.get(i).getDatascan().equals(data_scan_ArrayList.get(j).getDatascan())) {
                                    data_scan_ArrayList.remove(i);
                                }
                            }
                        }

                        sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
                        editor = sharedPreferences.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(data_scan_ArrayList);
                        editor.putString("datanya", json);
                        editor.commit();
                        progressDialog.dismiss();
                        Toast.makeText(ScannerActivity.this, "Status lulus!", Toast.LENGTH_SHORT).show();
                        initView();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(ScannerActivity.this, "Status belum lulus!", Toast.LENGTH_SHORT).show();
                        initView();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseCheck> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("errordataAPI", t.getLocalizedMessage());
                Toast.makeText(ScannerActivity.this, "Server Maintenance : " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    String replaceString(String string) {
        return string.replaceAll("[^a-zA-Z0-9-/,.]","");


}}