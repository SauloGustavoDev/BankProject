package saulodev.com.bankproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        //CountDown para trocar de tela
        new Handler().postDelayed(() -> verificaUsuarios(),  3000);
    }

    private void abrirCadastro(){
        startActivity(new Intent(getApplicationContext(), CadastroActivity.class));
        finish();
    }

    private void abrirLogin(){
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
    //Método que valida se já existe usuário criado no banco, se sim, já manda para a tela de login em vez da tela de cadastro!
    private void verificaUsuarios(){
        try {
            SQLiteDatabase banco = openOrCreateDatabase("usuario", MODE_PRIVATE, null);
            //banco.execSQL("DELETE FROM usuario");
            String consulta = "SELECT nomeCompleto, cpf FROM usuario";

            Cursor cursor = banco.rawQuery(consulta, null);
            int indiceNome = cursor.getColumnIndex("nomeCompleto");
            int indiceCpf = cursor.getColumnIndex("cpf");
            String nomeCompleto = "";
            String cpf = "";
                cursor.moveToFirst();
                try {
                    nomeCompleto = cursor.getString(indiceNome);
                    cpf = cursor.getString(indiceCpf);
                    cursor.close();
                    banco.close();
                }catch (Exception e) {
                   abrirCadastro();
                }

            if (!nomeCompleto.isEmpty() && !cpf.isEmpty()){
                abrirLogin();
            }else {
                abrirCadastro();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
