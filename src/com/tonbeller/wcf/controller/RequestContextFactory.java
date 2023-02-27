package com.tonbeller.wcf.controller;

import com.tonbeller.tbutils.res.Resources;
import com.tonbeller.wcf.convert.Converter;
import com.tonbeller.wcf.format.Formatter;
import java.io.Serializable;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RequestContextFactory extends Serializable {

	public abstract RequestContext createContext(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse);

    public abstract Formatter getFormatter();

    public abstract Converter getConverter();

    public abstract Locale getLocale();

    public abstract Resources getResources();

    /**
     * @deprecated Method setLocale is deprecated
     */

    public abstract void setLocale(Locale locale);

    public abstract void setLocale(HttpServletRequest httpservletrequest, Locale locale);

    public abstract String getRemoteUser();

    public abstract String getRemoteDomain();
}
