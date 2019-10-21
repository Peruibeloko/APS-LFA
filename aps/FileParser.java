import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileParser {

    private StringBuilder currentLine = new StringBuilder();
    private BufferedReader input = null;
    private String filename;
    private Output gui;
    private ArrayList<KeyValue> varList = new ArrayList<>();

    private boolean isSettingVars = true;

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

        if (isValidChar(ch)) {

            if (ch == 13) { // Carriage Return

                gui.addText("\\r");
                currentLine.append('\r');

            } else if (ch == 10) { // Line Feed

                gui.addText("\\n\n");
                currentLine.append('\n');

                if (currentLine.toString().equals("\r\n")) { // linha vazia

                    isSettingVars = false;
                    gui.addText("\n----- VARIABLES CREATED, BEGIN PROCESSING -----\n\n");

                } else if (isSettingVars) { // estamos criando vars

                    String[] newVar = currentLine.toString().split(" ");

                    if (isValidName(newVar[0])) {

                        if (isValidValue(newVar[2])) {

                            varList.add(new KeyValue(newVar[0], Double.parseDouble(newVar[2])));

                        } else {

                            gui.addText("Invalid Value: " + newVar[2].trim() + "\n\n");
                        }

                    } else {

                        gui.addText("Invalid Identifier: " + newVar[0] + "\n\n");
                    }

                } else { // estamos processando atribuições

                    String[] newVar = currentLine.toString().split(" ");

                    if (isValidName(newVar[0])) {

                        try {

                            varList.add(new KeyValue(newVar[0], Double.parseDouble(newVar[2])));

                        } catch (NumberFormatException ex2) {

                            gui.addText("Invalid Value: " + newVar[2].trim() + "\n\n");
                        }

                    } else {

                        gui.addText("Invalid Identifier: " + newVar[0] + "\n\n");
                    }
                }

                currentLine = null;
                currentLine = new StringBuilder();

            } else if (ch == 32) { // Space

                gui.addText(" ");
                currentLine.append(' ');

            } else if (ch == 59) { // ;

            } else {

                currentLine.append((char) ch);
                gui.addText((char) ch);
            }
        }
    }

    boolean isValidChar(int ch) {

        return (ch == 10 || ch == 13 || ch == 32) || // CR LF Space
                (ch == 42 || ch == 43 || (ch >= 45 && ch <= 47) || // Caracteres
                        ch == 59 || ch == 61 || ch == 95) ||
                (ch >= 48 && ch <= 57) || // Números
                (ch >= 97 && ch <= 122); // Letras minúsculas
    }

    boolean isValidName(String name) {

        System.out.println("NAME: " + name);

        return name.trim().matches("[a-z][a-z_0-9]*");
    }

    boolean isValidValue(String value) {

        return value.trim().matches("[0-9]*(\\.[0-9]+)?");
    }
}
