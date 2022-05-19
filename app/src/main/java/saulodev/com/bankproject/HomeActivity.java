package saulodev.com.bankproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    private ImageView exibeDados;
    private ImageView transferir;
    private ImageView eyes;
    private TextView nome;
    private TextView saldo;
    private TextView agencia;
    private TextView conta;
    private int cont = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        inicializaComponentes();
        String saldoAtual = saldo.getText().toString();
        String agenciaAtual = agencia.getText().toString();
        String contaAtual = conta.getText().toString();
        exibeDados.setOnClickListener(View ->{abrirExibeDados();});
        transferir.setOnClickListener(View ->{ abrirTransferencia();});

        eyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cont == 1) {
                    eyes.setImageResource(R.drawable.ic_eye_off);
                    saldo.setText("Saldo: ****** R$");
                    agencia.setText("Agencia: *****");
                    conta.setText("Conta: *******");
                    cont = 0;

                } else {
                    eyes.setImageResource(R.drawable.ic_eye);
                    saldo.setText(saldoAtual);
                    agencia.setText(agenciaAtual);
                    conta.setText(contaAtual);
                    cont = 1;
                }
            }
        });


    }

    private void inicializaComponentes(){
        exibeDados = findViewById(R.id.profile);
        transferir = findViewById(R.id.transferir);
        nome = findViewById(R.id.personName);
        saldo = findViewById(R.id.saldo);
        agencia = findViewById(R.id.agencia);
        conta = findViewById(R.id.conta);
        eyes = findViewById(R.id.Eyes);
        exibeNome();
    }

    private void abrirExibeDados(){
        Intent passaDados = new Intent(getApplicationContext(), ExibeDadosActivity.class);
        Bundle extras = getIntent().getExtras();
        String cpfResgatado = extras.getString("cpf");
        passaDados.putExtra("cpf", cpfResgatado);
        startActivity(passaDados);
    }

    private void abrirTransferencia(){
        Intent passaDados = new Intent(getApplicationContext(), TransferenciaActivity.class);
        Bundle extras = getIntent().getExtras();
        String cpfResgatado = extras.getString("cpf");
        passaDados.putExtra("cpf", cpfResgatado);
        startActivity(passaDados);
    }

    private void exibeNome(){
        Bundle extras = getIntent().getExtras();
        String cpfResgatado = extras.getString("cpf");


        SQLiteDatabase banco = openOrCreateDatabase("usuario", MODE_PRIVATE, null);
        String consulta = "SELECT nomeCompleto, saldo, agencia, conta, digito FROM usuario WHERE cpf = " + cpfResgatado;
        Cursor cursor = banco.rawQuery(consulta, null);
        int indiceNome = cursor.getColumnIndex("nomeCompleto");
        int indiceSaldo = cursor.getColumnIndex("saldo");
        int indiceAgencia = cursor.getColumnIndex("agencia");
        int indiceConta = cursor.getColumnIndex("conta");
        int indiceDigito = cursor.getColumnIndex("digito");


        cursor.moveToFirst();
        String nome = cursor.getString(indiceNome);
        String saldo = cursor.getString(indiceSaldo);
        String agencia = cursor.getString(indiceAgencia);
        String conta = cursor.getString(indiceConta);
        String digito = cursor.getString(indiceDigito);

        cursor.close();
        banco.close();

        this.nome.setText("Olá, "+ nome);
        this.saldo.setText("Saldo: "+ saldo + " R$");
        this.agencia.setText("Agencia: 0000"+ agencia);
        this.conta.setText("Conta: 0000" + conta + "-" + digito);
    }

    @Override
    protected void onResume() {
        super.onResume();
        exibeNome();
    }
}