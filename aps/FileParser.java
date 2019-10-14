import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileParser {

    private char[] charStream;
    private StringBuilder fileString = new StringBuilder();
    private BufferedReader input = null;
    private String currentLine = "";
    private String filename;
    private Output gui;

    public FileParser(Output gui, String filename) {

        this.gui = gui; /* Reference to the output screen */
        this.filename = filename; /* File to be processed */
    }

    void readFile() {

        try {

            input = new BufferedReader(new FileReader(filename));
            gui.addText("\n\n----- BEGIN FILE READ -----\n\n");

            while (input.ready()) {

                parseCharacter(input.read());
            }

            input.close();
            gui.addText("\n\n----- END FILE READ -----\n\n");

        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo " + filename);
        }

        charStream = fileString.toString().toCharArray().clone();
        gui.addText(
                "Characters read: " + charStream.length + "\n" +
                        "charStream Array: \n\n" +
                        new String(charStream) + "\n\n"
        );
    }

    void parseCharacter(int ch) {

        fileString.append((char) ch);

        if (ch == 13) { // Carriage Return

            gui.addText("[CR]");

        } else if (ch == 10) { // Line Feed

            gui.addText("[LF]\n");

        } else if (ch == 32) { // Space

            gui.addText("[ ]");

        } else {

            gui.addText((char) ch);
        }
    }
}
