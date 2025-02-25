package com.pablo.fierro.api_example.controller;

import com.pablo.fierro.api_example.dto.cliente;
import com.pablo.fierro.api_example.generator.BarcodeGenerator;
import com.pablo.fierro.api_example.service.IQrGen;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import net.sourceforge.barbecue.output.OutputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.springframework.http.ResponseEntity.ok;

//https://ricardogeek.com/como-generar-codigos-de-barras-y-qr-en-java/

@RestController
public class controllerEcho {


    @Autowired
    private  IQrGen qrGen;

    @GetMapping("/echo")
    cliente echo() throws OutputException, BarcodeException {
        cliente cli = new cliente();
        cli.setNombre("Pablo");
        cli.setApellido("Fierro");
        cli.setEdad(20);


        return cli;
    }

    @GetMapping("/qr")
    BufferedImage qr() throws OutputException, BarcodeException {
        return qrGen.generateCode128BarCodeImage("Pablo Fierro");
    }

    @GetMapping("/qr2disk")
    void qr2disk() throws IOException {
        BufferedImage bufferedImage = qrGen.generateQrCode("ricardogeek.com", 350, 350);
        File codigoQR = new File("codigo-qr.jpg");
        ImageIO.write(bufferedImage, "jpg", codigoQR);
    }

    @GetMapping("/c128-2disk")
    void cod128toDisk() throws IOException, OutputException, BarcodeException {
        BufferedImage bufferedImage = qrGen.generateCode128BarCodeImage("Pablo Fierro");
        File codigo128 = new File("codigo-128.jpg");
        ImageIO.write(bufferedImage, "jpg", codigo128);
    }

    @GetMapping("/pdf417b")
    void pdf417() throws BarcodeException, OutputException, IOException {
        final Barcode barcode = BarcodeFactory.createPDF417("Pablo Fierro Vallejofdgdfgdfgdfgdfgdfgdffffffffffffffffffffffffffffffffffffffffffffffffs");
        barcode.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        System.out.println("generatePDF417BarcodeImage: " + "barcodeText");
        BufferedImage bufferedImage = BarcodeImageHandler.getImage(barcode);

        File pdf417 = new File("codigo-pdf417.jpg");
        ImageIO.write(bufferedImage, "jpg", pdf417);
    }


    @GetMapping(value = "/barcodes/{type}/{barcode}", produces = IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> generate(@PathVariable("type") final String type,
                                                  @PathVariable("barcode") final String barcodeText,
                                                  @Autowired BarcodeGenerator barcodeGenerator) {

        try {
            System.out.println("type: " + type);
            System.out.println("barcode: " + barcodeText);
            switch (type) {
                case "EAN13":
                    // 978020137962
                    return ok(barcodeGenerator.generateEAN13BarcodeImage(barcodeText));
                case "UPC":
                    // 12345678901
                    return ok(barcodeGenerator.generateUPCBarcodeImage(barcodeText));
                case "EAN128":
                    // 0101234567890128TEC
                    return ok(barcodeGenerator.generateEAN128BarCodeImage(barcodeText));
                case "CODE128":
                    // any-string
                    return ok(barcodeGenerator.generateCode128BarCodeImage(barcodeText));
                case "USPS":
                    // 123456789
                    return ok(barcodeGenerator.generateUSPSBarcodeImage(barcodeText));
                case "SCC14":
                    return ok(barcodeGenerator.generateSCC14ShippingCodeBarcodeImage(barcodeText));
                case "CODE39":
                    return ok(barcodeGenerator.generateCode39BarcodeImage(barcodeText));
                case "GTIN":
                    return ok(barcodeGenerator.generateGlobalTradeItemNumberBarcodeImage(barcodeText));
                case "PDF417":
                    return ok(barcodeGenerator.generatePDF417BarcodeImage(barcodeText));
                default:
                    return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    private ResponseEntity<BufferedImage> ok(final BufferedImage bufferedImage) {
        return new ResponseEntity<>(bufferedImage, OK);
    }

}
