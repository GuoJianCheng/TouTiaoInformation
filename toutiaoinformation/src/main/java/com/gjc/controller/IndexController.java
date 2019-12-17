package com.gjc.controller;

import com.gjc.model.User;
import com.gjc.service.ToutiaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

//@Controller
public class IndexController {
    @Autowired
    private ToutiaoService toutiaoService;

    @RequestMapping(path={"/","/index"})
    @ResponseBody
    public String index(HttpSession session){
        return "Hello NowCoder "+session.getAttribute("msg")+"<br/>Say: "+toutiaoService.say();
    }

    @RequestMapping(value={"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("groupId") String groupId,
                          @PathVariable("userId") int userId,
                          @RequestParam(value="type",defaultValue = "1") int type,
                          @RequestParam(value="key",defaultValue = "nowcoder") String key){
        return String.format("GID{%s},UID{%d},TYPE{%d},KEY{%s}",groupId,userId,type,key);
    }

    @RequestMapping(value={"/vm"})
    public String news(Model model){
        model.addAttribute("value1","vv1");
        List<String> colors = Arrays.asList(new String[]{"RED","GREEN","BLUE"});
        Map<String,String> map = new HashMap<String,String>();
        for(int i = 0;i<4;i++){
            map.put(String.valueOf(i),String.valueOf(i*i));
        }
        model.addAttribute("colors",colors);
        model.addAttribute("map",map);
        model.addAttribute("user",new User("Tom"));
        return "news";//要返回一个模板
    }

    @RequestMapping(value={"/request"})
    @ResponseBody
    public String request(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpSession session){
        StringBuilder sb = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            sb.append(name+":"+request.getHeader(name)+"<br/>");
        }

        for(Cookie cookie:request.getCookies()){
            sb.append("COOKIES:");
            sb.append(cookie.getName());
            sb.append("::");
            sb.append(cookie.getValue());
            sb.append("<br/>");
        }

        sb.append("getMethod:"+request.getMethod()+"<br/>");
        sb.append("getPathInfo:"+request.getPathInfo()+"<br/>");
        sb.append("getQueryString:"+request.getQueryString()+"<br/>");
        sb.append("getRequestURI:"+request.getRequestURI()+"<br/>");
        sb.append("getRequestURL:"+request.getRequestURL()+"<br/>");

        return sb.toString();
    }


    @RequestMapping(value={"/response"})
    @ResponseBody
    public String response(@CookieValue(value="nowcoderid",defaultValue = "a") String nowcoderid,
                           @RequestParam(value="key",defaultValue = "key") String key,
                           @RequestParam(value="value",defaultValue = "value") String value,
                           HttpServletResponse response
                           ) {
        response.addCookie(new Cookie(key,value));
        response.addHeader(key,value);
        return "NowCoderId From Cookie: "+nowcoderid;
    }

    @RequestMapping("/redirect/{code}")
    public String redirectView(@PathVariable("code")int code,
                               HttpSession session){
//        RedirectView redirectView = new RedirectView("/",true);
//        if(code == 301){
//            redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
//        }
        session.setAttribute("msg","Jump from redirect.");
        return "redirect:/";
    }


    @RequestMapping("/admin")
    @ResponseBody
    public String admin(@RequestParam(value="key",required = false) String key){
        if("admin".equals(key)){
            return "hello admin";
        }
        throw new IllegalArgumentException("Key错误");
    }

    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e){
        return "error:"+e.getMessage();
    }

}
