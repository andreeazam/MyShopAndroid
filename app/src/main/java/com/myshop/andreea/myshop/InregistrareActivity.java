package com.myshop.andreea.myshop;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

public class InregistrareActivity extends AppCompatActivity implements View.OnClickListener {

    Button bInregistrare;
    EditText etNume, etPrenume, etUsername, etParola, etEmail,
            etStrada, etNumarStrada, etTelefon;

    String ipURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inregistrare);

        Bundle bundle = getIntent().getExtras();
        ipURL = bundle.getString("ipURL");

        etNume = (EditText) findViewById(R.id.etNume);
        etPrenume = (EditText) findViewById(R.id.etPrenume);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etParola = (EditText) findViewById(R.id.etParola);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etStrada = (EditText) findViewById(R.id.etStrada);
        etNumarStrada = (EditText) findViewById(R.id.etNumarStrada);
        etTelefon = (EditText) findViewById(R.id.etTelefon);

        bInregistrare = (Button) findViewById(R.id.bInregistrare);

        bInregistrare.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bInregistrare:
                actionRegister();
                startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void actionRegister() {
        String nume = etNume.getText().toString();
        String prenume = etPrenume.getText().toString();
        String username = etUsername.getText().toString();
        String parola = etParola.getText().toString();
        String email = etEmail.getText().toString();
        String strada = etStrada.getText().toString();
        int numarStrada = Integer.parseInt(etNumarStrada.getText().toString());
        long telefon = Long.parseLong(etTelefon.getText().toString());
        long latitudine = 123456;
        long longitudine = 456987;

        RequestParams params = new RequestParams();
        params.put("nume", nume);
        params.put("prenume", prenume);
        params.put("username", username);
        params.put("parola", parola);
        params.put("email", email);
        params.put("strada", strada);
        params.put("nrstrada", numarStrada);
        params.put("telefon", telefon);
        params.put("lat", 234);
        params.put("long", 567);

        if(params != null && !params.toString().equals("")) {
            invokeWS(params);
        }else{
            Toast.makeText(getApplicationContext(), "Va rugam sa completati toate campurile!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Method that performs RESTful webservice invocations
     *
     * @param params
     */
    public void invokeWS(RequestParams params) {
        // Show Progress Dialog
//        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
      //  String URL = "http://10.30.7.163:8080/TestW/rest/test/inregistrareclient";
        String URL = "http://"+ipURL+":8080/TestW/rest/client/inregistrareclient";
        client.get(URL, params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
  //              prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    Log.i("", "Am primit raspuns");
                    if (obj.getBoolean("existaClient")) {
                        Toast.makeText(getApplicationContext(), "Bine ai venit "+obj.getString("prenumeClient"), Toast.LENGTH_LONG).show();
                        // Navigate to AcasaActivity screen
                        //navigatetoHomeActivity();
                    }
                    // Else display error message
                    else {
                        //  errorMsg.setText(obj.getString("error_msg"));
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                // Hide Progress Dialog
    //            prgDialog.hide();
                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
