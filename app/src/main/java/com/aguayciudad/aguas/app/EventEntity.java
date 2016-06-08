package com.aguayciudad.aguas.app;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

/**
 * Created by Antonio on 05/06/16.
 */
public class EventEntity extends GenericJson {
    @Key("_id")
    private String id;
    @Key
    private String Tipo_Reporte;
    @Key
    private String fecha;
    @Key
    private String hora;
    @Key
    private double latitud;
    @Key
    private double longitud;
    @Key
    private String comentario;
    @Key
    private String tipo;
    @Key
    private String imagen;

    public EventEntity(){}  //GenericJson classes must have a public empty constructor

    public String getId(){
        return this.id;
    }

    public String getTipo_Reporte() {
        return Tipo_Reporte;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public String getComentario() {
        return comentario;
    }

    public String getTipo() {
        return tipo;
    }

    public String getImagen() {
        return imagen;
    }
}