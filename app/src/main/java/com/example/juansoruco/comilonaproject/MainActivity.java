package com.example.juansoruco.comilonaproject;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.juansoruco.comilonaproject.data.ComilonaDbHelper;
import com.example.juansoruco.comilonaproject.data.GroupColumns;
import com.example.juansoruco.comilonaproject.data.GroupMemberColumns;
import com.example.juansoruco.comilonaproject.data.WeeklyOrderColumns;
import com.example.juansoruco.comilonaproject.employee.Employee;
import com.example.juansoruco.comilonaproject.group.Group;
import com.example.juansoruco.comilonaproject.groupDetails.GroupDetails;
import com.example.juansoruco.comilonaproject.menu.MenuDia;
import com.example.juansoruco.comilonaproject.weeklyOrder.WeeklyOrder;
import com.example.juansoruco.comilonaproject.weeklyOrder.WeeklyOrderLogic;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends ActionBarActivity {

    private EmployeeListAdapter employeeListAdapter;
    private ListView listView;
    String[] employeesList;
    private int userId;
    private Group groupActive;
    private Spinner selectGroup;
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
        openDb();
        showContentsDb();

        userId = 1;

        fillGroupItemsOnSpinner();
        addListenerOnSpinnerItemSelection();

        refreshResults();

    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshResults();
    }

    public void fillGroupItemsOnSpinner() {
        String[] groupDescriptionList = null;
        GroupMemberColumns groupMemberAdapter = new GroupMemberColumns(MainActivity.this);
        try {
            ArrayList<GroupDetails> list = groupMemberAdapter.getGroupsByMember(userId);
            groupDescriptionList = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                groupDescriptionList[i] = list.get(i).getGroupFullname();
            }

            selectGroup = (Spinner) findViewById(R.id.cbx_grupo);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, groupDescriptionList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            selectGroup.setAdapter(dataAdapter);

            GroupColumns groupAdapter = new GroupColumns(MainActivity.this);
            groupActive = groupAdapter.getRecord(list.get(0).getGroupId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addListenerOnSpinnerItemSelection() {
        selectGroup = (Spinner) findViewById(R.id.cbx_grupo);
        selectGroup.setOnItemSelectedListener(new GroupSelectedListener());
    }

    private void refreshResults() {
        startData(groupActive.get_id());

        responsable = (TextView)findViewById(R.id.txt_nombre_responsable);
        responsable.setText(weeklyOrderActive.getResponsibleFullname());
        //Fecha
        fecha = (TextView)findViewById(R.id.txt_value_fecha);
        fecha.setText(WeeklyOrderColumns.df.format(weeklyOrderActive.getDate()));

        loadMenus();

        switch (weeklyOrderActive.getStatus()) {
            case WeeklyOrderLogic.cActive: scenarioIniciarMenu();
                break;
            case WeeklyOrderLogic.cMenuCompleted: scenarioVotacionAbierta();
                break;
            case WeeklyOrderLogic.cPollCompleted: scenarioCerrarPedido();
                break;
            default: Toast.makeText(this, "El estado de la orden es incompatible: " + weeklyOrderActive.getStatus(), Toast.LENGTH_SHORT);
                break;
        }

    }

    private void openDb() {
        ComilonaDbHelper dbHelper = new ComilonaDbHelper(getBaseContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.dropTables(db);
        dbHelper.installTables(db);
    }

    private void hideAll() {
        LinearLayout linearLayout = (LinearLayout)this.findViewById(R.id.menu_w_o_poll_closed);
        linearLayout.setVisibility(LinearLayout.GONE);
        linearLayout = (LinearLayout)this.findViewById(R.id.menu_w_o_poll_open);
        linearLayout.setVisibility(LinearLayout.GONE);
        linearLayout = (LinearLayout)this.findViewById(R.id.menu_w_o_active);
        linearLayout.setVisibility(LinearLayout.GONE);
        linearLayout = (LinearLayout)this.findViewById(R.id.button_vote_layout);
        linearLayout.setVisibility(LinearLayout.GONE);
    }

    private void showLayout(int _id) {
        hideAll();
        LinearLayout linearLayout = (LinearLayout)this.findViewById(_id);
        linearLayout.setVisibility(LinearLayout.VISIBLE);
        if (_id == R.id.menu_w_o_poll_open) {
            linearLayout = (LinearLayout)this.findViewById(R.id.button_vote_layout);
            linearLayout.setVisibility(LinearLayout.VISIBLE);
        }
    }

    private void scenarioIniciarMenu() {
        showLayout(R.id.menu_w_o_active);
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

    private void loadMenus() {
        menus = WeeklyOrderLogic.getMenusList(weeklyOrderActive);

        if(menuListAdapter!= null){
            menuListAdapter.clear();
        }

        menuListAdapter = new MenuListAdapter(this, menus);
        listView = (ListView)findViewById(R.id.employees_list);
        listView.setAdapter(menuListAdapter);
    }

    private void scenarioVotacionAbierta() {
        showLayout(R.id.menu_w_o_poll_open);
        final Button w_o_complete_poll_button = (Button) findViewById(R.id.w_o_completePoll_button);
        w_o_complete_poll_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeeklyOrderLogic.completePoll(weeklyOrderActive, MainActivity.this);
                refreshResults();
            }
        });

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
        showLayout(R.id.menu_w_o_poll_closed);

        TextView winnerTxt = (TextView)this.findViewById(R.id.w_o_winner_txt);
        winnerTxt.setText(weeklyOrderActive.getMenuSelected());

        final Button w_o_close_order_button = (Button) findViewById(R.id.w_o_closeOrder_button);
        w_o_close_order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeeklyOrderLogic.closeOrder(weeklyOrderActive, MainActivity.this);
                refreshResults();
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
        WeeklyOrderLogic.scheduleOrders((weeklyOrderActive==null || weeklyOrderActive.getDate()==null)?  new Date() : weeklyOrderActive.getDate(),
                groupId, this);

        weeklyOrderActive = WeeklyOrderLogic.getActiveWeeklyOrder(groupId, this);

    }

    //TODO borrar este metodo de trace
    private void showContentsDb() {

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

    public class GroupSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Log.d("onItemSelected ", "start");
            GroupColumns groupAdapter = new GroupColumns(getBaseContext());
            try {
                groupActive = groupAdapter.getRecord(parent.getItemAtPosition(position).toString());
                if (groupActive == null) {
                    Toast.makeText(MainActivity.this, "El grupo seleccionado no tiene información", Toast.LENGTH_SHORT).show();
                    groupActive = new Group(1, "Mi Grupo de Comilona", "A");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "El grupo seleccionado no tiene información", Toast.LENGTH_SHORT).show();
                groupActive = new Group(1, "Mi Grupo de Comilona", "A");
            }
            Log.d("Fin onItemSelected", groupActive.toString());
            refreshResults();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
