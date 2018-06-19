package modelo.objetos;

import gui.MyWaypoint;

import java.awt.*;

public class Aeroporto{
	private String codigo;
	private String nome;
	private Geo loc;
	private Pais pais;
	
	public Aeroporto(String codigo, String nome, Geo loc, Pais pais) {
		this.codigo = codigo;
		this.nome = nome;
		this.loc = loc;
		this.pais = pais;
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	public String getNome() {
		return nome;
	}
	
	public Geo getLocal() {
		return loc;
	}

	public Pais getPais() {
		return pais;
	}

	public MyWaypoint waypoint(int trafego){
		// TODO
		// calculo dinamico da cor baseado no trafego
		return new MyWaypoint(Color.BLUE, codigo, loc, trafego);
	}

    @Override
    public String toString() {
        return nome;
    }
}
