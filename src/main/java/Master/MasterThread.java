package Master; /**
 * Created by dic on 18-09-2015.
 */


import Master.Common.Chronometer;
import Master.Common.FolderInfo;
import Master.Common.XMLClasses.XMLCreator;
import Master.Common.XMLClasses.XMLParser;
import com.sun.corba.se.impl.orbutil.concurrent.Mutex;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;

public class MasterThread extends Thread
{  private Master master = null;
    private Socket           socket    = null;
    private int              ID        = -1;
    private DataInputStream  streamIn  =  null;
    private DataOutputStream streamOut = null;
    private InetAddress           ip;
    private String PATH = "C:\\TSMS";
    private Mutex mutex;
    private FolderInfo folderInfo;
    private Socket socketFileReceive;
    private Socket socketFileSend;
    private int filesCount;
    public int repeted= 0;
    private byte[] mybytearray;
    public Hashtable osInfo;
    public String[][] pythonScriptsAvailable;
    public String lastScriptResults;
    public ArrayList<String> pythonVersions;

    public MasterThread(Master _master, Socket _socket, Socket _socketFileReceive, Socket _socketFileSend, Mutex _mutex, FolderInfo _folderInfo)
    {  super();
        master = _master;
        socket = _socket;
        ID     = socket.getPort();
        mutex = _mutex;
        folderInfo = _folderInfo;
        socketFileReceive = _socketFileReceive;
        socketFileSend = _socketFileSend;
        ip = socket.getInetAddress();

    }
    public void sendMessage(String _msg) {
        final String msg = _msg;
        //Thread t1 = new Thread(new Runnable() {
           // public void run() {
                try

                {  // System.out.println("Aquiring in SENDMESSAGE");
                    mutex.acquire();
                    streamOut.writeUTF(msg);
                  //  System.out.println("ACQUIRED in SENDMESSAGE");
                    mutex.release();
                } catch (IOException ioe) {
                    System.out.println(ID + " ERROR sending: " + ioe.getMessage());
                    master.remove(ID);
                    stop();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        streamOut.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
       // });
       // t1.start();
    //}

    public void sendMessageWithoutMutex(String message)
    {
        try {
            streamOut.writeUTF(message);
            streamOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getID()
    {
        return ID;
    }

    public void setIp(InetAddress ip)
    {
        this.ip = ip;
    }

    public InetAddress getIp()
    {
        return ip;
    }

    public void run()
    {
        System.out.println("Master Thread " + ID + " running.");
        while (true)
        {
            try
            {


               // while(master.inUse);
                String s = streamIn.readUTF();

                master.handle(ID, s );


            }
            catch(IOException ioe)
            {
                ioe.printStackTrace();
                System.out.println(ID + " ERROR reading: " + ioe.getMessage());
               master.remove(ID);
                stop();
            }
        }
    }

    public void open() throws IOException
    {
        streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    public void close() throws IOException
    {   if (socket != null)    socket.close();
        if (streamIn != null)  streamIn.close();
        if (streamOut != null) streamOut.close();
    }

    public void receiveFile(String imageName, String imagePath, long fileSize)
    {
//        try {
//            master.receiveMutex.acquire();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        try {
            new DataOutputStream(socketFileReceive.getOutputStream()).writeUTF(imageName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        master.inUse = true;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        long sizeReceived = 0;
        // long fileSize = 0;
        String IMAGE_TO_BE_RECEIVED="";
        try
        {   //sendMessage("Go");
            long startTime = System.currentTimeMillis();
            BufferedInputStream bis = new BufferedInputStream(socketFileReceive.getInputStream());
            DataInputStream dis = new DataInputStream(bis);
          //  String imageName = dis.readUTF();
          //  String imagePath = dis.readUTF();
            imagePath = imagePath.replace("\\", File.separator);
            File path2 =  new File(PATH +getIp().toString()+ File.separator + imagePath);
            path2.mkdirs();
            //String imageFound = dis.readUTF();
//            System.out.println(imageFound);
//            if (!imageFound.equals("ImageFound") || imageFound.equals("ImageNotFound")  )
//            {
//                throw new Exception();
//            }
            sleepTime();
            IMAGE_TO_BE_RECEIVED = path2.getCanonicalPath() + File.separator + imageName ;
            fos = new FileOutputStream(IMAGE_TO_BE_RECEIVED);
            bos = new BufferedOutputStream(fos);
           // fileSize = dis.readLong();
            //System.out.println("File size: " + fileSize);

            int bytesRead = 8192;
            byte[] buffer = new byte[bytesRead];
            while(sizeReceived<fileSize && (bytesRead = bis.read(buffer, 0, 8192))>0)
                {
                    sizeReceived += bytesRead;
                    //System.out.println(sizeReceived + " Available: " + bis.available() + "Count: " + bytesRead);
                    bos.write(buffer, 0, bytesRead);
                    bos.flush();
                }
            long estimatedTime = System.currentTimeMillis() - startTime;
            System.out.println("File " + IMAGE_TO_BE_RECEIVED + " downloaded (" + sizeReceived + " bytes read)"
                                       + " repeated:  " + repeted + " Time Elapsed: " + estimatedTime/1000.0 );



        }
        catch (Exception e)
        {
            e.printStackTrace();

        }
        finally
        {
            try
            {
                if (bos != null) bos.close();
                if (fos != null) fos.close();
                if (fileSize != sizeReceived ) {
                    System.out.println("\n\n\n\n-----------------------malicious file sent: + " +  IMAGE_TO_BE_RECEIVED +  ": Ip: " + ip + "-------------------------\n\n\n\n");
                    new File(IMAGE_TO_BE_RECEIVED).delete();

                }





            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        master.receiveMutex.release();


        master.inUse = false;
    }

    public void sendMissingFiles(File folder, FolderInfo folderInfo)
    {
        ArrayList<File> allFilesInSubFolders= new ArrayList<File>();
        folderInfo.listf(folder.toString(), allFilesInSubFolders);

//        for (File f : allFilesInSubFolders)
//        {
//            System.out.println("FileName: " + f.getAbsolutePath().toString());
//        }

        XMLCreator xmlCreator = new XMLCreator(folderInfo);
        String xmlFiles = xmlCreator.createSendMultipleFilesXml(allFilesInSubFolders);
        sendMessage(xmlFiles);


    }

    public void sendMultipleFilesFromList(String[][] list)
    {
        for (String[] row: list)
        {
            File path2 =  new File(folderInfo.folderPath + row[2]);

            File file = new File(path2.getAbsolutePath() + File.separator + row[0]);

            if (file.exists())
                sendFile(file,folderInfo);
        }
    }

    public void sendMultipleFiles(File folder, FolderInfo folderInfo)
    {
        for (File f: folderInfo.getOnlyFiles(folder))
        {

            sendFile(f,folderInfo);
            System.out.println(f.getName() + " path from TSMS: " + f.getAbsolutePath().substring( folderInfo.folderPath.getAbsolutePath().length()));

        }

        for (File f : folderInfo.getFolders(folder))
        {
            System.out.println(f.getName() + ": " + f.getAbsolutePath().substring( folderInfo.folderPath.getAbsolutePath().length()));
            sendMultipleFiles(f,folderInfo);
        }

    }


    public void sendFile(File myFile, FolderInfo folderInfo) {

        XMLCreator xmlCreator = new XMLCreator(folderInfo);
        String fileXml = xmlCreator.createSendFileXMLDoc(myFile);
        sendMessage(fileXml);
        System.out.println(fileXml);

        try {
            System.out.println("Try to acquire sendMutex send");
            mutex.acquire();
            System.out.println("Mutex acquired send");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        FileInputStream fis = null;
        BufferedInputStream bis = null;
        //OutputStream os = null;
        BufferedOutputStream bos = null;
        DataOutputStream dos;
        //sendMessageWithoutMutex("server:sendToClient");

        try
        {
            bos = new BufferedOutputStream(socketFileSend.getOutputStream());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        dos = new DataOutputStream(bos);
        sleepTime();

        try
        {   System.out.println("Sending " + myFile.getCanonicalPath() + "(" + myFile.length() + " bytes)");

            //DataInputStream streamIn  = new DataInputStream(socket.getInputStream());
            //while (!streamIn.readUTF().equals("Go")){}
            if (myFile.length()> 150502457)
                throw new FileNotFoundException();
           // sendMessageWithoutMutex(myFile.getName()); //sending file name
            //sendMessageWithoutMutex(myFile.getParentFile().getAbsolutePath().substring(folderInfo.folderPath.getAbsolutePath().length()));

            mybytearray = new byte[(int) myFile.length()];
            fis = new FileInputStream(myFile);
            bis = new BufferedInputStream(fis);

            bis.read(mybytearray, 0, mybytearray.length);
            //sendMessageWithoutMutex("ImageFound");

            //long fileLength = myFile.length();
            //dos.writeLong(fileLength);

            bos.write(mybytearray, 0, mybytearray.length);
            bos.flush();
            System.out.println("Done.");
            mutex.release();
            System.out.println("Mutex released SEND");


        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            System.out.println("File not found!");
            //sendMessageWithoutMutex("ImageNotFound");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            System.out.println("sent");
            sleepTime();

            //sendMessage("succesfully sent");
         try {
            if (bis != null) bis.close();
            if (fis != null) fis.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

        }


        master.status[0]= master.status[1];
        master.status[1] = System.currentTimeMillis();

    }

    public void sleepTime()
    {
        try {
            sleep(500);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public ArrayList<File> getXmlResults()
    {
        File location = new File(PATH + File.separator + getIp().toString());

        return folderInfo.getAllFilesWithExtensionFromSubfolders("xml",location);


    }

    public ArrayList<Hashtable> getXmlResultsHashtables()
    {   ArrayList<Hashtable> hashtables = new ArrayList<Hashtable>();
        for (File file: getXmlResults())
        {
            XMLParser xmlParser = new XMLParser();
            Hashtable hashtable = xmlParser.parseXmlResult(file);
            hashtables.add(hashtable);
        }

        return hashtables;

    }

    public ArrayList<Boolean> getXmlResultsBoolean()
    {
        ArrayList<Hashtable> hashtables = getXmlResultsHashtables();
        ArrayList<Boolean> booleans = new ArrayList<Boolean>();

        for (Hashtable hashtable: hashtables)
        {
            if (hashtable.get("failures").equals("0"))
                booleans.add(true);
                else
                booleans.add(false);
        }
        for(Boolean boo : booleans)
        {
            System.out.println("Script true or false? :" + boo.toString());
        }
        return booleans;
    }
}