package com.devi.kamusku.cari;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.devi.kamusku.DetailActivity;
import com.devi.kamusku.R;
import com.devi.kamusku.model.KamusModel;

class SearchViewHolder extends RecyclerView.ViewHolder {
    TextView txtKata, txtArti;

    public SearchViewHolder(View itemView) {
        super(itemView);

        txtKata = itemView.findViewById(R.id.txt_kata);
        txtArti = itemView.findViewById(R.id.txt_arti);
    }

    public void bind(final KamusModel kamusModel) {
        txtKata.setText(kamusModel.getKata());
        txtArti.setText(kamusModel.getArti());

        itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(itemView.getContext(), DetailActivity.class);
                intent.putExtra(DetailActivity.DETAIL_KATA, kamusModel.getKata());
                intent.putExtra(DetailActivity.DETAIL_ARTI, kamusModel.getArti());

                itemView.getContext().startActivity(intent);
            }
        });
    }
}
