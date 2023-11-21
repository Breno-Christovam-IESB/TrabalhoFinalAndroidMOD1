package com.breno.trabalhofinalandroidmod1;

//  Importações Android necessárias para a classe.
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;


//  Esta linha declara a classe EditContactActivity, que é a classe principal da atividade (ou tela) no Android.
//  Ela estende a classe AppCompatActivity, que é uma classe de suporte da biblioteca de compatibilidade do Android.
public class EditContactActivity extends AppCompatActivity {
    private Contact contact; //  Essa variável de membro armazena temporariamente o contato selecionado para edição.
    //  Quando o usuário clica em um item da lista para edição, o contato correspondente é atribuído a essa variável para que ele possa ser passado para a atividade de edição.
    private static final int EDIT_CONTACT_REQUEST = 2; //  Esta é uma constante inteira usada como código de solicitação para a atividade de edição. Quando a atividade de edição é iniciada usando startActivityForResult, ela recebe esse código.

    //  Utilizando ActivityResultLauncher para obter resultados
    private final ActivityResultLauncher<Intent> editContactLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        // Obter o contato editado da Intent de retorno
                        Intent data = result.getData();
                        if (data != null) {
                            Contact editedContact = (Contact) data.getSerializableExtra("editedContact");

                            // Atualizar o contato na lista e notificar o adapter
                            ((MainActivity) getParent()).updateContactList(editedContact);
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        EditText nameEditText = findViewById(R.id.nameEditText); //  Encontra o elemento EditText no layout pelo ID atribuído a ele no arquivo XML e associa a variável nameEditText a esse elemento.
        EditText emailEditText = findViewById(R.id.emailEditText); //  Encontra o elemento EditText no layout pelo ID atribuído a ele no arquivo XML e associa a variável emailEditText a esse elemento.
        EditText phoneEditText = findViewById(R.id.phoneEditText); //  Encontra o elemento EditText no layout pelo ID atribuído a ele no arquivo XML e associa a variável phoneEditText a esse elemento.
        Button saveButton = findViewById(R.id.saveButton); // Encontra o botão no layout pelo ID atribuído a ele no arquivo XML e associa a variável saveButton a esse botão. Que será usado para salvar as alterações feitas pelo usuário.

        // Obtém os dados do contato passados através da Intent
        contact = (Contact) getIntent().getSerializableExtra("contact");

        // Preenche os campos com os dados do contato
        nameEditText.setText(contact.getName());
        emailEditText.setText(contact.getEmail());
        phoneEditText.setText(contact.getPhone());

        saveButton.setOnClickListener(view -> {
            // Atualiza os dados do contato no banco de dados
            DatabaseHelper.getInstance(this).updateContact(contact.getId(),
                    nameEditText.getText().toString(),
                    emailEditText.getText().toString(),
                    phoneEditText.getText().toString());

            // Cria um novo contato com as informações atualizadas
            Contact editedContact = new Contact(
                    contact.getId(),
                    nameEditText.getText().toString(),
                    emailEditText.getText().toString(),
                    phoneEditText.getText().toString());

            // Cria uma Intent de retorno e adiciona o contato editado
            Intent resultIntent = new Intent();
            resultIntent.putExtra("editedContact", editedContact);

            // Define o resultado da atividade como RESULT_OK
            setResult(RESULT_OK, resultIntent);

            // Fecha a atividade de edição
            finishEditing();
        });
    }

    //  Método usado para iniciar a EditContactActivity com dados específicos do contato.
    //  Ele cria uma Intent, adiciona o objeto Contact como um extra com a chave "contact" e, em seguida, inicia a atividade usando o editContactLauncher.
    private void openEditActivity(Contact contact) {
        Intent intent = new Intent(EditContactActivity.this, EditContactActivity.class);
        intent.putExtra("contact", contact);
        editContactLauncher.launch(intent);
    }

    //  Encerra a atividade após configurar o resultado.
    private void finishEditing() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("updatedContact", contact);
        setResult(RESULT_OK, resultIntent);
        finishEditing();
    }
}