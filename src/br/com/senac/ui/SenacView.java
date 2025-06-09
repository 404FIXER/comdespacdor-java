package br.com.senac.ui;

import br.com.senac.bo.CompactadorBO;
import br.com.senac.bo.ICompactadorBO;
import br.com.senac.exception.SenacException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class SenacView extends JFrame {

    private JButton compactarButton;
    private JButton descompactarButton;

    public SenacView() {
        // Configurações da janela
        setTitle("SenacView");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // Inicialização dos botões
        compactarButton = new JButton("Compactar");
        descompactarButton = new JButton("Descompactar");

        // Adiciona os botões à janela
        add(compactarButton);
        add(descompactarButton);

        // Adiciona ação ao botão Compactar
        compactarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fileChooser.showOpenDialog(SenacView.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    // Aqui você pode adicionar a lógica de compactação do arquivo
                    System.out.println("Arquivo selecionado para compactação: " + selectedFile.getAbsolutePath());
                    ICompactadorBO compactadorBO = new CompactadorBO();
                    File destino = new File(selectedFile.getParent());
                    File arquivoDestino = new File (destino, "arquivo.zip");
                    File[] arquivosParaCompactar = new File [1];
                    arquivosParaCompactar[0] = selectedFile;
                    try {
                        compactadorBO.criarZip(arquivoDestino, arquivosParaCompactar);
                    } catch (SenacException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        // Adiciona ação ao botão Descompactar
        descompactarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser dirChooser = new JFileChooser();
                dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = dirChooser.showOpenDialog(SenacView.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedDir = dirChooser.getSelectedFile();
                    // Aqui você pode adicionar a lógica de descompactação do diretório
                    System.out.println("Diretório selecionado para descompactação: " + selectedDir.getAbsolutePath());



                }
            }
        });
    }

    public static void main(String[] args) {
        // Cria e exibe a janela
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SenacView().setVisible(true);
            }
        });
    }
}

