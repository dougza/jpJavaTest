/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jp.processfile;

import static com.jp.processfile.Utils.totalFiles;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.FileSystem;
import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.SAXException;

/**
 * Class to process the files
 * @author douglas
 */
public class ProcessFile {
    
    static final Logger logger = Logger.getLogger(ProcessFile.class.getName());
    
    /**
     * Method to run the application
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SAXException {
        logger.log(Level.INFO, "Initializing: {0}", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        Scanner entrada = new Scanner(System.in);
        System.out.println("Input the path of files, ex.: c:\\\\temp\\\\purchase -->");
        
        //System.out.println("Informe o nome do arquivo a ser gerado: ");
        //String nome = entrada.next();
        //Path path = Paths.get("c:\\temp\\purchase");
        Path path = Paths.get(entrada.next());
        System.out.println("Input the path to move the files processed, ex.: c:\\\\temp\\\\processed -->");
        Path dest = Paths.get(entrada.next());

        try {
            Utils.loadDirectory(path, dest, "");
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        watchDirectoryPath(path,dest, "");

        //logger.log(Level.INFO, "Fim Processamento Final: {0}", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
    }

    /**
     * Method to watch any update on directory
     * @param path
     * @param name 
     */
    private static void watchDirectoryPath(Path path,Path dest,String name) throws SAXException {
        try {
            Boolean isFolder = (Boolean) Files.getAttribute(path, "basic:isDirectory", NOFOLLOW_LINKS);
            if (!isFolder) {
                throw new IllegalArgumentException("Caminho: " + path + " não é uma pasta");
            }
        } catch (IOException ioe) {
            logger.log(Level.SEVERE, null, ioe);
        }

        FileSystem fs = path.getFileSystem();

        try (WatchService service = fs.newWatchService()) {

            path.register(service, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);

            WatchKey key = null;
            while (true) {
                key = service.take();
                WatchEvent.Kind<?> kind = null;
                for (WatchEvent<?> watchEvent : key.pollEvents()) {
                    kind = watchEvent.kind();
                    if (ENTRY_CREATE == kind) {

                        Utils.loadDirectory(path, dest, name);
                    } else if (ENTRY_MODIFY == kind) {

                        Utils.loadDirectory(path, dest, name);
                    }
                }

                if (!key.reset()) {
                    break;
                }
            }

        } catch (IOException | InterruptedException ioe) {
            logger.log(Level.SEVERE, null, ioe);
        }

    }
}