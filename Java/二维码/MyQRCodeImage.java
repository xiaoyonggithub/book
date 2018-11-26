package com.xy.util;

import jp.sourceforge.qrcode.data.QRCodeImage;

import java.awt.image.BufferedImage;

/**
 * @ClassName MyQRCodeImage
 * @Description
 * @Author xiaoyong
 * @Date 2018-10-30 9:39
 */
public class MyQRCodeImage implements QRCodeImage {

    BufferedImage bi;

    public MyQRCodeImage(BufferedImage bi) {
        this.bi = bi;
    }

    @Override
    public int getWidth() {
        return bi.getWidth();
    }

    @Override
    public int getHeight() {
        return bi.getHeight();
    }

    @Override
    public int getPixel(int i, int i1) {
        return bi.getRGB(i, i1);
    }
}
