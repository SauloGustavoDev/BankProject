package saulodev.com.bankproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AlterarSenhaActivity extends AppCompatActivity {
    private EditText passwordAtual;
    private EditText password;
    private EditText passwordConfirmar;
    private Button alterarSenha;
    private Button cancelar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_senha);

        passwordAtual = findViewById(R.id.passwordAtual);
        password = findViewById(R.id.password);
        passwordConfirmar = findViewById(R.id.confirmaPassword);
        cancelar = findViewById(R.id.buttonCancelar);
        alterarSenha = findViewById(R.id.buttonAlterarSenha);

        alterarSenha.setOnClickListener(View ->{
            alterarSenha();
        });

        cancelar.setOnClickListener(View ->{
            onBackPressed();});
    }

    private void alterarSenha(){
        Bundle extras = getIntent().getExtras();
        String cpfResgatado = extras.getString("cpf");
        String senha = password.getText().toString();
        String passwordAtual1 = passwordAtual.getText().toString();
        String confirmaSenha = passwordConfirmar.getText().toString();
        SQLiteDatabase banco = openOrCreateDatabase("usuario", MODE_PRIVATE, null);
        String consulta = "SELECT senha FROM usuario WHERE cpf = " + cpfResgatado  + "";
        Cursor cursor = banco.rawQuery(consulta, null);
        int indiceSenha = cursor.getColumnIndex("senha");
        cursor.moveToFirst();
        String senhaAtual = cursor.getString(indiceSenha);
        Log.e("Senha atual", senhaAtual);
        Log.e("Senha atual2", passwordAtual1);
        if(senhaAtual.equals(passwordAtual1)) {
          if (validaSenha(senha) == true && validaConfirmarSenha(confirmaSenha) == true) {
              try {
                  banco.execSQL("UPDATE USUARIO SET senha = " + senha + " WHERE cpf = " + cpfResgatado + "");
                  Toast.makeText(getApplicationContext(), "Senha alterada com sucesso!", Toast.LENGTH_SHORT).show();
                  banco.close();
                  onBackPressed();
                  finish();

              } catch (Exception e) {
                  e.printStackTrace();
                  Toast.makeText(getApplicationContext(), "Erro ao alterar senha!", Toast.LENGTH_LONG).show();
              }
          } else {
              Toast.makeText(getApplicationContext(), "As senhas não são iguais", Toast.LENGTH_SHORT).show();

          }
      }else {
            Toast.makeText(getApplicationContext(), "A senha atual está incorreta!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private boolean validaSenha(String password) {
        if (password.indexOf("0") == 0){
            Toast.makeText(getApplicationContext(), "A senha não pode começar com 0", Toast.LENGTH_SHORT).show();
            return (false);
        }
        if (password.equals("000000") || password.equals("111111") ||
                password.equals("222222") || password.equals("333333") ||
                password.equals("444444") || password.equals("555555") ||
                password.equals("666666") || password.equals("777777") ||
                password.equals("888888") || password.equals("999999") ||
                password.length() < 6){
            Toast.makeText(getApplicationContext(), "É necessário informar uma senha válida!", Toast.LENGTH_SHORT).show();
            return (false);
        }else{
            return (true);
        }
    }

    private boolean validaConfirmarSenha(String senhaConfirma_Validando) {
        return senhaConfirma_Validando.equals(password.getText().toString());
    }


}