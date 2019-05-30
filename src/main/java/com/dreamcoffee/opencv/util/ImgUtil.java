package com.dreamcoffee.opencv.util;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.util.Assert;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.List;

/**
 * ImgUtil
 *
 * @author chenhuihua
 * @date 2019/5/7
 */
public class ImgUtil {

    /**
     * 切割后的图片
     */
    private static Mat leftImg;
    private static Mat rightImg;

    /**
     * 模板集
     */
    private static List<Mat> templateImgList;

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    /**
     * 数字识别
     *
     * @param filename
     * @param templateDir
     * @param binaryType
     */
    public static int compare(String filename, String templateDir, int binaryType) {
        Mat src = Imgcodecs.imread(filename, Imgcodecs.IMREAD_GRAYSCALE);
        Assert.isTrue(!src.empty(), filename + "不存在");
        Imgproc.threshold(src, src, 100, 255, binaryType);
        if (templateImgList == null) {
            initTemplate(templateDir, binaryType);
        }
        int res = cutLeft(src);
        Size size = new Size(32, 48);
        Mat resultImg = new Mat();
        StringBuilder result = new StringBuilder();
        while (res == 0) {
            Imgproc.resize(leftImg, leftImg, size, 0, 0, Imgproc.INTER_LINEAR);
            for (int i = 0; i < templateImgList.size(); i++) {
                Imgproc.matchTemplate(leftImg, templateImgList.get(i), resultImg, Imgproc.TM_CCOEFF_NORMED);
                if (Core.minMaxLoc(resultImg).maxVal > 0.99) {
                    result.append(i);
                    break;
                }
            }
            res = cutLeft(rightImg);
        }
        return Integer.parseInt(result.toString());
    }

    /**
     * 初始化模板0-9
     *
     * @param templateDir
     * @param binaryType
     */
    private static void initTemplate(String templateDir, int binaryType) {
        int length = 10;
        boolean needCreate = false;
        templateImgList = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            Mat templateImg = Imgcodecs.imread(templateDir + i + ".jpg", Imgcodecs.IMREAD_GRAYSCALE);
            if (templateImg.empty()) {
                needCreate = true;
                break;
            }
            templateImgList.add(templateImg);
        }
        if (needCreate) {
            createTemplate(templateDir, binaryType);
            initTemplate(templateDir, binaryType);
        }
    }

    /**
     * 创建模板
     *
     * @param templateDir
     * @param binaryType
     */
    private static void createTemplate(String templateDir, int binaryType) {
        Mat src = Imgcodecs.imread(templateDir + "template.jpg", Imgcodecs.IMREAD_GRAYSCALE);
        Assert.isTrue(!src.empty(), "template.jpg不存在");
        Imgproc.threshold(src, src, 100, 255, binaryType);
        int res = cutLeft(src);
        Size size = new Size(32, 48);
        int i = 0;
        while (res == 0) {
            Imgproc.resize(leftImg, leftImg, size, 0, 0, Imgproc.INTER_LINEAR);
            Imgcodecs.imwrite(templateDir + i++ + ".jpg", leftImg);
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
     * 从剪切板获得图片
     *
     * @return
     * @throws Exception
     */
    public static BufferedImage getImgFromClip() throws Exception {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable transferable = clipboard.getContents(null);
        if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
            return (BufferedImage) transferable.getTransferData(DataFlavor.imageFlavor);
        }
        return null;
    }

    /**
     * 缓存图片转mat
     *
     * @param sourceImg
     * @return
     */
    public static Mat imgToMat(BufferedImage sourceImg) {
        int w = sourceImg.getWidth();
        int h = sourceImg.getHeight();
        BufferedImage targetImg;
        if (sourceImg.getType() != BufferedImage.TYPE_3BYTE_BGR) {
            targetImg = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
            targetImg.setData(sourceImg.getData());
        } else {
            targetImg = sourceImg;
        }
        byte[] pixels = ((DataBufferByte) targetImg.getRaster().getDataBuffer()).getData();
        Mat mat = new Mat(h, w, CvType.CV_8UC3);
        mat.put(0, 0, pixels);
        return mat;
    }
}
