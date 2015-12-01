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
    List<Boolean> listBoolean;

    @RequestMapping("/website")
    public String something(ModelMap model) {
        if (master == null)
            master = new Master(7777);
        System.out.println("started!: " + ++count);
        File folderPath = master.folderInfo.folderPath;
        if (folderPath !=null) {
            list = new ArrayList<File>();
            listBoolean = new ArrayList<Boolean>();
            try {
                showDir(1, folderPath);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        else
        {
            File[] roots = File.listRoots();
            list = new ArrayList<File>();
            listBoolean = new ArrayList<Boolean>();
            for(int i = 0; i < roots.length ; i++) {
                list.add(roots[i]);
                listBoolean.add(true);
            }
        }
        model.addAttribute("folderList", list);
        model.addAttribute("folderListBoolean", listBoolean);


        return "anotherpage.jsp";
    }

    @RequestMapping("/website/goToParentFolder")
    public String goToParentFolder(@RequestHeader("referer") String referedFrom) {
        try {


            //if (master.folderInfo.folderPath.getParentFile().exists()) {
                System.out.println("Parent is: " + master.folderInfo.folderPath.getParent());
                master.folderInfo.folderPath = new File(master.folderInfo.folderPath.getParent());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            master.folderInfo.folderPath = null;
        }


        return "redirect:" + referedFrom;
    }

    @RequestMapping(value = "/website/{folderIndex}")
    public String changeMainFolder(@PathVariable("folderIndex") int folderIndex, @RequestHeader("referer") String referedFrom) {

        master.folderInfo.folderPath = list.get(folderIndex);

        return "redirect:" + referedFrom;

    }


    @RequestMapping(value = "/sendFiles/{folderIndex}")
    public String sendFiles(@PathVariable("folderIndex") int folderIndex, @RequestHeader("referer") String referedFrom) {
        File file = master.folderInfo.folderPath;
        master.folderInfo.folderPath = list.get(folderIndex);
        System.out.println("sending this folder:" + master.folderInfo.folderPath);
        master.getAllSlaves()[0].sendMissingFiles(master.folderInfo.folderPath,master.folderInfo);

        //master.folderInfo.folderPath = file;


        return "redirect:" + referedFrom;

    }
    void showDir(int indent, File file)
            throws IOException {


        list.add(file);


        if (file.isDirectory() && !file.isHidden() || file.isAbsolute()) {
            listBoolean.add(false);
            File[] files;
            try {
                files = file.listFiles();
                for (int i = 0; i < files.length; i++)
                    //showDir(indent + 4, files[i], list);
                    if (!files[i].isHidden()) {
                        list.add(files[i]);
                        if (files[i].isDirectory())
                            listBoolean.add(true);
                        else
                            listBoolean.add(false);
                    }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
