package com.le.matrix.redis.service.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import com.alibaba.dubbo.rpc.RpcContext;
import com.letv.common.email.ITemplateMessageSender;
import com.letv.common.email.bean.MailMessage;
import com.letv.common.exception.ApiNotFoundException;
import com.letv.common.exception.CommonException;
import com.letv.common.exception.OauthException;
import com.letv.common.exception.ValidateException;

@Service("dealExceptionService")
public class DealExceptionService {
	
	private Logger logger = LoggerFactory.getLogger(DealExceptionService.class);
	
	@Value("${error.email.to}")
	protected String ERROR_MAIL_ADDRESS;
	
	@Value("${error.email.enabled}")
	protected Boolean ERROR_MAIL_ENABLED;
	
	@Autowired
	protected ITemplateMessageSender defaultEmailSender;
	
	public void sendExceptionEmail(Exception ex) {
		Exception e = transE(ex);
		if(e instanceof OauthException || e instanceof CommonException || e instanceof ValidateException || e instanceof ApiNotFoundException) {
			//do not send email.
			logger.debug(e.getMessage());
		} else if(ERROR_MAIL_ENABLED) {
			String stackTraceStr = com.letv.common.util.ExceptionUtils.getRootCauseStackTrace(e);
			String exceptionMessage = e.getMessage();
			HttpServletRequest request = RpcContext.getContext().getRequest(HttpServletRequest.class);
			sendErrorMail(request, exceptionMessage, stackTraceStr);
			logger.error(exceptionMessage, e);
		}
	}
	
	private Exception transE(Exception e) {
    	if(e instanceof TypeMismatchException) {
    		e = new ApiNotFoundException("参数传递异常");
    	} 
    	if(e instanceof HttpRequestMethodNotSupportedException) {
    		e = new ApiNotFoundException("请求URL方法不支持");
    	} 
    	return e;
    }
	
	protected void sendErrorMail(HttpServletRequest request,String exceptionMessage,String stackTraceStr)
	{
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("exceptionContent", stackTraceStr);
		params.put("requestUrl", request.getRequestURL());
		
		String requestValue = getRequestValue(request);
		params.put("exceptionParams",  StringUtils.isBlank(requestValue) ? "无" : requestValue);
		params.put("exceptionMessage",  exceptionMessage == null ? "无" : exceptionMessage);
		
		params.put("hostIp", request.getRemoteHost());
		
		
		MailMessage mailMessage = new MailMessage("乐视云平台web-porta系统", ERROR_MAIL_ADDRESS,"异常错误发生","erroremail.ftl",params);
		try {
			defaultEmailSender.sendMessage(mailMessage);
		} catch (Exception e) {
			logger.error(getStackTrace(e));
		}
	}
	
	protected String getStackTrace(Throwable t) 
	{
        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        fillStackTrace(t, pw);
        return writer.toString();
    }
	
	private void fillStackTrace(Throwable t, PrintWriter pw)
    {
		Throwable ex = ExceptionUtils.getRootCause(t);
		if(null == ex)
			ex = t;
        ex.printStackTrace(pw);
    }
	
	protected String getRequestValue(HttpServletRequest request)
	{
		StringBuilder sb = new StringBuilder();
		Map<String, String[]> requestParams = (Map<String, String[]>)request.getParameterMap();
		Set<String> keySets = requestParams.keySet();
		for(String key:keySets)
		{
			String[] valueArrays = requestParams.get(key);
			sb.append(key);
			sb.append("=");
			sb.append(getRequestItemValue(valueArrays));
			sb.append("&");
		}
		String result = sb.toString();
		return result;
		
	}
	
	private String getRequestItemValue(String[] valueArrays)
	{
		String valueStr = "";
		for(String value:valueArrays)
		{
			valueStr += value;
		}
		return valueStr;
	}
}
