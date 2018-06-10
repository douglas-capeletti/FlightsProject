package gui;

import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import modelo.Setup;
import modelo.gerenciadores.*;
import modelo.objetos.Aeroporto;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    final SwingNode mapkit = new SwingNode();
    private GerenciadorMapa gerMapa;
    private Controller.EventosMouse mouse;

    private GerenciadorAeronaves gerAvioes = new GerenciadorAeronaves();
    private GerenciadorAeroportos gerAero = new GerenciadorAeroportos();
    private GerenciadorCias gerCias = new GerenciadorCias();
    private GerenciadorPaises gerPaises = new GerenciadorPaises();
    private GerenciadorRotas gerRotas = new GerenciadorRotas();

    @FXML BorderPane PainelPrincipal;
    @FXML Button BTNFlipMap;
    @FXML ComboBox CBPartida;
    @FXML ComboBox CBDestino;

    @FXML private void Consulta() {

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

        gerMapa.addTracado(tr);

        Tracado tr2 = new Tracado();
        tr2.setWidth(5);
        tr2.setCor(Color.BLUE);
        tr2.addPonto(gru.getLocal());
        tr2.addPonto(lis.getLocal());
        gerMapa.addTracado(tr2);

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

    private class EventosMouse extends MouseAdapter {

        private int lastButton = -1;
        @Override
        public void mousePressed(MouseEvent e) {
            JXMapViewer mapa = gerMapa.getMapKit().getMainMap();
            GeoPosition loc = mapa.convertPointToGeoPosition(e.getPoint());
            lastButton = e.getButton();
            // Botão 3: seleciona localização
            if (lastButton == MouseEvent.BUTTON3) {
                gerMapa.setPosicao(loc);
                gerMapa.getMapKit().repaint();
            }
        }

    }
    private void createSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(() -> swingNode.setContent(gerMapa.getMapKit()));
    }

    @FXML private void FlipMap(){
        gerMapa.flipTipoMapa();
        BTNFlipMap.setText(gerMapa.getTipoMapa().toString());
    }

    void inicializacaoGerenciadores(){
        mouse = new Controller.EventosMouse();
        new Setup(gerAvioes, gerAero, gerCias, gerPaises, gerRotas);
        //Posicão inicial do Mapa, listeners do mouse e texto de FlipMap
        gerMapa = new GerenciadorMapa(gerAero.buscarPorCodigo("POA").getLocal(), GerenciadorMapa.FonteImagens.VirtualEarth);
        gerMapa.getMapKit().getMainMap().addMouseListener(mouse);
        gerMapa.getMapKit().getMainMap().addMouseMotionListener(mouse);
        BTNFlipMap.setText(gerMapa.getTipoMapa().toString());
    }

    void inicializacaoOpcoes(){
//        CBPartida.setItems(FXCollections.observableArrayList(gerRotas.listarTodosCodigos()));
//        if(CBPartida.getValue() != null){
//            CBDestino.setItems(FXCollections.observableArrayList(gerRotas.listaDestinos((String)CBPartida.getValue())));
//        }else{
//            CBDestino.setItems(null);
//        }


    }

    @FXML void initialize(){
        inicializacaoGerenciadores();
        inicializacaoOpcoes();
        createSwingContent(mapkit);
        PainelPrincipal.setCenter(mapkit); //inicializacao do mapa
    }

}
