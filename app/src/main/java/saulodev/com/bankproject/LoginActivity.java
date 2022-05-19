package saulodev.com.bankproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText loginCpf;
    private EditText loginSenha;
    private EditText loginAgencia;
    private EditText loginConta;
    private EditText loginDigito;
    private Button autenticar;
    private TextView cadastro;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inicializaComponentes();
        checkBox.setOnClickListener(View -> loginAgenciaConta());
        autenticar.setOnClickListener(view -> {
            if (checkBox.isChecked()) {
                if (!autenticacaoAgenciaConta()) {
                    Toast.makeText(getApplicationContext(), "Agencia, conta, digito ou Senha inválidos!", Toast.LENGTH_SHORT).show();
                } else {
                    abrirHomeAgenciaConta();
                }
            }else {
                    if (!autenticacaoCpf()) {
                        Toast.makeText(getApplicationContext(), "CPF ou Senha inválidos!", Toast.LENGTH_SHORT).show();
                    } else {
                        abrirHomeCpf();
                    }
            }
        });
        cadastro.setOnClickListener(View -> abrirCadastro());
    }
    private boolean autenticacaoCpf() {
        try {
            String ponto = "\\.";
            String cpfCerto = loginCpf.getText().toString().replaceAll( ponto, "");
            cpfCerto = cpfCerto.replaceAll("-", "");
            Log.e("resultado", cpfCerto);
            SQLiteDatabase banco = openOrCreateDatabase("usuario", MODE_PRIVATE, null);
            String consulta = "SELECT cpf, senha FROM usuario WHERE cpf = " +cpfCerto + " AND senha = " + loginSenha.getText().toString() + "";
            Cursor cursor = banco.rawQuery(consulta, null);
            int indiceCpf = cursor.getColumnIndex("cpf");
            int indiceSenha = cursor.getColumnIndex("senha");
            String cpf = "";
            String senha = "";
            String cpfFormatado = cpfCerto;
            String senhaFormatada = loginSenha.getText().toString();
            cursor.moveToFirst();
            while (cursor != null) {
                cpf = cursor.getString(indiceCpf);
                senha = cursor.getString(indiceSenha);
                cursor = null;
            }
            if(loginCpf.getText().toString().indexOf("0") != 1){
            cpfFormatado = cpfFormatado.replaceFirst("0", "").replaceFirst("0", "");
            }
            return cpf.equals(cpfFormatado) && senha.equals(senhaFormatada) ;
        }catch (Exception e) {
                e.printStackTrace();
                return (false);
            }
        }
    private boolean autenticacaoAgenciaConta() {
        try {
            int agenciaF = Integer.parseInt(loginAgencia.getText().toString());
            int contaF = Integer.parseInt(loginConta.getText().toString());
            int digitoF = Integer.parseInt(loginDigito.getText().toString());
            String senhaFormatada = loginSenha.getText().toString();
            SQLiteDatabase banco = openOrCreateDatabase("usuario", MODE_PRIVATE, null);
            String consulta = "SELECT agencia, conta, digito, senha FROM usuario WHERE agencia = " +agenciaF +" AND conta = " +contaF + " AND digito = "+ digitoF + " AND senha = " + loginSenha.getText().toString() + "";
            Cursor cursor = banco.rawQuery(consulta, null);
            int indiceAgencia = cursor.getColumnIndex("agencia");
            int indiceConta = cursor.getColumnIndex("conta");
            int indiceDigito = cursor.getColumnIndex("digito");
            int indiceSenha = cursor.getColumnIndex("senha");
            String agencia = "";
            String conta = "";
            String digito = "";
            String senha = "";
            cursor.moveToFirst();
            while (cursor != null) {
                agencia = cursor.getString(indiceAgencia);
                conta = cursor.getString(indiceConta);
                digito = cursor.getString(indiceDigito);
                senha = cursor.getString(indiceSenha);
                cursor = null;
            }
            String agenciaFormatada = String.valueOf(agenciaF);
            String contaFormatada = String.valueOf(contaF);
            String digitoFormatado = String.valueOf(digitoF);

            Log.e("Resultado Banco: ", agencia);
            Log.e("Resultado Banco: ", conta);
            Log.e("Resultado Banco: ", digito);
            Log.e("Resultado Banco: ", senha);
            Log.e("Resultado: ", agenciaFormatada);
            Log.e("Resultado: ", contaFormatada);
            Log.e("Resultado: ", digitoFormatado);
            Log.e("Resultado: ", senhaFormatada);
            return agencia.equals(agenciaFormatada) && conta.equals(contaFormatada) && digito.equals(digitoFormatado) && senha.equals(senhaFormatada);
        } catch (Exception e) {
            e.printStackTrace();
            return (false);
        }
    }

    private void inicializaComponentes(){
        loginCpf = findViewById(R.id.loginCpf);
        loginCpf.addTextChangedListener(TextMask.insert(TextMask.CPF_MASK, loginCpf));
        loginSenha = findViewById(R.id.loginSenha);
        autenticar = findViewById(R.id.loginEntrar);
        cadastro = findViewById(R.id.irCadastro);
        checkBox = findViewById(R.id.checkBoxAgenciaeConta);
        loginAgencia = findViewById(R.id.loginAgencia);
        loginConta = findViewById(R.id.loginConta);
        loginDigito = findViewById(R.id.digitoVerificador);

    }

    private void abrirCadastro(){
        startActivity(new Intent(getApplicationContext(), CadastroActivity.class));
        finish();
    }

    private void abrirHomeCpf(){
        String ponto = "\\.";
        String cpfCerto = loginCpf.getText().toString().replaceAll( ponto, "");
        Intent passaDados = new Intent(getApplicationContext(), HomeActivity.class);
        passaDados.putExtra("cpf", cpfCerto.replaceAll("-", ""));
        startActivity(passaDados);
        finish();
    }

    private void abrirHomeAgenciaConta(){
        try {
            int contaF = Integer.parseInt(loginConta.getText().toString());
            int digitoF = Integer.parseInt(loginDigito.getText().toString());
            SQLiteDatabase banco = openOrCreateDatabase("usuario", MODE_PRIVATE, null);
            String consulta = "SELECT cpf FROM usuario WHERE conta = " +contaF + " AND digito = "+ digitoF + " AND senha = " + loginSenha.getText().toString() + "";
            Cursor cursor = banco.rawQuery(consulta, null);
            int indiceCpf = cursor.getColumnIndex("cpf");
            cursor.moveToFirst();
            String cpf = cursor.getString(indiceCpf);
            Intent passaDados = new Intent(getApplicationContext(), HomeActivity.class);
            passaDados.putExtra("cpf", cpf);
            startActivity(passaDados);
            finish();
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void loginAgenciaConta(){
        if (checkBox.isChecked()){
            loginCpf.setVisibility(View.INVISIBLE);
            loginAgencia.setVisibility(View.VISIBLE);
            loginConta.setVisibility(View.VISIBLE);
            loginDigito.setVisibility(View.VISIBLE);
        }else {
            loginCpf.setVisibility(View.VISIBLE);
            loginAgencia.setVisibility(View.INVISIBLE);
            loginConta.setVisibility(View.INVISIBLE);
            loginDigito.setVisibility(View.INVISIBLE);
        }
    }
}

