package org.smart4j.framework.shiro;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

public class HelloShiro {

	private static final Logger LOGGER = Logger.getLogger(HelloShiro.class);
	
	/**
	 * 1 需要读取classpath下的shiro.ini配置文件，并通过工厂类创建类SecurityManager对象
	 * 最终将其放入SecurityUtils,供shiro框架随时获取。
	 * 2 同样通过SecurityUtils类获取Subject对象，其实就是当前用户，只不过在Shiro的世界里优雅
	 * 地将其称为Subject主体。
	 * 3 首先使用一个Username与password来创建UsernamePasswordToken对象，然后通过这个Token
	 * 对象调用Subject对象的login方法，让shiro进行用户身份认证。
	 * 4 当登录失败的时候，可以使用AuthenticationException来捕获这个异常；当登录成功时，可以调用
	 * Subject对象的getPrincipal方法来获取Username,此时shiro已经创建好了一个Session.
	 * 5 最后还是通过Subject对象的logout来注销本次Session
	 * @param args
	 */
	public static void main (String[] args){
		//初始化 SecurityManager
		Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
		SecurityManager securityManager = factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
		
		//获取当前用户
		Subject subject = SecurityUtils.getSubject();
		
		//登录
		UsernamePasswordToken token = new UsernamePasswordToken("shiro", "201314");
		
		try {
			subject.login(token);
		} catch (Exception e) {
			LOGGER.info("登录失败!");
			return;
		}
	
		LOGGER.info("登录成功!" + subject.getSession().getId());
		//注销
		subject.logout();
	}
	
	
}
