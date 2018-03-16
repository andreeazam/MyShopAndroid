package com.myshop.andreea.myshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class ScaneazaProdusActivity extends AppCompatActivity implements View.OnClickListener {
    private String idClient = "";
    private String idComanda = "";
    private String numarComanda = "";
    private String ipURL = "";

    Button btnCauta, btnAdaugaInCos, btnScaneazaAltProdus;
    //String tetIdProdus;
    EditText etIdProdus;
    EditText etDenumireProdus;
    EditText etPretProdus;
    EditText etCantitateInStoc;
    EditText etCantitateComandata;
    TextView txtNrComanda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scaneaza_produs);

        Bundle bundle = getIntent().getExtras();
        idClient = bundle.getString("idClient");
        idComanda = bundle.getString("idComanda");
        numarComanda = bundle.getString("numarComanda");
        ipURL = bundle.getString("ipURL");

        etIdProdus = (EditText) findViewById(R.id.etIdProdus);
        etDenumireProdus = (EditText) findViewById(R.id.etDenumireProdus);
        etCantitateInStoc = (EditText) findViewById(R.id.etCantitateInStoc);
        etCantitateComandata = (EditText) findViewById(R.id.etCantitateComandata);
        etPretProdus= (EditText) findViewById(R.id.etPretProdus);
        txtNrComanda = (TextView) findViewById(R.id.txtNrComanda);
        txtNrComanda.setText(numarComanda);

        btnAdaugaInCos = (Button) findViewById(R.id.btnAdaugaInCos);
        btnCauta = (Button) findViewById(R.id.btnCauta);
        btnScaneazaAltProdus = (Button) findViewById(R.id.btnScaneazaAltProdus);

        btnScaneazaAltProdus.setOnClickListener(this);
        btnCauta.setOnClickListener(this);
        btnAdaugaInCos.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnCauta:
                loadProduct();
              //  String idProdus = String.valueOf(etIdProdus.getText());
                break;

            case R.id.btnAdaugaInCos:
                loadProduct();
               // System.out.println("BTNaDAUGAiNCOS Valoarea lui idProdus este "+ etIdProdus.toString() +" VS "+ etIdProdus.getText().toString()+"end;");
                creazaLinieComanda(idComanda, etIdProdus.getText().toString(), etCantitateInStoc.getText().toString(),
                        etCantitateComandata.getText().toString(), etPretProdus.getText().toString());
                //Trimitem idClient ca paramentru in ecranul urmator:
                Intent intent = new Intent(this, VeziComandaActivity.class);
                intent.putExtra("ipURL", ipURL);
                intent.putExtra("idClient", idClient);
                intent.putExtra("numarComanda", numarComanda);
                intent.putExtra("idComanda", idComanda);

                this.startActivity(intent);
                break;

            case R.id.btnScaneazaAltProdus:
                loadProductFromScanner();

                break;
            case R.id.txtNrComanda:
                Intent intent2 = new Intent(this, VeziComandaActivity.class);
                intent2.putExtra("ipURL", ipURL);
                intent2.putExtra("idClient", idClient);
                intent2.putExtra("numarComanda", numarComanda);
                intent2.putExtra("idComanda", idComanda);

                this.startActivity(intent2);
                break;
        }
    }

    private void loadProductFromScanner(){
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");

        intent.putExtra("SCAN_MODE", "PRODUCT_MODE");//for Qr code, its "QR_CODE_MODE" instead of "PRODUCT_MODE"
        intent.putExtra("SAVE_HISTORY", false);//this stops saving ur barcode in barcode scanner app's history
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT"); //this is the result
                //Toast.makeText(this, "Scan result este "+contents, Toast.LENGTH_SHORT).show();
                String idProdus = contents;
                btnCauta.callOnClick();


            } else
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Eroare la scanare", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadProduct(){
        String idProdus =  etIdProdus.getText().toString();
        System.out.println("Valoarea lui idProdus este "+ idProdus);
                //etIdProdus.toString();

        RequestParams params = new RequestParams();
        params.put("idProdus", idProdus);

        InvokeWS(params);
    }

    private void creazaLinieComanda(String idComanda, String idProdus, String sCantitateInStoc, String sCantitateComandata, String pretProdus){


        idProdus = etIdProdus.toString();
        sCantitateComandata = etCantitateComandata.getText().toString();
        sCantitateInStoc = etCantitateInStoc.getText().toString();

        int cantitateComandata = parseInt(etCantitateComandata.getText().toString());
        int cantitateInStoc = parseInt(etCantitateInStoc.getText().toString());
        pretProdus = etPretProdus.getText().toString();

        if((cantitateComandata<=cantitateInStoc)&&(cantitateComandata>=0)) {

            String slccValoare ="0";

        RequestParams params = new RequestParams();
        params.put("idComandaClient", idComanda );
        params.put("idProdus", etIdProdus.getText().toString());
        params.put("cantitate", sCantitateComandata);
        params.put("pretProdus", pretProdus);
        params.put("lccValoare", slccValoare);

        InvokeWSLC(params);
        }else{
            Toast.makeText(this, "Cantitate de comanda invalida", Toast.LENGTH_SHORT).show();
        }
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
        String URL = "http://"+ipURL+":8080/TestW/rest/produs/loadprodus";

        final ScaneazaProdusActivity that = this;
        client.get(URL, params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                Log.i("", response);
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    Log.i("", " Am primit raspuns");
                    if (obj.getBoolean("existaProdus")) {
                        that.etDenumireProdus.setText(obj.getString("denumireProdus"));
                        that.etPretProdus.setText(obj.getString("pretProdus"));
                        that.etCantitateInStoc.setText(obj.getString("cantitateInStoc"));

                        if(obj.getString("cantitateInStoc").equals("0")) {
                            Toast.makeText(getApplicationContext(), "Nu sunt produse in stoc", Toast.LENGTH_LONG).show();
                            that.btnAdaugaInCos.setText("Produs Indisponibil");
                            that.btnAdaugaInCos.setClickable(false);
                        }else{
                            that.btnAdaugaInCos.setText("Adauga In cos");
                            that.btnAdaugaInCos.setClickable(true);
                        }
                    }
                    // Else display error message
                    else {
                        //  errorMsg.setText(obj.getString("error_msg"));
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    //   Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "Nu exista produsul in magazin", Toast.LENGTH_LONG).show();
                        that.etDenumireProdus.setText("");
                        that.etPretProdus.setText("");
                        that.etCantitateInStoc.setText("");
                        that.btnAdaugaInCos.setText("Produs Indisponibil");
                        that.btnAdaugaInCos.setClickable(false);
                    e.printStackTrace();
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
    public void InvokeWSLC(RequestParams params){

        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();

        String URL = "http://"+ipURL+":8080/TestW/rest/liniecomanda/creareliniecomanda";

        final ScaneazaProdusActivity that = this;
        client.get(URL, params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                Log.i("", response);

                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    Log.i("", " Am primit raspuns");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                       Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                }
                Intent intent = new Intent(that, VeziComandaActivity.class);
                intent.putExtra("ipURL", ipURL);
                intent.putExtra("idComanda", idComanda);
                intent.putExtra("numarComanda", numarComanda);
                intent.putExtra("idClient", idClient);
                that.startActivity(intent);
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
