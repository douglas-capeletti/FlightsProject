package modelo.objetos;

public class Paises {
    private String codigo;
    private String nome;

    public Paises(String codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString(){
        return "(" + codigo + ") - " + nome + "\n";
    }
}
