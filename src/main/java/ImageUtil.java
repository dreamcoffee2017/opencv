import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * ImageUtil
 *
 * @author chenhuihua
 * @date 2019/5/7
 */
public class ImageUtil {

    /**
     * 切割后的图片
     */
    private Mat leftImg, rightImg;

    /**
     * 与模板相减后的图片
     */
    private Mat diffImage = new Mat();

    static {
        // 本地安装opencv
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    /**
     * 制作模板0-9
     *
     * @param dir
     * @return
     */
    public void createTemplate(String dir) {
        Mat src = Imgcodecs.imread(dir + "template.jpg", Imgcodecs.IMREAD_GRAYSCALE);
        Imgproc.threshold(src, src, 100, 255, Imgproc.THRESH_BINARY_INV);
        int res = this.cutLeft(src);
        int i = 0;
        while (res == 0) {
            Imgcodecs.imwrite(dir + i++ + ".jpg", leftImg);
            res = this.cutLeft(rightImg);
        }
    }

    /**
     * 左右切割
     *
     * @param src
     * @return
     */
    private int cutLeft(Mat src) {
        int left = 0;
        int right = src.cols();
        int i;
        for (i = 0; i < src.cols(); i++) {
            int colValue = this.getColPxSum(src, i);
            if (colValue > 0) {
                left = i;
                break;
            }
        }
        if (left == 0) {
            return 1;
        }
        for (; i < src.cols(); i++) {
            int colValue = this.getColPxSum(src, i);
            if (colValue == 0) {
                right = i;
                break;
            }
        }
        int width = right - left;
        Rect rect = new Rect(left, 0, width, src.rows());
        leftImg = src.submat(rect).clone();
        Rect rectRight = new Rect(right, 0, src.cols() - right, src.rows());
        rightImg = src.submat(rectRight).clone();
        this.cutTop(leftImg);
        return 0;
    }

    /**
     * 上下切割
     *
     * @param src
     */
    private void cutTop(Mat src) {
        int top, bottom;
        top = 0;
        bottom = src.rows();
        int i;
        for (i = 0; i < src.rows(); i++) {
            int colValue = this.getRowPxSum(src, i);
            if (colValue > 0) {
                top = i;
                break;
            }
        }
        for (; i < src.rows(); i++) {
            int colValue = this.getRowPxSum(src, i);
            if (colValue == 0) {
                bottom = i;
                break;
            }
        }
        int height = bottom - top;
        Rect rect = new Rect(0, top, src.cols(), height);
        leftImg = src.submat(rect).clone();
    }

    /**
     * 数字识别
     *
     * @param filename
     * @param template
     */
    public void compare(String filename, String template) {
        Mat src = Imgcodecs.imread(filename, Imgcodecs.IMREAD_GRAYSCALE);
        Imgproc.threshold(src, src, 100, 255, Imgproc.THRESH_BINARY_INV);
        int res = this.cutLeft(src);
        while (res == 0) {
            this.getSubtract(template);
            res = this.cutLeft(rightImg);
        }
    }

    /**
     * 与模板相减
     *
     * @param template
     */
    private void getSubtract(String template) {
        Integer min = null, result = null;
        Size size = new Size(32, 48);
        // 遍历模板0-9
        for (int i = 0; i < 10; i++) {
            Mat temp = Imgcodecs.imread(template + i + ".jpg", Imgcodecs.IMREAD_GRAYSCALE);
            Imgproc.resize(temp, temp, size, 0, 0, Imgproc.INTER_LINEAR);
            Imgproc.resize(leftImg, leftImg, size, 0, 0, Imgproc.INTER_LINEAR);
            Core.absdiff(temp, leftImg, diffImage);
            int diff = this.getPxSum(diffImage);
            if (min == null || diff < min) {
                min = diff;
                result = i;
            }
        }
        System.out.println(result);
    }

    /**
     * 像素点求和
     *
     * @param src
     * @return
     */
    private int getPxSum(Mat src) {
        int result = 0;
        for (int i = 0; i < src.rows(); i++) {
            for (int j = 0; j < src.cols(); j++) {
                result += src.get(i, j)[0];
            }
        }
        return result;
    }

    /**
     * 第row行像素点求和
     *
     * @param src
     * @param row
     * @return
     */
    private int getRowPxSum(Mat src, int row) {
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
    private int getColPxSum(Mat src, int col) {
        int result = 0;
        for (int i = 0; i < src.rows(); i++) {
            result += src.get(i, col)[0];
        }
        return result;
    }

}
