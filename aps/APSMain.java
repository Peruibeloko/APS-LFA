import javax.swing.*;
import java.awt.*;
import java.io.*;

public class APSMain {

    public static void main(String[] args) {

        int sym;
        char[] charStream;
        String filename = null;
        StringBuilder fileString = new StringBuilder();
        BufferedReader input = null;
        Output gui = new Output();

        FileDialog fd = new FileDialog(new JFrame(), "Choose a file", FileDialog.LOAD);
        fd.setDirectory("C:\\");
        fd.setVisible(true);

        if (fd.getDirectory() == null || fd.getFile() == null)
            System.exit(0);
        else {
            filename = fd.getDirectory() + fd.getFile();
            gui.run();
        }
        try {
            input = new BufferedReader(new FileReader(filename));

            gui.addText("\n\n----- BEGIN FILE READ -----\n\n");
            while (input.ready()) {
                sym = input.read();
                fileString.append((char) sym);

                if (sym == 13)
                    gui.addText("[CR]");
                else if (sym == 10)
                    gui.addText("[LF]\n");
                else if (sym == 32)
                    gui.addText("[ ]");
                else
                    gui.addText((char) sym);
            }
            input.close();
            gui.addText("\n\n----- END FILE READ -----\n\n");

            charStream = fileString.toString().toCharArray().clone();
            gui.addText(
                    "Characters read: " + charStream.length + "\n" +
                            "charStream Array: \n\n" +
                            new String(charStream) + "\n\n"
            );
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo " + filename);
        }
    }
}