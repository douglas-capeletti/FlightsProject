package modelo.gerenciadores;

import modelo.Util;
import modelo.objetos.Aeronave;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GerenciadorAeronaves {

    private Map<String, Aeronave> avioes;
    private Util util = new Util();

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

    public void carregaDados(){
        for (String[] linha :  util.carregaDados(Util.Arquivos.AERONAVES))
            avioes.put(linha[0], new Aeronave(linha[0], linha[1], Integer.parseInt(linha[2])));
    }
}
