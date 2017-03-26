package com.cantekin.ocr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 10001;
    private static final int REQUEST_GALERY = 10002;
    private MyTessOCR mTessOCR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn=(Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(intent, REQUEST_GALERY);
            }
        });
        Button btn2=(Button) findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CAMERA);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.w("onActivityResult","onActivityResult");
        TextView t = (TextView) findViewById(R.id.textData);
        mTessOCR = new MyTessOCR(MainActivity.this);
        if (resultCode == RESULT_OK)
            switch (requestCode) {
                case REQUEST_GALERY:
                    Uri selectedImage = intent.getData();
                    Bitmap bitmap=null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String temp = mTessOCR.getOCRResult(bitmap);
                    Log.w("okunan",temp);
                    t.setText(temp);
                    bitmap=null;
                    break;
                case REQUEST_CAMERA:
                    Bitmap photo = (Bitmap) intent.getExtras().get("data");
                    String txt = mTessOCR.getOCRResult(photo);
                    photo=null;
                    Log.w("okunan",txt);
                    t.setText(txt);
                    break;
                default:
                    break;
            }

        mTessOCR.onDestroy();
        System.gc();
    }
}

