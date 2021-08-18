package info.wkweb.com.vmart;

import org.json.JSONArray;

/**
 * Created by learntodrill on 20/02/20.
 */

public class SingletonObject
{

    public  static  SingletonObject instance;

    private JSONArray Array_MyAddress ;
    private JSONArray Array_Addcart;

    public  static  SingletonObject Instance()
    {
        if (instance==null)
        {
            instance=new SingletonObject();
        }
        return  instance;
    }


    //MyAddress Activity

    public  JSONArray getArray_MyAddress()
    {
        return  Array_MyAddress;
    }
    public  void  setArray_MyAddress(JSONArray Array_MyAddress)
    {
        this.Array_MyAddress=Array_MyAddress;
    }

    public  JSONArray getArray_Addcart()
    {
        return  Array_Addcart;
    }
    public  void  setArray_Addcart(JSONArray Array_Addcart)
    {
        this.Array_Addcart=Array_Addcart;
    }

//    public void onBackPressed() {
//
//    }
}
