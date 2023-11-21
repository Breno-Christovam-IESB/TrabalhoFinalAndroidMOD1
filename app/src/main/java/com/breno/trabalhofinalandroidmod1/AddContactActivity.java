package com.breno.trabalhofinalandroidmod1;

//  Importações Android necessárias para a classe.
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

//  Define uma atividade (Activity) para adicionar um novo contato.
public class AddContactActivity extends AppCompatActivity {

    //  Método chamado quando a atividade é criada.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //  Define o layout da atividade a partir do arquivo XML.
        setContentView(R.layout.activity_add_contact);

        //  Obtém referências aos elementos de interface do usuário (UI) na atividade.
        EditText nameEditText = findViewById(R.id.nameEditText);
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText phoneEditText = findViewById(R.id.phoneEditText);
        Button saveButton = findViewById(R.id.saveButton);

        //  Configura um ouvinte de clique para o botão de salvar.
        saveButton.setOnClickListener(view -> {
            //  Obtém os textos inseridos nos campos de edição.
            String name = nameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String phone = phoneEditText.getText().toString();

            //  Salva os dados no banco de dados.
            long contactId = DatabaseHelper.getInstance(this).addContact(name, email, phone);

            //  Cria um objeto Contact com os dados inseridos.
            Contact newContact = new Contact(contactId, name, email, phone);

            //  Configura o intent para retornar o novo contato à atividade principal
            Intent resultIntent = new Intent();
            resultIntent.putExtra("newContact", newContact);
            setResult(RESULT_OK, resultIntent);

            //  Fecha a atividade de adição
            finish();
        });
    }
}