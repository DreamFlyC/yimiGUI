package czp;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @ author     ：CZP.
 * @ Date       ：Created in 11:00 2019/1/8
 * @ Description：
 * @ Modified By：
 * @ Version    : $
 */
public class RunTest {


    @Test
    public void  read() {
        File file=new File("D:\\abc\\aabbcc_list.jsp");
        String encoding="UTF-8";
        try {
            String data = FileUtils.readFileToString(file, encoding);
            System.out.println(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
