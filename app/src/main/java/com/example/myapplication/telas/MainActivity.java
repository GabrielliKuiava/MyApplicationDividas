package com.example.myapplication.telas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.adapter.ItemAdapter;
import com.example.myapplication.R;
import com.example.myapplication.database.DatabaseClient;
import com.example.myapplication.model.Divida;
import com.example.myapplication.database.DividaDao;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import static com.example.myapplication.utils.Formatacao.getValorFormatado;

public class MainActivity extends AppCompatActivity {

    private TextView textViewSemRegistro;
    private TextView textTotalPago;
    private TextView textTotalNaoPago;
    private final ItemAdapter adapter = new ItemAdapter();

    private DividaDao dividaDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dividaDao = DatabaseClient
                .getInstance(getApplicationContext())
                .dividaDao();

        //pega a referenciados elementos
        textViewSemRegistro = findViewById(R.id.textView_semRegistro);
        textTotalPago = findViewById(R.id.textView_totalPago);
        textTotalNaoPago = findViewById(R.id.textView_totalNaoPago);

        atualizarTotais();

        //configura o recyclerview
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);

        //configura o botão de cadastro
        MaterialButton buttonCadastrar = findViewById(R.id.button_cadastrar);
        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CadastroActivity.class));
            }
        });

        //congigura o click do item da lista para abrir o detalhe
        adapter.setItemClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Divida divida = adapter.getDividaSelecionada();

                if (divida != null) {
                    Intent intent = CadastroActivity.newIntent(getApplicationContext(),
                            divida.getId(),
                            divida.getNome(),
                            divida.getValor(),
                            divida.getPago(),
                            divida.getCategoria()
                    );
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        atualizarLista();
        atualizarTotais();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //abre os relatórios
        if (item.getItemId() == R.id.action_relatorio) {
            startActivity(new Intent(this, RelatorioActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void atualizarTotais() {
        textTotalPago.setText(getValorFormatado(dividaDao.totalPago()));
        textTotalNaoPago.setText(getValorFormatado(dividaDao.totalNaoPago()));
    }

    private void atualizarLista() {
        List<Divida> dividas = dividaDao.getAll();
        adapter.setDividaList(dividas);

        //mostra ou oculta a msg que não tem nenhum registro
        if (dividas.isEmpty()) {
            textViewSemRegistro.setVisibility(View.VISIBLE);
        } else {
            textViewSemRegistro.setVisibility(View.GONE);
        }
    }
}