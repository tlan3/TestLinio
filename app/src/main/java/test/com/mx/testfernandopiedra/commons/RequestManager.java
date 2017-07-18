package test.com.mx.testfernandopiedra.commons;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import test.com.mx.testfernandopiedra.BuildConfig;
import test.com.mx.testfernandopiedra.R;
import test.com.mx.testfernandopiedra.entities.ResponseResult;
import test.com.mx.testfernandopiedra.utils.UtilLog;
import test.com.mx.testfernandopiedra.utils.UtilStatusConnection;

import static test.com.mx.testfernandopiedra.utils.UtilStatusConnection.TYPE_NOT_CONNECTED;

/**
 * Created by fernandostone on 17/07/17.
 *
 * RequestManager nos permite hacer peticiones a cualquier ws rest
 *
 */

public class RequestManager implements Callback {

    private String TAG = RequestManager.class.getSimpleName();

    private final OkHttpClient client;
    public Object lazyObject;
    private static RequestManager mRequestManager;

    private Context context;

    public enum TipoTAG {
        NOTHING, FAVORITOS
    }

    public enum Metodo {PUT, DELETE, POST, GET, PATCH}

    private String error_message = "";

    public static synchronized RequestManager getInstance() {
        return mRequestManager;
    }

    static void initInstance(Context mContext) {
        try {
            mRequestManager = new RequestManager(mContext);
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            e.printStackTrace();
        }
    }

    /**
     * Se crea la instancia del cliente
     *
     * @param context Android Context
     */
    private RequestManager(final Context context) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        this.context = context;

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);
//                .readTimeout(30, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }
        client = builder.build();
        error_message = context.getResources().getString(R.string.error_general);
    }

    /**
     * Método que realiza una petición asíncronza
     *
     * @param requestName nombre del servicio
     * @param tipoTAG     tag
     * @param metodo      método
     * @param jsonString  los datos que se envian al servidor como json
     */

    public void requestAPI(String requestName, TipoTAG tipoTAG, Metodo metodo, String jsonString) {
        StringBuilder sbParams = new StringBuilder();
        Request request;
        try {

            Request.Builder requestBuilder = new Request.Builder()
                    .url(requestName)
                    .tag(tipoTAG);

            switch (metodo) {
                case POST:
                    requestBuilder.post(getRequestBody(jsonString));
                    break;
                case PATCH:
                    requestBuilder.patch(getRequestBody(jsonString));
                    break;
                case PUT:
                    requestBuilder.put(getRequestBody(jsonString));
                    break;
                case DELETE:
                    requestBuilder.delete();
                    break;
                case GET:
                    requestBuilder.url(String.format("%s%s", requestName, sbParams));
                    break;
            }
            request = requestBuilder.build();

            if (UtilStatusConnection.getConnectivityStatus(context) == TYPE_NOT_CONNECTED) {
                ErrorReponse errorReponse = new ErrorReponse();
                errorReponse.tipoTAG = tipoTAG;
                errorReponse.errorMessage = UtilStatusConnection.getConnectivityStatusString(context);
                verifyPostEvent(errorReponse);
            } else client.newCall(request).enqueue(this);
        } catch (Exception e) {
            UtilLog.E(TAG, "Exception... " + e.getMessage());
        }
    }

    /**
     * Retorna un requestbody con la información data
     *
     * @param data JSON data
     * @return retorna un requestbody
     */
    private RequestBody getRequestBody(String data) {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), data);
    }

    /**
     * Lllamado cuando la petición falla
     *
     * @param call        Call is a request that has been prepared for execution
     * @param iOException Error detail message
     */
    @Override
    public void onFailure(Call call, IOException iOException) {
        verifyPostEvent(getGenericErrorResponse());
    }

    private ErrorReponse getGenericErrorResponse() {
        ErrorReponse errorReponse = new ErrorReponse();
        errorReponse.tipoTAG = TipoTAG.NOTHING;
        errorReponse.errorMessage = error_message;
        return errorReponse;
    }

    /**
     * Lllamado cuando la petición es exitosa
     *
     * @param call     Call is a request that has been prepared for execution
     * @param response Returns the raw response received from the network.
     * @throws IOException Error detail message
     */
    @Override
    public void onResponse(Call call, Response response) throws IOException {
        try {
            String result = response.body().string();
            UtilLog.I(TAG, result);
            if (response.isSuccessful()) {
                ResponseResult responseResult = new ResponseResult();
                responseResult.result = new JSONArray(result);
                verifyPostEvent(responseResult);
            } else {
                verifyPostEvent(getGenericErrorResponse());
            }
        } catch (Exception e) {
            verifyPostEvent(getGenericErrorResponse());
        }
    }

    public void clearLazyObject() {
        lazyObject = null;
    }

    private void verifyPostEvent(Object object) {
        clearLazyObject();
        if (EventBus.getDefault().hasSubscriberForEvent(object.getClass())) {
            EventBus.getDefault().post(object);
        } else {
            UtilLog.E(TAG, context.getString(R.string.no_listeners));
            lazyObject = object;
        }
    }


}
