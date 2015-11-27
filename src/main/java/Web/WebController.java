package Web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import Master.Master;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dic on 20-11-2015.
 */
@Controller
public class WebController {
    private int count;
     Master master = null;

    @RequestMapping("/website")
    public String something(ModelMap model)
    {
        if(master==null)
        master = new Master(7777);
        System.out.println("started!: " +  ++count);
        File folderPath = master.folderInfo.folderPath;
        List<String> list = new ArrayList<String>();
        try {
            showDir(1,folderPath, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] list2 = {"1", "2"};
        model.addAttribute("folderList", list);

        for(String k : list)
        {
            System.out.println(k);
        }

        return "anotherpage.jsp";
    }
    static void showDir(int indent, File file, List<String> list)
            throws IOException {
        String s= "";
        for (int i = 0; i < indent; i++)
            s+="-";
        s+=file.getName();
        list.add(s);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++)
                showDir(indent + 4, files[i], list);
        }
    }
}
