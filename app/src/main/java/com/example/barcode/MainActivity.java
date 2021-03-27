package com.example.barcode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.notbytes.barcode_reader.BarcodeReaderActivity;
import com.notbytes.barcode_reader.BarcodeReaderFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,BarcodeReaderFragment.BarcodeReaderListener {
    private static final int BARCODE_READER_ACTIVITY_REQUEST = 1208;
    private TextView mTvResult;
    private TextView mTvResultHeader;
    int count=0;
    SQLiteDatabase myDatabase;
    List<String> list=new ArrayList<String>();

    public void onShow(View view){
        Intent intent = new Intent(getApplicationContext(),RecyclerView.class);
        String[] array = list.toArray(new String[list.size()]);
        intent.putExtra("sqlData",array);
        startActivity(intent);
        finish();

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //findViewById(R.id.btn_activity).setOnClickListener((View.OnClickListener) this);
        findViewById(R.id.btn_fragment).setOnClickListener(this);
        mTvResult = findViewById(R.id.tv_result);
        mTvResultHeader = findViewById(R.id.tv_result_head);

    }


    private void addBarcodeReaderFragment() {
        BarcodeReaderFragment readerFragment = BarcodeReaderFragment.newInstance(true, false, View.VISIBLE);
        readerFragment.setListener(this);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fm_container, readerFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }



        @Override
        public void onClick (View v) {
        switch (v.getId()) {
            case R.id.btn_fragment:
                try {
                    addBarcodeReaderFragment();
                } catch (Exception e){
                    e.printStackTrace();
                }
                break;
//            case R.id.btn_activity:
//                FragmentManager supportFragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
//                Fragment fragmentById = supportFragmentManager.findFragmentById(R.id.fm_container);
//                if (fragmentById != null) {
//                    fragmentTransaction.remove(fragmentById);
//                }
//                fragmentTransaction.commitAllowingStateLoss();
//                launchBarCodeActivity();
//                break;
        }
    }



    private void launchBarCodeActivity() {
        Intent launchIntent = BarcodeReaderActivity.getLaunchIntent(this, true, false);
        startActivityForResult(launchIntent, BARCODE_READER_ACTIVITY_REQUEST);
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "error in  scanning", Toast.LENGTH_SHORT).show();
            return;
        }

        if (requestCode == BARCODE_READER_ACTIVITY_REQUEST && data != null) {
            Barcode barcode = data.getParcelableExtra(BarcodeReaderActivity.KEY_CAPTURED_BARCODE);
            Toast.makeText(this, barcode.rawValue, Toast.LENGTH_SHORT).show();
            mTvResultHeader.setText("On Activity Result");
            mTvResult.setText(barcode.rawValue);
        }

    }

    @Override
    public void onScanned(Barcode barcode) {
        Toast.makeText(this, barcode.rawValue, Toast.LENGTH_SHORT).show();
        mTvResultHeader.setText("Barcode value from fragment");
        mTvResult.setText(barcode.rawValue);
        String data = barcode.rawValue.toString();
        count =count +1;
        String sql="INSERT INTO scannedData (serialNo,code) VALUES ("+ count +",'"+ data +"')";

        try{
            myDatabase = this.openOrCreateDatabase("scannedData",MODE_PRIVATE,null);

            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS scannedData (serialNo INT(3), Code STRING(200))");
            myDatabase.execSQL(sql);

            Cursor c = myDatabase.rawQuery("SELECT * FROM scannedData", null);

            int nameIndex = c.getColumnIndex("serialNo");
            int ageIndex = c.getColumnIndex("Code");
            c.moveToFirst();

            while (c != null) {
                Log.i("serialNo", Integer.toString(c.getInt(nameIndex)));
                String s= c.getString(ageIndex);
                list.add(s);

                c.moveToNext();}

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {

    }

    @Override
    public void onCameraPermissionDenied() {
        Toast.makeText(this, "Camera permission denied!", Toast.LENGTH_LONG).show();
    }

    //my code starts here



    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDatabase.execSQL("delete from scannedData");

    }




}