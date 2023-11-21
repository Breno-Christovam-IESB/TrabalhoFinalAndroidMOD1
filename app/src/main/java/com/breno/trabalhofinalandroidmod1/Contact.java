package com.breno.trabalhofinalandroidmod1;

//  Importações de ferramentas Java
import java.io.Serializable;

//  Define uma classe chamada Contact que implementa a interface Serializable.
public class Contact implements Serializable {

    //  Atributos da classe representando as informações de um contato.
    private long id;
    private String name;
    private String email;
    private String phone;

    //  Construtor da classe usado para criar uma instância de Contact com dados iniciais.
    public Contact(long id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    //  Método para obter o ID do contato.
    public long getId() {
        return id;
    }

    //  Método para obter o nome do contato.
    public String getName() {
        return name;
    }

    //  Método para definir o nome do contato.
    public void setName(String name) {
        this.name = name;
    }

    //  Método para obter o e-mail do contato.
    public String getEmail() {
        return email;
    }

    //  Método para definir o e-mail do contato.
    public void setEmail(String email) {
        this.email = email;
    }

    //  Método para obter o número de telefone do contato.
    public String getPhone() {
        return phone;
    }

    //  Método para definir o número de telefone do contato.
    public void setPhone(String phone) {
        this.phone = phone;
    }
}