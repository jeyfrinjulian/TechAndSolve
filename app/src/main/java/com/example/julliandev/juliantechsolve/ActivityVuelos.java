package com.example.julliandev.juliantechsolve;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class ActivityVuelos extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public String vuelo_elegido = "";
    public String cnsuseuario = "";
    public int edad=0;
    public long idvuelo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        insertFly();
        setContentView(R.layout.activity_vuelos);
        final RecyclerView rc_v=(RecyclerView)findViewById(R.id.recycle_layout);
        configurarRecycle(rc_v);

        mAdapter = new AdapterRecycler(this,getFly());
        rc_v.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        rc_v.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                TextView lbl = view.findViewById(R.id.descripcionvuelo);
                vuelo_elegido = lbl.getText().toString();
                idvuelo = rc_v.getAdapter().getItemId(position);
                openDialog();
            }

        }));
        rc_v.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.consultar) {
            //vuelosRegistradosDialog();
            vuelosRegistradosDialogCedula();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void vuelosRegistradosDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflate = this.getLayoutInflater();
        View view = inflate.inflate(R.layout.activity_vuelos, null);

        RecyclerView rc_v = (RecyclerView)view.findViewById(R.id.recycle_layout);
        configurarRecycle(rc_v);
        mAdapter = new AdapterRecycler(this,getVuelosRegistrados(cnsuseuario));
        rc_v.setAdapter(mAdapter);

        AlertDialog dialog = builder.create();
        dialog.setView(view);
        dialog.show();
    }

    public void vuelosRegistradosDialogCedula(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflate = this.getLayoutInflater();
        View view = inflate.inflate(R.layout.activity_vuelos_cedula, null);

        final RecyclerView rc_v = (RecyclerView)view.findViewById(R.id.recycle_layout);
        final EditText txtbuscarvuelo = (EditText)view.findViewById(R.id.txtbuscarvuelo);
        Button btnbuscar = (Button) view.findViewById(R.id.btnbuscarvuelo);

        configurarRecycle(rc_v);
        btnbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Dictionary<String,String>> vuelos= getVuelosRegistrados(txtbuscarvuelo.getText().toString());
                mAdapter = new AdapterRecycler(view.getContext(),vuelos);
                rc_v.setAdapter(mAdapter);
                if(vuelos.size()==0){
                    Toast.makeText(view.getContext(),"No hay vuelos registrados para la cedula: "+txtbuscarvuelo.getText().toString(),Toast.LENGTH_LONG).show();
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setView(view);
        dialog.show();
    }

    public void configurarRecycle(RecyclerView rc_v){
        rc_v.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rc_v.setLayoutManager(layoutManager);

    }

    public void openDialog(){
        final Dictionary<String, String> datos = new Hashtable<String, String>();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.formreservavuelo,null);
        TextView lbltitle = (TextView)view.findViewById(R.id.lbltitlevuelo);

        lbltitle.setText(lbltitle.getText()+ vuelo_elegido + idvuelo);

        final Spinner ddlhorarios = (Spinner)view.findViewById(R.id.ddlhorarios);
        final EditText txtedad = (EditText)view.findViewById(R.id.txtededformvuelo);
        final EditText txtcedula = (EditText)view.findViewById(R.id.txtcedulaformvuelo);
        final TextView costo = (TextView)view.findViewById(R.id.lblcostovuelo);

        String[] turnos={"03-07-2018 09:00","12-07-2018 12:00","13-07-2018 09:30","15-07-2018 13:00","01-08-2018 14:00"};
        ArrayAdapter<String> turnos_adapter;
        turnos_adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,turnos);
        ddlhorarios.setAdapter(turnos_adapter);


        Button btn_cancelar_vuelo = (Button)view.findViewById(R.id.btncancelarregvuelos);
        Button btn_guardar_vuelo = (Button)view.findViewById(R.id.btnregistrovuelo);
        final AlertDialog dialog = builder.create();
        dialog.setView(view);

        ddlhorarios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String horario = (String)adapterView.getItemAtPosition(i);
                Utils utils = new Utils();
                boolean esfindesemana = utils.esFinDeSemana(horario);
                int validarhora = utils.validarHora(horario);
                if (esfindesemana){
                    costo.setText("Es dia "+utils.getNombreDia(horario)+", el costo aumenta.");
                }else{
                    if(validarhora<=12){
                        costo.setText(" Los vuelos en la mañana el costo aumenta");
                    }else{
                        costo.setText("Los vuelos en la tarde son mas economicos");
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*btn_guardar_vuelo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datos.put("VUELO",vuelo_elegido);
                datos.put("HORA",ddlhorarios.getSelectedItem().toString());

                if (edad>18){
                    long res = resgistrarVuelo(datos);
                    if(res!=-1){
                        Toast.makeText(view.getContext(),"Vuelo Registrado",Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                }else{
                    Toast.makeText(view.getContext(),"No fue posible registrar el vuelo, no tienes la edad suficiente permitida.",Toast.LENGTH_LONG).show();
                }
            }
        });*/
        btn_guardar_vuelo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModelDataBase db = new ModelDataBase(getApplicationContext());
                String edaduser = txtedad.getText().toString();
                String cedulauser = txtcedula.getText().toString();
                //int existevuelo = db.existRegistroVuelo(String.valueOf(idvuelo), ddlhorarios.getSelectedItem().toString());
                boolean existediavuelo = existeDiaVuelo(String.valueOf(idvuelo), ddlhorarios.getSelectedItem().toString(),cedulauser);
                if(existediavuelo){
                    final Dictionary<String, String> datoscedula = new Hashtable<String, String>();
                    if(!cedulauser.equals("") && !edaduser.equals("")){
                        datoscedula.put("CEDULA",cedulauser);
                        datoscedula.put("VUELO",vuelo_elegido);
                        datoscedula.put("HORA",ddlhorarios.getSelectedItem().toString());
                        datoscedula.put("CNSVUELO",String.valueOf(idvuelo));
                        if (Integer.valueOf(edaduser)>=18){
                            long res = resgistrarVueloCedula(datoscedula);
                            if(res!=-1){
                                Toast.makeText(view.getContext(),"Vuelo Registrado "+cedulauser,Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        }else{
                            Toast.makeText(view.getContext(),"No fue posible registrar el vuelo, no tienes la edad suficiente permitida.",Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(view.getContext(),"Por favor completar los campos",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(view.getContext(),"Ya tienes vuelo para esta fecha",Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_cancelar_vuelo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Utils().closeDialog(dialog);
            }
        });

        dialog.show();
    }

    private boolean existeDiaVuelo(String idvuelo, String horario, String cnsusuario) {
        ModelDataBase db = new ModelDataBase(this);
        Toast.makeText(this,cnsusuario,Toast.LENGTH_LONG).show();
        boolean res = db.existRegistroVuelo(idvuelo, horario, cnsusuario) == 0 ? true : false;
        return res;
    }

    public long resgistrarVueloCedula(Dictionary<String, String> datos){
        ModelDataBase db = new ModelDataBase(this);
        long res = db.registrarVueloCedula(datos);
        return res;
    }

    public List<Dictionary<String, String>> getVuelosRegistrados(String userid){
        ModelDataBase db = new ModelDataBase(this);
        Cursor vuelos_cursor = db.getVuelosUsuario(userid);
        vuelos_cursor.moveToFirst();
        List<Dictionary<String, String>> input = new ArrayList<Dictionary<String, String>>() {};
        while(!vuelos_cursor.isAfterLast()){
            Dictionary<String, String> infovuelo = new Hashtable<>();
            infovuelo.put("name",vuelos_cursor.getString(vuelos_cursor.getColumnIndex("VUELO")));
            infovuelo.put("vueloid",vuelos_cursor.getString(vuelos_cursor.getColumnIndex("DESCRIPVUELOID")));
            infovuelo.put("description","programación del vuelo: "+vuelos_cursor.getString(vuelos_cursor.getColumnIndex("HORA")));
            input.add(infovuelo);
            vuelos_cursor.moveToNext();
        }

        return input;
    }


    public List<Dictionary<String, String>> getFly(){
        ModelDataBase db = new ModelDataBase(this);
        Cursor vuelos_cursor = db.getVuelos();
        vuelos_cursor.moveToFirst();
        List<Dictionary<String, String>> input = new ArrayList<Dictionary<String, String>>() {};
        while(!vuelos_cursor.isAfterLast()){
            Dictionary<String, String> infovuelo = new Hashtable<>();
            infovuelo.put("name",vuelos_cursor.getString(vuelos_cursor.getColumnIndex("VUELO")));
            infovuelo.put("vueloid",vuelos_cursor.getString(vuelos_cursor.getColumnIndex("VUELOID")));
            infovuelo.put("description","vuelo disponible");
            input.add(infovuelo);
            vuelos_cursor.moveToNext();
        }

        return input;
    }

    public boolean insertFly(){
        Dictionary<String, String> vuelo = new Hashtable<>();
        ModelDataBase db = new ModelDataBase(this);
        db.deleteDatosTabla("VUELOS");
        for (int i=0; i<=30; i++){
            vuelo.put("VUELO", "VUELO CODIGO "+i);
            db.insertVuelo(vuelo);
        }
        return true;
    }
}
