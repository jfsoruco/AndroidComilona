package com.example.juansoruco.comilonaproject;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.juansoruco.comilonaproject.data.ComilonaDbHelper;
import com.example.juansoruco.comilonaproject.data.GroupColumns;
import com.example.juansoruco.comilonaproject.data.GroupMemberColumns;
import com.example.juansoruco.comilonaproject.data.WeeklyOrderColumns;
import com.example.juansoruco.comilonaproject.employee.Employee;
import com.example.juansoruco.comilonaproject.group.Group;
import com.example.juansoruco.comilonaproject.menu.MenuDia;
import com.example.juansoruco.comilonaproject.weeklyOrder.WeeklyOrder;
import com.example.juansoruco.comilonaproject.weeklyOrder.WeeklyOrderLogic;

import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends ActionBarActivity {

    private EmployeeListAdapter employeeListAdapter;
    private ListView listView;
    String[] employeesList;
    private Group groupActive;
    private WeeklyOrder weeklyOrderActive;

    private MenuListAdapter menuListAdapter;
    String[] menusList;
    ArrayList<MenuDia> menus =null;
    MenuDia menuSelected = null;
    private Button btn_votar;
    private RadioButton rb_opcion;
    private int index = -1;
    private String usuario = "admin";

    private TextView responsable;
    private TextView fecha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println(">>>>>>>>>>>> OnCreate ");
        openDb();
        System.out.println(">>>>>>>>>>>>> Tras openDB");
        showContentsDb();
        System.out.println(">>>>>>>>>>>>> Tras showContents");
        // TODO: Establecer el grupo activo
        groupActive = new Group(1, "Mi Grupo de Comilona", "A");

        refreshResults();

/*        employeesList = getResources().getStringArray(R.array.employee_names);
        ArrayList<Employee> employees = new ArrayList<Employee>();
        for (int i = 0; i < employeesList.length; i++) {
            Employee employee = new Employee(i, employeesList[i], "A");
            employees.add(employee);
        }
        employeeListAdapter = new EmployeeListAdapter(this, employees);

        listView = (ListView)findViewById(R.id.employees_list);
        listView.setAdapter(employeeListAdapter);*/

    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshResults();
    }

    private void refreshResults() {
        startData(groupActive.get_id());

        responsable = (TextView)findViewById(R.id.txt_nombre_responsable);
        responsable.setText(weeklyOrderActive.getResponsibleFullname());
        //Fecha
        fecha = (TextView)findViewById(R.id.txt_value_fecha);
        fecha.setText(WeeklyOrderColumns.df.format(weeklyOrderActive.getDate()));

        scenarioIniciarMenu();
        scenarioVotacionAbierta();
        scenarioCerrarPedido();

        System.out.println(">>>>>>>>>>>>>>>>>>>>>><" + WeeklyOrderLogic.toFriday(new Date()));
    }

    private void openDb() {
        System.out.println(">>>>>>>>>>>> Start openDb");
        ComilonaDbHelper dbHelper = new ComilonaDbHelper(getBaseContext());
        System.out.println(">>>>>>>>>>> dbHelper " + dbHelper.toString());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        System.out.println(">>>>>>>>>>> db " + db.toString());
    }

    private void scenarioIniciarMenu() {
        LinearLayout linearLayout = (LinearLayout)this.findViewById(R.id.menu_w_o_poll_closed);
        linearLayout.setVisibility(LinearLayout.GONE);
        linearLayout = (LinearLayout)this.findViewById(R.id.menu_w_o_poll_open);
        linearLayout.setVisibility(LinearLayout.GONE);
        // Definicion del boton para llenar el menu del dia
        final Button w_o_menu_button = (Button) findViewById(R.id.w_o_menus_button);
        w_o_menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int weekly_order_id = weeklyOrderActive.get_id();

                Intent w_o_menu_intent = new Intent(getBaseContext(), WeeklyOrderActivity.class);
                w_o_menu_intent.putExtra("WEEKLY_ORDER_ID", weekly_order_id);
                startActivity(w_o_menu_intent);

            }
        });

        final Button w_o_resign_responsible_button = (Button) findViewById(R.id.w_o_resignResponsible_button);
        w_o_resign_responsible_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeeklyOrderLogic.resignResponsability(weeklyOrderActive, MainActivity.this);
            }
        });
    }

    private void scenarioVotacionAbierta() {

        final Button w_o_complete_poll_button = (Button) findViewById(R.id.w_o_completePoll_button);
        w_o_complete_poll_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeeklyOrderLogic.completePoll(weeklyOrderActive, MainActivity.this);
            }
        });

        // menus
        //menusList = getResources().getStringArray(R.array.menu_options);
        menus = WeeklyOrderLogic.getMenusList(weeklyOrderActive);
        /*menus = new ArrayList<MenuDia>();
        for (int i = 0; i < menusList.length; i++) {
            //Log.i(null,menusList[i].toString());
            MenuDia menu = new MenuDia(i, (i+1)+"."+menusList[i], 0);
            //Log.i(null,""+menu.get_id()+" "+menu.getDescription()+" "+menu.getNro_voto());
            //System.out.println(""+menu.get_id()+" "+menu.getDescription()+" "+menu.getNro_voto());
            menus.add(menu);
        }*/

        //menuListAdapter = new MenuListAdapter(this, menus,0);
        if(menuListAdapter!= null){
            menuListAdapter.clear();
        }

        menuListAdapter = new MenuListAdapter(this, menus);
        listView = (ListView)findViewById(R.id.employees_list);
        listView.setAdapter(menuListAdapter);


        //---MRT
        eligeOpcion(listView);
        votarOpcionMenu();

    }

    public void eligeOpcion(ListView listView) {
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RadioButton rb = (RadioButton) view.findViewById(R.id.rb_option);
                //System.out.println("################################  onItemClick: "+i);
                MenuDia m = (MenuDia) menus.get(i);
                menuSelected = m;
                index = i;//position
                System.out.println("################################  MenuDia: ID: " + m.get_id());
                System.out.println("################################  MenuDia: Description: " + m.getDescription());
                System.out.println("################################  MenuDia: Voto: " + m.getNro_voto());

                if (!rb.isChecked()) {
                    for (MenuDia m1 : menus) {
                        m1.setChecked(false);
                    }
                    menus.get(i).setChecked(true);
                    menuListAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void votarOpcionMenu() {
        btn_votar = (Button)findViewById(R.id.btn_votar);
        btn_votar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!usuario.equalsIgnoreCase(menuSelected.getVotoUsuario())) {
                    //System.out.println("---VVVVVVVVVVVVVVVVVVVVVVVV  EVENTO BOTON VOTAR");
                    //System.out.println("VVVVVVVVVVVVVVVVVVVVVVVV  MenuDia: ID: "+menuSelected.get_id());
                    //System.out.println("################################  MenuDia: Description: "+menuSelected.getDescription());
                    //System.out.println("################################  MenuDia: Voto: " + menuSelected.getNro_voto());

                    //Contar el voto para el usuario
                    menus.get(index).setNro_voto(menus.get(index).getNro_voto() + 1);
                    menus.get(index).setVotoUsuario(usuario);//registrar usuario votador
                    menuListAdapter.notifyDataSetChanged();//actualizar lista

                    switch (index) {
                        case 0: weeklyOrderActive.setMenuCount1(menus.get(index).getNro_voto());
                                break;
                        case 1: weeklyOrderActive.setMenuCount2(menus.get(index).getNro_voto());
                                break;
                        case 2: weeklyOrderActive.setMenuCount3(menus.get(index).getNro_voto());
                                break;
                        case 3: weeklyOrderActive.setMenuCount4(menus.get(index).getNro_voto());
                                break;
                    }

                    WeeklyOrderLogic.saveOrder(weeklyOrderActive, MainActivity.this);

                    Toast.makeText(MainActivity.this,
                            "MENU: " + menuSelected.getDescription() + "\nVOTOS: " + menus.get(index).getNro_voto(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this,
                            usuario + "\nNo puede votar otra vez", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void scenarioCerrarPedido() {
        final Button w_o_close_order_button = (Button) findViewById(R.id.w_o_closeOrder_button);
        w_o_close_order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeeklyOrderLogic.closeOrder(weeklyOrderActive, MainActivity.this);
            }
        });
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

    public void startData(int groupId) {
        // Programar w_o de ser necesario
        WeeklyOrderLogic.scheduleOrders(new Date(), groupId, this);

        weeklyOrderActive = WeeklyOrderLogic.getActiveWeeklyOrder(groupId, this);
        System.out.println("start Data >>>>>>>>>>>>>>>>>> " + weeklyOrderActive.toString());
    }

    //TODO borrar este metodo de trace
    private void showContentsDb() {
        System.out.println(">>>>>>>>>>>>>>>>>> showContentsDb ");
        GroupColumns groupAdapter = new GroupColumns(getBaseContext());
        GroupMemberColumns membersAdapter = new GroupMemberColumns(getBaseContext());
        WeeklyOrderColumns woAdapter = new WeeklyOrderColumns(getBaseContext());
        try {
            groupAdapter.showContent();
            membersAdapter.showContents();
            woAdapter.showContent();
        } catch (Exception e) {

        }

    }
}
