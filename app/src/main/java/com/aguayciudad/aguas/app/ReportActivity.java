package com.aguayciudad.aguas.app;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class ReportActivity extends Activity {

    private final int RESULT_LOAD_IMAGE = 10;
    private int type;

    private ImageView titleIM;
    private EditText dateTV;
    private EditText timeTV;
    private EditText placeTV;
    private EditText commentTV;
    private Spinner typeSP;
    private TextView typeLB;

    private ImageView imagePreviewIV;


    private Calendar calendar;
    private int year, month, day, hour, minute;

    private ParseObject p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Intent intent = getIntent();
        type = intent.getIntExtra("type", -1);

        titleIM = (ImageView) findViewById(R.id.title_image);

        dateTV = (EditText) findViewById(R.id.dateTV);
        timeTV = (EditText) findViewById(R.id.timeTV);
        placeTV = (EditText) findViewById(R.id.placeTV);
        placeTV.setText(Globals.latitude + ", " + Globals.longitude);
        commentTV = (EditText) findViewById(R.id.commentTV);
        typeSP = (Spinner) findViewById(R.id.typeSP);
        typeLB = (TextView) findViewById(R.id.typeLB);
        imagePreviewIV = (ImageView) findViewById(R.id.imagePreview);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        setDate();

        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        setTime();

        switch (type){
            case 0: case 3: case 4: case 5:
                typeSP.setVisibility(View.GONE);
                typeLB.setVisibility(View.GONE);
                break;
            case 1: case 2: case 6: case 7:
                typeSP.setVisibility(View.VISIBLE);
                typeLB.setVisibility(View.VISIBLE);
                setSpinnerAdapter(type);
                break;
        }

        setTitleImage();

        p = new ParseObject("Report");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_falta_agua, menu);
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

    /*
        DIALOG FOR DATE AND TIME
     */

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 1) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }else if(id == 2){
            return new TimePickerDialog(this, myTimeListener, hour, minute, false);
        }
        return null;
    }

    /*
        DATE
     */

    private DatePickerDialog.OnDateSetListener myDateListener
            = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker arg0, int y, int m, int d) {
            year = y;
            month = m;
            day = d;
            setDate();
        }
    };

    public void setDate(){
        dateTV.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/")
                .append(year));
    }

    public void showDateWidget(View v){
        showDialog(1);
    }

    /*
        TIME
     */

    private TimePickerDialog.OnTimeSetListener myTimeListener =  new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int h, int m) {
            hour = h;
            minute = m;
            setTime();
        }
    };

    public void setTime(){
        String min_tmp;
        if(minute < 10 && minute > 0){
            min_tmp = "0" + String.valueOf(minute);
        } else if (minute == 0){
            min_tmp = "00";
        }else{
            min_tmp = String.valueOf(minute);
        }
        timeTV.setText(new StringBuilder().append(hour).append(":").append(min_tmp));
    }

    public void showTimeWidget(View v){
        showDialog(2);
    }

    /*
        PARSE
     */

    public void saveReport(View v){

        switch (type){
            case 0:
                p.put("Tipo_Reporte", "Falta de agua");
                break;
            case 1:
                p.put("Tipo_Reporte", "Agua contaminada");
                break;
            case 2:
                p.put("Tipo_Reporte", "Inundaciones");
                break;
            case 3:
                p.put("Tipo_Reporte", "Encharcamientos");
                break;
            case 6:
                p.put("Tipo_Reporte", "Fugas de agua");
                break;
            case 4:
                p.put("Tipo_Reporte", "Deslaves");
                break;
            case 5:
                p.put("Tipo_Reporte", "Socavamientos");
                break;
            case 7:
                p.put("Tipo_Reporte", "Infraestructura");
                break;
            default:
                p.put("Tipo_Reporte", "Desconocido");
        }

        p.put( "Fecha", dateTV.getText().toString() );
        p.put( "Hora", timeTV.getText().toString() );
        p.put( "Latitud", Globals.latitude );
        p.put( "Longitud", Globals.longitude );
        p.put( "Comentario", commentTV.getText().toString() );

        switch (type){
            case 1: case 2: case 6: case 7:
                p.put( "Tipo", typeSP.getSelectedItem().toString() );
                break;
        }



        if(imagePreviewIV.getDrawable() != null){
            Bitmap bitmap = ((BitmapDrawable) imagePreviewIV.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, stream);
            byte[] image = stream.toByteArray();

            ParseFile file = new ParseFile("image.png", image);
            file.saveInBackground();

            p.put("ImageFile", file);
        }

        p.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    //Success
                    //Toast.makeText(getApplicationContext(), "Save on Parse", Toast.LENGTH_SHORT).show();
                } else {
                    //Error
                    //Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Intent intent = new Intent(this, Confirmation.class);
        startActivity(intent);
        this.finish();
    }

    public void regresaReportes3(View view){
        //Intent intent = new Intent(this, MainActivity.class);
        //startActivity(intent);
        this.finish();
    }

    public void returnToMap1(View view){
        Intent intent = new Intent(this, MapsActivity2.class);
        //startActivity(intent);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        Toast.makeText(this, requestCode + " " + resultCode, Toast.LENGTH_LONG);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK){
            Globals.latitude = data.getDoubleExtra("latitude", 1);
            Globals.longitude = data.getDoubleExtra("longitude", 1);
            updateLocation();
        }else if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK){
            Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
            imagePreviewIV.setImageBitmap(bitmap);
            /*
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            Toast.makeText(this, picturePath, Toast.LENGTH_LONG);
            cursor.close();

            imagePreviewIV.setImageBitmap( BitmapFactory.decodeFile(picturePath) );
            */
        }else{
            Toast.makeText(this, "Error", Toast.LENGTH_LONG);
        }
    }

    public void updateLocation(){
        placeTV.setText(Globals.latitude + ", " + Globals.longitude);
    }

    public void selectImage(View v){
        //Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(i, RESULT_TAKE_PICTURE);

        //Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //startActivityForResult(i, RESULT_LOAD_IMAGE);

        Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent, RESULT_LOAD_IMAGE);
    }


    public void setSpinnerAdapter(int type){

        String[] values = new String[0];
        switch (type){
            case 1:
                values = new String[]{"Basura en vialidades",
                    "Basura en arroyos, ríos, lagos o presas",
                    "Desechos orgánicos",
                    "Animales muertos",
                    "Grasas y/o aceites",
                    "Sustancias químicas"
                };
                break;
            case 2:
                typeLB.setText("Nivel");
                values = new String[]{ "Calle: inundación baja",
                    "Banqueta: inundación media",
                    "Casa: inundación alta"
                };
                break;
            case 6:
                values = new String[]{ "Agua potable, ojo de agua o venero",
                    "Agua residual"
                };
                break;
            case 7:
                values = new String[]{ "Infraestructura con riesgo de colapso",
                    "Pozos de visita o registros abiertos",
                    "Bocas de tormenta inseguras"
                };
                break;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                values);

        typeSP.setAdapter(adapter);
    }

    public void setTitleImage(){
        switch (type){
            case 0:
                titleIM.setImageResource(R.drawable.boton_reportar_on_01);
                break;
            case 1:
                titleIM.setImageResource(R.drawable.boton_reportar_on_02);
                break;
            case 2:
                titleIM.setImageResource(R.drawable.boton_reportar_on_03);
                break;
            case 3:
                titleIM.setImageResource(R.drawable.boton_reportar_on_04);
                break;
            case 6:
                titleIM.setImageResource(R.drawable.boton_reportar_on_05);
                break;
            case 4:
                titleIM.setImageResource(R.drawable.boton_reportar_on_06);
                break;
            case 5:
                titleIM.setImageResource(R.drawable.boton_reportar_on_07);
                break;
            case 7:
                titleIM.setImageResource(R.drawable.boton_reportar_on_08);
                break;
        }
    }

}