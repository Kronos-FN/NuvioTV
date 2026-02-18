import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

import javax.swing.*;
import java.awt.*;

public class DesktopMediaPlayer {
    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

    public DesktopMediaPlayer() {
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        JFrame frame = new JFrame("VLCJ Media Player");
        frame.setContentPane(mediaPlayerComponent);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    public void play(String mediaPath) {
        mediaPlayerComponent.mediaPlayer().media().play(mediaPath);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DesktopMediaPlayer player = new DesktopMediaPlayer();
            player.play("path/to/media/file.mp4"); // Replace with a valid media path
        });
    }
}