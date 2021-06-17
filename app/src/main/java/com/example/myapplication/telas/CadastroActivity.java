package com.example.myapplication.telas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.myapplication.R;
import com.example.myapplication.database.DatabaseClient;
import com.example.myapplication.model.Divida;
import com.example.myapplication.database.DividaDao;
import com.example.myapplication.utils.MoneyTextWatcher;
import com.google.android.material.button.MaterialButton;

import java.util.Locale;

public class CadastroActivity extends AppCompatActivity {

    private static final String INTENT_ID = "id";
    private static final String INTENT_NOME = "nome";
    private static final String INTENT_VALOR = "valor";
    private static final String INTENT_CATEGORIA = "categoria";
    private static final String INTENT_PAGO = "pago";

    private EditText editTextNome;
    private EditText editTextValor;
    private Spinner spinnerCategoria;
    private SwitchCompat switchPago;

    private Integer dividaId;

    private Menu menu;

    private ArrayAdapter<CharSequence> adapterCategoria;

    private Integer categSelecionada = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_cadastro);

        //verifica se tem algum id (atualização ou cadastro)
        dividaId = getIntent().getIntExtra(INTENT_ID, 0);

        //cria o adapter do spinner de categorias
        adapterCategoria = ArrayAdapter.createFromResource(this,
                R.array.categoria_array, android.R.layout.simple_spinner_item);

        //congigura a action bar com o titulo correto
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (dividaId != 0) {
                actionBar.setTitle(R.string.atualizar);
            } else {
                actionBar.setTitle(R.string.cadastrar);
            }
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //pega a referencia dos componentes
        editTextNome = findViewById(R.id.editText_nome);
        editTextValor = findViewById(R.id.editText_valor);
        spinnerCategoria = findViewById(R.id.spinner_categorias);
        switchPago = findViewById(R.id.switch_pago);

        //coloca a mascara monetária
        Locale mLocale = new Locale("pt", "BR");
        editTextValor.addTextChangedListener(new MoneyTextWatcher(editTextValor, mLocale));

        //configura o spinner
        adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapterCategoria);
        spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categSelecionada = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //configura o botão salvar
        MaterialButton buttonSalvar = findViewById(R.id.button_salvar);
        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarDivida();
            }
        });

        preencheCampos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        this.menu = menu;
        inflater.inflate(R.menu.menu, this.menu);

        mostraDeletar();

        return true;
    }

    //Preenche os campos
    private void preencheCampos() {
        String nome = getIntent().getStringExtra(INTENT_NOME);
        double valor = getIntent().getDoubleExtra(INTENT_VALOR, 0.0);
        int categoria = getIntent().getIntExtra(INTENT_CATEGORIA, 0);
        boolean pago = getIntent().getBooleanExtra(INTENT_PAGO, false);

        if (nome != null) {
            editTextNome.setText(nome);
        }

        editTextValor.setText(String.valueOf(valor * 10));
        spinnerCategoria.setSelection(categoria);
        switchPago.setChecked(pago);
    }

    //Mostra ou oculta o botão de deleção
    private void mostraDeletar() {
        if (menu != null) {
            MenuItem item = menu.findItem(R.id.action_deletar);
            if (item != null) {
                item.setVisible(dividaId != 0);
                invalidateOptionsMenu();
            }
        }
    }

    //Pega só o valor sem a formatação de R$
    private double getValor() {
        String valor = editTextValor.getText().toString()
                .replace("R$", "")
                .trim()
                .replace(".", "")
                .replace(",", ".")
                .replaceAll("\\s+", "")
                .trim();

        return Double.parseDouble(valor);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //ação do botão voltar
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        //ação deletar
        if (item.getItemId() == R.id.action_deletar) {
            deletarDivida();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //valida se todos os campos estão preenchidos
    public boolean validarCadastro() {
        return !editTextNome.getText().toString().isEmpty()
                && !editTextValor.getText().toString().isEmpty() && categSelecionada != 0;
    }

    public void deletarDivida() {
        DividaDao dividaDao = DatabaseClient
                .getInstance(getApplicationContext())
                .dividaDao();

        int delete = dividaDao.deleteByUserId(dividaId);

        if (delete != 1) {
            Toast.makeText(getApplicationContext(), getString(R.string.cadastro_dao_erro), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.cadastro_deletar), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void salvarDivida() {
        if (validarCadastro()) {

            try {
                DividaDao dividaDao = DatabaseClient
                        .getInstance(getApplicationContext())
                        .dividaDao();

                Divida divida = new Divida(
                        editTextNome.getText().toString(),
                        getValor(),
                        switchPago.isChecked(),
                        categSelecionada
                );

                boolean jaExiste = (dividaId != 0);

                //valida se já existe um cadastro ou é um novo
                if (jaExiste) {
                    divida.setId(dividaId);
                    dividaDao.update(divida);
                } else {
                    dividaDao.insertAll(divida);
                    mostraDeletar();
                }

                finish();

            } catch (Exception exception) {
                Toast.makeText(getApplicationContext(), getString(R.string.cadastro_dao_erro), Toast.LENGTH_SHORT).show();
            }
        } else {
            //Se não for válido mostra uma msg de erro
            Toast.makeText(getApplicationContext(), getString(R.string.cadastro_erro), Toast.LENGTH_SHORT).show();
        }
    }

    public static Intent newIntent(Context context, Integer id, String nome, double valor, boolean pago, int categoria) {
        Intent intent = new Intent(context, CadastroActivity.class);

        intent.putExtra(INTENT_ID, id);
        intent.putExtra(INTENT_NOME, nome);
        intent.putExtra(INTENT_VALOR, valor);
        intent.putExtra(INTENT_CATEGORIA, categoria);
        intent.putExtra(INTENT_PAGO, pago);

        return intent;
    }
}