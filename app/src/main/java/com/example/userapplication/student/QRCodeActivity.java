package com.example.userapplication.student;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.userapplication.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class QRCodeActivity extends AppCompatActivity {

    ImageView ivQRCode;
    private final int QRCodeWidth = 500;
    private final int QRCodeHeight = 500;
    Bitmap bitmap;
    SharedPreferences preferences;
    String strUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        preferences = PreferenceManager.getDefaultSharedPreferences(QRCodeActivity.this);
        strUsername = preferences.getString("username","");

        ivQRCode = findViewById(R.id.ivStudentQRCodeActivityQRCode);

        try {
            createQRCode();

        } catch (WriterException e) {

            Toast.makeText(this, " " + e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private void createQRCode() throws WriterException {

        bitmap = textToImageEncode(strUsername);
        ivQRCode.setImageBitmap(bitmap);
    }

    private Bitmap textToImageEncode(String strUsername) throws WriterException {

        BitMatrix bitMatrix;    // 2D QRCode Creation
        bitMatrix = new MultiFormatWriter().encode(strUsername, BarcodeFormat.QR_CODE, QRCodeWidth, QRCodeHeight);

        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int x=0; x < bitMatrixWidth; x++){

            int offset = x * bitMatrixHeight;

            for (int y=0; y < bitMatrixHeight; y++){

                pixels[offset + y] = bitMatrix.get(x,y) ? getResources().getColor(R.color.black)
                        : getResources().getColor(R.color.white);

            }
        }

        bitmap = Bitmap.createBitmap(bitMatrixWidth,bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels,0,500,0,0,bitMatrixWidth,bitMatrixHeight);

        return bitmap;
    }
}