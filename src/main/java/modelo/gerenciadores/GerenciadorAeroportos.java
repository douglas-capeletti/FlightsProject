package modelo.gerenciadores;

import modelo.Util;
import modelo.objetos.Aeroporto;
import modelo.objetos.Geo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GerenciadorAeroportos {

    private Map<String, Aeroporto> aeroportos;
    private Util util = new Util();

    public GerenciadorAeroportos() {
        this.aeroportos = new HashMap<>();
    }

    public void adicionar(Aeroporto aero) {
        aeroportos.put(aero.getCodigo(), aero);
    }

    public ArrayList<Aeroporto> listarTodos() {
        return new ArrayList<>(aeroportos.values());
    }

    public Aeroporto buscarPorCodigo(String codigo) {
        return aeroportos.get(codigo);
    }

    public void carregaDados(){
        for (String[] linha :  util.carregaDados(Util.Arquivos.AEROPORTOS))
            aeroportos.put(linha[0], new Aeroporto(linha[0], linha[3], new Geo(Double.parseDouble(linha[1]), Double.parseDouble(linha[2]))));
    }

}
