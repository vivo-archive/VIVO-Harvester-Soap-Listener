



package org.vivoweb.harvester.soap;
import java.io.*;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;
import org.xml.sax.SAXException;


public class Schemavalidator{ 

    public static void main(String[] args) throws SAXException, IOException {

           SchemaFactory factory =
            SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
       
        File schemaLocation = new File("sample.xsd");
        Schema schema = factory.newSchema(schemaLocation);
           
        Validator validator = schema.newValidator();
       
       
        Source source = new StreamSource(args[0]);
 
        try {
            validator.validate(source);
            System.out.println(args[0] + " is valid.");
        }
        catch (SAXException ex) {
            System.out.println(args[0] + " is not valid because ");
            System.out.println(ex.getMessage());
        } 
       
    }

}





