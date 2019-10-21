import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileParser {

    private StringBuilder currentWord = new StringBuilder();
    private StateMachine st = new StateMachine();
    private BufferedReader input = null;
    private String filename, name;
    private double value;
    private Output gui;
    private ArrayList<KeyValue> varList = new ArrayList<>();

    public FileParser(Output gui, String filename) {

        this.gui = gui; /* Reference to the output screen */
        this.filename = filename; /* File to be processed */
    }

    void readFile() {

        int i = 0;

        try {

            input = new BufferedReader(new FileReader(filename));
            gui.addText("\n\n----- BEGIN FILE READ -----\n\n");

            while (input.ready()) {

                parseCharacter(input.read());
                i++;
            }

            input.close();
            gui.addText("\n\n----- END FILE READ -----\n\n");

        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo " + filename);
        }

        gui.addText("Characters read: " + i + "\n");
        gui.addText("Variables: \n\n");

        for (int j = 0; j < varList.size(); j++) {
            gui.addText(varList.get(j).toString() + "\n");
        }
    }

    void parseCharacter(int ch) {

        currentWord.append((char) ch);
        String newState = st.transition("" + (char) ch);

        if (newState.equals("q2")) {

            name = currentWord.toString().trim();

        } else if (newState.equals("f1")) {

            value = Double.parseDouble(currentWord.toString().trim());

        }

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
