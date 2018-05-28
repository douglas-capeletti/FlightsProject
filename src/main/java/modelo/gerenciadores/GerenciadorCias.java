package modelo.gerenciadores;

import modelo.Util;
import modelo.objetos.CiaAerea;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GerenciadorCias {
    private Map<String, CiaAerea> empresas;
    private Util util = new Util();

    public GerenciadorCias() {
        this.empresas = new HashMap<>();
    }

    public ArrayList<CiaAerea> listarTodas() {
        return new ArrayList<>(empresas.values());
    }

    public void adicionar(CiaAerea cia1) {
        empresas.put(cia1.getCodigo(),cia1);
    }

    public CiaAerea buscarCodigo(String cod) {
        return empresas.get(cod);
    }

    public CiaAerea buscarNome(String nome) {
        for(CiaAerea cia: empresas.values()) if(cia.getNome().equals(nome)) return cia;
        return null;
    }

    public void carregaDados(){
        for (String[] linha :  util.carregaDados(Util.Arquivos.CIA_AEREA))
            empresas.put(linha[0], new CiaAerea(linha[0], linha[1]));
    }
}
