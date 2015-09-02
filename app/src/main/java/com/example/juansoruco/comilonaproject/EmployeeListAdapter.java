package com.example.juansoruco.comilonaproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.juansoruco.comilonaproject.employee.Employee;

import java.util.List;

/**
 * Created by juan.soruco on 31/08/2015.
 */
public class EmployeeListAdapter extends ArrayAdapter {

    public EmployeeListAdapter(Context context, List objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.employees_list_item, null);
        }
        TextView fullName = (TextView) convertView.findViewById(R.id.fullName);
        TextView status = (TextView) convertView.findViewById(R.id.status);

        Employee employee = (Employee) getItem(position);
        fullName.setText(employee.getFullName());
        status.setText(employee.getStatus());

        return convertView;
    }
}
