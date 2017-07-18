package test.com.mx.testfernandopiedra.adapters;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.databinding.ViewDataBinding;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import test.com.mx.testfernandopiedra.BR;
import test.com.mx.testfernandopiedra.R;
import test.com.mx.testfernandopiedra.databinding.ItemFavoritoLayoutBinding;
import test.com.mx.testfernandopiedra.databinding.ItemSeccionLayoutBinding;
import test.com.mx.testfernandopiedra.databinding.ItemSpanshotLayoutBinding;
import test.com.mx.testfernandopiedra.entities.Constants;
import test.com.mx.testfernandopiedra.entities.ListItem;

/**
 * Created by fernandostone on 17/07/17.
 *
 */

public class GenericAdapter extends RecyclerView.Adapter<GenericAdapter.ViewHolder> {

   private ArrayList<ListItem> listItems;

    public GenericAdapter(ArrayList<ListItem> listItems) {
        this.listItems = listItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ViewDataBinding binding;

        switch (viewType) {

            case Constants.SNAPSHOPT:
                binding = ItemSpanshotLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                break;
            case Constants.SECCION:
                binding = ItemSeccionLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                break;
            case Constants.FAVORITO:
                binding = ItemFavoritoLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                break;
            default:
                binding = ItemSeccionLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        }
        return new ViewHolder(binding, binding.getRoot());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindConncection(listItems.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return listItems.get(position).tipoLayout;
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    // inner class to hold a reference to each item of RecyclerView
    static class ViewHolder extends RecyclerView.ViewHolder {

        void bindConncection(ListItem listItem) {
            binding.setVariable(BR.listItem, listItem);

            switch (listItem.tipoLayout) {
                case Constants.SNAPSHOPT:
                    if (listItem.snapshotItem.aImages.get(0) != null) {
                        loadImage(listItem.snapshotItem.aImages.get(0), R.id.imgPreview1);
                    }
                    if (listItem.snapshotItem.aImages.get(1) != null) {
                        loadImage(listItem.snapshotItem.aImages.get(1), R.id.imgPreview2);
                    }
                    if (listItem.snapshotItem.aImages.get(2) != null) {
                        loadImage(listItem.snapshotItem.aImages.get(2), R.id.imgPreview3);
                    }
                    break;
                case Constants.SECCION:
                    break;
                case Constants.FAVORITO:
                    Glide.with(binding.getRoot())
                            .load(listItem.favoritoItem.image)
                            .into((ImageView) binding.getRoot().findViewById(R.id.imgFavorito));

                    if (listItem.favoritoItem.linioPlusLevel == 1) {
                        listItem.favoritoItem.linioPlusDrawable = getVectorDrawable(R.drawable.nd_ic_plus_30);
                    } else if (listItem.favoritoItem.linioPlusLevel == 2) {
                        listItem.favoritoItem.linioPlusDrawable = VectorDrawableCompat.create(binding.getRoot().getContext().getResources(), R.drawable.linio_plus_48, null);
                    }

                    if (listItem.favoritoItem.conditionType.equalsIgnoreCase("new")) {
                        listItem.favoritoItem.conditionTypeDrawable = VectorDrawableCompat.create(binding.getRoot().getContext().getResources(), R.drawable.nd_ic_new_30, null);
                    } else {
                        listItem.favoritoItem.conditionTypeDrawable = VectorDrawableCompat.create(binding.getRoot().getContext().getResources(), R.drawable.nd_ic_refurbished_30, null);
                    }

                    break;
            }

            ObjectAnimator translate = ObjectAnimator.ofFloat(binding.getRoot(), "translationY", -200, 0);
            ObjectAnimator alpha = ObjectAnimator.ofFloat(binding.getRoot(), "alpha", 0.5f, 1);
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(binding.getRoot(), "scaleX", 0.5f, 1);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(binding.getRoot(), "scaleY", 0.5f, 1);
            AnimatorSet as = new AnimatorSet();
            as.playTogether(translate,
                    alpha,
                    scaleX,
                    scaleY);
            as.setDuration(500);
            as.setInterpolator(new OvershootInterpolator());
            as.start();
        }

        ViewDataBinding binding;

        private ViewHolder(ViewDataBinding binding, final View itemLayoutView) {
            super(itemLayoutView);
            this.binding = binding;
        }

        private VectorDrawableCompat getVectorDrawable(int drawable) {
            return VectorDrawableCompat.create(binding.getRoot().getContext().getResources(), drawable, null);
        }

        private void loadImage(String url, int imageView) {
            Glide.with(binding.getRoot())
                    .load(url)
                    .into((ImageView) binding.getRoot().findViewById(imageView));
        }

    }
}
