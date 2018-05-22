package com.jp.processfile;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Utility class
 * @author Douglas Rossi
 */
public class Utils {
    
    static final Logger logger = Logger.getLogger(ProcessFile.class.getName());
    
    public static List<Purchase> totalPurchase = new ArrayList<Purchase>();
    
    public static Integer totalProcessedFiles = new Integer(0);
    
    public static Integer totalFiles = new Integer(0);
    
    /**
     * Method to read and process the files
     * @param path Path of files
     * @param dest Path to move the files
     * @param name
     * @throws IOException 
     */
    public static void loadDirectory(Path path, Path dest, String name) throws IOException, SAXException {
        List<Path> files = null;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path);){
            files = new ArrayList<>();
            for (Path entry : stream) {
                files.add(entry);
            }
            stream.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Empty folder", e);
        }
        
        if (files != null && !files.isEmpty()) {
            for (Path entry : files) {
                //System.out.println(entry.toString());
                Path path2 = Paths.get(entry.toString());

                File xmlFile = path2.toFile();
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder;
                try {
                    dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(xmlFile);
                    doc.getDocumentElement().normalize();
                    //logger.log(Level.INFO,"Root element :" + doc.getDocumentElement().getNodeName());
                    NodeList nodeList = doc.getElementsByTagName("Purchase");
                    //now XML is loaded as Document in memory, lets convert it to Object List
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        setPurchase(nodeList.item(i));
                    }
                    
                } catch (ParserConfigurationException | IOException e1) {
                    logger.log(Level.SEVERE, "Error", e1);
                }
                File xmlFileDest = new File(dest.toString()+"\\"+path2.getFileName());
                xmlFile.renameTo(xmlFileDest);
                totalFiles+=1;
                totalProcessedFiles+=1;
                
                if (totalFiles == 10) {
                    System.out.println("Total of Purchases = "+totalPurchase.size());
                    
                    Map<String, List<Purchase>> studlistGrouped = totalPurchase.stream().collect(Collectors.groupingBy(w -> w.getProductType()));
                    
                    totalPurchase.stream().collect(Collectors.groupingBy(w -> w.getProductType(),
                                    Collectors.summingInt(foo->foo.getValue())))
                    .forEach((id,sumTargetCost)->System.out.println("Product Type: "+id+"\t"+"Total: "+sumTargetCost));
                    
                    //for (Purchase emp : Utils.totalPurchase) {
                    //    System.out.println(emp.toString()); 
                    //}
                    totalFiles = 0;
                }
                
                if (totalProcessedFiles == 50) {
                    System.out.println();
                    System.out.println("Pausing ... ");
                    System.out.println("Total of Purchases = "+totalPurchase.size());
                    
                    Map<String, List<Purchase>> studlistGrouped = totalPurchase.stream().collect(Collectors.groupingBy(w -> w.getProductType()));
                    
                    totalPurchase.stream().collect(Collectors.groupingBy(w -> w.getProductType(),
                                    Collectors.summingInt(foo->foo.getValue())))
                    .forEach((id,sumTargetCost)->System.out.println("Product Type: "+id+"\t"+"Total: "+sumTargetCost));
                    System.exit(0);
                }
            }
        }
        
    }
 
    /**
     * Method to create bean
     * @param node 
     */
    private static void setPurchase(Node node) {
        //XMLReaderDOM domReader = new XMLReaderDOM();
        Purchase emp = new Purchase();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            emp.setProductType(getTagValue("productType", element));
            emp.setValue(Integer.parseInt(getTagValue("value", element)));
            
            if (!getTagValue("occurrenses", element).isEmpty()) {
                emp.setOccurrenses(Integer.parseInt(getTagValue("occurrenses", element)));
                for (int x = 0; x < emp.getOccurrenses().intValue(); x++) {
                    totalPurchase.add(emp);
                }
            } else {
                totalPurchase.add(emp);
            }
                
            if (!getTagValue("action", element).isEmpty()) {
                
                    for(Purchase p: totalPurchase){
                        if (getTagValue("action", element).equals("add")) {
                            if(p.getProductType().equals(emp.getProductType())) {
                                p.setValue(p.getValue()+emp.getValue());
                            }
                        }
                        if (getTagValue("action", element).equals("subtract")) {
                            if(p.getProductType().equals(emp.getProductType())) {
                                p.setValue(p.getValue()-emp.getValue());
                            }
                        }
                        if (getTagValue("action", element).equals("multiply ")) {
                            if(p.getProductType().equals(emp.getProductType())) {
                                p.setValue(p.getValue()*emp.getValue());
                            }
                        }
                    }
                //lets print Employee list information
                //for (Purchase ccc : totalPurchase) {
                //    System.out.println(ccc.toString());
                //}
            } 
        }
    }

    /**
     * Method to find the value in a tag
     * @param tag
     * @param element
     * @return 
     */
    private static String getTagValue(String tag, Element element) {
        if (element.getElementsByTagName(tag).getLength() > 0) {
            NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
            Node node = (Node) nodeList.item(0);
            return node.getNodeValue();
        } 
        return "";
    }
}