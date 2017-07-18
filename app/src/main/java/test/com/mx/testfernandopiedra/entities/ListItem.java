package test.com.mx.testfernandopiedra.entities;

/**
 * Created by fernandostone on 17/07/17.
 *
 */

public class ListItem {
    public SnapshotItem snapshotItem;
    public FavoritoItem favoritoItem;
    public String count;
    public int tipoLayout;

    public ListItem(SnapshotItem snapshotItem, int tipoLayout) {
        this.snapshotItem = snapshotItem;
        this.tipoLayout = tipoLayout;
    }

    public ListItem(FavoritoItem favoritoItem, int tipoLayout) {
        this.favoritoItem = favoritoItem;
        this.tipoLayout = tipoLayout;
    }

    public ListItem(String count,int tipoLayout) {
        this.count = count;
        this.tipoLayout = tipoLayout;
    }
}
