package com.temenos.dshubhamrajput.genericnet;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import net.glxn.qrgen.android.QRCode;
import net.glxn.qrgen.core.scheme.VCard;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dshubhamrajput on 31-03-2017.
 */


public class QrCodeGenerate extends AppCompatActivity{
    private Button buttonScan;
    private EditText EditTextName,EditTextAddress,EditTextAmount;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrgenerator);
        buttonScan = (Button) findViewById(R.id.qr_Button);
        EditTextName = (EditText) findViewById(R.id.cusNameEditText);
        EditTextAddress = (EditText) findViewById(R.id.cusAddressEditText);
        EditTextAmount = (EditText) findViewById(R.id.cusAmountEditText);


        buttonScan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String name = EditTextName.getText().toString();
                String address = EditTextAddress.getText().toString();
                String amount = EditTextAmount.getText().toString();
                HashMap<String, String> list = new HashMap<String, String>();
                list.put("Name",name);
                list.put("Address",address);
                list.put("Amount",amount);
                VCard cus = new VCard(name)
                        .setAddress(address)
                        .setPhoneNumber(amount);
                Bitmap myBitmap = QRCode.from(cus).bitmap();
                ImageView myImage = (ImageView) findViewById(R.id.qrCode);
                myImage.setImageBitmap(myBitmap);
            }
        });
    }
}
