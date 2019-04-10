package nz.co.cocca.test.javatest;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.List;

import java.net.URL;
import java.net.URLConnection;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONObject;

public class usersdoc 
{
	public String err;
	public String key;
	public String url = "https://reqres.in/api/users";
	/*---------------------------------------------------------------------------------------------------------------------------------------------------*/
	public usersdoc()
	{
		this.err = new String("");
		this.key = new String("");
		this.buffer = new ArrayList<JSONObject>();
		this.reset();
	}
	/*---------------------------------------------------------------------------------------------------------------------------------------------------*/
	public usersdoc reset()
	{
		this.err = "";
		this.key = "";
		this.index = 0;		
		this.limit = 0;
		this.buffer.clear();
		this.executed = false;
		return this;
	}
	/*---------------------------------------------------------------------------------------------------------------------------------------------------*/
	public Boolean sort(String sort_key)
	{
		if (this.executed == false) { return this.error("The object needs to be executed before data can be sorted"); }
		if (this.buffer.isEmpty()) { return this.error("There is no data to sort here"); }
		if (this.buffer.get(0).has(sort_key)) { return this.error("Invalid sort key requested"); }
		return (!this.bsort(sort_key)) ? false : true;
	}
	/*---------------------------------------------------------------------------------------------------------------------------------------------------*/
	public JSONObject as_error()
	{
		JSONObject element = new JSONObject();
		try { element.put("error", this.err); } catch (Exception e) { }
		return element;
	}
	/*---------------------------------------------------------------------------------------------------------------------------------------------------*/
	public JSONArray as_object()
	{
		JSONArray json_array = new JSONArray();
		for (int index = 0; index < this.buffer.size(); index++) { json_array.put(this.buffer.get(index)); }
		return json_array;
	}
	/*---------------------------------------------------------------------------------------------------------------------------------------------------*/
	public String as_string()
	{
		JSONArray json_array = new JSONArray();
		for (int index = 0; index < this.buffer.size(); index++) { json_array.put(this.buffer.get(index)); }
		return json_array.toString();
	}
	/*---------------------------------------------------------------------------------------------------------------------------------------------------*/
	public Boolean execute()
	{
		this.limit = 0;
		this.index = 0;
		this.buffer.clear();
		String data = this.get_source(this.url);
		if (data.isEmpty()) { return false; }
		try
		{
			JSONObject element = new JSONObject(data);
			this.limit = element.getInt("total") + 1;
			JSONArray nodes = element.getJSONArray("data");
			for (int index = 0; index < nodes.length(); index++) 
			{ 
				this.buffer.add(nodes.getJSONObject(index));
				this.index = nodes.getJSONObject(index).getInt("id") + 1;
			}
			return this.traverse();
		}
		catch (Exception e) { return this.error(e.getMessage()); }
	}
	/*---------------------------------------------------------------------------------------------------------------------------------------------------*/
	private int index;
	private int limit;
	private List<JSONObject> buffer;
	private Boolean executed = false;
	/*---------------------------------------------------------------------------------------------------------------------------------------------------*/
	private Boolean error(String error_message)
	{
		this.err = error_message;
		return false;
	}
	/*---------------------------------------------------------------------------------------------------------------------------------------------------*/
	private Boolean bsort(String sort_key)
	{
		try
		{
			boolean sorting = true;
			while (sorting)
			{
				sorting = false;
				for (int index = 0; index < this.buffer.size() - 1; index++)
				{
					String source = this.buffer.get(index).getString(sort_key);
					String target = this.buffer.get((index + 1)).getString(sort_key);
					if (source.compareToIgnoreCase(target) > 0)
					{
						JSONObject buffer = this.buffer.get(index);
						this.buffer.set(index, this.buffer.get(index + 1));
						this.buffer.set((index + 1), buffer);
						sorting = true;
					}
				}
			}
			return true;
		} catch(Exception e) { return this.error(e.getMessage()); }
	}
	/*---------------------------------------------------------------------------------------------------------------------------------------------------*/
    private Boolean traverse()
    {
    	String json_data = this.get_source(String.format(this.url + "/%s", String.valueOf(this.index)));
    	if (json_data.isEmpty()) { return (this.err.isEmpty()) ? this.error("Something went wrong with API") : this.error(this.err); }
    	try
    	{
    		JSONObject element = new JSONObject(json_data);
    		this.buffer.add(element.getJSONObject("data"));
    		this.index = this.index + 1;
    		return (this.index < this.limit) ? this.traverse() : true;
    	} catch (Exception e) { return this.error(e.getMessage()); }
    }
    /*---------------------------------------------------------------------------------------------------------------------------------------------------*/
    private String get_source(String url_string)
    {
    	try
    	{
    		String pointer;
    		URL url_object = new URL(url_string);
    		StringBuilder buffer = new StringBuilder();
    		URLConnection connection = url_object.openConnection();
    		BufferedReader data_object = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    		while ((pointer = data_object.readLine()) != null) { buffer.append(pointer); }
    		return buffer.toString();
    	} 
    	catch (Exception e) 
    	{ 
    		this.err = e.toString();
    		return ""; 
    	}
    }
	/*---------------------------------------------------------------------------------------------------------------------------------------------------*/
}
