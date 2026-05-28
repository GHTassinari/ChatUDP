package ChatUDP;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class ChatApplication {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ChatView view = new ChatView();
                ChatController controller = new ChatController(view);
                view.setController(controller);
                view.setVisible(true);
            }
        });
    }
}