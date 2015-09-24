package com.example.juansoruco.comilonaproject;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.juansoruco.comilonaproject.weeklyOrder.WeeklyOrder;
import com.example.juansoruco.comilonaproject.weeklyOrder.WeeklyOrderLogic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WeeklyOrderActivity extends ActionBarActivity {

    String[] menuOptionsList;

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

        final WeeklyOrder weeklyOrder = WeeklyOrderLogic.getWeeklyOrder(weekly_order_id, this);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>> " + weeklyOrder.toString());
        TextView dateView = (TextView) findViewById(R.id.w_o_date);
        TextView groupView = (TextView) findViewById(R.id.w_o_group);
        TextView responsibleView = (TextView) findViewById(R.id.w_o_responsible);

        final EditText menu1View = (EditText) findViewById(R.id.w_o_menu1);
        final EditText menu2View = (EditText) findViewById(R.id.w_o_menu2);
        final EditText menu3View = (EditText) findViewById(R.id.w_o_menu3);
        final EditText menu4View = (EditText) findViewById(R.id.w_o_menu4);

        dateView.setText(df.format(weeklyOrder.getDate()));
        groupView.setText(weeklyOrder.getGroupFullname());
        responsibleView.setText(weeklyOrder.getResponsibleFullname());

        menu1View.setText(weeklyOrder.getMenu1() != null && weeklyOrder.getMenu1().equals("") ? defaultMenu : weeklyOrder.getMenu1());
        menu2View.setText(weeklyOrder.getMenu2() != null && weeklyOrder.getMenu2().equals("") ? defaultMenu : weeklyOrder.getMenu2());
        menu3View.setText(weeklyOrder.getMenu3() != null && weeklyOrder.getMenu3().equals("") ? defaultMenu : weeklyOrder.getMenu3());
        menu4View.setText(weeklyOrder.getMenu4() != null && weeklyOrder.getMenu4().equals("") ? defaultMenu : weeklyOrder.getMenu4());

        final Button w_o_save_button = (Button) findViewById(R.id.w_o_save_button);
        final Button w_o_complete_button = (Button) findViewById(R.id.w_o_complete_button);

        w_o_save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weeklyOrder.setMenu1(menu1View.getText().toString());
                weeklyOrder.setMenu2(menu2View.getText().toString());
                weeklyOrder.setMenu3(menu3View.getText().toString());
                weeklyOrder.setMenu4(menu4View.getText().toString());

                System.out.println(">><save>>>> " + weeklyOrder.toString());

                WeeklyOrderLogic.saveOrder(weeklyOrder, WeeklyOrderActivity.this);

                Log.d("boton save", "boton presionado");
            }
        });

        w_o_complete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weeklyOrder.setMenu1(menu1View.getText().toString());
                weeklyOrder.setMenu2(menu2View.getText().toString());
                weeklyOrder.setMenu3(menu3View.getText().toString());
                weeklyOrder.setMenu4(menu4View.getText().toString());

                System.out.println(">><complete>>>> " + weeklyOrder.toString());

                WeeklyOrderLogic.completeMenu(weeklyOrder, WeeklyOrderActivity.this);

                Log.d("boton complete", "boton presionado");
            }
        });
    }

    public void fillMenuOptions() {
        /*
        employeesList = getResources().getStringArray(R.array.employee_names);
        ArrayList<Employee> employees = new ArrayList<Employee>();
        for (int i = 0; i < employeesList.length; i++) {
            Employee employee = new Employee(i, employeesList[i], "A");
            employees.add(employee);
        }
        employeeListAdapter = new EmployeeListAdapter(this, employees);

        listView = (ListView)findViewById(R.id.employees_list);
        listView.setAdapter(employeeListAdapter);
         */
        menuOptionsList = getResources().getStringArray(R.array.menu_options);

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
