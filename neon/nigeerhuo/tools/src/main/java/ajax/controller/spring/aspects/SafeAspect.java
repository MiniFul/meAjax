package ajax.controller.spring.aspects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;

import ajax.model.AjaxResponse;
import ajax.model.exception.LimitsOfAuthorityException;
import ajax.model.safe.User;

@Aspect
public class SafeAspect {
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	
	@Pointcut("@within(ajax.model.annotations.AdminPointcut) || @annotation(ajax.model.annotations.AdminPointcut)")
	public void AdmintCut(){};
	
	@Pointcut("@within(ajax.model.annotations.AdminPointcutForAjax) || @annotation(ajax.model.annotations.AdminPointcutForAjax)")
	public void AdmintCutForAjax(){};
//	@Pointcut("execution(** ajax.controller.spring.JobController.*(..))")
//	public void JobsCut(){};
	// 上面这种不安全, 被标注了 @ResponseBody的方法不会被切点捕获
	
	
	@Pointcut("@within(ajax.model.annotations.EditorPointcut) || @annotation(ajax.model.annotations.EditorPointcut)")
	public void editorPointcut(){};
	
	@Pointcut("@within(ajax.model.annotations.EditorPointcutForAjax) || @annotation(ajax.model.annotations.EditorPointcutForAjax)")
	public void editorPointcutForAjax(){};
	
	// admincut 和 admincutForAjax 都能跑的通, 可是当用户是admin时, 会俩个切点都执行, 用户不是admin的时候只执行最具体的切点
	@Around("AdmintCut()")
	public Object beforeTest(ProceedingJoinPoint proceedingJoinPoint) throws LimitsOfAuthorityException,Throwable {
		if(!User.isAdmin(request, response)) {
			
			request.setAttribute("model", "权限不足 (LimitsOfAuthorityException)");
			return "/views/error/error";
			
		} else {
			return proceedingJoinPoint.proceed();
		}
	}
	
	@Around("AdmintCutForAjax()")
	public Object AdmintCutForAjax(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		
		if(!User.isAdmin(request, response)) {
			AjaxResponse<String> ar = new AjaxResponse<>();
			ar.setIsok(false);
			ar.setData("权限不足");
			return ar;
		} else {
			return proceedingJoinPoint.proceed();
		}
	}
	
	
	@Around("editorPointcut()")
	public Object editorPointcutAuthority(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		
		User user = User.getLoginUser(request);
		if (user == null || !user.isCanSubmitBlog()) {
			request.setAttribute("model", "权限不足 (LimitsOfAuthorityException)");
			return "/views/error/error";
		} else {
			return proceedingJoinPoint.proceed();
		}
	}
	
	@Around("editorPointcutForAjax()")
	public Object editorPointcutForAjaxAuthority(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		
		User user = User.getLoginUser(request);
		if (user == null || !user.isCanSubmitBlog()) {
			AjaxResponse<String> ar = new AjaxResponse<>();
			ar.setIsok(false);
			ar.setData("权限不足, 您没有博客权限");
			return ar;
		} else {
			return proceedingJoinPoint.proceed();
		}
	}
	
	
	
}
