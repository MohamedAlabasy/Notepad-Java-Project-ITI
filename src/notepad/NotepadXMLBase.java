package notepad;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class NotepadXMLBase extends BorderPane {

    protected final MenuBar menuBar;
    protected final Menu fileMenu;
    protected final MenuItem newItem;
    protected final MenuItem openItem;
    protected final MenuItem saveItem;
    protected final SeparatorMenuItem separatorMenuItem;
    protected final MenuItem exitItem;
    protected final Menu editMenu;
    protected final MenuItem undoItem;
    protected final SeparatorMenuItem separatorMenuItem0;
    protected final MenuItem cutItem;
    protected final MenuItem copyItem;
    protected final MenuItem pasteItem;
    protected final MenuItem deleteItem;
    protected final SeparatorMenuItem separatorMenuItem1;
    protected final MenuItem selectAllItem;
    protected final Menu helpMenu;
    protected final MenuItem aboutNotepadItem;
    protected final TextArea textArea;
//Custome 
    String currentPath = null;
    String currentText = null;
    String noteName = null;
    protected FileChooser fileChooser = new FileChooser();

    public NotepadXMLBase(Stage stage) {

        menuBar = new MenuBar();
        fileMenu = new Menu();
        newItem = new MenuItem();
        openItem = new MenuItem();
        saveItem = new MenuItem();
        separatorMenuItem = new SeparatorMenuItem();
        exitItem = new MenuItem();
        editMenu = new Menu();
        undoItem = new MenuItem();
        separatorMenuItem0 = new SeparatorMenuItem();
        cutItem = new MenuItem();
        copyItem = new MenuItem();
        pasteItem = new MenuItem();
        deleteItem = new MenuItem();
        separatorMenuItem1 = new SeparatorMenuItem();
        selectAllItem = new MenuItem();
        helpMenu = new Menu();
        aboutNotepadItem = new MenuItem();
        textArea = new TextArea();

        setMaxHeight(USE_PREF_SIZE);
        setMaxWidth(USE_PREF_SIZE);
        setMinHeight(USE_PREF_SIZE);
        setMinWidth(USE_PREF_SIZE);
        setPrefHeight(400.0);
        setPrefWidth(600.0);

        BorderPane.setAlignment(menuBar, javafx.geometry.Pos.CENTER);
//********************************** File **************************************
        fileMenu.setMnemonicParsing(false);
        fileMenu.setText("File");
//|============================================================================|
//|                                  NEW                                       |
//|============================================================================|
        newItem.setMnemonicParsing(false);
        newItem.setText("New          ");//to macke distance between shortcut text 

        newItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                textArea.clear();
                Stage stage = (Stage) textArea.getScene().getWindow();
                stage.setTitle("Untitled - Al-Abasy Notepad");
                fileChooser = null;
                currentPath = null;
                currentText = null;
                noteName = null;
            }
        });
        newItem.setAccelerator(KeyCombination.keyCombination("Ctrl+n"));//Key Shortcuts
//|============================================================================|
//|                                  Open                                      |
//|============================================================================|
        openItem.setMnemonicParsing(false);
        openItem.setText("Open...");
        openItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Select File");
//                System.out.println("---------------------------------------------------------------------------------------------");
                File selectedFile = fileChooser.showOpenDialog(stage);

                if (selectedFile != null) {
                    String path = selectedFile.getPath();
                    currentPath = path;
                    try {
                        FileInputStream fileInputStream = new FileInputStream(path);
                        byte[] text = new byte[fileInputStream.available()];
                        fileInputStream.read(text);
                        textArea.setText(new String(text));
                        currentText = textArea.getText();
                        noteName = selectedFile.getName();
                        stage.setTitle(noteName);
                        fileInputStream.close();
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(NotepadXMLBase.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(NotepadXMLBase.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        openItem.setAccelerator(KeyCombination.keyCombination("Ctrl+o"));//Key Shortcuts
//|============================================================================|
//|                                  Save                                      |
//|============================================================================|
        saveItem.setMnemonicParsing(false);
        saveItem.setText("Save");
        saveItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (currentPath == null) {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Save File");
                    File selectedFile = fileChooser.showSaveDialog(stage);
                    if (selectedFile != null) {
                        String path = selectedFile.getPath();
                        currentPath = path;
                        noteName = selectedFile.getName();
                        stage.setTitle(noteName);
                    }
                }
                if (currentPath != null) {
                    try {
                        try (FileOutputStream fileOutputStream = new FileOutputStream(currentPath)) {
                            byte[] text = textArea.getText().getBytes();
                            fileOutputStream.write(text);
                            textArea.setText(new String(text));
                            currentText = textArea.getText();
                        }
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(NotepadXMLBase.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(NotepadXMLBase.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        saveItem.setAccelerator(KeyCombination.keyCombination("Ctrl+s"));//Key Shortcuts
        separatorMenuItem.setMnemonicParsing(false);
//|============================================================================|
//|                                  Exit                                      |
//|============================================================================|
        exitItem.setMnemonicParsing(false);
        exitItem.setText("Exit");
        exitItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ButtonType save = new ButtonType("Save");//ButtonType.OK;
                ButtonType cancel = new ButtonType("Donot Save");//ButtonType.CANCEL;
                ButtonType close = new ButtonType("Close");//ButtonType.CLOSE;
                String text = textArea.getText();
                if ((text.isEmpty() == true && currentText == null) || text.equals(currentText) == true) {
                    System.exit(0);
                } else {
                    Alert alert = new Alert(AlertType.WARNING, "Do you want to save changes??", save, cancel, close);
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == save) {
                        if (currentPath == null) {
                            FileChooser fileChooser = new FileChooser();
                            fileChooser.setTitle("Save File");
                            File selectedFile = fileChooser.showSaveDialog(stage);
                            if (selectedFile != null) {
                                String path = selectedFile.getPath();
                                currentPath = path;
                            }
                        }
                        try {
                            if (currentPath != null) {
                                FileOutputStream fileOutputStream = new FileOutputStream(currentPath);
                                byte[] textArray = textArea.getText().getBytes();
                                fileOutputStream.write(textArray);
                                textArea.setText(new String(textArray));
                                currentText = textArea.getText();
                                fileOutputStream.close();
                            } else {
                                System.err.println("You did not choose file to save\nPlease save the file to close");
                            }
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(NotepadXMLBase.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(NotepadXMLBase.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        if (result.get() == cancel) {
                            //System.exit(0);
                            stage.close();
                        }
                    }
                }

            }
        });
        exitItem.setAccelerator(KeyCombination.keyCombination("Ctrl+e"));//Key Shortcuts
//********************************** Edit **************************************
        editMenu.setMnemonicParsing(false);
        editMenu.setText("Edit");
//|============================================================================|
//|                                  Undo                                      |
//|============================================================================|
        undoItem.setMnemonicParsing(false);
        undoItem.setText("Undo          ");
        undoItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                textArea.undo();
            }

        });
        undoItem.setAccelerator(KeyCombination.keyCombination("Ctrl+z"));//Key Shortcuts
        separatorMenuItem0.setMnemonicParsing(false);
//|============================================================================|
//|                                  Cut                                       |
//|============================================================================|
        cutItem.setMnemonicParsing(false);
        cutItem.setText("Cut");
        cutItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                textArea.cut();
            }
        });
        cutItem.setAccelerator(KeyCombination.keyCombination("Ctrl+x"));//Key Shortcuts
//|============================================================================|
//|                                  Copy                                      |
//|============================================================================|
        copyItem.setMnemonicParsing(false);
        copyItem.setText("Copy");
        copyItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                textArea.copy();
            }
        });
        copyItem.setAccelerator(KeyCombination.keyCombination("Ctrl+c"));//Key Shortcuts
//|============================================================================|
//|                                  Paste                                     |
//|============================================================================|
        pasteItem.setMnemonicParsing(false);
        pasteItem.setText("Paste");
        pasteItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                textArea.paste();
            }
        });
        pasteItem.setAccelerator(KeyCombination.keyCombination("Ctrl+v"));//Key Shortcuts
//|============================================================================|
//|                                Delete                                      |
//|============================================================================|
        deleteItem.setMnemonicParsing(false);
        deleteItem.setText("Delete");
        deleteItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                textArea.deleteText(textArea.getSelection());
            }
        });
        deleteItem.setAccelerator(KeyCombination.keyCombination("Ctrl+d"));//Key Shortcuts
        separatorMenuItem1.setMnemonicParsing(false);
//|============================================================================|
//|                               Select All                                   |
//|============================================================================|
        selectAllItem.setMnemonicParsing(false);
        selectAllItem.setText("Select All");
        selectAllItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                textArea.selectAll();
            }
        });
        selectAllItem.setAccelerator(KeyCombination.keyCombination("Ctrl+a"));//Key Shortcuts

//********************************** Help **************************************
        helpMenu.setMnemonicParsing(false);
        helpMenu.setText("Help");
//|============================================================================|
//|                              About Notepad                                 |
//|============================================================================|
        aboutNotepadItem.setMnemonicParsing(false);
        aboutNotepadItem.setText("About Notepad          ");
        aboutNotepadItem.addEventHandler(ActionEvent.ACTION, (ActionEvent event) -> {
            try {
                ButtonType close = new ButtonType("Close");//ButtonType.CLOSE;
                Alert alert = new Alert(AlertType.NONE, "This NotePad Created by Mohamed Al-Abasy"
                        + "\nGmail : Eng.Mohamed.Alabasy@gamil.com"
                        + "\nLinkedIn : https://www.linkedin.com/in/mohamed-alabasy-036971224/",
                        close);
                alert.setTitle("About Me");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == close) {
                    //System.exit(0);
                    stage.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        aboutNotepadItem.setAccelerator(KeyCombination.keyCombination("Ctrl+h"));//Key Shortcuts

        setTop(menuBar);

        BorderPane.setAlignment(textArea, javafx.geometry.Pos.CENTER);
        textArea.setPrefHeight(200.0);
        textArea.setPrefWidth(200.0);
        setCenter(textArea);

        fileMenu.getItems().add(newItem);
        fileMenu.getItems().add(openItem);
        fileMenu.getItems().add(saveItem);
        fileMenu.getItems().add(separatorMenuItem);
        fileMenu.getItems().add(exitItem);
        menuBar.getMenus().add(fileMenu);
        editMenu.getItems().add(undoItem);
        editMenu.getItems().add(separatorMenuItem0);
        editMenu.getItems().add(cutItem);
        editMenu.getItems().add(copyItem);
        editMenu.getItems().add(pasteItem);
        editMenu.getItems().add(deleteItem);
        editMenu.getItems().add(separatorMenuItem1);
        editMenu.getItems().add(selectAllItem);
        menuBar.getMenus().add(editMenu);
        helpMenu.getItems().add(aboutNotepadItem);
        menuBar.getMenus().add(helpMenu);

    }
}
