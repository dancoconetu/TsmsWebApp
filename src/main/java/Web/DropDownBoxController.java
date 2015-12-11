package Web;

import Master.Master;
import Web.POJOS.ComputerName;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import Master.MasterThread;

/**
 * Created by dic on 11-12-2015.
 */
public class DropDownBoxController extends SimpleFormController
{
    public DropDownBoxController()
    {
        setCommandClass(ComputerName.class);
        setCommandName("SlaveDropDown");
    }


    @Override
    protected Object formBackingObject(HttpServletRequest request)
            throws Exception {

        ComputerName comp = new ComputerName();

        //make "Spring" as the default java skills selection
        comp.setName("Android");

        return comp;

    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
                                    HttpServletResponse response, Object command, BindException errors)
            throws Exception {

        ComputerName comp = (ComputerName) command;
        return new ModelAndView("SlaveDropDown","comp",comp);

    }

    protected Map referenceData(HttpServletRequest request) throws Exception {

        Map referenceData = new HashMap();
        Master master = Master.getInstance();

        Map<String,String> computerNames = new LinkedHashMap<String,String>();
        for (MasterThread masterThread :master.getAllSlaves()) {
           computerNames.put(masterThread.getName(), masterThread.getIp().toString());
        }
        referenceData.put("computerNames", computerNames);


        return referenceData;
    }
}
