/*
 * Copyright 2005 The JA-SIG Collaborative.  All rights reserved.
 * See license distributed with this file and
 * available online at http://www.uportal.org/license.html
 */
package org.jasig.cas.services.advice;

import java.lang.reflect.Method;

import org.jasig.cas.authentication.Service;
import org.jasig.cas.services.AuthenticatedService;
import org.jasig.cas.services.ServiceRegistry;
import org.jasig.cas.services.UnauthorizedServiceException;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public class ServiceAllowedMethodBeforeAdvice implements MethodBeforeAdvice,
    InitializingBean {

    private ServiceRegistry serviceRegistry;

    /**
     * @see org.springframework.aop.MethodBeforeAdvice#before(java.lang.reflect.Method,
     * java.lang.Object[], java.lang.Object)
     */
    public final void before(Method method, Object[] args, Object target)
        throws Throwable {
        Service service = (Service)args[1];
        AuthenticatedService authenticatedService = this.serviceRegistry
            .getService(service.getId());

        if (authenticatedService == null) {
            throw new UnauthorizedServiceException();
        }

        beforeInternal(method, args, target, authenticatedService);
    }

    protected void beforeInternal(Method method, Object[] args, Object target,
        AuthenticatedService service) throws Exception {
        // this will be overwritten by extending classes
    }

    /**
     * @return Returns the serviceRegistry.
     */
    public ServiceRegistry getServiceRegistry() {
        return this.serviceRegistry;
    }

    /**
     * @param serviceRegistry The serviceRegistry to set.
     */
    public void setServiceRegistry(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    /**
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public final void afterPropertiesSet() throws Exception {
        if (this.serviceRegistry == null) {
            throw new IllegalStateException(
                "ServiceRegistry cannot be null on "
                    + this.getClass().getName());
        }

        afterPropertiesSetInternal();
    }

    public void afterPropertiesSetInternal() throws Exception {
        // designed to be overwritten
    }
}
