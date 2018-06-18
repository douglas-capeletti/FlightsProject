package modelo.gerenciadores;

import modelo.objetos.Aeroporto;
import modelo.objetos.Rota;

import java.util.ArrayList;
import java.util.List;

public class GerenciadorRotaComposta {
    private Aeroporto origem;
    private Aeroporto destino;
    private int numEscalas;
    private List<Rota> rotasOrigem;
    private List<Aeroporto> escalas = new ArrayList<>();

    public GerenciadorRotaComposta(GerenciadorRotas gerRotas, Aeroporto origem, Aeroporto destino, int numEscalas) {
        this.origem = origem;
        this.destino = destino;
        this.numEscalas = numEscalas;
        this.rotasOrigem = gerRotas.buscarOrigem(origem.getCodigo());
    }

    public Aeroporto getOrigem() {
        return origem;
    }

    public void setOrigem(Aeroporto origem) {
        this.origem = origem;
    }

    public Aeroporto getDestino() {
        return destino;
    }

    public void setDestino(Aeroporto destino) {
        this.destino = destino;
    }

    public List<Aeroporto> getEscalas() {
        return null;
    }

    public void addEscala(Aeroporto escala) {
        escalas.add(escala);
    }
}
