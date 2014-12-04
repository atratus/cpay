/**
 *
 */
package trsit.cpay;

import javax.inject.Inject;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * @author black
 *
 */
@Service
public final class SpringContextHolder {
    private static ApplicationContext ctx;

    @Inject
    public void setApplicationContext(ApplicationContext ctx) {
        SpringContextHolder.ctx = ctx;
    }

    public static ApplicationContext getCtx() {
        return ctx;
    }
}
