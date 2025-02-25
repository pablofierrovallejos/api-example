package com.pablo.fierro.api_example.service;

import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.output.OutputException;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface IQrGen {
    public BufferedImage generateCode128BarCodeImage(final String barcodeText) throws BarcodeException, OutputException;

    public BufferedImage generateQrCode(final String qrCodeText, final int width, final int height) throws IOException;
}
