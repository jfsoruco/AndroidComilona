package com.example.juansoruco.comilonaproject.menu;

/**
 * Created by milton.torrico on 08/09/2015.
 */
public class MenuDia {

    private int _id;
    private String description;
    private int nro_voto;
    private boolean checked;
    private String votoUsuario;

    public MenuDia(){}

    public MenuDia(int _id, String description, int nro_voto){
        this._id = _id;
        this.description = description;
        this.nro_voto = nro_voto;
        this.checked = false;
        this.votoUsuario = "";
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNro_voto() {
        return nro_voto;
    }

    public void setNro_voto(int nro_voto) {
        this.nro_voto = nro_voto;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getVotoUsuario() {
        return votoUsuario;
    }

    public void setVotoUsuario(String votoUsuario) {
        this.votoUsuario = votoUsuario;
    }
}

