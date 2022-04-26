package saulodev.com.bankproject;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private EditText loginCpf;
    private EditText loginSenha;
    private Button autenticar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inicializaComponentes();

        autenticar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (autenticacao() != true){
                    Toast.makeText(getApplicationContext(), "CPF ou Senha inválidos!", Toast.LENGTH_SHORT).show();
                }else{
                    Log.i("resultado", "Passou na autenticação");
                }
            }
        });
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
            if(loginCpf.getText().toString().indexOf("0", 0) != 1){
            cpfFormatado = cpfFormatado.replaceFirst("0", "").replaceFirst("0", "");
            }
            if (cpf.equals(cpfFormatado) && senha.equals(senhaFormatada)){
                return (true);
            }else {
                return (false);
            }
        }catch (Exception e) {
                e.printStackTrace();
                return (false);
            }
        }





    private void inicializaComponentes(){
        loginCpf = findViewById(R.id.loginCpf);
        loginSenha = findViewById(R.id.loginSenha);
        autenticar = findViewById(R.id.loginEntrar);
    }

}

