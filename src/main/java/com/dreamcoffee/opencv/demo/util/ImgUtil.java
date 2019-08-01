package com.dreamcoffee.opencv.demo.util;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * ImgUtil
 *
 * @author chenhuihua
 * @date 2019/5/7
 */
public class ImgUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImgUtil.class);

    /**
     * 切割后的图片
     */
    private static Mat leftImg;
    private static Mat rightImg;

    /**
     * 模板
     */
    private static final String TEMP = "template/template.jpg";
    private static final String TEMP_DIR = "tmp/";
    private static final String IMG_SUF = ".jpg";
    private static final int THRESH_TYPE = Imgproc.THRESH_BINARY;
    private static List<Mat> tempImgList;

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    /**
     * 数字识别
     *
     * @param bufImg
     */
    public static Integer compare(BufferedImage bufImg) {
        Mat src = imgToMat(bufImg);
        Imgproc.threshold(src, src, 100, 255, THRESH_TYPE);
        int res = cutLeft(src);
        Size size = new Size(32, 48);
        Mat resultImg = new Mat();
        StringBuilder result = new StringBuilder();
        while (res == 0) {
            Imgproc.resize(leftImg, leftImg, size, 0, 0, Imgproc.INTER_LINEAR);
            for (int i = 0; i < tempImgList.size(); i++) {
                Imgproc.matchTemplate(leftImg, tempImgList.get(i), resultImg, Imgproc.TM_CCOEFF_NORMED);
                if (Core.minMaxLoc(resultImg).maxVal > 0.99) {
                    result.append(i);
                    break;
                }
            }
            res = cutLeft(rightImg);
        }
        return StringUtils.isEmpty(result.toString()) ? null : Integer.parseInt(result.toString());
    }

    /**
     * 初始化模板0-9
     */
    public static void initTemplate() throws IOException {
        int length = 10;
        boolean needCreate = false;
        tempImgList = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            String filename = TEMP_DIR + i + IMG_SUF;
            Mat tempImg = Imgcodecs.imread(filename, Imgcodecs.IMREAD_GRAYSCALE);
            if (tempImg.empty()) {
                needCreate = true;
                break;
            }
            tempImgList.add(tempImg);
        }
        if (needCreate) {
            createTemplate();
            initTemplate();
        }
    }

    /**
     * 创建模板
     */
    private static void createTemplate() throws IOException {
        File file = new File(TEMP_DIR);
        if (!file.exists()) {
            Assert.isTrue(file.mkdir(), "模板目录创建失败");
        }
        BufferedImage bufferedImage;
        try (InputStream is = ImgUtil.class.getClassLoader().getResourceAsStream(TEMP)) {
            Assert.notNull(is, "模板不存在");
            bufferedImage = ImageIO.read(is);
        }
        Assert.notNull(bufferedImage, "模板格式错误");
        Mat src = imgToMat(bufferedImage);
        Imgproc.threshold(src, src, 100, 255, THRESH_TYPE);
        int res = cutLeft(src);
        Size size = new Size(32, 48);
        int i = 0;
        while (res == 0) {
            Imgproc.resize(leftImg, leftImg, size, 0, 0, Imgproc.INTER_LINEAR);
            String filename = TEMP_DIR + i++ + IMG_SUF;
            Imgcodecs.imwrite(filename, leftImg);
            LOGGER.info("初始化模板" + filename);
            res = cutLeft(rightImg);
        }
    }

    /**
     * 左右切割
     *
     * @param src
     * @return
     */
    private static int cutLeft(Mat src) {
        int left = 0;
        int right = src.cols();
        int i;
        for (i = 0; i < src.cols(); i++) {
            int colValue = getColPxSum(src, i);
            if (colValue > 0) {
                left = i;
                break;
            }
        }
        if (left == 0) {
            return 1;
        }
        for (; i < src.cols(); i++) {
            int colValue = getColPxSum(src, i);
            if (colValue == 0) {
                right = i;
                break;
            }
        }
        int width = right - left;
        Rect rect = new Rect(left, 0, width, src.rows());
        leftImg = new Mat(src, rect);
        Rect rectRight = new Rect(right, 0, src.cols() - right, src.rows());
        rightImg = new Mat(src, rectRight);
        cutTop(leftImg);
        return 0;
    }

    /**
     * 上下切割
     *
     * @param src
     */
    private static void cutTop(Mat src) {
        int top = 0;
        int bottom = src.rows();
        int i;
        for (i = 0; i < src.rows(); i++) {
            int colValue = getRowPxSum(src, i);
            if (colValue > 0) {
                top = i;
                break;
            }
        }
        for (; i < src.rows(); i++) {
            int colValue = getRowPxSum(src, i);
            if (colValue == 0) {
                bottom = i;
                break;
            }
        }
        int height = bottom - top;
        Rect rect = new Rect(0, top, src.cols(), height);
        leftImg = new Mat(src, rect);
    }

    /**
     * 第row行像素点求和
     *
     * @param src
     * @param row
     * @return
     */
    private static int getRowPxSum(Mat src, int row) {
        int result = 0;
        for (int i = 0; i < src.cols(); i++) {
            result += src.get(row, i)[0];
        }
        return result;
    }

    /**
     * 第col列像素点求和
     *
     * @param src
     * @param col
     * @return
     */
    private static int getColPxSum(Mat src, int col) {
        int result = 0;
        for (int i = 0; i < src.rows(); i++) {
            result += src.get(i, col)[0];
        }
        return result;
    }

    /**
     * 缓存图片转mat
     *
     * @param sourceImg
     * @return
     */
    private static Mat imgToMat(BufferedImage sourceImg) {
        int w = sourceImg.getWidth();
        int h = sourceImg.getHeight();
        BufferedImage targetImg;
        // robot.createScreenCapture TYPE_INT_RGB
        if (sourceImg.getType() != BufferedImage.TYPE_3BYTE_BGR) {
            targetImg = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
            targetImg.setData(sourceImg.getData());
        } else {
            // ImageIO.read TYPE_3BYTE_BGR
            targetImg = sourceImg;
        }
        byte[] data = ((DataBufferByte) targetImg.getRaster().getDataBuffer()).getData();
        // Imgcodecs.imread CV_8UC3
        Mat result = new Mat(h, w, CvType.CV_8UC3);
        result.put(0, 0, data);
        Imgproc.cvtColor(result, result, Imgproc.COLOR_BGR2GRAY);
        return result;
    }
}
