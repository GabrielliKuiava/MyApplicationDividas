package com.example.myapplication.telas;

import android.graphics.Color;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.myapplication.R;
import com.example.myapplication.database.DatabaseClient;
import com.example.myapplication.database.DividaDao;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class RelatorioActivity extends AppCompatActivity {

    private PieChart relatorioPago;
    private PieChart relatorioNaoPago;

    private AppCompatTextView textViewPago;
    private AppCompatTextView textViewNaoPago;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);

        //congigura a action bar com o titulo correto
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.relatorios);
        actionBar.setDisplayHomeAsUpEnabled(true);

        relatorioPago = findViewById(R.id.relatorio_paga);
        relatorioNaoPago = findViewById(R.id.relatorio_nao_paga);

        textViewPago = findViewById(R.id.textView_relatorio_pago);
        textViewNaoPago = findViewById(R.id.textView_relatorio_nao_pago);

        DividaDao dividaDao = DatabaseClient
                .getInstance(getApplicationContext())
                .dividaDao();

        String[] categorias = getResources().getStringArray(R.array.categoria_array);

        ArrayList<PieEntry> pagoList = new ArrayList<>();
        ArrayList<PieEntry> naoPagoList = new ArrayList<>();

        for (int id = 1; id < categorias.length; id++) {
            String categoriaNome = categorias[id];
            double somaPagas = dividaDao.totalPagasCategoria(id);
            double somaNaoPagas = dividaDao.totalNaoPagasCategoria(id);

            if (somaPagas > 0) {
                pagoList.add(new PieEntry((float) somaPagas, categoriaNome));
            }

            if (somaNaoPagas > 0) {
                naoPagoList.add(new PieEntry((float) somaNaoPagas, categoriaNome));
            }
        }

        setDados(pagoList, naoPagoList);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setDados(ArrayList<PieEntry> pagoList, ArrayList<PieEntry> naoPagoList) {
        if (pagoList.isEmpty()) {
            relatorioPago.setVisibility(View.GONE);
        } else {
            textViewPago.setVisibility(View.GONE);
            setPie(R.id.relatorio_paga, pagoList, getString(R.string.pago));
        }

        if (naoPagoList.isEmpty()) {
            relatorioNaoPago.setVisibility(View.GONE);
        } else {
            textViewNaoPago.setVisibility(View.GONE);
            setPie(R.id.relatorio_nao_paga, naoPagoList, getString(R.string.nao_pago));
        }
    }

    private void setPie(Integer idLayout, ArrayList<PieEntry> list, String label) {
        ArrayList<Integer> colors = new ArrayList<>();
        for (int color : ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        for (int color : ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieChart pieChart = findViewById(idLayout);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(10);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText(label);
        pieChart.setCenterTextSize(18);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);

        PieDataSet dataSet = new PieDataSet(list, "");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.animateY(1400, Easing.EaseInOutQuad);
    }
}