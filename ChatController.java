package ChatUDP;

import javax.swing.SwingUtilities;

public class ChatController {

    private ChatView view;
    private Sender sender;

    public ChatController(ChatView view) {
        this.view = view;
    }

    public void iniciarConexao() {
        try {
            int localPort = Integer.parseInt(view.getLocalPort());
            int remotePort = Integer.parseInt(view.getRemotePort());
            String remoteIP = view.getRemoteIP();
            boolean isTCP = view.isTCP();

            view.appendMessage(">>> [DEBUG] Iniciando configurações...");
            view.lockConfig();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (isTCP) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    view.appendMessage(">>> [DEBUG-TCP] Criando Receptor local na porta " + localPort);
                                }
                            });
                        }
                        
                        sender = ChatFactory.build(isTCP, remoteIP, remotePort, localPort, view);
                        
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                view.appendMessage(">>> [DEBUG] Conexão estabelecida com sucesso! Pronto para conversar.");
                            }
                        });
                    } catch (final ChatException ex) {
                        ex.printStackTrace();
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                view.showError("Erro na inicialização: " + ex.getMessage(), "Erro de Conexão");
                            }
                        });
                    }
                }
            }).start();

        } catch (NumberFormatException ex) {
            view.showError("Por favor, insira números válidos para as portas.", "Erro de Validação");
        }
    }

    public void enviarMensagem() {
        String msg = view.getMessageText();
        if (!msg.isEmpty() && sender != null) {
            try {
                sender.send(msg);
                view.appendMessage("Você: " + msg);
                view.clearMessageText();
            } catch (ChatException ex) {
                view.showError("Erro ao enviar: " + ex.getMessage(), "Erro de Envio");
            }
        }
    }
}
