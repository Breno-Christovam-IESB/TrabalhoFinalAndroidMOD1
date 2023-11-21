package com.breno.trabalhofinalandroidmod1;

//  Importações Android necessárias para a classe.
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//  Importações de ferramentas Java
import java.util.ArrayList;


//  Define uma classe chamada DatabaseHelper que estende SQLiteOpenHelper.
public class DatabaseHelper extends SQLiteOpenHelper {

    //  Nome do banco de dados SQLite.
    private static final String DATABASE_NAME = "contacts.db";

    //  Versão do banco de dados. Alterações no esquema do banco de dados devem incrementar isso.
    private static final int DATABASE_VERSION = 1;

    //  Nome da tabela no banco de dados.
    private static final String TABLE_CONTACTS = "contacts";

    //  Nomes das colunas na tabela de contatos.
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHONE = "phone";

    //  Instância única da classe DatabaseHelper usando o padrão Singleton.
    private static DatabaseHelper instance;

    //  Construtor privado para evitar a criação de múltiplas instâncias da classe.
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //  Método para obter a instância única da classe DatabaseHelper.
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    //  Método chamado quando o banco de dados é criado pela primeira vez.
    @Override
    public void onCreate(SQLiteDatabase db) {
        //  Cria a tabela de contatos no banco de dados.
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_PHONE + " TEXT)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    //  Método chamado quando a versão do banco de dados é atualizada.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //  Descarta a tabela antiga e recria uma nova quando a versão do banco de dados é incrementada.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    //  Método para adicionar um novo contato ao banco de dados.
    public long addContact(String name, String email, String phone) {
        // Obtém uma referência gravável ao banco de dados.
        SQLiteDatabase db = this.getWritableDatabase();

        // Cria um conjunto de valores para serem inseridos na tabela.
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PHONE, phone);

        // Insere os valores na tabela e obtém o ID do novo registro.
        long id = db.insert(TABLE_CONTACTS, null, values);

        // Fecha o banco de dados para liberar recursos.
        db.close();

        // Retorna o ID do novo contato.
        return id;
    }

    // Método para recuperar todos os contatos do banco de dados.
    public ArrayList<Contact> getAllContacts() {
        // Lista para armazenar os contatos recuperados.
        ArrayList<Contact> contactList = new ArrayList<>();

        // Consulta SQL para recuperar todos os registros da tabela de contatos.
        String selectQuery = "SELECT * FROM " + TABLE_CONTACTS;

        // Obtém uma referência legível ao banco de dados.
        SQLiteDatabase db = this.getWritableDatabase();

        // Executa a consulta e obtém um cursor para percorrer os resultados.
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Verifica se há pelo menos um resultado.
        if (cursor.moveToFirst()) {
            do {
                // Obtém índices das colunas na consulta.
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
                int emailIndex = cursor.getColumnIndex(COLUMN_EMAIL);
                int phoneIndex = cursor.getColumnIndex(COLUMN_PHONE);

                // Verifica se os índices são válidos.
                if (idIndex >= 0 && nameIndex >= 0 && emailIndex >= 0 && phoneIndex >= 0) {
                    // Obtém os valores das colunas.
                    long id = cursor.getLong(idIndex);
                    String name = cursor.getString(nameIndex);
                    String email = cursor.getString(emailIndex);
                    String phone = cursor.getString(phoneIndex);

                    // Cria um objeto Contact e o adiciona à lista.
                    Contact contact = new Contact(id, name, email, phone);
                    contactList.add(contact);
                } else {
                    // Lida com índices inválidos, se necessário.
                }
            } while (cursor.moveToNext());
        }

        // Fecha o cursor e o banco de dados.
        cursor.close();
        db.close();

        // Retorna a lista de contatos.
        return contactList;
    }

    // Método para atualizar um contato existente no banco de dados.
    public void updateContact(long id, String name, String email, String phone) {
        // Obtém uma referência gravável ao banco de dados.
        SQLiteDatabase db = this.getWritableDatabase();

        // Cria um conjunto de valores com os novos dados do contato.
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PHONE, phone);

        // Atualiza o registro na tabela com base no ID.
        db.update(TABLE_CONTACTS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});

        // Fecha o banco de dados.
        db.close();
    }

    // Método para excluir um contato do banco de dados.
    public void deleteContact(long id) {
        // Obtém uma referência gravável ao banco de dados.
        SQLiteDatabase db = this.getWritableDatabase();

        // Exclui o registro da tabela com base no ID.
        db.delete(TABLE_CONTACTS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});

        // Fecha o banco de dados.
        db.close();
    }
}