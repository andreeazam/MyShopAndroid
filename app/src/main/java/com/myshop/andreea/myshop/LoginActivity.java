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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{
    Button bLogin;
    EditText etUsername, etParola;
    TextView tvInregistrareLink;
    String idClient = "";
    String ipURL = "10.10.6.163";

    static final long serialVersionUID = 42L;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etParola = (EditText) findViewById(R.id.etParola);
        etUsername = (EditText) findViewById(R.id.etUsername);
        tvInregistrareLink = (TextView) findViewById(R.id.tvInregistrareLink);
        bLogin = (Button) findViewById(R.id.bLogin);

        bLogin.setOnClickListener(this);
        tvInregistrareLink.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String username = String.valueOf(etUsername.getText());
        String parola = String.valueOf(etParola.getText());

        switch(v.getId()){
            case R.id.bLogin:
                actionLogin();

                break;

            case R.id.tvInregistrareLink:
                Intent intent = new Intent(this, InregistrareActivity.class);
                intent.putExtra("ipURL", ipURL);
                startActivity(intent);
                break;
        }
    }

    private void actionLogin() {

        String username = etUsername.getText().toString();
        String parola = etParola.getText().toString();

         Client client = new Client();

        RequestParams params = new RequestParams();
        params.put("username", username);
        params.put("parola", parola);

        invokeWS(params);
    }

    /**
     * Method that performs RESTful webservice invocations
     *
     * @param params
     */
    public void invokeWS(RequestParams params) {
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        String URL = "http://"+ipURL.toString()+":8080/TestW/rest/client/loginclient";


        final LoginActivity that = this;
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
                    if (obj.getBoolean("existaClient")) {

                        that.idClient = obj.getString("idClient");
                        Toast.makeText(getApplicationContext(), "Bine ai venit "+ obj.getString("prenumeClient") +" "+obj.getString("numeClient"), Toast.LENGTH_LONG).show();
                        // Navigate to AcasaActivity()
                        Intent intent = new Intent(that, AcasaActivity.class);
                        //Trimitem idClient ca paramentru in ecranul urmator:
                        intent.putExtra("idClient", idClient);
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
                 //   Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "Date invalide!", Toast.LENGTH_LONG).show();

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

}
