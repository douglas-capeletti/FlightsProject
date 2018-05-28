package modelo.gerenciadores;

import modelo.objetos.Aeronave;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GerenciadorAeronaves {

    private Map<String, Aeronave> avioes;

    public GerenciadorAeronaves(){
        this.avioes = new HashMap<>();
    }

    public void adicionar(Aeronave aviao) {
        avioes.put(aviao.getCodigo(), aviao);
    }

    public ArrayList<Aeronave> listarTodas() {
        return new ArrayList<>(avioes.values());
    }

    public Aeronave buscarPorCodigo(String codigo) {
        return avioes.get(codigo);
    }

}
