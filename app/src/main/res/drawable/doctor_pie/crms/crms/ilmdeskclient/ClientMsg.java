import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.net.*;
import java.util.*;
// connection between server and client , where server is recieving clinets data.....

public class ClientMsg implements Runnable
{
int flag=1;
InputStream in=null; //Client recieve data from Server
OutputStream out=null;// Client send data to Server
ObjectOutputStream objout=null;
	public ClientMsg()
		{ 
		}

public void readMsg()
	{
	  try{
				
		int r=in.read();
		String msg="";
		while(r!='^')
			{
			msg=msg+(char)r;
			r=in.read();
			}  
		if(msg.startsWith("cmd:")==true)
			{
			 machineoperation(msg.substring(4));
			}
		else if(msg.startsWith("sys:")==true)
			{
			 objectoperation(msg.substring(4));
			}
		else
			{     	
		JOptionPane.showMessageDialog(null,"MSG By ADMIN:-"+msg);
			}
		
		
	     }
	catch(Exception ee)
		{ flag=0;
		  System.out.println(" Client 601 :-"+ee);
		}			    
	}

 public void machineoperation(String cmd)	
	{
		 try{
		 Runtime rt=Runtime.getRuntime();
		 Process pr=rt.exec(cmd); 
		 //pr.destroy();
			}
		  catch(Exception ee)
			{ System.out.println(" Client 701 :-"+ee);
			};
		
		
	}
public void objectoperation(String sys)
	{	if(sys.equals("551")==true)  // 551 for system info
		{
	 	try{
		LoadSystemInfo lif=new LoadSystemInfo();
		objout.writeObject(lif.vf);
			}
		  catch(Exception ee)
			{ System.out.println(" Client Object Operation 801 :-"+ee);
			};
		}
	}
public void run()
	{  try{
		objout=new ObjectOutputStream(out);
	        }
	 catch(Exception ee)	
		{ System.out.println(" Client Object Output Creation 901 :-"+ee);
		}
	 while(flag==1)
		{
		readMsg();
		}
	}
}