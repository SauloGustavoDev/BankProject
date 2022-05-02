package saulodev.com.bankproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText loginCpf;
    private EditText loginSenha;
    private Button autenticar;
    private TextView cadastro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inicializaComponentes();
        autenticar.setOnClickListener(view -> {
            if (!autenticacao()){
                Toast.makeText(getApplicationContext(), "CPF ou Senha inválidos!", Toast.LENGTH_SHORT).show();
            }else{
                abrirHome();
            }
        });
        cadastro.setOnClickListener(View -> abrirCadastro());
    }

    private boolean autenticacao() {
        try {
            SQLiteDatabase banco = openOrCreateDatabase("usuario", MODE_PRIVATE, null);
            String consulta = "SELECT cpf, senha FROM usuario WHERE cpf = " + loginCpf.getText().toString() + " AND senha = " + loginSenha.getText().toString() + "";
            Cursor cursor = banco.rawQuery(consulta, null);
            int indiceCpf = cursor.getColumnIndex("cpf");
            int indiceSenha = cursor.getColumnIndex("senha");
            String cpf = "";
            String senha = "";
            String cpfFormatado = loginCpf.getText().toString();
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

    private void inicializaComponentes(){
        loginCpf = findViewById(R.id.loginCpf);
        loginSenha = findViewById(R.id.loginSenha);
        autenticar = findViewById(R.id.loginEntrar);
        cadastro = findViewById(R.id.irCadastro);
    }

    private void abrirCadastro(){
        startActivity(new Intent(getApplicationContext(), CadastroActivity.class));
        finish();
    }

    private void abrirHome(){
        Intent passaDados = new Intent(getApplicationContext(), HomeActivity.class);
        passaDados.putExtra("cpf", loginCpf.getText().toString());
        startActivity(passaDados);
        finish();
    }

}

