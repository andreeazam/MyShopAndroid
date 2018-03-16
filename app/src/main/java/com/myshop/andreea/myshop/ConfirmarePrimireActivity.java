package com.myshop.andreea.myshop;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfirmarePrimireActivity extends AppCompatActivity implements View.OnClickListener {
    private String idClient;
    private String numarComanda = "";
    private String idComanda = "";
    private String ipURL = "";

    Button bConfirmaPrimire, bDetaliiComanda, bComenzileMele;
    TextView tvComanda, tvStatus, tvValoare, tvNumarComanda, tvTextStatus, tvSumaValoare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmare_primire);

        //apelul metodei de incarcare a detaliilor comenzii

        Bundle bundle = getIntent().getExtras();
        idClient = bundle.getString("idClient");
        ipURL = bundle.getString("ipURL");

        incarcaDetaliiComanda();

        tvComanda = (TextView) findViewById(R.id.tvComanda);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        tvValoare = (TextView) findViewById(R.id.tvValoare);
        tvNumarComanda = (TextView) findViewById(R.id.tvNumarComanda);
        tvTextStatus = (TextView) findViewById(R.id.tvTextStatus);
        tvSumaValoare = (TextView) findViewById(R.id.tvSumaValoare);

        bConfirmaPrimire = (Button) findViewById(R.id.bConfirmaPrimire);
        bDetaliiComanda = (Button) findViewById(R.id.bDetaliiComanda);
        bComenzileMele = (Button) findViewById(R.id.bComenzileMele);

        bConfirmaPrimire.setOnClickListener(this);
        bDetaliiComanda.setOnClickListener(this);
        bComenzileMele.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bConfirmaPrimire:
                confirmaPrimire();
                incarcaDetaliiComanda();

                break;
            case R.id.bDetaliiComanda:
                Intent intent = new Intent(this, VeziComandaActivity.class);
                intent.putExtra("ipURL", ipURL);
                intent.putExtra("idClient", idClient);
                intent.putExtra("idComanda", idComanda);
                startActivity(intent);
                break;
            case R.id.bComenzileMele:
                intent = new Intent(this, ComenzileMeleActivity.class);
                intent.putExtra("ipURL", ipURL);
                intent.putExtra("idClient", idClient);
                startActivity(intent);
                break;
        }
    }

    public void incarcaDetaliiComanda() {
        RequestParams params = new RequestParams();
        params.put("idClient", idClient);
        InvokeWS(params);
    }

    public void confirmaPrimire() {

        RequestParams params = new RequestParams();
        params.put("idComandaClient", idComanda);
        InvokeWSC(params);
    }

    /**
     * Method that performs RESTful webservice invocations
     *
     * @param params
     */
    public void InvokeWS(RequestParams params) {
        // Show Progress Dialog
        //   prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        String URL = "http://" + ipURL + ":8080/TestW/rest/comanda/incarcadetaliicomanda";

        final ConfirmarePrimireActivity that = this;
        client.get(URL, params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                Log.i("", response);
                // Hide Progress Dialog
                //         prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    Log.i("", " Am primit raspuns");
                    if (!obj.getString("idComandaClient").equals("null")) {
                        that.idComanda = obj.getString("idComandaClient");
                        that.tvNumarComanda.setText(obj.getString("numarComanda"));
                        that.tvSumaValoare.setText(obj.getString("valoareComanda"));

                        if (obj.getString("idStatus").equals("4")) {
                            that.tvTextStatus.setText("In curs de livrare");
                        } else {
                            that.tvTextStatus.setText("Comanda in curs de procesare");
                            that.bConfirmaPrimire.setClickable(false);
                            that.bConfirmaPrimire.setText("Nimic de confirmat");
                            Toast.makeText(getApplicationContext(), "Nu exista comanda in curs de livrare", Toast.LENGTH_LONG).show();

                        }
                    }
                    // Else display error message
                    else {
                        //  errorMsg.setText(obj.getString("error_msg"));
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    //Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "Nu exista comanda in curs de procesare", Toast.LENGTH_LONG).show();

                }
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                // Hide Progress Dialog
                //       prgDialog.hide();
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


    /**
     * Method that performs RESTful webservice invocations
     *
     * @param params
     */


    public void InvokeWSC(RequestParams params) {
        // Show Progress Dialog
        //   prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        String URL = "http://" + ipURL + ":8080/TestW/rest/comanda/confirmaprimire";

        final ConfirmarePrimireActivity that = this;
        client.get(URL, params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                Log.i("", response);
                // Hide Progress Dialog
                //         prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    Log.i("", " Am primit raspuns");
                    if (obj.getString("idStatus").equals("5")) {
                        Toast.makeText(getApplicationContext(), obj.getString("Primirea  a fost confirmata pentru comanda "+obj.getString("numarComanda")), Toast.LENGTH_LONG).show();
                    }
                    // Else display error message
                    else {
                        //  errorMsg.setText(obj.getString("error_msg"));
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    //Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "Nu exista comanda in curs de livrare", Toast.LENGTH_LONG).show();

                }
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                // Hide Progress Dialog
                //       prgDialog.hide();
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
