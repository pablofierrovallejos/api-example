package com.pablo.fierro.api_example.service.impl;

import com.pablo.fierro.api_example.service.IQrGen;
import net.glxn.qrgen.javase.QRCode;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import net.sourceforge.barbecue.output.OutputException;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class QrGenImpl implements IQrGen {

    @Override
    public BufferedImage generateCode128BarCodeImage(final String barcodeText) throws BarcodeException, OutputException {
        final Barcode barcode = BarcodeFactory.createCode128(barcodeText);
        barcode.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

        return BarcodeImageHandler.getImage(barcode);
    }


    public BufferedImage generateQrCode(final String qrCodeText, final int width, final int height) throws IOException {
        final ByteArrayOutputStream stream = QRCode
                .from(qrCodeText)
                .withSize(width, height)
                .stream();

        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(stream.toByteArray());

        return ImageIO.read(byteArrayInputStream);
    }


}
