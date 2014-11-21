/**
 * 
 */
package trsit.cpay.web.edit;

import trsit.cpay.persistence.model.User;

/**
 * @author black
 */
public class UserViewBeanBuilder {

    public static UserViewBean from(User user) {
        if(user == null) {
            return null;
        }
        return UserViewBean.builder()
                .name(user.getName())
                .userId(user.getId())
                .build();
    }

}
