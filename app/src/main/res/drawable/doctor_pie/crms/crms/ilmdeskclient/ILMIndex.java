import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.net.*;
import java.util.Date;
// connection between server and client , where server is recieving clinets data.....

public class ILMIndex implements Runnable
{
String IP="";
int portNo=5050;
int portMsgNo=5051;
InputStream in=null; //Client recieve data from Server
OutputStream out=null;// Client send data to Server
Robot screen=null;
Rectangle rect=null;
int frwidth=800,frheight=600;
int flag=1;
	public ILMIndex()
		{ IP=ServerIP.IP;
		System.out.println("COnnection to :-"+IP);
		  try{
			Toolkit kit=Toolkit.getDefaultToolkit();
			int scwidth=(int)kit.getScreenSize().getWidth();
			int scheight=(int)kit.getScreenSize().getHeight();
			rect=new Rectangle(0,0,scwidth,scheight);
			screen=new Robot();
		
			}
		 catch(Exception ee)
			{
		  System.out.println(" Screen 701 :="+ee);
			}
		}

public void startClient()
	{
	int checkserver=1;
	while(checkserver==1)
	{
	  try{
				
		Socket client=new Socket(IP,portNo);
		Socket clientmsg=new Socket(IP,portMsgNo);

		ClientMsg mcc=new ClientMsg();
		mcc.in=clientmsg.getInputStream();
		mcc.out=clientmsg.getOutputStream();
		Thread msgth=new Thread(mcc);
			msgth.start();

		in=client.getInputStream();
		out=client.getOutputStream();
		
		Thread th=new Thread(this);
		th.start();
		checkserver=0;		
		
	     }
	catch(Exception ee)
		{ System.out.println(" Server 601 :-"+ee); 
		try{
			Thread.sleep(5*1000);
		      }
		 catch(Exception er){}
		  
		}			    
	 }
	}

public void run()
	{ 
	try {ObjectOutputStream writesc=new ObjectOutputStream(out);
		while(flag==1)
			{
			try{
			ImageIcon ico=getScreen();
			writesc.writeObject(ico);
			writesc.reset();
			   }
			  catch(Exception er)
				{ flag=0;
			System.out.println(" Sending Object 702:-"+er);
				}
			}
		}
	catch(Exception ee)
		{
		System.out.println(" Object Pipe 601:-"+ee);
		}
	}

public  ImageIcon getScreen()
	{
		try{
		BufferedImage bimg=screen.createScreenCapture(rect);
		Image img=(Image)bimg;   // BuffredImage is child og Image class.
		Image rimg=img.getScaledInstance(frwidth, frheight, 4) ;
		ImageIcon icon=new ImageIcon(rimg);
		
		return(icon);		  }
		catch(Exception ee)
		{
		System.out.println(" Capturing 501 :-"+ee);
		}
		return(null);

	}

  


public static void main(String cmd[])
	{
	if(cmd!=null)
		ServerIP.IP=cmd[0]; 
	ILMIndex obj=new ILMIndex();
			obj.startClient();
	
	}
}