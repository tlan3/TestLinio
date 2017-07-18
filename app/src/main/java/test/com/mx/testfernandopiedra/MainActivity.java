package test.com.mx.testfernandopiedra;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.com.mx.testfernandopiedra.adapters.GenericAdapter;
import test.com.mx.testfernandopiedra.commons.ErrorReponse;
import test.com.mx.testfernandopiedra.commons.ProgressDialogBlock;
import test.com.mx.testfernandopiedra.commons.RequestConstants;
import test.com.mx.testfernandopiedra.commons.RequestManager;
import test.com.mx.testfernandopiedra.databinding.ActivityMainBinding;
import test.com.mx.testfernandopiedra.entities.Constants;
import test.com.mx.testfernandopiedra.entities.FavoritoItem;
import test.com.mx.testfernandopiedra.entities.ListItem;
import test.com.mx.testfernandopiedra.entities.ResponseResult;
import test.com.mx.testfernandopiedra.entities.SnapshotItem;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv1)
    RecyclerView rv1;

    public RequestManager requestManager;
    ArrayList<ListItem> listItems = new ArrayList<>();
    ProgressDialogBlock progressDialogBlock = new ProgressDialogBlock();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        ButterKnife.bind(this);
        requestManager = RequestManager.getInstance();
        activityMainBinding.setProgressDialogBlock(progressDialogBlock);

        //Solicitamos el json con la información de favoritos
        requestManager.requestAPI(
                RequestConstants.getFavoritos(this),
                RequestManager.TipoTAG.FAVORITOS,
                RequestManager.Metodo.GET,
                "");
    }


    /**
     * Se mostrata un toast si existe algún error al solicitar algún servicio
     * @param errorReponse
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onErrorReponse(ErrorReponse errorReponse) {
        Toast.makeText(this, errorReponse.errorMessage, Toast.LENGTH_LONG).show();
    }

    /**
     * Respuesta de algún servicio
     * @param responseResult
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResponseResult(ResponseResult responseResult) {


        final ArrayList<ListItem> listSnapshots = new ArrayList<>();
        ArrayList<ListItem> listFavoritos = new ArrayList<>();

        for (int i = 0; i < responseResult.result.length(); i++) {
            try {
                SnapshotItem snapshotItem = new SnapshotItem();
                snapshotItem.titulo = responseResult.result.getJSONObject(i).getString("name");
                JSONObject products = responseResult.result.getJSONObject(i).getJSONObject("products");
                Iterator<String> keys = products.keys();
                snapshotItem.count = String.valueOf(products.length());

                int count = 0;
                while (keys.hasNext()) {
                    FavoritoItem favoritoItem = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().fromJson(products.getJSONObject(keys.next()).toString(), FavoritoItem.class);
                    if (count < 3) {
                        snapshotItem.aImages.add(favoritoItem.image);
                    }
                    listFavoritos.add(new ListItem(favoritoItem, Constants.FAVORITO));
                    count++;
                }
                listSnapshots.add(new ListItem(snapshotItem, Constants.SNAPSHOPT));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        listItems.addAll(listSnapshots);
        listItems.add(new ListItem(String.format("Todos mis favoritos (%s)", String.valueOf(listFavoritos.size())), Constants.SECCION));
        listItems.addAll(listFavoritos);


        GenericAdapter adapter = new GenericAdapter(listItems);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == listSnapshots.size()) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        rv1.setLayoutManager(gridLayoutManager);
        rv1.setAdapter(adapter);

        progressDialogBlock.setBlock(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

        //Si algún evento se completo mientras se estaba en segundo plano, se vuelve enviar el evento
        //para su término
        if (requestManager.lazyObject != null) {
            EventBus.getDefault().post(requestManager.lazyObject);
            requestManager.clearLazyObject();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}
