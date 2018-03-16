package com.myshop.andreea.myshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class ComenzileMeleActivity extends AppCompatActivity {

    public ListView comenzileMeleListView;
    private String ipURL = "";
    private String idClient = "";

    public List<Comanda> comList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comenzile_mele);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        ipURL = bundle.getString("ipURL");
        idClient = bundle.getString("idClient");

        RequestParams params = new RequestParams();
        params.put("idClient", idClient);

        if(params != null && !params.toString().equals("")) {
            invokeWS(params);
        }else{
            Toast.makeText(getApplicationContext(), "Va rugam sa completati toate campurile!", Toast.LENGTH_LONG).show();
        }

        comenzileMeleListView = (ListView)findViewById(R.id.comenzile_mele_list_view);
    }


    /**
     * Method that performs RESTful webservice invocations
     *
     * @param params
     */
    public void invokeWS(RequestParams params) {
        // Show Progress Dialog
        //   prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        String URL = "http://"+ipURL+":8080/TestW/rest/comanda/obtinecomenziclient";

        final ComenzileMeleActivity that = this;
        client.get(URL, params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                Log.i("", response);
                // Hide Progress Dialog
                //         prgDialog.hide();
                try {
                    // JSON Object
                    JSONArray obj = new JSONArray(response);
                    for (int index = 0; index < obj.length(); index++) {
                        JSONObject comanda = obj.getJSONObject(index);
                        Comanda com = new Comanda();
                        com.setIdClient(comanda.getLong("idClient"));
                        com.setIdComandaClient(comanda.getLong("idComandaClient"));
                        com.setNumarComanda(comanda.getLong("numarComanda"));
                        com.setValoareComanda(comanda.getDouble("valoareComanda"));
                        com.setTextStatus(comanda.getString("textStatus"));
                        that.comList.add(com);
                    }

                    ComandaAdapter comandaAdapter = new ComandaAdapter(getApplicationContext(), that.comList);
                    that.comenzileMeleListView.setAdapter(comandaAdapter);
                    comandaAdapter.notifyDataSetChanged();
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

}
