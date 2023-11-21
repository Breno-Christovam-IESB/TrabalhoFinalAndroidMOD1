package com.breno.trabalhofinalandroidmod1;

//  Importações Android necessárias para a classe.
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

//  Importações de ferramentas Java
import java.util.ArrayList;

//  Define uma classe chamada ContactAdapter que estende BaseAdapter.
public class ContactAdapter extends BaseAdapter {

    //  Contexto da aplicação e lista de contatos a serem exibidos.
    private Context context;
    private ArrayList<Contact> contactList;

    //  Interfaces para lidar com cliques nos botões de exclusão e edição.
    private OnDeleteClickListener onDeleteClickListener;
    private OnEditClickListener onEditClickListener;

    //  Construtor da classe que recebe o contexto e a lista de contatos.
    public ContactAdapter(Context context, ArrayList<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    //  Interface para lidar com cliques no botão de exclusão.
    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    //  Método para definir o ouvinte de cliques no botão de exclusão.
    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    //  Interface para lidar com cliques no botão de edição.
    public interface OnEditClickListener {
        void onEditClick(int position);
    }

    //  Método para definir o ouvinte de cliques no botão de edição.
    public void setOnEditClickListener(OnEditClickListener listener) {
        this.onEditClickListener = listener;
    }

    //  Classe interna estática que representa a estrutura de uma linha na lista.
    private static class ViewHolder {
        TextView nameTextView;
        TextView emailTextView;
        TextView phoneTextView;
        TextView deleteTextView;
        Button editButton;  // Adiciona a declaração do botão de edição
    }

    //  Método que retorna o número de itens na lista.
    @Override
    public int getCount() {
        return contactList.size();
    }

    //  Método que retorna o item na posição especificada.
    @Override
    public Object getItem(int position) {
        return contactList.get(position);
    }

    //  Método que retorna o ID do item na posição especificada.
    @Override
    public long getItemId(int position) {
        return position;
    }

    //  Método chamado para criar ou reutilizar uma view para um item na posição especificada.
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        //  Verifica se a view está sendo reutilizada. Se não estiver, infla uma nova.
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.contact_item, parent, false);

            //  Inicializa a ViewHolder e associa os elementos de layout aos membros da ViewHolder.
            viewHolder = new ViewHolder();
            viewHolder.nameTextView = convertView.findViewById(R.id.nameTextView);
            viewHolder.emailTextView = convertView.findViewById(R.id.emailTextView);
            viewHolder.phoneTextView = convertView.findViewById(R.id.phoneTextView);
            viewHolder.deleteTextView = convertView.findViewById(R.id.deleteTextView);
            viewHolder.editButton = convertView.findViewById(R.id.editButton);  // Atribui o botão de edição

            //  Define a ViewHolder como uma tag da convertView.
            convertView.setTag(viewHolder);
        } else {
            //  Se a view está sendo reutilizada, obtém a ViewHolder da tag.
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //  Obtém o contato na posição atual.
        Contact contact = contactList.get(position);

        //  Define os textos dos TextViews com os dados do contato.
        viewHolder.nameTextView.setText(contact.getName());
        viewHolder.emailTextView.setText(contact.getEmail());
        viewHolder.phoneTextView.setText(contact.getPhone());

        //  Configura o clique do botão de edição.
        viewHolder.editButton.setOnClickListener((View v) -> {
            if (onEditClickListener != null) {
                onEditClickListener.onEditClick(position);
            }
        });

        //  Configura o clique do botão de exclusão.
        viewHolder.deleteTextView.setOnClickListener(view -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(position);
            }
        });

        //  Retorna a convertView atualizada para ser exibida na lista.
        return convertView;
    }
}