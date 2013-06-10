package core.prototype.web;

import core.prototype.config.Application;
import core.prototype.dao.DaoProfilesConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {

    @Autowired
    DaoProfilesConfig dbConfig;

    @RequestMapping(value = "/riris", method = RequestMethod.GET)
    public String rIris() {
        return "r-iris-plot";
    }

    @RequestMapping(value = "/frame", method = RequestMethod.GET)
    public String rFrame() {
        return "r-frame-plot";
    }

    @RequestMapping(value = "/")
    public String home(Model model) {
        Application app = Application.getInstance();
        String configs = app.getDomain().displayConfigParameters();
        model.addAttribute("value", configs);
        return "home";
    }
}
