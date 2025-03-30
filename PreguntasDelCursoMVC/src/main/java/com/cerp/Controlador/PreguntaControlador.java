package com.cerp.Controlador;

import com.cerp.Randomizer;
import com.cerp.Modelo.Pregunta;
import com.cerp.Vista.InicioVista;
import com.cerp.Vista.PreguntaVista;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 * Clase para representar el controlador de la interfaz gráfica de preguntas.
 */
public class PreguntaControlador implements ActionListener {
    private List<Pregunta> modelo;
    private PreguntaVista vista;
    private InicioVista vistaInicio;
    private int correctIndex;

    public PreguntaControlador(List<Pregunta> modelo, PreguntaVista vista, InicioVista vistaInicio) {
        this.modelo = modelo;
        this.vista = vista;
        this.vistaInicio = vistaInicio;

        vista.getConfirmarButton().addActionListener(this);
        vista.getAtrasButton().addActionListener(this);
        vista.getSiguienteButton().addActionListener(this);

        this.correctIndex = cargarPregunta();

        this.vista.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                vistaInicio.getControlador().guardarPreguntas();
                System.exit(0);
            }
        });
    }

    private int cargarPregunta() {
        Randomizer<Pregunta> randomizer = new Randomizer<>();
        Pregunta preguntaVisible = randomizer.getRandomElement(modelo);

        vista.getIdLabel().setText("ID de pregunta: " + preguntaVisible.getIdPregunta());
        vista.getPreguntaLabel().setText(preguntaVisible.getPregunta());

        int correctButtonIndex = Randomizer.getRandomIndex(0, vista.getRespuestaButtons().length - 1);
        vista.getRespuestaButtons()[correctButtonIndex].setText(preguntaVisible.getCorrecta());

        int optionIndex = 0;
        for (int i = 0; i < vista.getRespuestaButtons().length; i++) {
            if (i == correctButtonIndex) continue;
            vista.getRespuestaButtons()[i].setText(preguntaVisible.getOpciones()[optionIndex]);
            optionIndex++;
        }
        return correctButtonIndex;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getSiguienteButton()) {
            this.correctIndex = cargarPregunta();
            vista.getButtonGroup().clearSelection();
        } else if (e.getSource() == vista.getConfirmarButton()) {
            if (vista.getButtonGroup().getSelection() != null) {
                int selectedAnswer = Integer.parseInt(vista.getButtonGroup().getSelection().getActionCommand());
                if (selectedAnswer == this.correctIndex) {
                    vista.mostrarMensajeConfirmacion("Respuesta correcta.");
                } else {
                    vista.mostrarMensajeError("Respuesta incorrecta, vuelve a intentarlo.");
                }
            } else {
                vista.mostrarMensajeError("Debes seleccionar una respuesta.");
            }
        } else if (e.getSource() == vista.getAtrasButton()) {
            vista.getParentWindow().setVisible(true);
            vista.dispose();
        }
    }
}
