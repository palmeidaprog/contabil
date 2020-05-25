package com.react.contabil.usuario;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.react.contabil.datalayer.dataobject.UsuarioDO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Usuario {

    @Size(max = 15, message="O codigo do usuário não pode ser maior que {max}")
    private Long codigo;

    @Size(max = 16, message="O login do usuário não pode ser maior que {max}")
    @NotBlank(message="O login do usuário não pode ser nulo")
    private String login;

    @NotBlank(message="O nome do usuário não pode ser nulo")
    private String nome;

    private String sobrenome;

    private boolean congelado;

    public Usuario() { }

    public Usuario(UsuarioDO usuarioDO) {
        this.codigo = usuarioDO.getCodigo();
        this.login = usuarioDO.getLogin();
        this.nome = usuarioDO.getNome();
        this.sobrenome = usuarioDO.getSobrenome();
        this.congelado = usuarioDO.isCongelado();
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public boolean isCongelado() {
        return congelado;
    }

    public void setCongelado(boolean congelado) {
        this.congelado = congelado;
    }

    @Override
    public String toString() {
        return "Usuario codigo: " + this.codigo +  " login: " + this.login;
    }

    /**
     * Gera usuario data Object
     * @return usuarioDO
     */
    public UsuarioDO toDataObject() {
         final UsuarioDO usuarioDO = new UsuarioDO();
         usuarioDO.setCongelado(this.congelado);
         usuarioDO.setNome(this.nome);
         usuarioDO.setSobrenome(this.sobrenome);
         usuarioDO.setLogin(this.login);
         usuarioDO.setCodigo(this.codigo);

         return usuarioDO;
    }
}
