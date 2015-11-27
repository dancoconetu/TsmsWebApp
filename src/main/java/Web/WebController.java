package Web;

import Master.Master;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dic on 20-11-2015.
 */
@Controller
public class WebController {
    private int count;
    Master master = null;
    List<File> list;

    @RequestMapping("/website")
    public String something(ModelMap model) {
        if (master == null)
            master = new Master(7777);
        System.out.println("started!: " + ++count);
        File folderPath = master.folderInfo.folderPath;
        list = new ArrayList<File>();
        try {
            showDir(1, folderPath, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] list2 = {"1", "2"};
        model.addAttribute("folderList", list);


        return "anotherpage.jsp";
    }

    @RequestMapping("/website/goToParentFolder")
    public String goToParentFolder(@RequestHeader("referer") String referedFrom) {

        System.out.println("Parent is: " + master.folderInfo.folderPath.getParent());
        master.folderInfo.folderPath = new File(master.folderInfo.folderPath.getParent());


        return "redirect:" + referedFrom;
    }

    @RequestMapping(value = "/website/{folderIndex}")
    public String changeMainFolder(@PathVariable("folderIndex") int folderIndex, @RequestHeader("referer") String referedFrom) {

        master.folderInfo.folderPath = list.get(folderIndex);

        return "redirect:" + referedFrom;

    }

    static void showDir(int indent, File file, List<File> list)
            throws IOException {


        list.add(file);
        if (file.isDirectory() && !file.isHidden() || file.isAbsolute()) {
            File[] files;
            try {
                files = file.listFiles();
                for (int i = 0; i < files.length; i++)
                    //showDir(indent + 4, files[i], list);
                    if (!files[i].isHidden())
                        list.add(files[i]);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
