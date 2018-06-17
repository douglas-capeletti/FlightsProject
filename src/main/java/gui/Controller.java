package gui;

import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import modelo.Setup;
import modelo.gerenciadores.*;
import modelo.objetos.Aeroporto;
import modelo.objetos.Geo;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    private final SwingNode mapkit = new SwingNode();
    private GerenciadorMapa gerMapa;
    private Controller.EventosMouse mouse;
    private ContextMenu contextMenu = new ContextMenu();

    private GerenciadorAeronaves gerAvioes = new GerenciadorAeronaves();
    private GerenciadorAeroportos gerAero = new GerenciadorAeroportos();
    private GerenciadorCias gerCias = new GerenciadorCias();
    private GerenciadorPaises gerPaises = new GerenciadorPaises();
    private GerenciadorRotas gerRotas = new GerenciadorRotas();

    @FXML BorderPane PainelPrincipal;
    @FXML Button BTNFlipMap;

    // Pesquisa Cia Aerea
    @FXML ComboBox CBCiaAerea;
    @FXML ComboBox CBRotaCiaAerea;

    // Pesquisa Aeroporto;
    @FXML ComboBox CBPais;

    // Pesquisa Rota
    @FXML ComboBox CBOrigem;
    @FXML ComboBox CBDestino;
    @FXML ComboBox CBConexoes;
    @FXML ComboBox CBTempoVoo;

    // Altera o modo de exibicao do Mapa
    @FXML private void FlipMap(){
        gerMapa.flipTipoMapa();
        BTNFlipMap.setText(gerMapa.getTipoMapa().toString());
    }

    @FXML private void PesquisaCiaAerea(){
        Consulta();
    }

    @FXML private void PesquisaAeroporto(){
        System.out.println("Limpar");
        gerMapa.clear();
        gerMapa.getMapKit().repaint();
    }

    @FXML private void PesquisaRota(){

    }

    private void contextMenu(){
        MenuItem todosAero = new MenuItem("Buscar Todos Aeroportos");
        todosAero.setOnAction(event -> pintor(buscaTodosAeroportos()));

        Menu pesquisarAeroporto = new Menu("Buscar Aeroporto...");
        MenuItem distancia1 = new MenuItem("5KM de distância");

        distancia1.setOnAction(event -> pintor(buscarPorDistancia(5)));

        MenuItem distancia2 = new MenuItem("12KM de distância");
        distancia2.setOnAction(event -> pintor(buscarPorDistancia(12)));

        MenuItem distanciaX = new MenuItem("Mais próximo");
        distanciaX.setOnAction(event -> pintor(buscarPorDistancia(-1)));

        MenuItem fechar = new MenuItem("(Fechar Menu)");
        pesquisarAeroporto.getItems().addAll(distancia1, distancia2, distanciaX);
        contextMenu.getItems().addAll(todosAero, pesquisarAeroporto, fechar);
    }

    private void Consulta() {

        // Lista para armazenar o resultado da consulta
        List<MyWaypoint> lstPoints = new ArrayList<>();

        Aeroporto poa = gerAero.buscarPorCodigo("POA");
        Aeroporto gru = gerAero.buscarPorCodigo("GRU");
        Aeroporto lis = gerAero.buscarPorCodigo("LIS");
        Aeroporto mia = gerAero.buscarPorCodigo("MIA");

        gerMapa.clear();
        Tracado tr = new Tracado();
        tr.setLabel("Teste");
        tr.setWidth(5);
        tr.setCor(new Color(0,0,0,60));
        tr.addPonto(poa.getLocal());
        tr.addPonto(mia.getLocal());
        tr.addPonto(gru.getLocal());
        tr.addPonto(lis.getLocal());

        gerMapa.addTracado(tr);

        // Adiciona os locais de cada aeroporto (sem repetir) na lista de
        // waypoints

        lstPoints.add(new MyWaypoint(Color.RED, poa.getCodigo(), poa.getLocal(), 5));
        lstPoints.add(new MyWaypoint(Color.RED, gru.getCodigo(), gru.getLocal(), 5));
        lstPoints.add(new MyWaypoint(Color.RED, lis.getCodigo(), lis.getLocal(), 5));
        lstPoints.add(new MyWaypoint(Color.RED, mia.getCodigo(), mia.getLocal(), 5));

        // Para obter um ponto clicado no mapa, usar como segue:
        // GeoPosition pos = gerMapa.getPosicao();

        // Informa o resultado para o gerMapa
        gerMapa.setPontos(lstPoints);

        // Quando for o caso de limpar os traçados...
        // gerMapa.clear();

        gerMapa.getMapKit().repaint();
    }

    private List<Aeroporto> buscaTodosAeroportos(){
        return new ArrayList<>(gerAero.listarTodos());
    }

    private Aeroporto buscarPorDistancia(int distancia){
        Geo posicaoAtual = new Geo(gerMapa.getPosicaoAtual().getLatitude(), gerMapa.getPosicaoAtual().getLongitude());
        Aeroporto aeroporto = null;
        double menorDistancia = Double.POSITIVE_INFINITY;
        for(Aeroporto aero: gerAero.listarTodos()){
            if(posicaoAtual.distancia(aero.getLocal()) < menorDistancia){
                menorDistancia = posicaoAtual.distancia(aero.getLocal());
                aeroporto = aero;
            }
        }
        return (menorDistancia < distancia || distancia == -1) && aeroporto != null ? aeroporto : null;
    }

    private void pintor(Aeroporto aeroporto){
        List<Aeroporto> pontos = new ArrayList<>();
        if(aeroporto != null)
            pontos.add(aeroporto);
        pintor(pontos);
    }

    private void pintor(List<Aeroporto> aeroportos){
        List<MyWaypoint> pontos = new ArrayList<>();
        for (Aeroporto aero: aeroportos)
            pontos.add(aero.waypoint(gerRotas.buscaTrafego(aero.getCodigo())));
        gerMapa.setPontos(pontos);
        gerMapa.getMapKit().repaint();
    }

    private class EventosMouse extends MouseAdapter {
        private int lastButton = -1;

        @Override
        public void mousePressed(MouseEvent e) {
            JXMapViewer mapa = gerMapa.getMapKit().getMainMap();
            GeoPosition loc = mapa.convertPointToGeoPosition(e.getPoint());
            lastButton = e.getButton();
            gerMapa.setPosicao(loc);
            // Botão direito: seleciona localização
            if (lastButton == MouseEvent.BUTTON3) {
                mapkit.setOnContextMenuRequested(event -> contextMenu.show(mapkit, event.getScreenX(), event.getScreenY()));
            }
        }
    }

    private void createSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(() -> swingNode.setContent(gerMapa.getMapKit()));
    }

    void inicializacaoGerenciadores(){
        new Setup(gerAvioes, gerAero, gerCias, gerPaises, gerRotas);
        gerMapa = new GerenciadorMapa(gerAero.buscarPorCodigo("POA").getLocal(), GerenciadorMapa.FonteImagens.VirtualEarth);
        BTNFlipMap.setText(gerMapa.getTipoMapa().toString());
    }

    @FXML void initialize(){
        contextMenu();
        inicializacaoGerenciadores();
        mouse = new Controller.EventosMouse();
        gerMapa.getMapKit().getMainMap().addMouseListener(mouse);
        gerMapa.getMapKit().getMainMap().addMouseMotionListener(mouse);
        createSwingContent(mapkit);
        PainelPrincipal.setCenter(mapkit);
    }

}
