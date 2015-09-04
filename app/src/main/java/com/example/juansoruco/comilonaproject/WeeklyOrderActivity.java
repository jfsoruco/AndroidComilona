package com.example.juansoruco.comilonaproject;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.example.juansoruco.comilonaproject.weeklyOrder.WeeklyOrder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class WeeklyOrderActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_order);

        int weekly_order_id = this.getIntent().getIntExtra("WEEKLY_ORDER_ID", 0);

        TextView textView = (TextView)  findViewById(R.id.w_o_id);
        textView.setText(String.valueOf(weekly_order_id));
        Log.d("Id desde principal", Integer.toString(weekly_order_id));
        String defaultMenu = "Llenar Comilona";
        DateFormat df = new SimpleDateFormat("yyyy-mm-dd");

        // TODO recuperar el registro del weekly order y mostrar formulario para llenar/editar menu
        WeeklyOrder weeklyOrder = new WeeklyOrder(weekly_order_id, new Date(), 1, defaultMenu, defaultMenu, defaultMenu);
        weeklyOrder.setGroupFullname("Grupo Independiente de Comilona");
        weeklyOrder.setResponsibleFullname("Este eres tu");

        TextView dateView = (TextView) findViewById(R.id.w_o_date);
        TextView groupView = (TextView) findViewById(R.id.w_o_group);
        TextView responsibleView = (TextView) findViewById(R.id.w_o_responsible);

        EditText menu1View = (EditText) findViewById(R.id.w_o_menu1);
        EditText menu2View = (EditText) findViewById(R.id.w_o_menu2);
        EditText menu3View = (EditText) findViewById(R.id.w_o_menu3);

        dateView.setText(df.format(weeklyOrder.getDate()));
        groupView.setText(weeklyOrder.getGroupFullname());
        responsibleView.setText(weeklyOrder.getResponsibleFullname());

        // TODO botones de guardado y de completado

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_weekly_order, menu);
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
}
