package GUI.Swing;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Domain.DomainFacade;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Dimension;
import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class CreateCategory extends JFrame {

    private JPanel contentPane;
    private JTextField textField;
    private JTextPane textPane;

    public CreateCategory() {
        initComponents();
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 208, 351);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        JLabel lblNewLabel = new JLabel("Kategorinamn:");
        lblNewLabel.setBounds(3, 8, 169, 14);
        panel.add(lblNewLabel);

        textField = new JTextField();
        textField.setBounds(3, 27, 169, 20);
        textField.setMinimumSize(new Dimension(6, 30));
        panel.add(textField);
        textField.setColumns(10);

        JLabel lblNewLabel_1 = new JLabel("Beskrivning:");
        lblNewLabel_1.setBounds(3, 58, 70, 14);
        panel.add(lblNewLabel_1);

        textPane = new JTextPane();
        textPane.setBounds(3, 83, 169, 175);
        panel.add(textPane);

        JPanel panel_1 = new JPanel();
        contentPane.add(panel_1, BorderLayout.SOUTH);

        JButton btnSkapaKategori = new JButton("Skapa kategori");
        btnSkapaKategori.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonClicked();
            }
        });
        panel_1.add(btnSkapaKategori);
    }

    private void buttonClicked() {
        DomainFacade.getInstance().createCategory(this.textField.getText(), this.textPane.getText());
        this.dispose();
    }
}
