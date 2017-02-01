package com.ckr.otms.common.web.interceptor;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ckr.otms.exception.SystemException;

import com.google.common.io.ByteSource;
import com.google.common.io.Files;

public class StaticFileDownLoadInterceptor extends HandlerInterceptorAdapter {

    private static Logger LOG = LoggerFactory.getLogger(StaticFileDownLoadInterceptor.class);

    private Long expiryDuration = (long) -1;

    private static Boolean isInDev = null;

    private static final String DEV_PATH = "/src";

    private static final String PRD_PATH = "/pub";

    //if this is true, it is allowed to download static file from src folder.
    private boolean allowUseSrcFolder = true;


    public void setExpiryDuration(Long expiryDuration) {
        this.expiryDuration = expiryDuration;
    }


    public void setAllowUseSrcFolder(boolean allowUseSrcFolder) {
        this.allowUseSrcFolder = allowUseSrcFolder;
    }


    private boolean getIsInDev(HttpServletRequest request) {

        if (isInDev == null) {

            //if "/src" folder is exist, that means current app is running in dev env.
            String devRealPath = request.getServletContext().getRealPath(DEV_PATH);

            File devSrcFolder = new File(devRealPath);

            isInDev = allowUseSrcFolder && devSrcFolder.isDirectory();
        }

        return isInDev;
    }


    private String getMappedPath(HttpServletRequest request) {

        String servletPath = request.getServletPath();

        //if current application is running in dev env and the url start with "/pub/**"
        //change the uri to "/src/**".
        if (getIsInDev(request) && servletPath.startsWith(PRD_PATH)) {
            servletPath = DEV_PATH + servletPath.substring(PRD_PATH.length());

            LOG.debug("adjusted servletPath {} ", servletPath);
        }

        String realPath = request.getServletContext().getRealPath(servletPath);


        return realPath;
    }


    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {


        long ifModifiedSince = request.getDateHeader("If-Modified-Since");


        LOG.debug("Last-If-Modified-Since {}", ifModifiedSince);

        String mappedPath = getMappedPath(request);

        LOG.debug("Download static file. mapped path is {}", mappedPath);

        File orgFile = new File(mappedPath);

        if (!orgFile.isFile()) {

            LOG.debug("The static file is not existed");


            return true;
        }

        long lastModified = orgFile.lastModified();


        LOG.debug("LastModified {}", lastModified);

        String verNo = request.getParameter("verNo");

        boolean setExpires = verNo != null && !"".equals(verNo) && expiryDuration > 0;

        if (setExpires) {

            LOG.debug("Cache-Control " + "max-age=" + expiryDuration);

            response.setHeader("Cache-Control", "max-age=" + expiryDuration);

        }


        //set the last modified time of this content.
        response.setDateHeader("Last-Modified", lastModified);


        if (lastModified / 1000 <= ifModifiedSince / 1000) {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);


            LOG.debug("Not modified. Returned status is " + HttpServletResponse.SC_NOT_MODIFIED);


            return false;
        }


        //response.setHeader("Cache-Control", "max-age=1, public");

        if (isGzipSupported(request) && !isGzipDisabled(request)) {
            File gzFile = new File(mappedPath + ".gz");

            if (gzFile.isFile()) {
                response.setHeader("Content-Encoding", "gzip");

                returnFile(response, gzFile);


                LOG.debug("Return gzip file {}", gzFile.getAbsolutePath());


                return false;
            }
        }

        returnFile(response, orgFile);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Return file " + orgFile.getAbsolutePath());
        }

        return false;

    }


    private boolean isGzipSupported(HttpServletRequest request) {
        String encodings = request.getHeader("Accept-Encoding");
        return ((encodings != null) && (encodings.indexOf("gzip") != -1));
    }


    //for debug purpose, sometimes, just want to disable the gz feature through parameter "disableGzip".
    private boolean isGzipDisabled(HttpServletRequest request) {
        String flag = request.getParameter("disableGzip");
        return ((flag != null) && (!flag.equalsIgnoreCase("false")));
    }


    private void returnFile(HttpServletResponse response, File file) {
        try {
            ByteSource bs = Files.asByteSource(file);

            bs.copyTo(response.getOutputStream());

        } catch (IOException e) {
            throw new SystemException(e);
        }
    }
}
