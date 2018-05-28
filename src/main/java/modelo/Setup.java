package modelo;

import modelo.gerenciadores.GerenciadorCias;

public class Setup {

	public static void main(String[] args) {

		GerenciadorCias gerCias = new GerenciadorCias();
		System.out.println(gerCias.listarTodas());

	}
}
