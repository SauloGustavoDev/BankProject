package saulodev.com.bankproject;

import static saulodev.com.bankproject.MoneyMask.formatPriceSave;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
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
            if(!validaFinancas(renda.getText().toString())){
                Toast.makeText(getApplicationContext(), "Informe um valor valido", Toast.LENGTH_LONG).show();
            }else if (!validaPatrimonio(patrimonio.getText().toString())){
                Toast.makeText(getApplicationContext(), "Informe um valor de patrimonio valido maior que 100R$", Toast.LENGTH_LONG).show();
            }else if(!validaCodigo(codigo.getText().toString())) {
                Toast.makeText(getApplicationContext(), "O código informado está incorreto!", Toast.LENGTH_LONG).show();
            }else {
                criarUsuario();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Bundle extras = getIntent().getExtras();
                            String email = extras.getString("email");
                            String agencia ="0001";
                            int conta = verificaUltimaConta();
                            MailSenderActivity sender = new MailSenderActivity("conectbankpj@gmail.com",
                                    "projeto123");
                            sender.sendMail("Agencia e conta do usuário! |- CONECT BANK -|", "|- Segue abaixo sua agencia e conta! -|"+"\nAgencia: " + agencia +"\nConta: 0000" + conta + "-" + digitoVerificadorConta(String.valueOf(conta)),
                                    email, email);
                            Log.e("resultado", "enviado");
                        } catch (Exception e) {
                            Log.e("SendMail", e.getMessage(), e);
                        }
                    }
                }).start();
                Toast.makeText(getApplicationContext(), "Sua agencia e conta foram encaminhadas para seu e-mail!", Toast.LENGTH_LONG).show();
                finish();

            }
        });

    }
    private void inicializaComponentes(){
        renda = findViewById(R.id.renda_mensal);
        renda.addTextChangedListener(new MoneyMask(renda));
        patrimonio = findViewById(R.id.renda_patrimonio_liquido);
        patrimonio.addTextChangedListener(new MoneyMask(patrimonio));
        codigo = findViewById(R.id.confirmaEmail);
        reenviarEmail = findViewById(R.id.reenviaCodigo);
        cadastrar = findViewById(R.id.cadastrar);
    }
    private boolean validaCodigo(String cod){
        Bundle extras = getIntent().getExtras();
        String codigo = extras.getString("codigo");
        if (cod.equals(codigo)){
            return (true);
        }else {
            return (false);
        }
    }
    private boolean validaFinancas(String rendaMensal){
        if (rendaMensal == null || rendaMensal.isEmpty() ){
            return (false);
        }else {
            String ponto = "\\.";
            rendaMensal = rendaMensal.replaceAll(ponto, "");
            rendaMensal = rendaMensal.replaceAll(",", "");
            int rendaM = Integer.parseInt(rendaMensal);
            return (true);
        }
    }
    private boolean validaPatrimonio(String patrimonioLiquido){
        if (patrimonioLiquido == null|| patrimonioLiquido.isEmpty() ){
            return (false);
        }else {
            String ponto = "\\.";
            patrimonioLiquido = patrimonioLiquido.replaceAll(ponto, "");
            patrimonioLiquido = patrimonioLiquido.replaceAll(",", "");
            int patrimonioL = Integer.parseInt(patrimonioLiquido);
            if (patrimonioL < 10000){
                return (false);
            }else {
                return (true);
            }
        }
    }
    private void criarUsuario(){
        try {
            Bundle extras = getIntent().getExtras();
            String nome = extras.getString("nome");
            String cpf = extras.getString("cpf");
            String nascimento = extras.getString("nascimento");
            String email = extras.getString("email");
            String celular = extras.getString("celular");
            String senha = extras.getString("senha");
            Double saldo = 999.50;
            int agencia = 0001;
            int conta = verificaUltimaConta();
            conta = conta + 1;
            Log.e("Resultado: ", String.valueOf(conta));
            String rendaFormatada = formatPriceSave(renda.getText().toString());
            String patrimonioFormatado = formatPriceSave(patrimonio.getText().toString());
            SQLiteDatabase banco = openOrCreateDatabase("usuario", MODE_PRIVATE, null);
            //banco.execSQL("CREATE TABLE IF NOT EXISTS usuario (id INTEGER PRIMARY KEY AUTOINCREMENT, nomeCompleto VARCHAR(40),cpf NUMERIC(11), nascimento VARCHAR(10), email VARCHAR(40), celular NUMERIC(11), rendaMensal NUMERIC(9), valorPatrimonio NUMERIC(9),  senha NUMERIC(6))");
            banco.execSQL("INSERT INTO usuario(nomeCompleto, cpf, nascimento, email, celular, rendaMensal, valorPatrimonio, senha, saldo, agencia, conta, digito) VALUES (" + "'" + nome + "'" + "," + "'" + cpf + "'" + "," + "'" + nascimento + "'" + "," + "'" + email + "'" + "," + "'" + celular + "'" + "," + "'" + rendaFormatada + "'" + "," + "'" + patrimonioFormatado + "'" + "," + Integer.parseInt(senha) + ", " + saldo + "," + agencia + "," + conta + "," + digitoVerificadorConta(String.valueOf(conta))+")");
            //banco.execSQL("DELETE FROM usuario");
            //banco.execSQL("DROP TABLE usuario");
            banco.close();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private int digitoVerificadorConta(String conta){
        conta = conta.trim();
        conta = String.format("%1$12s", conta).replace(' ', '0');
        String indices = "543298765432";
        int soma = 0;
        for (int i = 0; i < conta.length(); i++){
            soma = soma + Integer.parseInt(String.valueOf(conta.charAt(i))) *
                    Integer.parseInt(String.valueOf(indices.charAt(i)));
        }
        int resto = soma % 11;
        int digito = 11 - resto;
        if (digito > 9){
            digito = 0;
        }
        return digito;
    }
    private int verificaUltimaConta(){
        try {
            SQLiteDatabase banco = openOrCreateDatabase("usuario", MODE_PRIVATE, null);
            String consulta = "SELECT MAX(conta) AS MaiorConta FROM usuario";
            Cursor cursor = banco.rawQuery(consulta, null);
            int indiceConta = cursor.getColumnIndex("MaiorConta");
            cursor.moveToFirst();
            String ultimaConta = cursor.getString(indiceConta);
            banco.close();
            cursor.close();
            return Integer.parseInt(ultimaConta);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }

    }



}