package com.xy.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.io.File;

/**
 * @ClassName QRCodeUtil
 * @Description
 * @Author xiaoyong
 * @Date 2018-10-29 14:22
 */
public class ZxingQRCodeUtil {

    //编码
    private static final String CHARSET = "UTF-8";
    // 图片格式
    private static final String FORMAT_NAME = "JPG";
    // 二维码尺寸
    private static final int QRCODE_SIZE = 200;
    // LOGO宽度
    private static final int LOGO_WIDTH = 60;
    // LOGO高度
    private static final int LOGO_HEIGHT = 60;

    /**
     * 添加logo图标
     *
     * @param src        二维码
     * @param logo       图标路径
     * @param isCompress 是否压缩
     * @return
     * @throws IOException
     */
    private static BufferedImage insertLogo(BufferedImage src, String logo, boolean isCompress) throws IOException {
        // 读取图片
        Image image = ImageIO.read(new File(logo));

        if (isCompress) {
            //图片的大小
            int height = image.getHeight(null);
            int width = image.getWidth(null);
            //图片大小不能大于logo的大小
            if (width > LOGO_WIDTH) {
                width = LOGO_WIDTH;
            }
            if (height > LOGO_HEIGHT) {
                height = LOGO_HEIGHT;
            }
            //压缩图片
            Image zImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics zoomG2d = bi.getGraphics();
            //绘制压缩图片
            zoomG2d.drawImage(zImage, 0, 0, null);
            zoomG2d.dispose();
            //使用压缩后的图片
            image = zImage;
        }

        // 获取画图对象
        Graphics2D graphics = src.createGraphics();
        // 绘制logo
        int x = (QRCODE_SIZE - LOGO_WIDTH) / 2;
        int y = (QRCODE_SIZE - LOGO_HEIGHT) / 2;
        graphics.drawImage(image, x, y, LOGO_WIDTH, LOGO_HEIGHT, null);
        // 绘制logo的矩形框
        Shape shape = new RoundRectangle2D.Float(x, y, LOGO_WIDTH, LOGO_HEIGHT, 6, 6);
        graphics.setStroke(new BasicStroke(3f));
        graphics.draw(shape);
        graphics.dispose();
        return src;
    }

    /**
     * 生成二维码
     *
     * @param content    二维码内容
     * @param logo       二维码logo
     * @param target     二维码保存路径
     * @param isCompress 是否压缩logo
     * @throws IOException
     * @throws WriterException
     */
    public static void encode(String content, String logo, String target, boolean isCompress) throws IOException, WriterException {
        //目录不存在生成目录
        mkdirs(target);
        // 二维码参数
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        // 容错等级
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 编码
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        // 空白边距
        hints.put(EncodeHintType.MARGIN, 1);
        // 生成二维码
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);
        // 获取二维码的大小
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        // 图片缓存区
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 设置颜色
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                bi.setRGB(i, j, (bitMatrix.get(i, j) ? 0xFF000000 : 0xFFFFFFFF));
            }
        }
        // 给二维码添加logo
        if (logo != null || "".equals(logo)) {
            File file = new File(logo);
            // 图片文件存在
            if (file.exists()) {
                BufferedImage image = insertLogo(bi, logo, isCompress);
                ImageIO.write(image,FORMAT_NAME,new File(target));
            }
        } else {
            //设置保存路径
            Path path = new File(target).toPath();
            //保存二维码
            MatrixToImageWriter.writeToPath(bitMatrix, FORMAT_NAME, path);
        }
    }


    /*
     * 目录不存在，生成目录
     */
    private static void mkdirs(String dir) {
        if (StringUtils.isEmpty(dir)) {
            return;
        }
        File file = new File(dir);
        if (file.isDirectory()) {
            return;
        } else {
            file.mkdirs();
        }
    }

    @Test
    public void test01() throws IOException, WriterException {
        ZxingQRCodeUtil.encode("二维码",null,"f:\\code.jpg",true);
    }

}
