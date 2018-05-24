package myFlight.gui;

import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import myFlight.modelo.*;
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

    private GerenciadorCias gerCias;
    private GerenciadorAeroportos gerAero;
    private GerenciadorRotas gerRotas;
    private GerenciadorAeronaves gerAvioes;
    private GerenciadorMapa gerenciador;
    private Controller.EventosMouse mouse;
    private ObservableList<CiaAerea> comboCiasData;
    private ComboBox<CiaAerea> comboCia;

    @FXML BorderPane PainelPrincipal;
    @FXML Button BTNFlipMap;

    @FXML private void exemplo() {

        // Lista para armazenar o resultado da consulta
        List<MyWaypoint> lstPoints = new ArrayList<>();

        Aeroporto poa = new Aeroporto("POA", "Salgado Filho", new Geo(-29.9939, -51.1711));
        Aeroporto gru = new Aeroporto("GRU", "Guarulhos", new Geo(-23.4356, -46.4731));
        Aeroporto lis = new Aeroporto("LIS", "Lisbon", new Geo(38.772,-9.1342));
        Aeroporto mia = new Aeroporto("MIA", "Miami International", new Geo(25.7933, -80.2906));

        gerenciador.clear();
        Tracado tr = new Tracado();
        tr.setLabel("Teste");
        tr.setWidth(5);
        tr.setCor(new Color(0,0,0,60));
        tr.addPonto(poa.getLocal());
        tr.addPonto(mia.getLocal());

        gerenciador.addTracado(tr);

        Tracado tr2 = new Tracado();
        tr2.setWidth(5);
        tr2.setCor(Color.BLUE);
        tr2.addPonto(gru.getLocal());
        tr2.addPonto(lis.getLocal());
        gerenciador.addTracado(tr2);

        // Adiciona os locais de cada aeroporto (sem repetir) na lista de
        // waypoints

        lstPoints.add(new MyWaypoint(Color.RED, poa.getCodigo(), poa.getLocal(), 5));
        lstPoints.add(new MyWaypoint(Color.RED, gru.getCodigo(), gru.getLocal(), 5));
        lstPoints.add(new MyWaypoint(Color.RED, lis.getCodigo(), lis.getLocal(), 5));
        lstPoints.add(new MyWaypoint(Color.RED, mia.getCodigo(), mia.getLocal(), 5));

        // Para obter um ponto clicado no mapa, usar como segue:
        // GeoPosition pos = gerenciador.getPosicao();

        // Informa o resultado para o gerenciador
        gerenciador.setPontos(lstPoints);

        // Quando for o caso de limpar os traçados...
        // gerenciador.clear();

        gerenciador.getMapKit().repaint();
    }

    @FXML private void FlipMap(){
        gerenciador.flipTipoMapa();
        BTNFlipMap.setText(gerenciador.getTipoMapa().toString());
    }

    private class EventosMouse extends MouseAdapter {
        private int lastButton = -1;

        @Override
        public void mousePressed(MouseEvent e) {
            JXMapViewer mapa = gerenciador.getMapKit().getMainMap();
            GeoPosition loc = mapa.convertPointToGeoPosition(e.getPoint());
            // System.out.println(loc.getLatitude()+", "+loc.getLongitude());
            lastButton = e.getButton();
            // Botão 3: seleciona localização
            if (lastButton == MouseEvent.BUTTON3) {
                gerenciador.setPosicao(loc);
                gerenciador.getMapKit().repaint();
            }
        }
    }

    private void createSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(() -> swingNode.setContent(gerenciador.getMapKit()));
    }

    @FXML void initialize(){
        gerCias = new GerenciadorCias();
        gerAero = new GerenciadorAeroportos();
        gerRotas = new GerenciadorRotas();
        gerAvioes = new GerenciadorAeronaves();
        GeoPosition inicial = new GeoPosition(-30.05, -51.18);
        gerenciador = new GerenciadorMapa(inicial, GerenciadorMapa.FonteImagens.VirtualEarth);
        mouse = new Controller.EventosMouse();
        gerenciador.getMapKit().getMainMap().addMouseListener(mouse);
        gerenciador.getMapKit().getMainMap().addMouseMotionListener(mouse);
        BTNFlipMap.setText(gerenciador.getTipoMapa().toString());

        //inicializacao do mapa
        createSwingContent(mapkit);
        PainelPrincipal.setCenter(mapkit);
    }

}
