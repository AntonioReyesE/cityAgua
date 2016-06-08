package com.aguayciudad.aguas.app;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

/**
 * Created by Antonio on 05/06/16.
 */
public class Report extends GenericJson {
    @Key("_id")
    private String id;
    @Key
    private String Tipo_Reporte;
    @Key
    private String Fecha;
    @Key
    private String Hora;
    @Key
    private double Latitud;
    @Key
    private double Longitud;
    @Key
    private String Comentario;
    @Key
    private String Tipo;
    @Key
    private String Nivel_inundacion;
    @Key
    private String ImagenFile;

    public Report(){}  //GenericJson classes must have a public empty constructor

    public String getId(){
        return this.id;
    }

    public String getTipo_Reporte() {
        return Tipo_Reporte;
    }

    public String getFecha() {
        return Fecha;
    }

    public String getHora() {
        return Hora;
    }

    public double getLatitud() {
        return Latitud;
    }

    public double getLongitud() {
        return Longitud;
    }

    public String getComentario() {
        return Comentario;
    }

    public String getTipo() {
        return Tipo;
    }

    public String getImagenFile() {
        return ImagenFile;
    }

    public String getNivel_inundacion() {
        return Nivel_inundacion;
    }
}