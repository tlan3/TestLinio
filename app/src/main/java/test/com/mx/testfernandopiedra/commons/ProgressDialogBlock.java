package test.com.mx.testfernandopiedra.commons;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import test.com.mx.testfernandopiedra.BR;

/**
 * Created by fernandostone on 18/07/17.
 * <p>
 * Esta clase nos permite ocultar y mostrar dinamicamente el loader de la vista
 */
public class ProgressDialogBlock extends BaseObservable {

    private Boolean block = true;

    @Bindable
    public Boolean getBlock() {
        return block;
    }

    public void setBlock(Boolean block) {
        this.block = block;
        notifyPropertyChanged(BR.block);
    }
}

