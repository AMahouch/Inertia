import java.io.File;

public class Filter extends javax.swing.filechooser.FileFilter {

    @Override
    public boolean accept(File f) {

        return f.isDirectory() || f.getName().toLowerCase().endsWith(".csv");
    }

    @Override
    public String getDescription() {
        return String.format("Player file (.csv)");
    }
}
