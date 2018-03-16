package com.myshop.andreea.myshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ComandaNouaActivity extends AppCompatActivity implements View.OnClickListener{

        Button bScaneazaProdus, bAlegeDinLista;
        String idClient = "";
        String idComanda = "";
        String numarComanda = "";
        String ipURL = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comanda_noua);

        Bundle bundle = getIntent().getExtras();
        idClient = bundle.getString("idClient");
        idComanda = bundle.getString("idComanda");
        numarComanda = bundle.getString("numarComanda");
        ipURL = bundle.getString("ipURL");


        bScaneazaProdus = (Button) findViewById(R.id.bScaneazaProdus);

        bScaneazaProdus.setOnClickListener(this);

        Toast.makeText(getApplicationContext(), "Am primit clientul "+idClient + " Cu comanda "+idComanda, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bScaneazaProdus:
                Intent intent = new Intent(this, ScaneazaProdusActivity.class);
                intent.putExtra("idClient", idClient);
                intent.putExtra("idComanda", idComanda);
                intent.putExtra("numarComanda", numarComanda);
                intent.putExtra("ipURL", ipURL);
                startActivity(intent);
                break;
        }
    }
}
