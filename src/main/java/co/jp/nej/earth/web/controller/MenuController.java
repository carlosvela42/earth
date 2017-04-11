package co.jp.nej.earth.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import co.jp.nej.earth.model.TemplateKey;
import co.jp.nej.earth.model.entity.MgrMenu;
import co.jp.nej.earth.model.enums.AccessRight;
import co.jp.nej.earth.util.MenuUtil;
import co.jp.nej.earth.util.TemplateUtil;

/**
 * 
 * @author p-tvo-thuynd
 *
 */
@Controller
@RequestMapping("/")
public class MenuController {
    
    @RequestMapping(value = "/testGetListMenu", method = RequestMethod.GET)
    public void testTetListMenu(HttpServletRequest request){
        List<MgrMenu> lst = MenuUtil.getListMenu(request.getSession());
        System.out.println("testGetListMenu : Listmenu size: " + lst.size());
    }
    
    @RequestMapping(value="/testGetMenuAuthority")
    public void testGetMenuAuthority(HttpServletRequest request){
        AccessRight accessRight = MenuUtil.getAuthority(request.getSession(), "functionId_1");
        System.out.println("testGetMenuAuthority : AccessRight: " + accessRight);
    }
    
    @RequestMapping(value="/testGetAccessibleTemplates")
    public void testGetAccessibleTemplates(HttpServletRequest request){
        List<String> lst = TemplateUtil.getAccessibleTemplates(request.getSession(), "001");
        System.out.println("testGetAccessibleTemplates : ListTemplate size: " + lst.size());
    }
    
    @RequestMapping(value="/testGetTemplateAuthority")
    public void testGetTemplateAuthority(HttpServletRequest request){
        TemplateKey templateKey = new TemplateKey();
        templateKey.setTemplateId("template1");
        templateKey.setWorkspaceId("001");
        AccessRight accessRight = TemplateUtil.getAuthority(request.getSession(), templateKey);
        System.out.println("testGetTemplateAuthority : AccessRight: " + accessRight);
    }
}
