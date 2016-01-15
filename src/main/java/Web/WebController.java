package Web;

import Master.Common.XMLClasses.XMLParser;
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
    int resultsNr ;

//    @RequestMapping("/website")
//    public String something(ModelMap model) {
//        if (master == null)
//            master = Master.getInstance();
//        System.out.println("started!: " + ++count);
//        File folderPath = master.folderInfo.folderPath;
//        if (folderPath !=null) {
//            list = new ArrayList<File>();
//            listBoolean = new ArrayList<Boolean>();
//            try {
//                showDir(1, folderPath);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//
//        }
//        else
//        {
//            File[] roots = File.listRoots();
//            list = new ArrayList<File>();
//            listBoolean = new ArrayList<Boolean>();
//            for(int i = 0; i < roots.length ; i++) {
//                list.add(roots[i]);
//                listBoolean.add(true);
//            }
//        }
//        model.addAttribute("folderList", list);
//        model.addAttribute("folderListBoolean", listBoolean);
//
//
//
//        return "main";
//    }



    @RequestMapping("/tsms")
    public String tsms(ModelMap model) {
        if (master == null)
            master = Master.getInstance();
        System.out.println("started!: " + ++count);
        ArrayList<String> ips = new ArrayList<String>();

//        if (master.clients[0]!=null)



        model.addAttribute("ips", ips);
        for(MasterThread masterThread : master.clients)
        {
            if (masterThread!=null)
                {
                masterThread.sendMessage("SendOsInfo");
                masterThread.sendMessage("AvailablePythonScripts");
                }
        }

        for (MasterThread masterThread :master.clients)
        {   if (masterThread != null)
            if (masterThread.osInfo!=null)
            ips.add(masterThread.getIp() + " : " + masterThread.osInfo.get("OsName") + " ---" + masterThread.osInfo.get("PcName") );
            else
                ips.add(masterThread.getIp()+"");
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
    public String goToSlave(@PathVariable("ipsPosition") int ipsPosition, ModelMap model) {

        currentMasterThread = master.clients[ipsPosition];
        currentMasterThread.sendMessage("AvailablePythonScripts");
        currentMasterThread.sendMessage("AvailablePythonVersions");
        File folderPath = master.folderInfo.folderPath;
        if (folderPath !=null) {
            list = new ArrayList<File>();
            listBoolean = new ArrayList<Boolean>();
            try {
                showDir(folderPath);
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
        model.addAttribute("PythonVersions", currentMasterThread.pythonVersions);
        model.addAttribute("XmlResults", currentMasterThread.getXmlResults());
        model.addAttribute("XmlResultsHashTables",currentMasterThread.getXmlResultsHashtables() );
        model.addAttribute("XmlResultsBooleans", currentMasterThread.getXmlResultsBoolean());
        if (currentMasterThread.lastScriptResults!=null)
        {
            model.addAttribute("LastScriptResults", currentMasterThread.lastScriptResults);
        }
        else
        {
            model.addAttribute("LastScriptResults", "No Script was run lately");
        }

        model.addAttribute("Status", currentMasterThread.status);

        while(currentMasterThread.pythonScriptsAvailable==null);
        ArrayList<String> availablePythonScripts = new ArrayList<String>();
        System.out.println("Nr of scripts: " + currentMasterThread.pythonScriptsAvailable.length);
        for (int i=0; i< currentMasterThread.pythonScriptsAvailable.length; i++ )
        {
            availablePythonScripts.add(currentMasterThread.pythonScriptsAvailable[i][0] + ":(" +currentMasterThread.pythonScriptsAvailable[i][2] + ")" );
        }
        model.addAttribute("Scripts", availablePythonScripts );
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



        return "redirect:" + referedFrom;

    }


    @RequestMapping(value = "/deleteFile/{fileIndex}")
    public String deleteFile(@PathVariable("fileIndex") int fileIndex, @RequestHeader("referer") String referedFrom) {
        currentMasterThread.sendMessage("deleteFile:" + currentMasterThread.filesAvailable[fileIndex][2]);



        return "redirect:" + referedFrom;

    }

    @RequestMapping(value = "/runScript/{scriptIndex}/{pythonVersion}")
    public String runSript(@PathVariable("scriptIndex") int scriptIndex,@PathVariable("pythonVersion") String pythonVersion , @RequestHeader("referer") String referedFrom) {

        try {
            System.out.println("Running this script:" + currentMasterThread.pythonScriptsAvailable[scriptIndex][2]);


            currentMasterThread.sendMessage("server:script" + currentMasterThread.pythonScriptsAvailable[scriptIndex][2] + ";" + pythonVersion);
            currentMasterThread.status = "Request to run script sent to slave";
        }
        catch (Exception ex)
        {   ex.printStackTrace();
            if (currentMasterThread.pythonScriptsAvailable.length<scriptIndex-1)
                System.out.println("Index out of bound.");
        }
        return "redirect:" + referedFrom;

    }

    @RequestMapping(value = "/showXml/{xmlIndex}")
    public String showXml(@PathVariable("xmlIndex") int xmlIndex , ModelMap model) {

        XMLParser xmlParser = new XMLParser();
        String xml = xmlParser.parseXmlFile(currentMasterThread.getXmlResults().get(xmlIndex));
        model.addAttribute("xml",xml);
        return "ShowXml";

    }

    @RequestMapping(value = "/showFiles")
    public String showFiles( ModelMap model) {
        currentMasterThread.sendMessage("FilesAvailable");
        ArrayList<String> filesAvailable = new ArrayList<String>();
        while (currentMasterThread.filesAvailable==null);
        System.out.println("Nr of filesAvailable: " + currentMasterThread.filesAvailable.length);
        for (int i=0; i< currentMasterThread.filesAvailable.length; i++ )
        {
            filesAvailable.add(currentMasterThread.filesAvailable[i][2]);
        }

        model.addAttribute("filesAvailable",filesAvailable);
        return "ShowFiles";

    }

    void showDir(File file)
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
