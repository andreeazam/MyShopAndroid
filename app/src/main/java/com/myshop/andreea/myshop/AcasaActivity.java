package com.myshop.andreea.myshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

public class AcasaActivity extends AppCompatActivity implements View.OnClickListener {

    Button bComandaNoua,bConfirmarePrimire, bComenzileMele;
    String idClient;
    String idStatus = "1";
    String ipURL = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Bundle bundle = getIntent().getExtras();
        idClient = bundle.getString("idClient");
        ipURL = bundle.getString("ipURL");

        Toast.makeText(getApplicationContext(), "Lucram cu clientul "+ idClient, Toast.LENGTH_LONG).show();

        bComandaNoua = (Button) findViewById(R.id.bComandaNoua);
        bComenzileMele = (Button) findViewById(R.id.bComenzileMele);
        bConfirmarePrimire = (Button) findViewById(R.id.bConfirmarePrimire);

        bComandaNoua.setOnClickListener(this);
        bComenzileMele.setOnClickListener(this);
        bConfirmarePrimire.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.bComandaNoua:
                inregistrareComandaNoua();
                break;
            case R.id.bConfirmarePrimire:

                Intent intent = new Intent(this, ConfirmarePrimireActivity.class);
                intent.putExtra("ipURL", ipURL);
                intent.putExtra("idClient", idClient);
                startActivity(intent);
                break;
            case R.id.bComenzileMele:
                Intent intent1 = new Intent(this, ComenzileMeleActivity.class);
                intent1.putExtra("ipURL", ipURL);
                intent1.putExtra("idClient", idClient);
                startActivity(intent1);
                break;
        }
    }

    private void inregistrareComandaNoua() {

        RequestParams params = new RequestParams();
        params.put("idClient", idClient);
        params.put("idStatus", idStatus);

        invokeWS(params);

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

        String URL = "http://"+ipURL+":8080/TestW/rest/comanda/inregistrarecomanda";

        final AcasaActivity that = this;
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
                    if (!obj.getString("idComandaClient").equals("")) {
                        Toast.makeText(getApplicationContext(), "S-a inregistrat comanda "+obj.getString("idComandaClient"), Toast.LENGTH_LONG).show();
                        // Navigate to AcasaActivity screen
                        //navigatetoHomeActivity();
                        Intent intent = new Intent(that, ScaneazaProdusActivity.class);
                        //Trimitem idClient ca paramentru in ecranul urmator:
                        intent.putExtra("idComanda", obj.getString("idComandaClient"));
                        intent.putExtra("numarComanda", obj.getString("numarComanda"));
                        intent.putExtra("idClient", obj.getString("idClient"));
                        intent.putExtra("ipURL", ipURL);
                        that.startActivity(intent);
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
