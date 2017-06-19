package edu.fjnu.birdie.notepad2.function;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import edu.fjnu.birdie.notepad2.Data.Data;
import edu.fjnu.birdie.notepad2.Data.Text;
import edu.fjnu.birdie.notepad2.Utils.NotePad;

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

    public String restore(SQLiteDatabase sql,String uid)
    {
        try {
            socket=new Socket(server_ip,server_port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("restore");
            out.writeUTF(uid);
            JSONArray array = new JSONArray(in.readUTF());
            for(int i=0;i<array.length();i++)
            {
                    JSONObject json=array.getJSONObject(i);
                    String s="delete from "+NotePad.Notes.TABLE_NAME_NOTES;
                    sql.execSQL(s);
                    String id=(String)json.get("id");
                    String title=(String)json.get("title");
                    String contain=(String)json.get("contain");
                    String date=(String)json.get("date");
                    String dirid=(String)json.get("dirid");
                    ContentValues values=new ContentValues();
                    values.put(NotePad.Notes._ID   ,id);
                    values.put(NotePad.Notes.COLUMN_NAME_NOTE_TITLE  ,title);
                    values.put(NotePad.Notes.COLUMN_NAME_NOTE_CONTENT,contain);
                    values.put(NotePad.Notes.COLUMN_NAME_NOTE_DATE,date);
                    values.put(NotePad.Notes.COLUMN_NAME_NOTE_CATEGORY,dirid);
                    sql.insert(NotePad.Notes.TABLE_NAME_NOTES,null,values);

                return "success";
            }

        } catch (IOException e) {
            Log.d("---register", e.getMessage());
        }
        catch (JSONException e) {
            // TODO 自动生成的 catch 块
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
                Log.d("---backup", e.getMessage());
            }
        }
        return "failue";
    }

    public String backup(SQLiteDatabase sql, String id)
    {
        String[] col={NotePad.Notes._ID ,
                NotePad.Notes.COLUMN_NAME_NOTE_TITLE ,
                NotePad.Notes.COLUMN_NAME_NOTE_CONTENT,
                NotePad.Notes.COLUMN_NAME_NOTE_CATEGORY ,
                NotePad.Notes.COLUMN_NAME_NOTE_DATE };
        final Cursor result=sql.query(NotePad.Notes.TABLE_NAME_NOTES,col,null,
                null,null,null,null);
        JSONArray array = new JSONArray();
        while(result.moveToNext())
        {
            JSONObject json=new JSONObject();
	        	try {
					json.put("id", result.getString(0));
					json.put("title", result.getString(1));
					json.put("contain", result.getString(2));
                    json.put("date", result.getString(4));
                    json.put("dirid", result.getString(3));
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
	        	array.put(json);
        }
        try {
            socket=new Socket(server_ip,server_port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("backup");
            out.writeUTF(id);
            out.writeUTF(array.toString());
            String info=in.readUTF();
            return info;
        } catch (IOException e) {
            Log.d("---backup", e.getMessage());
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
                Log.d("---backup", e.getMessage());
            }
        }
        return "failue";
    }

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
