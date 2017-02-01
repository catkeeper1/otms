package com.ckr.otms.exception;

import com.ckr.otms.common.util.ArrayUtil;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 9112831069901766558L;

    private String exceptionID;

    List<ExceptionMessage> messageList = new ArrayList<>();

    protected BaseException() {
        super();
        genExceptionId();
    }

    protected BaseException(String arg0, Throwable arg1) {
        super(arg0, arg1);
        genExceptionId();
    }

    protected BaseException(String arg0) {
        super(arg0);
        genExceptionId();
    }

    protected BaseException(Throwable arg0) {
        super(arg0);
        genExceptionId();
    }

    private void genExceptionId() {
        if (exceptionID != null) {
            return;
        }

        DateFormat format = new SimpleDateFormat("yyMMddkkmmssSSS");
        StringBuilder buffer = new StringBuilder();

        buffer.append(format.format(new Date()));

        NumberFormat numberFormat = new DecimalFormat("0000");
        numberFormat.setMaximumIntegerDigits(4);
        buffer.append(numberFormat.format(this.hashCode()));

        exceptionID = buffer.toString();
    }

    private String printParams(ExceptionMessage expMsg) {
        if (expMsg == null || expMsg.getMessageParams() == null) {
            return null;
        }

        return ArrayUtil.toString(expMsg.getMessageParams());
    }

    public String getExceptionID() {
        return exceptionID;
    }


    public String toString() {

        StringBuilder result = new StringBuilder();

        for (ExceptionMessage expMsg : messageList) {
            result.append("message code:" + expMsg.getMessageCode() + " \r\n")
                  .append("message params:" + printParams(expMsg) + " \r\n");
        }

        return "exception ID:" + getExceptionID() + " " + result.toString() + " \r\n"
                + super.toString();

    }

    public List<ExceptionMessage> getMessageList() {
        return messageList;
    }

    public void addMessage(String msgCode, Object[] params) {

        ExceptionMessage expMsg = new ExceptionMessage(msgCode, params);

        messageList.add(expMsg);

    }


    public static class ExceptionMessage implements Serializable {


        private static final long serialVersionUID = 887496710964984427L;

        private String messageCode = null;

        private Object[] messageParams = null;

        /*public ExceptionMessage(){
            super();
    	}*/

        public ExceptionMessage(String code, Object[] params) {
            messageCode = code;
            messageParams = params;
        }

        public String getMessageCode() {
            return messageCode;
        }

        public Object[] getMessageParams() {
            return messageParams;
        }

    }

}
