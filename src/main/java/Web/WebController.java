package Web;

import Master.Master;
import Master.MasterThread;
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
    private MasterThread currentMasterThread;
    Master master = null;
    List<File> list;
    List<Boolean> listBoolean;

    @RequestMapping("/website")
    public String something(ModelMap model) {
        if (master == null)
            master = Master.getInstance();
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



        return "main";
    }



    @RequestMapping("/tsms")
    public String tsms(ModelMap model) {
        if (master == null)
            master = Master.getInstance();
        System.out.println("started!: " + ++count);
        ArrayList<String> ips = new ArrayList<String>();

//        if (master.clients[0]!=null)
        for (MasterThread masterThread :master.clients)
        {   if (masterThread != null)
            ips.add(masterThread.getID() + "");
        }


        model.addAttribute("ips", ips);
        for(MasterThread masterThread : master.clients)
        {   if (masterThread!=null)
            masterThread.sendMessage("SendOsInfo");
        }


        return "tsms";
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



    @RequestMapping(value = "/website/ips/{ipsPosition}")
    public String sendFiles(@PathVariable("ipsPosition") int ipsPosition, ModelMap model) {

        currentMasterThread = master.clients[ipsPosition];

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
        model.addAttribute("PcName", currentMasterThread.osInfo.get("PcName"));
        model.addAttribute("OsName", currentMasterThread.osInfo.get("OsName"));

        return "main";
    }
    @RequestMapping(value = "/sendFiles/{folderIndex}")
    public String sendFiles(@PathVariable("folderIndex") int folderIndex, @RequestHeader("referer") String referedFrom) {
        File file = master.folderInfo.folderPath;
        System.out.println("sending this folder:" + list.get(folderIndex));
        if(! list.get(folderIndex).isFile()) {
            currentMasterThread.sendMissingFiles(list.get(folderIndex), master.folderInfo);
            System.out.println("it s a directory");
        }
        else {
            currentMasterThread.sendFile(list.get(folderIndex), master.folderInfo);
            //master.folderInfo.folderPath = file;
        }

        master.getAllSlaves()[0].sendMessage("server:script");

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
