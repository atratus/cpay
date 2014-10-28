/**
 * 
 */
package trsit.cpay;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author black
 *
 */
public class CPay {

    private static final String APP_CTX = "context.xml";

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(APP_CTX);
        System.in.read();
    }


}
