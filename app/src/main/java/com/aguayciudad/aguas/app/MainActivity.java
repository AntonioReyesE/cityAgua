package com.aguayciudad.aguas.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {

    public final static String EXTRA_MESSAGE_NEW = "com.example.antonioreyes.aguas.messageNew";

    ListView list;
    String[] web = {
            "FALTA DE AGUA",
            "CONTAMINACIÓN DEL AGUA",
            "INUNDACIONES",
            "ENCHARCAMIENTOS",
            "DESLAVES",
            "HUNDIMIENTOS Y/O AGRIETAMIENTOS",
            "FUGAS DE AGUA EN EXTERIORES",
            "INFRAESTRUCTURA EN RIESGO"
    } ;
    Integer[] imageId = {
            R.drawable.boton_reportar_on_01,
            R.drawable.boton_reportar_on_02,
            R.drawable.boton_reportar_on_03,
            R.drawable.boton_reportar_on_04,
            R.drawable.boton_reportar_on_06,
            R.drawable.boton_reportar_on_07,
            R.drawable.boton_reportar_on_05,
            R.drawable.boton_reportar_on_08
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomList adapter = new
                CustomList(MainActivity.this, web, imageId);

        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //magic will happen -> case which will send you to the correct report

                //Toast.makeText(MainActivity.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), ReportActivity.class);
                intent.putExtra("type", position);
                startActivity(intent);
            }
        });
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
