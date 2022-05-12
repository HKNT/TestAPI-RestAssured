package tests.desafio.ClassAuxDesafio;

public class Conta {
    private Long id;
    private String nome;
    private Boolean visivel;
    private Long usuario_id;


    public Conta() {
        setId(0L);
        setNome("nome");
        setVisivel(false);
        setUsuario_id(0L);
    }

    public Conta(String nome, Boolean visivel) {
        setNome(nome);
        setVisivel(visivel);
        setUsuario_id(usuario_id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getVisivel() {
        return visivel;
    }

    public void setVisivel(Boolean visivel) {
        this.visivel = visivel;
    }

    public Long getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(Long usuario_id) {
        this.usuario_id = usuario_id;
    }
}
