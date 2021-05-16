package com.example.login;



import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login.Config.Config;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;


public class PaymentMethods extends AppCompatActivity {

    public static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);

    Button btnPayNow;
    Button payPalWeb;
    EditText edtAmout;
    Button signOut;
    Button tiktaktoe;
    String amount ="";

    @Override
    protected void onDestroy(){
        stopService(new Intent(this,PayPalService.class));
        super.onDestroy();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_methods);


        //Start PayPal Service
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);



        btnPayNow = (Button)findViewById(R.id.btnPayNow);
        edtAmout = (EditText)findViewById(R.id.edtAmount);
        payPalWeb = (Button)findViewById(R.id.paypalweb);
        signOut = (Button)findViewById(R.id.signout);
        tiktaktoe = (Button)findViewById(R.id.ticktaktoe);


        payPalWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                website("https://paypal.me/pools/c/8zjqMxeko5");
            }

        });
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processSignOut();
            }

        });


        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                processPayment();
            }
        });

        tiktaktoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                processtiktatoe();
            }
        });


    }

    private void processtiktatoe() {
        Intent intent = new Intent(this, TikTakToe.class);
        startActivity(intent);
    }

    private void processSignOut() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void website(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
    private void processPayment(){
        amount = edtAmout.getText().toString();
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)),"USD",
                "Donate for Useless-Studio", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);

                        startActivity(new Intent(this, PayPalPaymentDetails.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", amount)

                        );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            } else if (resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();

        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();

    }
}