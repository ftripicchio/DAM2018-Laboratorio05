package ar.edu.utn.frsf.isi.dam.laboratorio05.modelo;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

@Entity
public class Reclamo {


    public enum TipoReclamo { VEREDAS,SEMAFOROS,ILUMINACION,CALLE_EN_MAL_ESTADO,RESIDUOS,RUIDOS_MOLESTOS,OTRO}

    @PrimaryKey(autoGenerate = true)
    private long id;
    private Double latitud;
    private Double longitud;
    private String reclamo;
    private String email;
    private String fotoPath;
    private String audioPath;
    @TypeConverters(TipoReclamoConverter.class)
    private TipoReclamo tipo;

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getReclamo() {
        return reclamo;
    }

    public void setReclamo(String reclamo) {
        this.reclamo = reclamo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TipoReclamo getTipo() {
        return tipo;
    }

    public void setTipo(TipoReclamo tipo) {
        this.tipo = tipo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFotoPath() {
        return fotoPath;
    }

    public void setFotoPath(String fotoPath) {
        this.fotoPath = fotoPath;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }
}
