package ChatUDP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatView extends JFrame implements MessageContainer {

    private static final long serialVersionUID = 1L;

    private JTextField txtLocalPort;
    private JTextField txtRemoteIP;
    private JTextField txtRemotePort;
    private JCheckBox chkTCP;
    private JButton btnStart;
    private JTextArea txtChatArea;
    private JTextField txtMessage;
    private JButton btnSend;

    private ChatController controller;

    public ChatView() {
        setTitle("Sistemas Distribuídos - Chat UDP/TCP");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        initComponents();
    }

    public void setController(ChatController controller) {
        this.controller = controller;
    }

    private void initComponents() {
        JPanel pnlConfig = new JPanel(new GridLayout(2, 4, 5, 5));
        pnlConfig.setBorder(BorderFactory.createTitledBorder("Configurações de Conexão"));

        pnlConfig.add(new JLabel("Porta Local:"));
        txtLocalPort = new JTextField("3000");
        pnlConfig.add(txtLocalPort);

        pnlConfig.add(new JLabel("IP Remoto:"));
        txtRemoteIP = new JTextField("127.0.0.1");
        pnlConfig.add(txtRemoteIP);

        pnlConfig.add(new JLabel("Porta Remota:"));
        txtRemotePort = new JTextField("3001");
        pnlConfig.add(txtRemotePort);

        chkTCP = new JCheckBox("Usar TCP");
        pnlConfig.add(chkTCP);

        btnStart = new JButton("Iniciar Chat");
        pnlConfig.add(btnStart);

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controller != null) controller.iniciarConexao();
            }
        });

        add(pnlConfig, BorderLayout.NORTH);

        txtChatArea = new JTextArea();
        txtChatArea.setEditable(false);
        txtChatArea.setLineWrap(true);
        txtChatArea.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(txtChatArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Mensagens"));
        add(scrollPane, BorderLayout.CENTER);

        JPanel pnlSend = new JPanel(new BorderLayout(5, 5));
        pnlSend.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        txtMessage = new JTextField();
        txtMessage.setEnabled(false);
        
        txtMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controller != null) controller.enviarMensagem();
            }
        });

        btnSend = new JButton("Enviar");
        btnSend.setEnabled(false);
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controller != null) controller.enviarMensagem();
            }
        });

        pnlSend.add(txtMessage, BorderLayout.CENTER);
        pnlSend.add(btnSend, BorderLayout.EAST);

        add(pnlSend, BorderLayout.SOUTH);
    }

    public String getLocalPort() { return txtLocalPort.getText().trim(); }
    public String getRemoteIP() { return txtRemoteIP.getText().trim(); }
    public String getRemotePort() { return txtRemotePort.getText().trim(); }
    public boolean isTCP() { return chkTCP.isSelected(); }
    public String getMessageText() { return txtMessage.getText().trim(); }

    public void clearMessageText() { txtMessage.setText(""); }
    
    public void appendMessage(String message) {
        txtChatArea.append(message + "\n");
    }

    public void lockConfig() {
        txtLocalPort.setEnabled(false);
        txtRemoteIP.setEnabled(false);
        txtRemotePort.setEnabled(false);
        chkTCP.setEnabled(false);
        btnStart.setEnabled(false);
        txtMessage.setEnabled(true);
        btnSend.setEnabled(true);
        txtMessage.requestFocus();
    }

    public void showError(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void newMessage(String message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                appendMessage("Remoto: " + message);
            }
        });
    }
}