package com.myshop.andreea.myshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VeziComandaActivity extends AppCompatActivity implements View.OnClickListener {

    public ListView liniiComandaListView;
    Button bFinalizareComanda, bAlegeAltProdus;
    String idComanda="";
    String numarComanda="";
    String idClient="";
    String ipURL="";

    public List<LinieComanda> liniiComList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vezi_comanda);

        Bundle bundle = getIntent().getExtras();
        idComanda = bundle.getString("idComanda");
        numarComanda = bundle.getString("numarComanda");
        idClient = bundle.getString("idClient");
        ipURL = bundle.getString("ipURL");


        loadLiniiComanda();

        bFinalizareComanda = (Button) findViewById(R.id.bFinalizareComanda);
        bAlegeAltProdus = (Button) findViewById(R.id.bAlegeAltprodus);

        liniiComandaListView = (ListView) findViewById(R.id.linie_comanda_list_view);

        bFinalizareComanda.setOnClickListener(this);
        bAlegeAltProdus.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.bFinalizareComanda:
                trimitereComanda();

                break;
            case R.id.bAlegeAltprodus:
                Intent intent = new Intent(this, ScaneazaProdusActivity.class);
                intent.putExtra("idComanda", idComanda);
                intent.putExtra("numarComanda", numarComanda);
                intent.putExtra("idClient", idClient);
                intent.putExtra("ipURL", ipURL);
                this.startActivity(intent);
                break;

        }
    }

    private void trimitereComanda(){
        RequestParams params = new RequestParams();
        params.put("idComandaClient", idComanda);
        Log.i("", " Am primit raspuns"+idComanda+"..........");
        invokeWS(params);
    }

    private void loadLiniiComanda(){
        RequestParams params = new RequestParams();
        params.put("idComandaClient", idComanda);
        invokeWSC(params);
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

        String URL = "http://"+ipURL+":8080/TestW/rest/comanda/trimiterecomanda";

        final VeziComandaActivity that = this;
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
                    if(!(obj.getString("idStatus")=="2")){
                        that.bFinalizareComanda.setText("Comanda in curs");
                        that.bFinalizareComanda.setClickable(false);
                        Intent intent = new Intent(that, ScaneazaProdusActivity.class);
                        intent.putExtra("idComanda", obj.getString("idComandaClient"));
                        intent.putExtra("numarComanda", obj.getString("numarComanda"));
                        intent.putExtra("idClient", obj.getString("idClient"));
                        that.startActivity(intent);
                    }
                   // if (obj.getString("idStatus")=="2")
                    else
                    {
                        Toast.makeText(getApplicationContext(), "S-a inregistrat comanda cu numarul "+obj.getString("numarComanda"), Toast.LENGTH_LONG).show();
                        // Navigate to AcasaActivity screen
                        Intent intent = new Intent(that, AcasaActivity.class);
                        //Trimitem idClient ca paramentru in ecranul urmator:
                        intent.putExtra("idComanda", obj.getString("idComandaClient"));
                        intent.putExtra("numarComanda", obj.getString("numarComanda"));
                        intent.putExtra("idClient", obj.getString("idClient"));

                        intent.putExtra("ipURL", ipURL);
                        that.startActivity(intent);

                    }


                    // Else display error message
                   // else {
                        //  errorMsg.setText(obj.getString("error_msg"));
                    //    Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                  //  }
                }
                catch (JSONException e) {
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

    /**
     * Method that performs RESTful webservice invocations
     *
     * @param params
     */
    public void invokeWSC(RequestParams params) {
        // Show Progress Dialog
//        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        //  String URL = "http://10.30.7.163:8080/TestW/rest/test/inregistrareclient";

        String URL = "http://"+ipURL+":8080/TestW/rest/liniecomanda/loadliniicomanda";

        final VeziComandaActivity that = this;
        client.get(URL, params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                //              prgDialog.hide();
                try {
                    // JSON Object
                    JSONArray obj = new JSONArray(response);
                    // When the JSON response has status boolean value assigned with true
                    for (int index = 0; index < obj.length(); index++) {
                        JSONObject linieComanda = obj.getJSONObject(index);
                        LinieComanda linieCom = new LinieComanda();
                        linieCom.setIdLinieComanda(linieComanda.getLong("idLinieComanda"));
                        linieCom.setIdComandaClient(linieComanda.getLong("idComandaClient"));
                        linieCom.setIdProdus(linieComanda.getLong("idProdus"));
                        linieCom.setCantitate(linieComanda.getInt("cantitate"));
                        linieCom.setPretProdus(linieComanda.getDouble("pretProdus"));
                        linieCom.setLccValoare(linieComanda.getDouble("lccValoare"));
                        linieCom.setDenumireProdus(linieComanda.getString("denumireProdus"));

                        that.liniiComList.add(linieCom);
                    }

                    LinieComandaAdapter linieComandaAdapter = new LinieComandaAdapter(getApplicationContext(), that.liniiComList);
                    that.liniiComandaListView.setAdapter(linieComandaAdapter);
                    linieComandaAdapter.notifyDataSetChanged();
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



