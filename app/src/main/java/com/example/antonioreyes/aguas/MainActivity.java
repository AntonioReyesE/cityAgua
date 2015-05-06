package com.example.antonioreyes.aguas;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;


public class MainActivity extends Activity {

    public final static String EXTRA_MESSAGE_NEW = "com.example.antonioreyes.aguas.messageNew";

    ListView list;
    String center;
    String[] web = {
            "FALTA DE AGUA",
            "CONTAMINACIÓN DEL AGUA",
            "INUNDACIONES",
            "ENCHARCAMIENTOS",
            "FUGAS DE AGUA EN EXTERIORES",
            "DESLAVES",
            "HUNDIMIENTOS Y/O AGRIETAMIENTOS",
            "INFRAESTRUCTURA EN RIESGO"
    } ;
    Integer[] imageId = {
            R.drawable.fa,
            R.drawable.ac,
            R.drawable.in,
            R.drawable.en,
            R.drawable.fu,
            R.drawable.de,
            R.drawable.so,
            R.drawable.ir
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        center = intent.getStringExtra(MapsActivity.EXTRA_MESSAGE);

        CustomList adapter = new
                CustomList(MainActivity.this, web, imageId);

        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //magic will happen -> case which will send you to the correct report
                Toast.makeText(MainActivity.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
                if(position == 0){
                    faltaAgua(view);
                }
                else if(position == 1){
                    contaminacionAgua(view);
                }
                else if(position == 2){
                    inundaciones(view);
                }
                else if(position == 3){
                    encharcamientos(view);
                }
                else if(position == 4){
                    fugaAgua(view);
                }
                else if(position == 5){
                    deslaves(view);
                }
                else if(position == 6){
                    hundimientos(view);
                }
                else if(position == 7){
                    infraestructura(view);
                }
            }
        });
    }

    public void faltaAgua(View view){
        Intent intent = new Intent(this, FaltaAgua.class);
        intent.putExtra(EXTRA_MESSAGE_NEW, center+" ");
        startActivity(intent);
        this.finish();
    }

    public void contaminacionAgua(View view){
        Intent intent = new Intent(this, ContaminacionAgua.class);
        intent.putExtra(EXTRA_MESSAGE_NEW, center+" ");
        startActivity(intent);
        this.finish();
    }

    public void inundaciones(View view){
        Intent intent = new Intent(this, Inundaciones.class);
        intent.putExtra(EXTRA_MESSAGE_NEW, center+" ");
        startActivity(intent);
        this.finish();
    }

    public void encharcamientos(View view){
        Intent intent = new Intent(this, Encharcamientos.class);
        intent.putExtra(EXTRA_MESSAGE_NEW, center+" ");
        startActivity(intent);
        this.finish();
    }

    public void fugaAgua(View view){
        Intent intent = new Intent(this, FugaAgua.class);
        intent.putExtra(EXTRA_MESSAGE_NEW, center+" ");
        startActivity(intent);
        this.finish();
    }

    public void deslaves(View view){
        Intent intent = new Intent(this, Deslaves.class);
        intent.putExtra(EXTRA_MESSAGE_NEW, center+" ");
        startActivity(intent);
        this.finish();
    }

    public void hundimientos(View view){
        Intent intent = new Intent(this, Hundimientos.class);
        intent.putExtra(EXTRA_MESSAGE_NEW, center+" ");
        startActivity(intent);
        this.finish();
    }

    public void infraestructura(View view){
        Intent intent = new Intent(this, Infraestructura.class);
        intent.putExtra(EXTRA_MESSAGE_NEW, center+" ");
        startActivity(intent);
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void returnToMainMap(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
        this.finish();
    }
}
