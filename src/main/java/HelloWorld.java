/**
 * HelloWorld
 *
 * @author chenhuihua
 * @date 2019/5/7
 */
public class HelloWorld {

    public static void main(String[] args) {
        ImgUtil util = new ImgUtil();
        String templateDir = "C:/work/project/opencv/template/";
        util.createTemplate(templateDir);
//        util.compare("C:/Users/Administrator/Documents/地下城与勇士/ScreenShot", templateDir);
    }

}
