package saulodev.com.bankproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        String conta = "00001";
        //int teste = 7;
        //conta = conta + String.valueOf(teste);
        int digito = digitoVerificadorConta(conta);
        Log.e("Resultado: ", conta +"-"+ digito);
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
            banco.execSQL("CREATE TABLE IF NOT EXISTS usuario (id INTEGER PRIMARY KEY AUTOINCREMENT, nomeCompleto VARCHAR(40),cpf NUMERIC(11), nascimento VARCHAR(10), email VARCHAR(40), celular VARCHAR(14), rendaMensal VARCHAR(9), valorPatrimonio VARCHAR(9),  senha NUMERIC(6), saldo REAL(10), agencia NUMERIC(4), conta NUMERIC(6), digito INTEGER(1))");
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
                cursor.close();
                banco.close();
                abrirLogin();
            }else {
                cursor.close();
                banco.close();
                abrirCadastro();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int digitoVerificadorConta(String conta){
        // primeiro vamos remover os espaços do número da conta
        conta = conta.trim();
        // agora precisamos adicionar os zeros necessários
        // para completar 12 posições
        conta = String.format("%1$12s", conta).replace(' ', '0');
        // agora vamos definir os índices de múltiplicação
        String indices = "543298765432";
        // e aqui a soma da multiplicação coluna por coluna
        int soma = 0;

        // fazemos a multiplicação coluna por coluna agora
        for (int i = 0; i < conta.length(); i++){
            soma = soma + Integer.parseInt(String.valueOf(conta.charAt(i))) *
                    Integer.parseInt(String.valueOf(indices.charAt(i)));
        }

        // obtemos o resto da divisão da soma por onze
        int resto = soma % 11;

        // subtraímos onze pelo resto da divisão
        int digito = 11 - resto;

        // atenção: Se o resultado da subtração for
        // maior que 9 (nove), o dígito será 0 (zero)
        if (digito > 9){
            digito = 0;
        }

        return digito;
    }
}

