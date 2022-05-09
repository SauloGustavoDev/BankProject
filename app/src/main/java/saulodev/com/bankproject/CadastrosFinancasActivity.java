package saulodev.com.bankproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class CadastrosFinancasActivity extends AppCompatActivity {
    private EditText renda;
    private EditText patrimonio;
    private EditText codigo;
    private Button reenviarEmail;
    private Button cadastrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastros_financas);
        inicializaComponentes();
        reenviarEmail.setOnClickListener(View ->{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Bundle extras = getIntent().getExtras();
                        String email = extras.getString("email");
                        String codigo = extras.getString("codigo");
                        MailSenderActivity sender = new MailSenderActivity("conectbankpj@gmail.com",
                                "projeto123");
                        sender.sendMail("Confirmação de email! |- CONECT BANK -|", "Seu código de cadastro é: " + codigo,
                                email, email);
                        Log.e("resultado", "enviado");
                    } catch (Exception e) {
                        Log.e("SendMail", e.getMessage(), e);
                    }
                }
            }).start();
        });

        cadastrar.setOnClickListener(View ->{
            validaCodigo(codigo.getText().toString());
            //criarUsuario();
        });

    }
    private void inicializaComponentes(){
        renda = findViewById(R.id.renda_mensal);
        patrimonio = findViewById(R.id.renda_patrimonio_liquido);
        codigo = findViewById(R.id.confirmaEmail);
        reenviarEmail = findViewById(R.id.reenviaCodigo);
        cadastrar = findViewById(R.id.cadastrar);
    }
    private boolean validaCodigo(String cod){
        Bundle extras = getIntent().getExtras();
        String codigo = extras.getString("codigo");
        if (cod.equals(codigo)){
            Log.e("resultado", "Deu certo");
            return (true);
        }else {
            Log.e("resultado", "Deu errado");
            return (false);
        }
    }
    private boolean validaFinancas(Integer rendaMensal){
        if (rendaMensal < 0 && rendaMensal > 999999999){
            Toast.makeText(getApplicationContext(), "Informe uma renda valida, menor que 999999999 e maior que 0", Toast.LENGTH_LONG).show();
            return (false);
        }else {
            return (true);
        }
    }
    private boolean validaPatrimonio(long patrimonioLiquido){
        if (patrimonioLiquido < 0 && patrimonioLiquido > 999999999){
            Toast.makeText(getApplicationContext(), "Informe um patrimonio valido, menor que 999999999 e maior que 0", Toast.LENGTH_LONG).show();
            return (false);
        }else {
            return (true);
        }

    }
    private void criarUsuario(){
        try {
            Bundle extras = getIntent().getExtras();
            String nome = extras.getString("nomeCompleto");
            String cpf = extras.getString("cpf");
            String nascimento = extras.getString("nascimento");
            String email = extras.getString("email");
            String celular = extras.getString("celular");
            String senha = extras.getString("senha");
            SQLiteDatabase banco = openOrCreateDatabase("usuario", MODE_PRIVATE, null);
            //banco.execSQL("CREATE TABLE IF NOT EXISTS usuario (id INTEGER PRIMARY KEY AUTOINCREMENT, nomeCompleto VARCHAR(40),cpf NUMERIC(11), nascimento VARCHAR(10), email VARCHAR(40), celular NUMERIC(11), rendaMensal NUMERIC(9), valorPatrimonio NUMERIC(9),  senha NUMERIC(6))");
            banco.execSQL("INSERT INTO usuario(nomeCompleto, cpf, nascimento, email, celular, rendaMensal, valorPatrimonio, senha) VALUES (" + "'" + nome + "'" + "," + Long.parseLong(cpf) + "," + "'" + nascimento + "'" + "," + "'" + email + "'" + "," + Long.parseLong(celular) + "," + Integer.parseInt(renda.getText().toString()) + "," + Integer.parseInt(patrimonio.getText().toString()) + "," + Integer.parseInt(senha) + ")");
            //banco.execSQL("DELETE FROM usuario");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}