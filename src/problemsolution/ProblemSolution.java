package problemsolution;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.OutputStreamWriter;
import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.parsers.DocumentBuilder;  
import org.w3c.dom.Document;  
import org.w3c.dom.NodeList;  
import org.w3c.dom.Node;  
import org.w3c.dom.Element;

class RssUpdater extends TimerTask
{  
    @Override
    public void run()
    {
        try
        {
            URL url = new URL("http://rss.cnn.com/rss/edition.rss");
            HttpURLConnection httpUrlConnection = (HttpURLConnection)url.openConnection();
            if(httpUrlConnection.getResponseCode() == 200)
            {
                InputStream inputStream = httpUrlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                FileOutputStream fileOutputStream = new FileOutputStream("d:\\a.rss");
                BufferedWriter bufferedWrite = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
                String line = bufferedReader.readLine();
                while (line != null)
                {
                    bufferedWrite.write(line);
                    bufferedWrite.newLine();
                    bufferedWrite.flush();
                    line = bufferedReader.readLine();
                }
                System.out.println("d:\\a.rss is updated.");
            }
        }
        catch(Exception e)
        {
          System.out.println(e);
        }
    }
}

class RssJpgImageReader extends TimerTask
{
    @Override
    public void run()
    {
        try
        {   
            //an instance of factory that gives a document builder  
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();  
            //an instance of builder to parse the specified xml file  
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            //parsing an XML file
            Document doc = documentBuilder.parse("d:\\a.rss");  
            doc.getDocumentElement().normalize();    
            NodeList nodeList = doc.getElementsByTagName("media:group");  
            int urlCount = 1;
            for (int itr = 0; itr < nodeList.getLength(); itr++)   
            {  
                Node node = nodeList.item(itr);   
                NodeList childNodeList = node.getChildNodes();
                for(int i = 0; i < childNodeList.getLength(); i++)
                {
                    Node child = childNodeList.item(i);
                    Element childElement = (Element) child; 
                    if ("media:content".equals(childElement.getNodeName()))
                    {
                         System.out.println("JPG image reference "+ urlCount+ " : " +childElement.getAttribute("url"));
                         urlCount++;
                    }
                }
            }  
        }   
        catch (Exception e)   
        {  
            e.printStackTrace();  
        }  
    }
}

public class ProblemSolution
{
    public static void main(String[] args)
    {
        Timer timer = new Timer();//create a new Timer
        timer.scheduleAtFixedRate(new RssUpdater(), 0, 15000);//this line starts the timer with delay and period
        timer.scheduleAtFixedRate(new RssJpgImageReader(), 20000, 20000);
    }   
}