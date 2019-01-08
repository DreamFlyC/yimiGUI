
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ author     ：
 * @ Date       ：
 * @ Description：
 * @ Modified By：
 * @ Version    : 
 */
@Controller
@RequestMapping(value = "/manage/aabbcc")
public class AaBbCcAction extends BaseAction {
    @Autowired
    private IAaBbCcService aaBbCcService;

    @RequestMapping("")
    public String list() {
        instantPage(20);
        List<AaBbCc> list = aaBbCcService.getList();
        int total = aaBbCcService.getCount();
        Pager pager = new Pager(super.getPage(), super.getRows(), total);
        pager.setDatas(list);
        getRequest().setAttribute("pager", pager);
        return "/WEB-INF/aabbcc/aabbcc_list";
    }

    @RequestMapping(value = "/post", method = RequestMethod.GET)
    public String addAaBbCc() {
        return "/WEB-INF/aabbcc/aabbcc_add";
    }

    //增加
    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public String addAaBbCc(AaBbCc aabbcc) {
        aaBbCcService.save(aabbcc);
        return "redirect:/manage/aabbcc";
    }

    //删除
    @RequestMapping(value = "/del/{id}")
    public String deleteAaBbCc(@PathVariable("id") int id) {
        aaBbCcService.del(id);
        return "redirect:/manage/aabbcc";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String updateAaBbCc(AaBbCc aaBbCc) {
        aaBbCcService.edit(aaBbCc);
        return "redirect:/manage/aabbcc";
    }

    @RequestMapping(value = "/{id}")
    public String viewAaBbCc(@PathVariable("id") int id) {
        AaBbCc aaBbCc = aaBbCcService.get(id);
        getRequest().setAttribute("aaBbCc", aaBbCc);
        return "/WEB-INF/aabbcc/aabbcc_detail";
    }
}

