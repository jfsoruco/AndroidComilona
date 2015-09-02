package com.example.juansoruco.comilonaproject;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.juansoruco.comilonaproject.employee.Employee;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private EmployeeListAdapter employeeListAdapter;
    private ListView listView;
    String[] employeesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        employeesList = getResources().getStringArray(R.array.employee_names);
        ArrayList<Employee> employees = new ArrayList<Employee>();
        for (int i = 0; i < employeesList.length; i++) {
            Employee employee = new Employee(i, employeesList[i], "A");
            employees.add(employee);
        }
        employeeListAdapter = new EmployeeListAdapter(this, employees);

        listView = (ListView)findViewById(R.id.employees_list);
        listView.setAdapter(employeeListAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
