package modelo.objetos;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Voo {
	
	public enum Status { CONFIRMADO, ATRASADO, CANCELADO }
	
	private LocalDateTime datahora;
	private Duration duracao;
	private Rota rota;
	private Status status;
	private DateTimeFormatter formatador;

	public Voo(Rota rota, LocalDateTime datahora, Duration duracao) {
		this.rota = rota;
		this.datahora = datahora;
		this.duracao = duracao;
		this.status = Status.CONFIRMADO; // default é confirmado
        this.formatador = DateTimeFormatter.ofPattern("dd/MMM/yyyy HH:mm");
	}
	
	public Rota getRota() {
		return rota;
	}
	
	public LocalDateTime getDatahora() {
		return datahora;
	}
	
	public Duration getDuracao() {
		return duracao;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status novo) {
		this.status = novo;
	}

    @Override
    public String toString() {
        return rota + " : " + formatador.format(datahora) + " [" +  duracao + "] - " + status + "\n";
    }
}

