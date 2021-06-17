package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Divida;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import static com.example.myapplication.utils.Formatacao.getValorFormatado;

public class ItemAdapter extends RecyclerView.Adapter {
    List<Divida> dividaList = Collections.emptyList();
    Divida dividaSelecionada;

    View.OnClickListener itemClick;

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new DividaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        ((DividaViewHolder) holder).bindData(dividaList.get(position));
    }

    @Override
    public int getItemCount() {
        return dividaList.size();
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.item_lista;
    }

    public void setDividaList(List<Divida> dividaList) {
        this.dividaList = dividaList;
        notifyDataSetChanged();
    }

    public void setItemClick(View.OnClickListener itemClick) {
        this.itemClick = itemClick;
    }

    public Divida getDividaSelecionada() {
        return dividaSelecionada;
    }

    class DividaViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private TextView nomeTextView;
        private TextView valorTextView;
        private TextView pagoTextView;
        private TextView naoPagoTextView;

        public DividaViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            this.itemView = itemView;

            nomeTextView = itemView.findViewById(R.id.textView_nome);
            valorTextView = itemView.findViewById(R.id.textView_valor);
            pagoTextView = itemView.findViewById(R.id.textView_pago);
            naoPagoTextView = itemView.findViewById(R.id.textView_nao_pago);
        }

        public void bindData(final Divida divida) {
            nomeTextView.setText(divida.getNome());
            valorTextView.setText(getValorFormatado(divida.getValor()));

            if (divida.getPago()) {
                pagoTextView.setVisibility(View.VISIBLE);
                naoPagoTextView.setVisibility(View.GONE);
            } else {
                pagoTextView.setVisibility(View.GONE);
                naoPagoTextView.setVisibility(View.VISIBLE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dividaSelecionada = divida;
                    itemClick.onClick(v);
                }
            });
        }
    }
}

