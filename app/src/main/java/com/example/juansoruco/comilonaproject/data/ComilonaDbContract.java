package com.example.juansoruco.comilonaproject.data;

import android.net.Uri;

/**
 * Created by juan.soruco on 30/08/2015.
 */
public class ComilonaDbContract {
    // El valor de Content Authority es un nombre para el content provider, esto es
    // simular a las relaciones entre el nombre de dominio de un sitio web.
    // Una cadena conveniente para el "Content Authority" es el nombre del paquete
    // de la aplicacion, que se garantiza que sea unico en el dispositivo
    public static final String CONTENT_AUTHORITY = "com.salamancasolutions.footballnews";

    // Esta es la URI Basica que identificara a nuestro Content Provider
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Paths posibles que se anaden a la URI base
    // Por ejemplo, la url content://com.salamancasolutions.footballnews/matchs
    // Obtendra una lista de todos los resultados, pero una url como
    // content://com.salamancasolutions.footballnews/loquesea devolvera un error
    public static final String EMPLOYEES_RESULT = "employees";
    public static final String GROUPS_RESULT = "groups";
    public static final String GROUP_MEMBERS_RESULT = "membersResult";
    public static final String WEEKLY_ORDERS_RESULT = "weeklyOrders";
}
