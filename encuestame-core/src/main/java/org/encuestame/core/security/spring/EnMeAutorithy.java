package org.encuestame.core.security.spring;

import org.springframework.security.core.authority.GrantedAuthorityImpl;

public class EnMeAutorithy  extends GrantedAuthorityImpl {

    public EnMeAutorithy(String role) {
        super(role);
    }

}