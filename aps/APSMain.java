import javax.swing.*;
import java.awt.*;

public class APSMain {

    public static void main(String[] args) {

        String filename = null;
        Output gui = new Output();
        FileParser fParser;

        FileDialog fd = new FileDialog(new JFrame(), "Choose a file", FileDialog.LOAD);
        fd.setDirectory("C:\\");
        fd.setVisible(true);

        if (fd.getDirectory() == null || fd.getFile() == null)

            System.exit(0);

        else {

            filename = fd.getDirectory() + fd.getFile();
            gui.run();

            fParser = new FileParser(gui, filename);
            fParser.readFile();
        }
    }
}