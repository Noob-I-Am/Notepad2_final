package edu.fjnu.birdie.notepad2.function;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by TOSHIBA on 2017/6/18.
 */
public class User_function {
    private String server_ip="182.61.53.189";
    private int server_port=8880;
    private DataInputStream in=null;
    private DataOutputStream out=null;
    private Socket socket;
    public static int isLogin = 0 ;



    public String updatepwd(String id,String pwd1,String pwd2)
    {
        String info="failure";
        try {
            socket=new Socket(server_ip,server_port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("updatepwd");
            out.writeUTF(id);
            out.writeUTF(pwd1);
            out.writeUTF(pwd2);
            info=in.readUTF();
        } catch (IOException e) {
            Log.d("---updatepwd", e.getMessage());
            e.printStackTrace();
        }
        finally
        {
            try {
                in.close();
                out.close();
                socket.close();
            }
            catch(Exception e)
            {
                Log.d("---forgetpwd", e.getMessage());
            }
        }
        return info;
    }

    public String forgetpwd(String email,String code)
    {
        String info="failure";
        try {
            socket=new Socket(server_ip,server_port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("forgetpwd");
            out.writeUTF(email);
            out.writeUTF(code);
            info=in.readUTF();
        } catch (IOException e) {
            Log.d("---forgetpwd", e.getMessage());
            e.printStackTrace();
        }
        finally
        {
            try {
                in.close();
                out.close();
                socket.close();
            }
            catch(Exception e)
            {
                Log.d("---forgetpwd", e.getMessage());
            }
        }
        return info;
    }

    public String getcode(String email)
    {
        String info="failure";
        try {
            socket=new Socket(server_ip,server_port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("getcode");
            out.writeUTF(email);
            info=in.readUTF();
        } catch (IOException e) {
            Log.d("---register", e.getMessage());
        }
        finally
        {
            try {
                in.close();
                out.close();
                socket.close();
            }
            catch(Exception e)
            {
                Log.d("---register", e.getMessage());
            }
        }
        return info;
    }

    public String register(String id,String pwd,String name,String email) {
        String info="failure";
        try {
            socket=new Socket(server_ip,server_port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("register");
            out.writeUTF(id);
            out.writeUTF(pwd);
            out.writeUTF(name);
            out.writeUTF(email);
            info=in.readUTF();
        } catch (IOException e) {
            Log.d("---register", e.getMessage());
        }
        finally
        {
            try {
                in.close();
                out.close();
                socket.close();
            }
            catch(Exception e)
            {
                Log.d("---register", e.getMessage());
            }
        }
        return info;
    }

    public String login(String id,String pwd) {
        String info="failure";
        try {
            socket=new Socket(server_ip,server_port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("login");
            out.writeUTF(id);
            out.writeUTF(pwd);
            info=in.readUTF();
        } catch (IOException e) {
            Log.d("---login", e.getMessage());
        }
        finally
        {
            try {
                in.close();
                out.close();
                socket.close();
            }
            catch(Exception e)
            {
                Log.d("---login", e.getMessage());
            }
        }
        return info;
    }

    public byte[] ObjectToByte(Object obj)
    {
        byte[] bytes = null;
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);

            bytes = bo.toByteArray();

            bo.close();
            oo.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return(bytes);

    }

    private Object ByteToObject(byte[] bytes){
        Object obj = null;
        try {
            //bytearray to object
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream oi = new ObjectInputStream(bi);

            obj = oi.readObject();

            bi.close();
            oi.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

}
