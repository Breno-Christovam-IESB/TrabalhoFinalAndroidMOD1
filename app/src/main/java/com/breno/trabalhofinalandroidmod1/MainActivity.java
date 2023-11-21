package com.breno.trabalhofinalandroidmod1;

//  Importações Android necessárias para a classe.
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.content.Intent;
import androidx.annotation.Nullable;
import android.view.View;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

//  Importações de ferramentas Java
import java.util.ArrayList;

//  Importações de materiais definidos como o botão flutuante de adição
import com.google.android.material.floatingactionbutton.FloatingActionButton;


//  Esta linha declara a classe MainActivity, que é a classe principal da atividade (ou tela) no Android.
//  Ela estende a classe AppCompatActivity, que é uma classe de suporte da biblioteca de compatibilidade do Android.
public class MainActivity extends AppCompatActivity {

    //  Instância do ContactAdapter, que é usado para exibir os contatos em um ListView.
    private ContactAdapter contactAdapter;

    //  Lista de contatos que armazena os dados dos contatos exibidos na tela.
    private ArrayList<Contact> contactList;

    //  Solicitação usada para identificar os resultados de atividades de adição. Ele é marcado como static final para indicar que é uma constante.
    private static final int ADD_CONTACT_REQUEST = 1;

    //  Solicitação usada para identificar os resultados de atividades de edição. Ele é marcado como static final para indicar que é uma constante.
    private static final int EDIT_CONTACT_REQUEST = 2;

    // Inicialização do lançador de resultados para a atividade de edição
    private final ActivityResultLauncher<Intent> editContactLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // Verifica se a atividade foi concluída com sucesso
                if (result.getResultCode() == RESULT_OK) {
                    // Obtém os dados retornados pela atividade de edição
                    Intent data = result.getData();
                    if (data != null) {
                        // Extrai o contato atualizado dos dados e atualiza a lista
                        Contact updatedContact = (Contact) data.getSerializableExtra("updatedContact");
                        updateContactList(updatedContact);
                    }
                }
            }
    );

    // Inicialização do lançador de resultados para a atividade de adição
    private final ActivityResultLauncher<Intent> addContactLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // Verifica se a atividade foi concluída com sucesso
                if (result.getResultCode() == RESULT_OK) {
                    // Obtém os dados retornados pela atividade de adição
                    Intent data = result.getData();
                    if (data != null) {
                        // Extrai o novo contato dos dados e atualiza a lista
                        Contact newContact = (Contact) data.getSerializableExtra("newContact");
                        updateContactList(newContact);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicia a lista de contatos a partir do banco de dados SQLite
        contactList = DatabaseHelper.getInstance(this).getAllContacts();

        // Inicia o adapter
        contactAdapter = new ContactAdapter(this, contactList);

        // Configura o ListView
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(contactAdapter);

        // Adiciona listeners para editar e excluir contatos
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Abre a tela de edição com os dados do contato selecionado
            Contact selectedContact = contactList.get(position);
            openEditActivity(selectedContact);
        });

        contactAdapter.setOnDeleteClickListener(position -> {
            // Exclui o contato do banco de dados
            DatabaseHelper.getInstance(MainActivity.this).deleteContact(contactList.get(position).getId());
            // Remove o contato da lista e notifica o adapter
            contactList.remove(position);
            contactAdapter.notifyDataSetChanged();
        });

        contactAdapter.setOnEditClickListener(position -> {
            // Abre a tela de edição com os dados do contato selecionado
            Contact selectedContact = contactList.get(position);
            openEditActivity(selectedContact);
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            // Inicia a atividade de adição de contato
            Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
            addContactLauncher.launch(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_CONTACT_REQUEST && resultCode == RESULT_OK && data != null) {
            // Obtém o novo contato adicionado
            Contact newContact = (Contact) data.getSerializableExtra("newContact");

            // Atualiza a lista na atividade principal
            updateContactList(newContact);
        } else if (requestCode == EDIT_CONTACT_REQUEST && resultCode == RESULT_OK && data != null) {
            // Lógica para tratar o resultado da edição
            // Exemplo: você pode obter o contato atualizado da Intent e atualizar a lista
            Contact updatedContact = (Contact) data.getSerializableExtra("updatedContact");
            updateContactList(updatedContact);
        }
    }

    public void updateContactList(Contact newContact) {
        // Adiciona o novo contato à lista e notifica o adapter
        contactList.add(newContact);
        contactAdapter.notifyDataSetChanged();
    }

    private void openEditActivity(Contact contact) {
        // Cria uma nova Intent para iniciar a atividade de edição (EditContactActivity)
        Intent intent = new Intent(MainActivity.this, EditContactActivity.class);

        // Adiciona dados extras à Intent, neste caso, o objeto Contact que será editado
        intent.putExtra("contact", contact);

        // Inicia a atividade de edição usando o editContactLauncher, que é um lançador de resultados
        // Este método repôe startActivityForResult e usa a API Activity Result, introduzida no AndroidX
        editContactLauncher.launch(intent);
    }

    public void onEditButtonClick(View view) {
        // Obtém a posição da lista com base no clique do botão de edição
        int position = (int) view.getTag();

        // Abre a tela de edição com os dados do contato selecionado
        openEditActivity(contactList.get(position));
    }
}

