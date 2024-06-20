package org.example.dataretriever;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;

@RestController
public class XmlController {

    private static final Logger logger = LoggerFactory.getLogger(XmlController.class);

    @PostMapping("/upload-xml")
    public ResponseEntity<String> handleXmlUpload(@RequestParam("file") MultipartFile file) {
        logger.info("Received a file upload request");

        String contentType = file.getContentType();
        logger.info("Uploaded File Content Type: {}", contentType);

        if (contentType != null && (contentType.equalsIgnoreCase("application/xml") || contentType.equalsIgnoreCase("text/xml"))) {
            try {
                String xmlContent = parseXmlContent(file.getInputStream());
                logger.info("XML file processed successfully");
                return ResponseEntity.ok(xmlContent);
            } catch (IOException e) {
                logger.error("Error processing XML file: ", e);
                return ResponseEntity.badRequest().body("Error processing XML file.");
            }
        } else {
            logger.warn("Only XML files are allowed : {}", contentType);
            return ResponseEntity.badRequest().body("Only XML files are allowed. Content Type: " + contentType);
        }
    }

    private String parseXmlContent(InputStream inputStream) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document doc = builder.parse(inputStream);
            logger.info("Processed XML content successfully");
            return doc.getDocumentElement().getTextContent();
        } catch (Exception e) {
            logger.error("Only XML files are allowed: ", e);
            return "Only XML files are allowed";
        }
    }
}
