import org.opencv.imgproc.Imgproc;

/**
 * HelloWorld
 *
 * @author chenhuihua
 * @date 2019/5/7
 */
public class HelloWorld {

    public static void main(String[] args) {
        ImgUtil util = new ImgUtil();
        String templateDir = "C:/Users/Administrator/Downloads/template/";
//        util.createTemplate(templateDir);
        util.compare("C:/Users/Administrator/Downloads/test.png", templateDir);
    }

}
