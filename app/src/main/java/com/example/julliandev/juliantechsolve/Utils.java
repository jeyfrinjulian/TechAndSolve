package com.example.julliandev.juliantechsolve;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;

public class Utils {

    public int existUser(Context ctx, String name, String password){
        ModelDataBase db = new ModelDataBase(ctx);
        Cursor cursor = db.existUser(name, password);
        return cursor.getCount();

    }

    public Dictionary<String, String> userData(Context ctx, String name, String password){
        ModelDataBase db = new ModelDataBase(ctx);
        Cursor cursor = db.existUser(name, password);
        Dictionary<String, String> datos = new Hashtable<String, String>();
        if(cursor.getCount()==1){
            cursor.moveToFirst();
            String iduser = cursor.getString(cursor.getColumnIndex("USERID"));
            String edad = cursor.getString(cursor.getColumnIndex("EDAD"));
            String nombre = cursor.getString(cursor.getColumnIndex("NOMBRE"));
            datos.put("USERID",iduser);
            datos.put("EDAD",edad);
            datos.put("NOMBRE",nombre);
        }
        return datos;

    }

    public static void closeDialog(DialogInterface dialog){
        dialog.dismiss();
    }

    public static int validarHora(String fecha){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        try {
            Date date = sdf.parse(fecha);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int hora = cal.get(Calendar.HOUR_OF_DAY);

            return hora;
        } catch (ParseException e) {
            return -1;
        }

    }

    public static String getNombreDia(String fecha){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm");

        try {
            Date date = sdf.parse(fecha);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int day = cal.get(Calendar.DAY_OF_WEEK);

            System.out.println(cal.get(Calendar.HOUR_OF_DAY));
            switch (day) {
                case 1:
                    return "Domingo";
                case 2:
                    return "Lunes";
                case 3:
                    return "Martes";
                case 4:
                    return "Miercoles";
                case 5:
                    return "Jueves";
                case 6:
                    return "Viernes";
                case 7:
                    return "Sabado";

            }
            return "";

        } catch (ParseException e) {
            return "";
        }
    }

    public static boolean esFinDeSemana(String fecha){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm");

        try {
            Date date = sdf.parse(fecha);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int day = cal.get(Calendar.DAY_OF_WEEK);

            System.out.println(cal.get(Calendar.HOUR_OF_DAY));
            switch (day) {
                case 1:
                    break;
                case 7:
                    break;

                default:
                    return false;
            }
            return true;

        } catch (ParseException e) {
            return false;
        }
    }
}
