package saulodev.com.bankproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CadastroActivity extends AppCompatActivity {

    private EditText nome;
    private EditText cpf;
    private EditText nascimentoValidado;
    private EditText email;
    private EditText celular;
    private EditText senha;
    private TextView irLogin;
    private EditText confirmarSenha;
    private Button button;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        inicializaComponentes();
        button.setOnClickListener(view -> {

            if (!validaNome(nome.getText().toString())) {
                Toast.makeText(getApplicationContext(), "É necessário informar um Nome válido!", Toast.LENGTH_SHORT).show();
            } else if (!validaCPF(cpf.getText().toString())) {
                Toast.makeText(getApplicationContext(), "É necessário informar um CPF válido!", Toast.LENGTH_SHORT).show();
            } else if (!validaNascimento(nascimentoValidado.getText().toString())) {
                Toast.makeText(getApplicationContext(), "É necessário ser maior de 18 anos ou informar uma data válida!", Toast.LENGTH_SHORT).show();
            } else if (!validaEmail(email.getText().toString())) {
                Toast.makeText(getApplicationContext(), "É necessário informar um e-mail válido!", Toast.LENGTH_SHORT).show();
            } else if (!validaCelular(celular.getText().toString())) {
                Toast.makeText(getApplicationContext(), "É necessário informar um celular válido!", Toast.LENGTH_SHORT).show();
            } else if (!validaSenha(senha.getText().toString())) {

            } else if (!validaConfirmarSenha(confirmarSenha.getText().toString())) {
                Toast.makeText(getApplicationContext(), "É necessário as senhas serem iguais!", Toast.LENGTH_SHORT).show();
            }else if(!verificaDuplicidade()){

            } else {
                    enviarUsuario();
                }
        });
        irLogin.setOnClickListener(view -> abrirLogin());
    }

    private boolean validaNome(String nomeValidando) {
        return nomeValidando.length() >= 5;
    }

    private boolean validaCPF(String cpfValidando) {
        String ponto = "\\.";
        cpfValidando = cpfValidando.replaceAll(ponto, "");
        cpfValidando = cpfValidando.replaceAll("-", "");
        if (cpfValidando.isEmpty() || cpfValidando.length() < 11) {
            return false;
        } else {
            // considera-se erro CPF's formados por uma sequencia de numeros iguais
            if (cpfValidando.equals("00000000000") ||
                    cpfValidando.equals("11111111111") ||
                    cpfValidando.equals("22222222222") || cpfValidando.equals("33333333333") ||
                    cpfValidando.equals("44444444444") || cpfValidando.equals("55555555555") ||
                    cpfValidando.equals("66666666666") || cpfValidando.equals("77777777777") ||
                    cpfValidando.equals("88888888888") || cpfValidando.equals("99999999999") ||
                    (cpfValidando.length() != 11))
                return (false);

            char dig10, dig11;
            int sm, i, r, num, peso;

            // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
            try {
                // Calculo do 1o. Digito Verificador
                sm = 0;
                peso = 10;
                for (i = 0; i < 9; i++) {
                    // converte o i-esimo caractere do CPF em um numero:
                    // por exemplo, transforma o caractere '0' no inteiro 0
                    // (48 eh a posicao de '0' na tabela ASCII)
                    num = cpfValidando.charAt(i) - 48;
                    sm = sm + (num * peso);
                    peso = peso - 1;
                }

                r = 11 - (sm % 11);
                if ((r == 10) || (r == 11))
                    dig10 = '0';
                else dig10 = (char) (r + 48); // converte no respectivo caractere numerico

                // Calculo do 2o. Digito Verificador
                sm = 0;
                peso = 11;
                for (i = 0; i < 10; i++) {
                    num = cpfValidando.charAt(i) - 48;
                    sm = sm + (num * peso);
                    peso = peso - 1;
                }

                r = 11 - (sm % 11);
                if ((r == 10) || (r == 11))
                    dig11 = '0';
                else dig11 = (char) (r + 48);

                // Verifica se os digitos calculados conferem com os digitos informados.
                return (dig10 == cpfValidando.charAt(9)) && (dig11 == cpfValidando.charAt(10));
            } catch (InputMismatchException erro) {
                return (false);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean validaNascimento(String nascimentoValidando) {
        if(isDateValid(nascimentoValidando) == true) {
            if (nascimentoValidando.isEmpty() || nascimentoValidando.length() < 8) {
                //Inválido
                return (false);
            } else {
                int tamanho = nascimentoValidando.length(); //Pega o tamanho da string de data de nascimento digitada
                int numero;
                int[] dataNascimentoAno = {0, 0, 0, 0};//Array para armazenar os digitos que compõe o ano
                //Passando os numeros do ano para o array
                for (int i = 0; i < 4; i++) {
                    numero = Integer.parseInt(String.valueOf(nascimentoValidando.charAt(tamanho - 1 - i)));
                    dataNascimentoAno[3 - i] = numero;
                }
                String dataNascimentoFinal = dataNascimentoAno[0] + String.valueOf(dataNascimentoAno[1]) + dataNascimentoAno[2] + dataNascimentoAno[3];
                //Válido
                return Integer.parseInt(dataNascimentoFinal) < 2022 && 2022 - Integer.parseInt(dataNascimentoFinal) >= 18 && 2022 - Integer.parseInt(dataNascimentoFinal) <= 122;
            }
        }else {
            return false;
        }
    }

    private boolean validaEmail(String emailValidando) {
        boolean isEmailIdValid = false;
        if (emailValidando != null && emailValidando.length() > 0) {
            String expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(emailValidando);
            if (matcher.matches()) {
                isEmailIdValid = true;
            }
        }
        return isEmailIdValid;
    }

    private boolean validaCelular(String celularValidando) {
        celularValidando = celularValidando.replaceAll("\\(","");
        celularValidando = celularValidando.replaceAll("\\)","");
        celularValidando = celularValidando.replaceAll("-","");
        return celularValidando.length() >= 11;
    }

    private boolean validaSenha(String senhaValidando) {
        if (senhaValidando.indexOf("0") == 0){
            Toast.makeText(getApplicationContext(), "A senha não pode começar com 0", Toast.LENGTH_SHORT).show();
            return (false);
        }
        if (senhaValidando.equals("000000") || senhaValidando.equals("111111") ||
                senhaValidando.equals("222222") || senhaValidando.equals("333333") ||
                senhaValidando.equals("444444") || senhaValidando.equals("555555") ||
                senhaValidando.equals("666666") || senhaValidando.equals("777777") ||
                senhaValidando.equals("888888") || senhaValidando.equals("999999") ||
                senhaValidando.length() < 6){
            Toast.makeText(getApplicationContext(), "É necessário informar uma senha válida!", Toast.LENGTH_SHORT).show();
            return (false);
        }else{
            return (true);
        }
    }

    private boolean validaConfirmarSenha(String senhaConfirma_Validando) {
        return senhaConfirma_Validando.equals(senha.getText().toString());
    }

    private void inicializaComponentes() {

        nome = findViewById(R.id.edt_nome_completo);
        cpf = findViewById(R.id.edt_cpf);
        cpf.addTextChangedListener(TextMask.insert(TextMask.CPF_MASK, cpf));
        nascimentoValidado = findViewById(R.id.edt_data_nascimento);
        nascimentoValidado.addTextChangedListener(TextMask.insert(TextMask.NASCIMENTO_MASK, nascimentoValidado));
        email = findViewById(R.id.edt_email);
        celular = findViewById(R.id.edt_celular);
        celular.addTextChangedListener(TextMask.insert(TextMask.CELULAR_MASK, celular));
        senha = findViewById(R.id.edt_senha);
        confirmarSenha = findViewById(R.id.confirmaSenha);
        irLogin = findViewById(R.id.irCadastro);
        button = findViewById(R.id.cadastrar);


    }

    private void enviarUsuario() {
        try {
            //SQLiteDatabase banco = openOrCreateDatabase("usuario", MODE_PRIVATE, null);
            //banco.execSQL("CREATE TABLE IF NOT EXISTS usuario (id INTEGER PRIMARY KEY AUTOINCREMENT, nomeCompleto VARCHAR(40),cpf NUMERIC(11), nascimento VARCHAR(10), email VARCHAR(40), celular NUMERIC(11), rendaMensal NUMERIC(9), valorPatrimonio NUMERIC(9),  senha NUMERIC(6))");
            //banco.execSQL("INSERT INTO usuario(nomeCompleto, cpf, nascimento, email, celular, senha) VALUES (" + "'" + nome.getText().toString() + "'" + "," + Long.parseLong(cpf.getText().toString()) + "," + "'" + nascimentoValidado.getText().toString() + "'" + "," + "'" + email.getText().toString() + "'" + "," + Long.parseLong(celular.getText().toString()) + "," + Integer.parseInt(senha.getText().toString()) + ")");
            //banco.execSQL("DELETE FROM usuario");
            String ponto = "\\.";
            String cpfCerto = cpf.getText().toString().replaceAll( ponto, "");
            cpfCerto = cpfCerto.replaceAll("-", "");
            Intent intent = new Intent(getApplicationContext(), CadastrosFinancasActivity.class);
            intent.putExtra("nome", nome.getText().toString());
            intent.putExtra("cpf", cpfCerto);
            intent.putExtra("nascimento", nascimentoValidado.getText().toString());
            intent.putExtra("email", email.getText().toString());
            intent.putExtra("celular", celular.getText().toString());
            intent.putExtra("senha", senha.getText().toString());
            Log.e("Resultado: ", cpf.getText().toString());
            Log.e("Resultado: ", nascimentoValidado.getText().toString());
            Log.e("Resultado: ", email.getText().toString());
            Log.e("Resultado: ", celular.getText().toString());
            Log.e("Resultado: ", senha.getText().toString());
            Random gerador = new Random();
            int codigo = (gerador.nextInt(999999));
            intent.putExtra("codigo", String.valueOf(codigo));
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {

                        MailSenderActivity sender = new MailSenderActivity("conectbankpj@gmail.com",
                                "projeto123");
                        sender.sendMail("Confirmação de email! |- CONECT BANK -|", "Seu código de cadastro é: " +String.valueOf(codigo),
                                email.getText().toString(), email.getText().toString());
                        Log.e("resultado", ""+ String.valueOf(codigo));
                        startActivity(intent);
                    } catch (Exception e) {
                        Log.e("SendMail", e.getMessage(), e);
                    }
                }

            }).start();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void abrirLogin(){
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isDateValid(String strDate) {
        if (strDate.isEmpty() || strDate.length() < 10){
            return false;
        }
        String dataDia = strDate.substring(0 , 2);
        String dataMes = strDate.substring(3 , 5);
        String dataAno = strDate.substring(6 , 10);
        String dateFormat = "dd/MM/uuuu";

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                .ofPattern(dateFormat)
                .withResolverStyle(ResolverStyle.STRICT);
        try {
            LocalDate date = LocalDate.parse(strDate, dateTimeFormatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean verificaDuplicidade(){
        int cont = 0;
        String cpfEncontrado = null;
        String celularEncontrado = null;
        String emailEncontrado = null;
        Cursor cursor = null;
        SQLiteDatabase banco = openOrCreateDatabase("usuario", MODE_PRIVATE, null);
        try {
            String consulta = "SELECT cpf FROM usuario WHERE cpf = "+ cpf.getText().toString() +"";
            cursor = banco.rawQuery(consulta, null);
            int indiceCpf = cursor.getColumnIndex("cpf");
            cursor.moveToFirst();
            cpfEncontrado = cursor.getString(indiceCpf);
        }catch (Exception e){
            e.printStackTrace();
            cont = cont + 1;
        }
        if (cpfEncontrado != null){
            Toast.makeText(getApplicationContext(), "Cpf já cadastrado!", Toast.LENGTH_SHORT).show();
            return (false);
        }
        try {
            String consulta = "SELECT email FROM usuario WHERE email = "+"'"+ email.getText().toString() + "'"+"";
            cursor = banco.rawQuery(consulta, null);
            int indiceEmail = cursor.getColumnIndex("email");
            cursor.moveToFirst();
            emailEncontrado = cursor.getString(indiceEmail);
        }catch (Exception e){
        e.printStackTrace();
        cont = cont + 1;
        }
        if (emailEncontrado != null){
            Toast.makeText(getApplicationContext(), "Email já cadastrado!", Toast.LENGTH_SHORT).show();
            return (false);
        }
        try {
            String consulta = "SELECT celular FROM usuario WHERE celular = "+ celular.getText().toString() +"";
            cursor = banco.rawQuery(consulta, null);
            int indiceCelular = cursor.getColumnIndex("celular");
            cursor.moveToFirst();
            celularEncontrado = cursor.getString(indiceCelular);
        }catch (Exception e){
            e.printStackTrace();
            cont = cont + 1;
        }
        if (celularEncontrado != null){
            Toast.makeText(getApplicationContext(), "Celular já cadastrado!", Toast.LENGTH_SHORT).show();
            return (false);
        }
        if (cont == 3){
            banco.close();
            cursor.close();
            return (true);
            
        }else {
            banco.close();
            cursor.close();
            return (false);
        }
    }

}
