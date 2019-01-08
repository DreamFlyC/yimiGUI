package czp;

import java.io.File;

/**
 * @ author     ：CZP.
 * @ Date       ：Created in 11:00 2019/1/8
 * @ Description：
 * @ Modified By：
 * @ Version    : $
 */
public class RunTest {

    //java模板文件
    private static final String TEMPLATES = "/src/main/resources/templates/";

    //jsp模板文件
    private static final String WEB_TEMPLATES = "src/main/resources/webTemplates/";

    public static void main(String[] args) {
        try {
            String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            File af = new File(path + "/webTemplates");
            System.out.println(af.getPath());
            if (!af.exists()){
                System.out.println("nullEXIST");
            }
            String[] files = af.list();
            if (files.length == 0){
                System.out.println("nullLENGTH");
            }
            for (String file : files) {
                if (file != null){
                    System.out.println(file);
                }
                else {
                    System.out.println("null");
                }
            }

        } catch (Exception e) {
            System.out.println("HugeERROR");
        }
    }

}
