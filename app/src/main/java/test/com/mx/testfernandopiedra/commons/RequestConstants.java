package test.com.mx.testfernandopiedra.commons;

import android.content.Context;

import test.com.mx.testfernandopiedra.R;

/**
 * Created by fernandostone on 17/07/17.
 *
 * Obtiene la dirección del servicio que se desea consumir
 */
public class RequestConstants {
    public static String getFavoritos(Context context) {
        return context.getResources().getString(R.string.favoritos_url);
    }
}
