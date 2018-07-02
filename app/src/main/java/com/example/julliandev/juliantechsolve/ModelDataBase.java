package com.example.julliandev.juliantechsolve;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Dictionary;

public class ModelDataBase extends SQLiteOpenHelper {
    public static final String nombre_bd="techandsolve";
    public static  final String TAG="CLASE_MODELO";

    public ModelDataBase(Context context) {
        super(context, nombre_bd, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE VUELOS (VUELOID INTEGER PRIMARY KEY, VUELO VARCHAR(20), HORA INTEGER(3))");
        sqLiteDatabase.execSQL("CREATE TABLE VUELOSREGISTRADOS (DESCRIPVUELOID INTEGER PRIMARY KEY, VUELO VARCHAR(20), HORA INTEGER(3), CNSUSUARIO INT(3), CNSVUELO INT(3))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean deleteDatosTabla(String tabla){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tabla,null,null);
        return true;
    }

    public Cursor existUser(String name, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM USUARIOS WHERE NOMBRE='"+name+"' AND PASSWORD='"+password+"'", null);
        return cursor;
    }

    public Cursor getVuelosUsuario(String cnsusuario){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM VUELOSREGISTRADOS WHERE CNSUSUARIO='"+cnsusuario+"'",null);
        return res;
    }

    public Cursor getVuelos(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM VUELOS",null);
        return res;
    }

    public long registrarVuelo(Dictionary<String, String> datos){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("VUELO", datos.get("VUELO"));
        valores.put("HORA", datos.get("HORA"));
        valores.put("CNSUSUARIO", datos.get("CNSUSUARIO"));
        long res = db.insert("VUELOSREGISTRADOS", null, valores);
        return res;
    }

    public long registrarVueloCedula(Dictionary<String, String> datos){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("VUELO", datos.get("VUELO"));
        valores.put("HORA", datos.get("HORA"));
        valores.put("CNSUSUARIO", datos.get("CEDULA"));
        valores.put("CNSVUELO", datos.get("CNSVUELO"));
        long res = db.insert("VUELOSREGISTRADOS", null, valores);
        return res;
    }

    public int validarRegistroFecha(String fecha){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM VUELOSREGISTRADOS WHERE HORA='"+fecha+"'", null);
        return cursor.getCount();
    }

    public boolean insertVuelo(Dictionary<String, String> datos){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("VUELO", datos.get("VUELO"));
        db.insert("VUELOS", null, valores);
        return true;
    }

    public int existRegistroVuelo(String idvuelo, String hora){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM VUELOSREGISTRADOS WHERE CNSVUELO='"+idvuelo+"' AND HORA='"+hora+"'", null);
        return cursor.getCount();
    }

}
