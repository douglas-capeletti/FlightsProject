package modelo.objetos;

import gui.MyWaypoint;

import java.awt.*;

public class Aeroporto{
	private String codigo;
	private String nome;
	private Geo loc;
	
	public Aeroporto(String codigo, String nome, Geo loc) {
		this.codigo = codigo;
		this.nome = nome;
		this.loc = loc;
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
